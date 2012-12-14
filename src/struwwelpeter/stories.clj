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
   "a ?naughty-descriptor ?main-character ?naughty-behavior. Eventually ?main-character(he) is ?consequence-condition by ?consequence-actor, who ?consequence-action while ?main-character(he) is ?consequence-condition:2."
   ])

(def main-character [{:name "boy" :pronoun "he"} {:name "girl" :pronoun "she"}])
(def positive-action ["brush teeth" "lap hands"])
(def consequence-condition ["unpopular" "bedridden" "bitten"])
(def consequence-action ["eats the ?main-character's sausage"])
(def naughty-behavior ["terrorizes animals and people"])
(def naughty-descriptor ["violent"])
(def consequence-actor ["a dog"])

(defn to-token [s]
  {:id (re-find #"^[\w-]*" s)
   :instance (re-find #"(?<=:)\w*" s)})

(defn token-coll [{:keys [id]}]
  @(resolve (symbol id)))

(defn token-key [{:keys [id instance]}]
  (str id instance))

(defn token-str [{:keys [id instance]}]
  (str "?" id (if instance (str ":" instance))))

(defn find-tokens [text]
  (let [token-strs (re-seq #"(?<=\?)[\w:-]*" text)
        tokens (map to-token token-strs)]
    (zipmap (map token-key tokens)
            tokens)))

(defn new-tokens-in-text [text context]
  (let [all-tokens (find-tokens text)
        new-keys (remove (set (keys context)) (keys all-tokens))]
    (select-keys all-tokens new-keys)))

(declare random-selection)

(defn add-to-token-context [context token]
  (let [new-key (token-key token)]
    (if (contains? context new-key)
      context
      (let [{:keys [text context]} (random-selection (token-coll token) context)]
        (assoc context new-key
               (assoc token :value text))))))

(defn substitute-from-tokens [text token]
  (let [value (:value token)]
    (string/replace text (token-str token) value)))

(defn random-item [coll]
  (let [item (rand-nth coll)]
    (if (map? item)
      (:name item)
      item)))

(defn random-selection
  ([coll] (random-selection coll {}))
  ([coll token-context]
    (let [text (random-item coll)
          new-tokens (new-tokens-in-text text token-context)
          new-context (reduce add-to-token-context token-context (vals new-tokens))]
      {:text (reduce substitute-from-tokens text (vals new-context))
       :context new-context})))

(defn generate-story [id]
  (let [randomizer (java.util.Random. id)]
    (with-redefs [rand-int (fn [i] (.nextInt randomizer i))]
      (:text (random-selection story-templates)))))

(generate-story 1)
