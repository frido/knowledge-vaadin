# JPA

## Lazy relations

* call method on relation
* fetch join (JPQL, Criteria API) [MultipleBagFetchException](#MultipleBagFetchException)
* <u>***named entity graph***</u>

### MultipleBagFetchException

Viac `fetch` na tabulke sposobuje tuto exception. Je to upozornenie na cartesian product ked sa pouzije viac `fetch` na tabulke.

## Flush

AUTO (pred commitom, pred jpql selectom), COMMIT, ALWAYS, MANUAL 

Flush by sa mal pouzivat len pred "JPQL or Criteria query to perform a bulk update or remove operations" - ak robim update cez query, musim zavolat predtym flush aby som update robil nad aktualnymi datami - tento flush musim zavolat manualne (automaticky sa deje len pred jpql select).

<u>**Ako ziskat id prave persistnutej entity**</u>

### Flush operation order

Ked sa zavola flush, nedeju sa sql query podla toho ako su nakodene ale podla flush poradia. A teda najskor su napriklad inserty a az potom delete.

**<u>Pozriet kde vsade a preco sa vola flush</u>**

## Locking

## Cache

merge test
merge test2
