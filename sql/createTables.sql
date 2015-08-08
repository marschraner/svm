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

DROP TABLE IF EXISTS Schueler_Kurs;
DROP TABLE IF EXISTS Kurs_Lehrkraft;
DROP TABLE IF EXISTS Kurs;
DROP TABLE IF EXISTS Semester;
DROP TABLE IF EXISTS Kursort;
DROP TABLE IF EXISTS Kurstyp;
DROP TABLE IF EXISTS MaerchenCode;
DROP TABLE IF EXISTS Schueler_SchuelerCode;
DROP TABLE IF EXISTS SchuelerCode;
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
    discriminator              VARCHAR(20)   NOT NULL,
    kuerzel                    VARCHAR(5)    NOT NULL,
    beschreibung               VARCHAR(50)   NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (code_id));

DESCRIBE Code;


-- SchuelerCode
-- ************

CREATE TABLE IF NOT EXISTS SchuelerCode (
    code_id                    INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)    REFERENCES Code (code_id));

DESCRIBE SchuelerCode;


-- Schueler_SchuelerCode
-- *********************

CREATE TABLE IF NOT EXISTS Schueler_SchuelerCode (
    person_id                  INT           NOT NULL,
    code_id                    INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, code_id),
    FOREIGN KEY (person_id)    REFERENCES Schueler (person_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));

DESCRIBE Schueler_SchuelerCode;


-- MaerchenCode
-- ************

CREATE TABLE IF NOT EXISTS MaerchenCode (
    code_id                    INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)    REFERENCES Code (code_id));

DESCRIBE MaerchenCode;


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


-- Kurs
-- ****

CREATE TABLE IF NOT EXISTS Kurs (
    kurs_id                    INT           NOT NULL AUTO_INCREMENT,
    semester_id                INT           NOT NULL,
    kurstyp_id                 INT           NOT NULL,
    altersbereich              VARCHAR(20)   NOT NULL,
    stufe                      VARCHAR(30)   NOT NULL,
    wochentag                  VARCHAR(10)   NOT NULL,
    zeit_beginn                TIME          NOT NULL,
    zeit_ende                  TIME          NOT NULL,
    kursort_id                 INT           NOT NULL,
    bemerkungen                VARCHAR(100)  NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurs_id),
    FOREIGN KEY (semester_id)  REFERENCES Semester (semester_id),
    FOREIGN KEY (kurstyp_id)   REFERENCES Kurstyp (kurstyp_id),
    FOREIGN KEY (kursort_id)   REFERENCES Kursort (kursort_id));

DESCRIBE Kurs;


-- Kurs_Lehrkraft
-- **************

CREATE TABLE IF NOT EXISTS Kurs_Lehrkraft (
    kurs_id                    INT           NOT NULL,
    person_id                  INT           NOT NULL,
    lehrkraefte_ORDER          INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurs_id, person_id),
    FOREIGN KEY (kurs_id)      REFERENCES Kurs (kurs_id),
    FOREIGN KEY (person_id)    REFERENCES Lehrkraft (person_id));

DESCRIBE Kurs_Lehrkraft;


-- Schueler_Kurs
-- *************

CREATE TABLE IF NOT EXISTS Schueler_Kurs (
    person_id                  INT           NOT NULL,
    kurs_id                    INT           NOT NULL,
    last_updated               TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, kurs_id),
    FOREIGN KEY (person_id)    REFERENCES Schueler (person_id),
    FOREIGN KEY (kurs_id)      REFERENCES Kurs (kurs_id));

DESCRIBE Schueler_Kurs;
