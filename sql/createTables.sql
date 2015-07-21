-- Erzeugen von DB und User für svm
-- ********************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source createTables.sql

-- Auf Jenkins-Server (Immer durchführen wenn DB ändert, damit Tests durchlaufen!):
-- ssh root@sinv-56059.edu.hsr.ch
-- cd /var/lib/jenkins/workspace/SVM/
-- mysql -u svm -psvm < sql/createTables.sql

USE svm;


-- Engine auf InnoDB setzen
-- ************************

SET default_storage_engine=InnoDB;


-- Alte Tabellen löschen
-- *********************

DROP TABLE IF EXISTS Semester;
DROP TABLE IF EXISTS Kursort;
DROP TABLE IF EXISTS Kurstyp;
DROP TABLE IF EXISTS Schueler_Code;
DROP TABLE IF EXISTS Code;
DROP TABLE IF EXISTS Dispensation;
DROP TABLE IF EXISTS Anmeldung;
DROP TABLE IF EXISTS Lehrkraft;
DROP TABLE IF EXISTS Schueler;
DROP TABLE IF EXISTS Angehoeriger;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Adresse;


-- Adresse
-- *******

CREATE TABLE IF NOT EXISTS Adresse (
    adresse_id                 INT           NOT NULL AUTO_INCREMENT,
    strasse                    VARCHAR(50)   NOT NULL,
    hausnummer                 VARCHAR(10),
    plz                        VARCHAR(10)   NOT NULL,
    ort                        VARCHAR(50)   NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (adresse_id));
     
DESCRIBE Adresse;


-- Person
-- ******

CREATE TABLE IF NOT EXISTS Person (
    person_id                  INT           NOT NULL AUTO_INCREMENT,
    discriminator              VARCHAR(20)   NOT NULL,
    anrede                     VARCHAR(5),
    vorname                    VARCHAR(50)   NOT NULL,
    nachname                   VARCHAR(50)   NOT NULL,
    geburtsdatum               DATE,
    festnetz                   VARCHAR(20),
    natel                      VARCHAR(20),
    email                      VARCHAR(50),
    adresse_id                 INT,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (adresse_id)   REFERENCES Adresse (adresse_id)); 

DESCRIBE Person;


-- Angehoeriger
-- ************

CREATE TABLE IF NOT EXISTS Angehoeriger (
    person_id                  INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id));

DESCRIBE Angehoeriger;


-- Schueler
-- ********

CREATE TABLE IF NOT EXISTS Schueler (
    person_id                  INT           NOT NULL,
    geschlecht                 VARCHAR(1)    NOT NULL,
    vater_id                   INT,
    mutter_id                  INT,
    rechnungsempfaenger_id     INT           NOT NULL,
    bemerkungen                TEXT,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id),
    FOREIGN KEY (vater_id)     REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (mutter_id)    REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (rechnungsempfaenger_id) REFERENCES Angehoeriger (person_id));

DESCRIBE Schueler;


-- Lehrkraft
-- *********

CREATE TABLE IF NOT EXISTS Lehrkraft (
    person_id                  INT           NOT NULL,
    ahvnummer                  VARCHAR(16)   NOT NULL,
    vertretungsmoeglichkeiten  VARCHAR(100),
    aktiv                      BOOLEAN       NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id));

DESCRIBE Lehrkraft;


-- Anmeldung
-- *********

CREATE TABLE IF NOT EXISTS Anmeldung (
    anmeldung_id               INT           NOT NULL AUTO_INCREMENT,
    anmeldedatum               DATE          NOT NULL,
    abmeldedatum               DATE,
    schueler_id                INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (anmeldung_id),
    FOREIGN KEY (schueler_id)  REFERENCES Schueler (person_id));

DESCRIBE Anmeldung;


-- Dispensation
-- ************

CREATE TABLE IF NOT EXISTS Dispensation (
    dispensation_id            INT           NOT NULL AUTO_INCREMENT,
    dispensationsbeginn        DATE          NOT NULL,
    dispensationsende          DATE,
    voraussichtliche_dauer     TEXT,
    grund                      TEXT          NOT NULL,
    schueler_id                INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (dispensation_id),
    FOREIGN KEY (schueler_id)  REFERENCES Schueler (person_id));

DESCRIBE Dispensation;


-- Code
-- ****

CREATE TABLE IF NOT EXISTS Code (
    code_id                    INT           NOT NULL AUTO_INCREMENT,
    kuerzel                    VARCHAR(5)    NOT NULL,
    beschreibung               VARCHAR(50)   NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (code_id));

DESCRIBE Code;


-- Schueler_Code
-- *************

CREATE TABLE IF NOT EXISTS Schueler_Code (
    schueler_id                INT           NOT NULL,
    code_id                    INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (schueler_id, code_id),
    FOREIGN KEY (schueler_id)  REFERENCES Schueler (person_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));

DESCRIBE Schueler_Code;


-- Kurstyp
-- *******

CREATE TABLE IF NOT EXISTS Kurstyp (
    kurstyp_id                 INT           NOT NULL AUTO_INCREMENT,
    bezeichnung                VARCHAR(50)   NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurstyp_id));

DESCRIBE Kurstyp;


-- Kursort
-- *******

CREATE TABLE IF NOT EXISTS Kursort (
    kursort_id                 INT           NOT NULL AUTO_INCREMENT,
    bezeichnung                VARCHAR(50)   NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (kursort_id));

DESCRIBE Kursort;


-- Semester
-- ********

CREATE TABLE IF NOT EXISTS Semester (
    semester_id                INT           NOT NULL AUTO_INCREMENT,
    schuljahr                  VARCHAR(9)    NOT NULL,
    semesterbezeichnung        VARCHAR(20)   NOT NULL,
    semesterbeginn             DATE          NOT NULL,
    semesterende               DATE          NOT NULL,
    anzahl_schulwochen         INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (semester_id));

DESCRIBE Semester;
