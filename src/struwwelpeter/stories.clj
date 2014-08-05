(ns struwwelpeter.stories
  (:require [clojure.string :as string]))

; main-character boy, girl
; good-action grooms themselves, listens at church
; great-descriptor become successful in life, get married, live past thirty
; mildly-bad-adjective

(defn new-story-id []
  (rand-int Integer/MAX_VALUE))

(def story-template
  [{:text "a ?main-character who does not ?positive-action is consequently ?consequence-condition"}
   {:text "a ?naughty-descriptor ?main-character ?naughty-behavior. Eventually ?main-character(pronoun) is made ?consequence-condition by ?consequence-actor, who ?consequence-action while ?main-character(pronoun) is ?consequence-condition:2"}
   {:text "a ?main-character ?naughty-behavior and ?consequence-effect"}
   {:text "?consequence-actor catches a ?main-character as ?main-character(pronoun) ?naughty-behavior. To teach ?main-character(object) a lesson, he ?consequence-action"}
   {:text "?consequence-actor ?naughty-behavior. In the ensuing chaos of the next ?time-period, ?consequence-actor(possessive) child ?consequence-effect and ?consequence-actor:2 ?consequence-effect:2"}
   {:text "?consequence-actor warns ?consequence-actor(possessive) ?main-character not to ?naughty-behavior:1. However, when ?consequence-actor(pronoun) goes out of the house ?main-character(pronoun) ?naughty-behavior:1, until ?consequence-actor appears and ?consequence-action."}
   {:text "A ?positive-descriptor:1, ?positive-descriptor:2 ?main-character proclaims that ?main-character(pronoun) will no longer ?positive-action. Over the next ?time-period ?main-character(pronoun) ?consequence-effect:1 and ?consequence-effect:2"}
   {:text "A ?main-character who won't ?positive-action accidentally ?naughty-behavior, much to the displeasure of ?consequence-actor"}
   {:text "A ?main-character habitually fails to ?positive-action. One day ?main-character(pronoun) ?naughty-behavior; ?main-character(pronoun) is soon rescued, but ?main-character(pronoun) is now ?consequence-condition"}
   {:text "A ?main-character ?naughty-behavior, but ?consequence-actor catches ?main-character(object) and ?consequence-action"}
   ])

(def main-character
  [{:text "boy" :pronoun "he" :object "him" :reflexive "himself" :possessive "his"}
   {:text "girl" :pronoun "she" :object "her" :reflexive "herself" :possessive "her"}])

(def positive-action
  [{:text "groom ?main-character(reflexive) properly"}
   {:text "sing loudly at church"}
   {:text "eat ?main-character(possessive) soup"}
   {:text "watch where ?main-character(pronoun)'s walking"}
   {:text "sing loudly at church"}
   ])

(def consequence-condition
  [{:text "unpopular"}
   {:text "bedridden"}
   {:text "loagy"}
   {:text "late"}
   {:text "bitten"}])

(def consequence-action
  [{:text "eats the ?main-character's sausage"}
   {:text "dips the ?main-character in black ink"}
   {:text "cuts off the ?main-character's thumbs with giant scissors"}
   {:text "sends ?main-character(object) to places unknown, presumably to ?main-character(possessive) doom"}
   ])

(def naughty-behavior
  [{:text "terrorizes animals and people"}
   {:text "plays with matches"}
   {:text "teases a dark-skinned boy"}
   {:text "sucks ?main-character(possessive) thumbs"}
   {:text "walks into a river"}
   {:text "goes outside during a storm"}
   {:text "knocks all the food onto the floor"}
   ])

(def naughty-descriptor
  [{:text "violent"}
   {:text "crude"}
   {:text "unserious"}
   {:text "whimsical"}
   {:text "greedy"}
   {:text "overly inquisitive"}
   {:text "sensitive"}
   ])

(def positive-descriptor
  [{:text "healthy"}
   {:text "strong"}])

(def consequence-effect
  [{:text "burns to death"}
   {:text "gets scalded by hot coffee"}
   {:text "wastes away"}
   {:text "dies"}
   {:text "watches ?main-character(possessive) writing-book wash away"}
   {:text "falls into a well, presumably to ?main-character(possessive) death"}
   ])

(def consequence-actor
  [{:text "a dog"    :possessive "its" :pronoun "it"}
   {:text "a hare"   :possessive "its" :pronoun "it"}
   {:text "the wind" :possessive "its" :pronoun "it"}
   {:text "a hunter" :possessive "his" :pronoun "he"}
   {:text "a mother" :possessive "her" :pronoun "she"}
   {:text "Nikolas (that is, Saint Nicholas)"   :possessive "his" :pronoun "he"}
   {:text "a roving tailor"   :possessive "his" :pronoun "he"}
   {:text "a priest"   :possessive "his" :pronoun "he"}
   {:text "a festival attendee"   :possessive "their" :pronoun "they"}])

(def time-period
  [{:text "five days"}
   {:text "three years"}])

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

;(generate-story 1)
