CREATE TABLE Pasazer (
  ID                SERIAL NOT NULL, 
  ImieNazwisko      varchar(100) NOT NULL, 
  Email             varchar(100) NOT NULL UNIQUE, 
  NumerTelefonu     varchar(15) NOT NULL, 
  Login             varchar(50) NOT NULL UNIQUE, 
  Haslo             varchar(100) NOT NULL,
  PRIMARY KEY (ID));

CREATE TABLE Administrator (
  ID        SERIAL NOT NULL, 
  PasazerID int4 NOT NULL UNIQUE, 
  PRIMARY KEY (ID));

CREATE TABLE Relacja (
  ID                     SERIAL NOT NULL, 
  PasazerID              int4 NOT NULL, 
  PrzystanekPoczatkowyID int4 NOT NULL, 
  PrzystanekKoncowyID    int4 NOT NULL, 
  PRIMARY KEY (ID));

CREATE TABLE Przystanek (
  ID        SERIAL NOT NULL, 
  Nazwa     varchar(100) NOT NULL, 
  Ulica     varchar(100) NOT NULL, 
  Polozenie varchar(100),
  PRIMARY KEY (ID));

CREATE TABLE Wyszukanie (
  ID        SERIAL NOT NULL, 
  Data      date NOT NULL, 
  Godzina   time(7) NOT NULL, 
  RelacjaID int4 NOT NULL,
  PRIMARY KEY (ID));

CREATE TABLE PrzystanekNaTrasie (
  ID               SERIAL NOT NULL, 
  CzasPrzejazdu    int4 NOT NULL, 
  Kolejnosc        int4 NOT NULL, 
  PrzystanekID     int4 NOT NULL, 
  TrasaID          int4 NOT NULL, 
  PRIMARY KEY (ID));

CREATE TABLE Trasa (
  ID            SERIAL NOT NULL, 
  NazwaTrasy    varchar(100) NOT NULL, 
  NumerTrasy    int4 NOT NULL, 
  Kierunek      int2 NOT NULL, 
  PRIMARY KEY (ID));

CREATE TABLE Kurs (
  ID               SERIAL NOT NULL, 
  GodzinaStartu    time(7) NOT NULL, 
  TrasaID          int4 NOT NULL, 
  PRIMARY KEY (ID));

ALTER TABLE Administrator ADD CONSTRAINT FKAdministra313299 FOREIGN KEY (PasazerID) REFERENCES Pasazer (ID);
ALTER TABLE Relacja ADD CONSTRAINT FKRelacja743201 FOREIGN KEY (PasazerID) REFERENCES Pasazer (ID);
ALTER TABLE Wyszukanie ADD CONSTRAINT FKWyszukanie213923 FOREIGN KEY (RelacjaID) REFERENCES Relacja (ID);
ALTER TABLE Relacja ADD CONSTRAINT FKRelacja359348 FOREIGN KEY (PrzystanekPoczatkowyID) REFERENCES Przystanek (ID);
ALTER TABLE Relacja ADD CONSTRAINT FKRelacja81545 FOREIGN KEY (PrzystanekKoncowyID) REFERENCES Przystanek (ID);
ALTER TABLE PrzystanekNaTrasie ADD CONSTRAINT FKPrzystanek342221 FOREIGN KEY (PrzystanekID) REFERENCES Przystanek (ID);
ALTER TABLE PrzystanekNaTrasie ADD CONSTRAINT FKPrzystanek97972 FOREIGN KEY (TrasaID) REFERENCES Trasa (ID);
ALTER TABLE Kurs ADD CONSTRAINT FKKurs146380 FOREIGN KEY (TrasaID) REFERENCES Trasa (ID);
