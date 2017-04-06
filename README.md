# Video Rental Application

## Opis
Celem aplikacji jest usprawnieni pracy wypożyczalni filmów. 

## Dokumentacja
>**Dokładna instrukcja użytkownika  z opisem możliwości aplikacji oraz z pokazaniem krok po kroku jak danej funkcjonalności użyć znajduje się w folderze `documentation/`**

  ```
  evince documentation/VideoRental_Mateusz_Tasz_PGS_2017_reference_manual.pdf
  ```

 Wygenerowana automatycznie dostępna jest w folderze `documentation/javadoc/`
  ```
  firefox documentation/javadoc/index.html
  ```

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
 </p>

## Baza danych  SQLite
Dane na których pracuje aplikacja są przechowywane w bazie danych. Oto struktura tabel:
<html>
<table>
  <tr>
    <td colspan="7"><b>Customer table</b></td>
  </tr>
  <tr>
    <td>CustomerId</td>
    <td>Login</td>
      <td>Pass</td>
   <td>Name</td>
<td>Surname</td>
<td>Address</td>
<td>Phone</td>
  </tr>
</table>

<table>
  <tr>
    <td colspan="5"><b>Movie table</b></td>
  </tr>
  <tr>
    <td>MovieId</td>
    <td>Title</td>
      <td>Rating</td>
   <td>Duration</td>
<td>Stack</td>
  </tr>
</table>

<table>
  <tr>
    <td colspan="4"><b>Rental table</b></td>
  </tr>
  <tr>
    <td>RentalId</td>
    <td>CustomerId</td>
      <td>MovieId</td>
   <td>DueRented</td>
  </tr>
</table>

<table>
  <tr>
    <td colspan="4"><b>Rental_History table</b></td>
  </tr>
  <tr>
    <td>RentalHistoryId</td>
    <td>CustomerId</td>
      <td>MovieId</td>
   <td>DueRented</td>
  </tr>
</table>
</html>

## Uruchomienie paczki
Uruchomienie jest dwuetapowe.

### Budowanie
Budowanie następuje za pomocą narzędzia:  maven
```
./build.sh
```
### Uruchomienie
```
./run.sh
```
