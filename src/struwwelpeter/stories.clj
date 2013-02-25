(ns struwwelpeter.stories
  (:require [clojure.string :as string]))

; main-character boy, girl
; good-action grooms themselves, listens at church
; great-descriptor become successful in life, get married, live past thirty
; mildly-bad-adjective

(defn new-story-id []
  (rand-int Integer/MAX_VALUE))

(def story-template
  [
   ;"a ?main-character who does not ?positive-action is consequently ?negative-condition."
   {:text "a ?naughty-descriptor ?main-character ?naughty-behavior. Eventually ?main-character(pronoun) is ?consequence-condition by ?consequence-actor, who ?consequence-action while ?main-character(pronoun) is ?consequence-condition:2."}
   ])

(def main-character [{:text "boy" :pronoun "he"} {:text "girl"}])
(def positive-action [{:text "brush teeth"} {:text "clap hands"}])
(def consequence-condition [{:text "unpopular"} {:text "bedridden"} {:text "bitten"}])
(def consequence-action [{:text "eats the ?main-character's sausage"}])
(def naughty-behavior [{:text "terrorizes animals and people"}])
(def naughty-descriptor [{:text "violent"}])
(def consequence-actor [{:text "a dog"}])

(def reserved-keys
  #{:keys :context :category :instance})

(defn to-token [s]
  {:category (re-find #"^[\w-]*" s)
   :instance (re-find #"(?<=:)\w*" s)})

(defn token-key [{:keys [category instance]}]
  (str category instance))

(defn find-tokens [text]
  (let [token-strs (re-seq #"(?<=\?)[\w:\-()]*" text)
        tokens (map to-token token-strs)]
    (zipmap (map token-key tokens)
            tokens)))

(defn token-coll [{:keys [category]}]
  @(resolve (symbol category)))

(defn token-replacement [token & [k]]
  (let [text-key (or k :text)
        {:keys [category instance]} token]
    {:match (str "?" category
                 (when instance (str ":" instance))
                 (when k (str "(" (name k) ")")))
     :replacement (text-key token)}))

(defn replacement-pairs [token]
  (let [ks (remove reserved-keys (keys token))]
    (concat (map #(token-replacement token %) ks)
            [(token-replacement token)])))

(declare replace-tokens)

(defn add-to-token-context [context token]
  (let [new-key (token-key token)]
    (if (contains? context new-key)
      context
      (let [new-value (rand-nth (token-coll token))
            {:keys [text context]} (replace-tokens new-value context)
            new-token (merge token new-value {:text text})]
        (assoc context new-key new-token)))))

(defn new-tokens-in-token [token token-context]
  (let [all-tokens (find-tokens (:text token))
        new-keys (remove (set (keys token-context)) (keys all-tokens))]
    (select-keys all-tokens new-keys)))

(defn substitute-from-tokens [current-text token]
  (let [pairs (replacement-pairs token)]
    (reduce #(string/replace %1 (:match %2) (:replacement %2)) current-text pairs)))

(defn replace-tokens
  ([token] (replace-tokens token {}))
  ([token token-context]
    (let [new-tokens (new-tokens-in-token token token-context)
          new-context (reduce add-to-token-context token-context (vals new-tokens))]
      {:text (reduce substitute-from-tokens (:text token) (vals new-context))
       :context new-context})))

(defn generate-story [id]
  (let [randomizer (java.util.Random. id)]
    (with-redefs [rand-int (fn [i] (.nextInt randomizer i))]
      (replace-tokens {:text "?story-template"}))))

(generate-story 1)
