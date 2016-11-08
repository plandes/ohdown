(defproject com.zensols.ohdown/ohfinder "0.1.0-SNAPSHOT"
  :description "WRITE ME"
  :url "https://github.com/USER/${project.name}"
  :license {:name "Apache License version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"
            :distribution :repo}
  :plugins [[lein-codox "0.9.5"]]
  :codox {:metadata {:doc/format :markdown}
          :project {:name "${project.name}"}
          :output-path "target/doc/codox"}
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-Xlint:unchecked"]
  :jar-exclusions [#".gitignore"]
  :exclusions [org.slf4j/slf4j-log4j12
               ch.qos.logback/logback-classic]
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; logging
                 [org.apache.logging.log4j/log4j-core "2.3"]
                 [org.apache.logging.log4j/log4j-api "2.3"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.3"]
                 [org.apache.logging.log4j/log4j-jcl "2.3"]
                 [org.clojure/tools.logging "0.3.1"]

                 ;; xpath
                 [com.github.kyleburton/clj-xpath "1.4.5"]

                 [clj-http "2.3.0"]

                 ;; command line
                 [com.zensols.tools/actioncli "0.0.11"]]
  :profiles {:uberjar {:aot [zensols.ohdown.core]}
             :appassem {:aot :all}
             :dev
             {:jvm-opts
              ["-Dlog4j.configurationFile=test-resources/log4j2.xml" "-Xms4g" "-Xmx12g" "-XX:+UseConcMarkSweepGC"]}}
  :main zensols.ohdown.core)
