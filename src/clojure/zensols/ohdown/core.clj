(ns zensols.ohdown.core
  (:require [zensols.actioncli.parse :as cli])
  (:gen-class :main true))

(defn- create-command-context []
  {:command-defs '((:find zensols.ohdown client find-command))})

(defn -main [& args]
  (let [command-context (create-command-context)]
    (apply cli/process-arguments command-context args)))
