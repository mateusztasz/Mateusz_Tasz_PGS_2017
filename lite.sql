
create table CUSTOMER(CustomerId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                      Login VARCHAR(100),
                      Pass VARCHAR(100),
                      Name VARCHAR(20),
                      Surname VARCHAR(20),
                      Address VARCHAR(100),
                      Phone INT);

create table RENTAL(RentalId INTEGER PRIMARY KEY AUTOINCREMENT,
                    CustomerId INT,
                    MovieId INT,
                    DueRented DATE);

create table RENTAL_HISTORY(RentalHistoryId INTEGER PRIMARY KEY AUTOINCREMENT,
                            CustomerId INT ,
                            MovieId INT,
                            DueRented DATE);

create table MOVIE(MovieId INTEGER PRIMARY KEY AUTOINCREMENT,
                   Title  VARCHAR(50),
                   Rating FLOAT,
                   Duration INT,
                   Stack INT);

insert into CUSTOMER(Login, Pass, Name, Surname, Address,  Phone)
values ('admin','admin','Mateusz','Tasz','Wroclaw', 668389501);

insert into CUSTOMER(Login, Pass, Name, Surname, Address,  Phone)
values ('pgs','pgs','PGS','Software','Wroclaw', 717982692);

