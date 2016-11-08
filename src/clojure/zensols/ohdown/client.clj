(ns zensols.ohdown.client
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.inspector :refer (inspect-tree)]
            [clojure.xml :as xml]
            [clj-xpath.core :refer
             [$x $x:tag $x:text $x:attrs $x:attrs* $x:node]]
            [clj-http.client :as client]))

(def ^:private api-key (atom nil))

(def output-dir (io/file "down"))

(defn- assert-success [res]
  (if-let [err (not (= 200 (:status res)))]
    (throw (ex-info "Bad status"
                    {:status (:status res)}))
    res))

(defn- xml-parse [xml]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes xml))))

(defn- get-request [url]
  (log/debugf "getting: %s" url)
  (try (client/get url {:insecure? true})
       (catch Exception e nil)))

(defn- download [id]
 (-> "https://www.openhub.net/projects/%d.xml?api_key=%s"
     (format id @api-key)
     get-request))

(defn- save [id content]
  (log/debugf "saving: %d" id)
  (.mkdirs output-dir)
  (with-open [writer (io/writer (io/file output-dir (format "%d.xml" id)))]
    (binding [*out* writer]
      (println (pr-str content))))
  content)

(defn- read-xml-back [id]
  (with-open [reader (io/reader (io/file output-dir (format "%d.xml" id)))]
    (->> reader
         slurp
         read-string)))

(defn- download-code-url [xml]
  (let [node ($x "/response/result/project/download_url/text()" xml)]
    (if (not (empty? node))
      (when-let [tnode (-> node first vals first)]
        (let [url (.getTextContent tnode)]
          (log/infof "url: %s" url)
          url)))))

(defn- write-url-result [id url]
  (with-open [writer (io/writer "results.txt" :append true)]
    (binding [*out* writer]
      (println (format "%d => %s" id url)))))

(defn check-id [id]
  (log/infof "checking ID: %d" id)
  (->> (download id)
       (save id)
       :body
       download-code-url
       (#(and % (write-url-result id %)))))

(defn- write-xml [id]
  (with-open [writer (io/writer (format "/d/%d.xml" id))]
    (binding [*out* writer]
      (->> (read-xml-back id)
           :body
           println))))

(defn- check-range [start queries]
  (let [offset 0
        start-off (+ offset start)
        end-off (+ start-off queries)]
    (log/infof "searching %d - %d" start-off end-off)
    (doseq [id (range start-off end-off)]
      (check-id id))
    (log/infof "complete for range: %d" start-off)))

(def find-command
  {:description "find for github URLs on OpenHub"
   :options
   [["-a" "--apikey" "Your 64 character OpenHub API key"
     :required "STRING"]
    ["-s" "--start" "Starting range (see Blackboard)"
     :required "NUMBER"
     :default 1
     :parse-fn read-string
     :validate [#(integer? %) "Not an integer"]]
    ["-q" "--queries" "Number of queries/downloads (you get 1000/day)"
     :required "NUMBER"
     :default 25
     :parse-fn read-string
     :validate [#(integer? %) "Not an integer"]]]
   :app (fn [{:keys [start queries apikey] :as opts} & args]
          (if (nil? apikey)
            (throw (ex-info "Missing API key (-a) parameter" {})))
          (reset! api-key apikey)
          (log/infof "using API key: <%s>" apikey)
          (log/infof "starting at %d for %d queries" start queries)
          (check-range start queries))})
