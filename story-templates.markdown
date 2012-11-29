#templates
a ?main-character who does not ?positive-action is consequently ?consequence-condition.

a ?naughty-descriptor ?main-character ?naughty-behavior(s). Eventually he is ?consequence-condition by ?consequence-actor, who ?consequence-action while ?he is ?consequence-condition:2.

a ?main-character ?naughty-behavior(s) and ?consequence-effect

?consequence-actor catches a ?main-character as ?he ?naughty-behavior. To teach ?him a lesson, he ?consequence-action.

?actor ?naughty-behavior. In the ensuing chaos of the next ?time-period, ?actor's child ?consequence-effect and ?actor# ?consequence-effect-2.

?consequence-actor warns ***HER*** ?main-character not to ?negative-behavior-1. However, when ***SHE*** goes out of the house he ?negative-behavior-1, until ?consequence-actor appears and ?consequence-action.

A ?positive-descriptor-1, ?positive-descriptor-2 ?main-character, proclaims that ***HE*** will no longer ?positive-action. Over the next ?time-period he ?consequence-effect-1 and ?consequence-effect-2.

a ?main-character who won't ?positive-action accidentally ?naughty-behavior, much to the displeasure of ?consequence-actor.

a ?main-character habitually fails to ?positive-action. One day ?he ?naughty-behavior; he is soon rescued, but he ?consequence-condition.

a ?main-character ?naughty-behavior. ?actor catches him and ?consequence-action.

#main-character
boy
girl

#positive-action ; Because he did not X
groom ?himself properly
eat his soup
sit still at dinner
watch where he's walking

#positive-descriptor
healthy
strong

#consequence-condition ; he is eaten while he is X
unpopular
bedridden
bitten

#consequence-effect
burns to death
gets scalded by hot coffee
wastes away
dies
watches his writing-book wash away
falls into a well, presumably to his death

#consequence-action
eats the ?main-character's sausage
dips the ?main-character in black ink
cuts off the ?main-character's thumbs with giant scissors
sends him to places unknown, presumably to his doom

#consequence-actor
a dog
a hare
Nikolas (that is, Saint Nicholas)
a roving tailor
The wind
a hunter
a mother

#naughty-descriptor
violent

#naughty-behavior
terrorizes animals and people
plays with matches
teases a dark-skinned boy
sucks his thumbs
walks into a river
goes outside during a storm
knocks all of the food onto the floor
steals ?actor#'s musket and eyeglasses and begins to hunt the ?actor#

#time-period
five days
three years

# Random Thoughts
Many items will need metadata on how they are to be referred - Nikolas, -> he, a dog -> it
Each item can also carry metadata on the story element it has, to make titles like "The Story of Little Andrew and The Food Spill"
How to handle the actor# story? The tailor and scissors? Disparate portions refer to the same element.
Store a map of "context" - keys like :main-character - boy. As keys in the story are not found, generate them. However, some things like consequence-effect may need arguments passed into them?
