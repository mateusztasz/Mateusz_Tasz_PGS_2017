# Video Rental

## Opis
Celem aplikacji jest usprawnieni pracy wypożyczalni filmów. 

## Implementacja
Aplikacja udostępnia interfejs konsolowy. 
Korzysta ona z bazy dancyh SQLite, która jest sterowana za pomocą sterowników JDBC. 
W repozytorium dostępne są dwie gałęzie warte zauważenia:
```
projekt
├── master
└── ObjectRelationalMapping
```
Master zawiera główną linię rozwoju aplikacji. 
<p>
ObjectRelationalMapping jest gałęzią pochodną od master i która w założeniu miała implementować 
zarządzanie bazą danych za pomocą ORMLite. Na tym etapie rozwoju ORM działa i instrukcje są rozumiana
i działają. Natomiast pełne zarządzanie bazą danych nie zostało przeniesione jeszcze na ORM.
 
## Dokumentacja
Wygenerowana automatycznie dostępna jest w folderze javadoc/
 ```
 firefox documentation/javadoc/index.html
 ```
 
## Uruchomienie paczki

### Budowanie
Budowanie następuje za pomocą narzędzia: maven
```
./build.sh
```
### Uruchomienie
```
./run.sh
```