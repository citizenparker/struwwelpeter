(ns struwwelpeter.stories
  (:require [clojure.string :as string]))

; main-character boy, girl
; good-action grooms themselves, listens at church
; great-descriptor become successful in life, get married, live past thirty
; mildly-bad-adjective

(defn new-story-id []
  (rand-int Integer/MAX_VALUE))

(def story-templates
  [
   ;"a ?main-character who does not ?positive-action is consequently ?negative-condition."
   "a ?naughty-descriptor ?main-character ?naughty-behavior. Eventually he is ?consequence-condition by ?consequence-actor, who ?consequence-action while ?main-character (actually he) is ?consequence-condition:2."
   ])

(def main-character ["boy" "girl"])
(def positive-action ["brush teeth" "lap hands"])
(def consequence-condition ["unpopular" "bedridden" "bitten"])
(def consequence-action ["eats the ?main-character's sausage"])
(def naughty-behavior ["terrorizes animals and people"])
(def naughty-descriptor ["violent"])
(def consequence-actor ["a dog"])

(defn to-token [s]
  {:id (re-find #"^[\w-]*" s)
   :instance (re-find #"(?<=:)\w*" s)})

(defn find-tokens [text]
  (let [token-strs (re-seq #"(?<=\?)[\w:-]*" text)]
    (set (map to-token token-strs))))

(defn token-coll [{:keys [id]}]
  (prn id)
  @(resolve (symbol id)))

(defn token-key [{:keys [id instance]}]
  (str id instance))

(defn token-str [{:keys [id instance]}]
  (str "?" id (if instance (str ":" instance))))

(declare random-selection)

(defn add-token-to-context [context token]
  (let [k (token-key token)
        v (:value (context k))]
    ; this is wrong
    ; need to add the value to the token, then assoc into the context
    (assoc context k
           (assoc token :value
                  (or v (random-selection (token-coll token) context))))))

(defn random-selection
  ([coll] (random-selection coll {}))
  ([coll context]
     (let [text (rand-nth coll)
           new-tokens (find-tokens text)
           tokens (reduce add-token-to-context {} new-tokens)]
       (reduce #(string/replace %1 (token-str %2) (:value %2)) text (vals tokens)))))

(defn generate-story [id]
  (let [randomizer (java.util.Random. id)]
    (with-redefs [rand-int (fn [i] (.nextInt randomizer i))]
      (random-selection story-templates))))
