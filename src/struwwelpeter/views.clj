(ns struwwelpeter.views
  (:require [net.cgrand.enlive-html :as enlive]
            [ring.util.response :as response]))

(defn new-story-id []
  1)

(defn index [] (response/redirect (str "/" (new-story-id))))

(enlive/deftemplate story "html/index.html" [id])
