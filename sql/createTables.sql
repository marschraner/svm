-- Erzeugen von DB und User für svm
-- ********************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source createTables.sql

USE svm;


-- Engine auf InnoDB setzen
-- ************************

SET default_storage_engine=InnoDB;


-- Alte Tabellen löschen
-- *********************

DROP TABLE IF EXISTS Dispensation;
DROP TABLE IF EXISTS Schueler;
DROP TABLE IF EXISTS Angehoeriger;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Adresse;


-- Adresse
-- *******

CREATE TABLE IF NOT EXISTS Adresse (
    adresse_id              INT           NOT NULL AUTO_INCREMENT,
    strasse                 VARCHAR(50)   NOT NULL,
    hausnummer              VARCHAR(10),
    plz                     VARCHAR(10)   NOT NULL,
    ort                     VARCHAR(50)   NOT NULL,
    festnetz                VARCHAR(20),
    last_updated            TIMESTAMP     NOT NULL,
    PRIMARY KEY (adresse_id));
     
DESCRIBE Adresse;


-- Person
-- ******

CREATE TABLE IF NOT EXISTS Person (
    person_id               INT           NOT NULL AUTO_INCREMENT,
    discriminator           VARCHAR(20)   NOT NULL,
    anrede                  VARCHAR(5),
    vorname                 VARCHAR(50)   NOT NULL,
    nachname                VARCHAR(50)   NOT NULL,
    geburtsdatum            DATE,
    natel                   VARCHAR(20),
    email                   VARCHAR(50),
    adresse_id              INT,
    last_updated            TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (adresse_id) REFERENCES Adresse (adresse_id)); 

DESCRIBE Person;


-- Angehoeriger
-- ************

CREATE TABLE IF NOT EXISTS Angehoeriger (
    person_id               INT           NOT NULL,
    last_updated            TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id) REFERENCES Person (person_id));

DESCRIBE Angehoeriger;


-- Schueler
-- ********

CREATE TABLE IF NOT EXISTS Schueler (
    person_id               INT           NOT NULL,
    geschlecht              VARCHAR(1)    NOT NULL,
    anmeldedatum            DATE          NOT NULL,
    abmeldedatum            DATE,
    vater_id                INT,
    mutter_id               INT,
    rechnungsempfaenger_id  INT           NOT NULL,
    bemerkungen             TEXT,
    last_updated            TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id) REFERENCES Person (person_id),
    FOREIGN KEY (vater_id)  REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (mutter_id) REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (rechnungsempfaenger_id) REFERENCES Angehoeriger (person_id));

DESCRIBE Schueler;


-- Dispensation
-- ************

CREATE TABLE IF NOT EXISTS Dispensation (
    dispensation_id         INT           NOT NULL AUTO_INCREMENT,
    dispensationsbeginn     DATE          NOT NULL,
    dispensationsende       DATE,
    grund                   TEXT          NOT NULL,
    schueler_id             INT           NOT NULL,
    last_updated            TIMESTAMP     NOT NULL,
    PRIMARY KEY (dispensation_id),
    FOREIGN KEY (schueler_id) REFERENCES Schueler (person_id));

DESCRIBE Dispensation;
