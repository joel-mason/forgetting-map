# Forgetting Map Task

The ForgettingMap extends LinkedHashMap. The reason for this was because 
I wanted the ordering of a LinkedHashMap. It Overrides the `put` and `get` methods. 

Designs can be found in the `/designs` folder in root

## `put`
Put still does what it doesn in `LinkedHashMap`, but it also now sorts the map based on the 
`count` and `timeAdded` fields inside `CacheWrapper`.

## `get`
Get still does what it does in `LinkedHashMap`, but it also removes the least used object, which will be
the first item in the map, and then sorts based on the same as above.

## Improvments
To improve this, I would add an `expiredDate` to the `CacheWrapper` which would be a parameter passed into
`ForgettingCache` and subsequently `ForgettingMap` (or even better, by using Spring Configuration)
which would be checked during the sort, where the item would be removed if it has gone past its `expiredDate`
which would help remove stale items, as currently this `ForgettingMap` assumes all items never get stale.


Another improvement would be to add a `delete` method, which could be used to remove stale items without waiting for
the `expiredDate` to pass.

In regards to testing, I would have also done more thread based tests.
