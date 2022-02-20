# JPA

## Lazy relations

* call method on relation
* fetch join (JPQL, Criteria API) [MultipleBagFetchException](#MultipleBagFetchException)
* <u>***named entity graph***</u>

### Fetch

Fetch creates LEFT JOIN query that creates row for each child. For more fetches there are cartesian product (combination) of all childs from both fetches.
This strategy is not applicable for real situation - maybe only if there is only one item per row.

### Select all asociated entities in own query like PM1

Collect all ids and entities to the map. In parent ask map to get childs.

#### MultipleBagFetchException

Viac `fetch` na tabulke sposobuje tuto exception. Je to upozornenie na cartesian product ked sa pouzije viac `fetch` na tabulke.

### Flush

AUTO (pred commitom, pred jpql selectom), COMMIT, ALWAYS, MANUAL

Flush by sa mal pouzivat len pred "JPQL or Criteria query to perform a bulk update or remove operations" - ak robim update cez query, musim zavolat predtym flush aby som update robil nad aktualnymi datami - tento flush musim zavolat manualne (automaticky sa deje len pred jpql select).

**Ako ziskat id prave persistnutej entity**

#### Flush operation order

Ked sa zavola flush, nedeju sa sql query podla toho ako su nakodene ale podla flush poradia. A teda najskor su napriklad inserty a az potom delete.

### Locking

@Version - funguje to celkom automaticky: automaticky sa zvysi version pri update; dalsi update na starej entite neprejde kedze je v nej stare version.

#### Locknute riadku pri edite

**LockWaitingExceedException (vymyslel som si) nejaka taka excepsion kym sa neuvolni editovaci lock**.

### Imutable

Very good for views, improve performance. Althou it is hard to test what happens under the hood.
Just set it for all view.

### Equals method

Hibernate vzdy vracia tu istu instanciu ak je v persistentnom contexte.
Equals metoda je potrebna ked sa pouzije detach a merge a hlavne ked sa entita pouziva v Setoch.
Problem je ked je entita v sete a zmeni sa jej equals vyhodnotenie (napriklad entita ziskala nove ID). To moze vzniknut problem.
Ked vsak entita este nie je v sete tak nevadi ked sa jej meni ID.

### @NaturalId

**???**

## Cache

Aku guava chage to pouzivame v projekte? Pohladat slova cache a ako rozne ju mame implementovany

## Lombok

Write less but provide: getters/setter, contructors, builders.
It is possible to use it with entity. @Getter, @Setter, @NoArgsConstructor, @ToString Can by applied on Entity.

## Quarkus Panache

Active record pattern build on hibernate.
