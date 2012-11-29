(ns struwwelpeter.views
  (:require [struwwelpeter.stories :as stories]
            [net.cgrand.enlive-html :as enlive]
            [ring.util.response :as response]))

(defn index [] (response/redirect (str "/" (stories/new-story-id))))

(enlive/deftemplate story "html/index.html" [id]
  [:h1] (enlive/content "Hello")
  [:#story-text] (enlive/content (stories/generate-story id)))
