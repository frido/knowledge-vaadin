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

#### @Transactional

Pre SELECT query nepotrebujem transakciu.
Ak ju aj napriek tomu pouzijem ma to efekty take ze:
Pred koncom transakcie sa kontroluje *findDirty* na entitach. Nasledne sa vykona *flush* ktory pri SELECToch nema vyznam.
Optimalizaciu dosiahnem s *@Transactional(readOnly = true)* - nekontroluje sa dirty ani sa nekona flush

**Ako ziskat id prave persistnutej entity**

#### Flush operation order

Ked sa zavola flush, nedeju sa sql query podla toho ako su nakodene ale podla flush poradia. A teda najskor su napriklad inserty a az potom delete.

### Relations

Ked mam bidirectional relation mal by som updatovat obe relacie a udrzat ich in sync.
Inak aj ked sa to neulozi do DB hned, v persistetnom contexte to ostava nespravne.
Mergovanie je take ze bud mergnem novu child entitu, alebo parent entitu s tym ze nad listom musi byt cascade.
Merge merguje len entitu ktora je tam vlozena - relacie to nemerguje pokial to nema nastavene cascade.
Flush robi update len nad managovanou entitou - nie nad relaciami, ani ked maju nastavene cascade.

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

**Figure out more about it**

### Persistent context transaction scope

flush spusta *checkDirty* aby zistil ktore entity treba updatnut. Toto moze zabrat naozaj vela casu ked je entityManager plny manazovanych entit.
*Clear* detachne vsetky entity v persistentnom contexte a *flush* nemusi nic kontrolovat. (Clear by sa mal volat aj po sql updatoch aby si persistentny context nedrzal neaktualne entity).
Clear zmaze cache, takze entity treba opat loadnut z DB.
Mergnutie detachnutej entity do prazdneho contextu sposobi select na kontrolu novej entity voci DB. Takze mergnutie detachnutej entity spravi select a nasledne pripadny update.
Persistentny context udrzuje entity len v ramci transakcie. Pri novej transakcii je entity manager prazdny, bez managovanych entit.
Nacitanie entity v service, vratit ju do view a nasledne ju mergnut v inom service sposobi ze entita je mergnuta do persistentneho contextu ktory ju nepozna a teda je potrebny select sql a az potom pripadny update sql (velmi neprakticke), preto by entity nemali opustat transakciu (servisnu metodu).

## Cache

Aku guava chage to pouzivame v projekte? Pohladat slova cache a ako rozne ju mame implementovany

## Lombok

Write less but provide: getters/setter, contructors, builders.
It is possible to use it with entity. @Getter, @Setter, @NoArgsConstructor, @ToString Can by applied on Entity.

## Quarkus Panache

Active record pattern build on hibernate.
