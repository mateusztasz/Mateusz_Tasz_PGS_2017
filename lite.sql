
create table CUSTOMER(CustomerId INT NOT NULL PRIMARY KEY AUTOINCREMENT,
                      Login VARCHAR(100),
                      Pass VARCHAR(100),
                      Name VARCHAR(20),
                      Surname VARCHAR(20),
                      Address VARCHAR(100),
                      Phone INT);

create table RENTAL(RentalId INT PRIMARY KEY AUTOINCREMENT,
                    CustomerId INT,
                    MovieId INT,
                    DueRented DATE);

create table RENTAL_HISTORY(RentalHistoryId INT AUTOINCREMENT,
                            CustomerId INT ,
                            MovieId INT,
                            DueRented DATE);

create table MOVIE(MovieId INT PRIMARY KEY AUTOINCREMENT,
                   Title  VARCHAR(50),
                   Rating FLOAT,
                   Duration INT,
                   Stack INT);

insert into CUSTOMER(Login, Pass, Name, Surname, Address,  Phone)
values ('admin','admin','Mateusz','Tasz','Wroclaw', 334222113);

