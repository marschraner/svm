-- Erzeugen von DB und User für svm
-- ********************************

-- Als svm auszuführen.

-- mysql -u svm -p
-- mysql> use svm;
-- mysql> source create_svm_tables.sql


-- Engine auf InnoDB setzen
-- ************************

SET default_storage_engine=InnoDB;


-- Alte Tabellen löschen
-- *********************

DROP TABLE IF EXISTS Semesterrechnung;
DROP TABLE IF EXISTS Maercheneinteilung;
DROP TABLE IF EXISTS Maerchen;
DROP TABLE IF EXISTS Kursanmeldung;
DROP TABLE IF EXISTS Kurs_Lehrkraft;
DROP TABLE IF EXISTS Kurs;
DROP TABLE IF EXISTS Semester;
DROP TABLE IF EXISTS Kursort;
DROP TABLE IF EXISTS Kurstyp;
DROP TABLE IF EXISTS SemesterrechnungCode;
DROP TABLE IF EXISTS ElternmithilfeCode;
DROP TABLE IF EXISTS Mitarbeiter_MitarbeiterCode;
DROP TABLE IF EXISTS MitarbeiterCode;
DROP TABLE IF EXISTS Schueler_SchuelerCode;
DROP TABLE IF EXISTS SchuelerCode;
DROP TABLE IF EXISTS Code;
DROP TABLE IF EXISTS Dispensation;
DROP TABLE IF EXISTS Anmeldung;
DROP TABLE IF EXISTS Mitarbeiter;
DROP TABLE IF EXISTS Schueler;
DROP TABLE IF EXISTS ElternmithilfeDrittperson;
DROP TABLE IF EXISTS Angehoeriger;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Adresse;
DROP TABLE IF EXISTS Lektionsgebuehren;


-- Lektionsgebuehren
-- *****************

CREATE TABLE IF NOT EXISTS Lektionsgebuehren (
    lektionslaenge             INT           NOT NULL,
    version                    INT           NOT NULL,
    betrag_1_kind              DECIMAL(6,2)  NOT NULL,
    betrag_2_kinder            DECIMAL(6,2)  NOT NULL,  
    betrag_3_kinder            DECIMAL(6,2)  NOT NULL,  
    betrag_4_kinder            DECIMAL(6,2)  NOT NULL,  
    betrag_5_kinder            DECIMAL(6,2)  NOT NULL, 
    betrag_6_kinder            DECIMAL(6,2)  NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (lektionslaenge));


-- Adresse
-- *******

CREATE TABLE IF NOT EXISTS Adresse (
    adresse_id                 INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    strasse                    VARCHAR(50),
    hausnummer                 VARCHAR(10),
    plz                        VARCHAR(10)   NOT NULL,
    ort                        VARCHAR(50)   NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (adresse_id));


-- Person
-- ******

CREATE TABLE IF NOT EXISTS Person (
    person_id                  INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    discriminator              VARCHAR(30)   NOT NULL,
    anrede                     VARCHAR(5),
    vorname                    VARCHAR(50)   NOT NULL,
    nachname                   VARCHAR(50)   NOT NULL,
    geburtsdatum               DATE,
    festnetz                   VARCHAR(20),
    natel                      VARCHAR(20),
    email                      VARCHAR(150),
    adresse_id                 INT,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (adresse_id)   REFERENCES Adresse (adresse_id));


-- Angehoeriger
-- ************

CREATE TABLE IF NOT EXISTS Angehoeriger (
    person_id                  INT           NOT NULL,
    wuenscht_emails            BOOLEAN,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id));


-- ElternmithilfeDrittperson
-- *************************

CREATE TABLE IF NOT EXISTS ElternmithilfeDrittperson (
    person_id                  INT           NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id));


-- Schueler
-- ********

CREATE TABLE IF NOT EXISTS Schueler (
    person_id                  INT           NOT NULL,
    geschlecht                 VARCHAR(1)    NOT NULL,
    vater_id                   INT,
    mutter_id                  INT,
    rechnungsempfaenger_id     INT           NOT NULL,
    bemerkungen                TEXT,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id),
    FOREIGN KEY (vater_id)     REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (mutter_id)    REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (rechnungsempfaenger_id) REFERENCES Angehoeriger (person_id));


-- Mitarbeiter
-- ***********

CREATE TABLE IF NOT EXISTS Mitarbeiter (
    person_id                  INT           NOT NULL,
    ahvnummer                  VARCHAR(16),
    ibannummer                 VARCHAR(40),
    lehrkraft                  BOOLEAN       NOT NULL,
    vertretungsmoeglichkeiten  TEXT,
    bemerkungen                TEXT,
    aktiv                      BOOLEAN       NOT NULL,
    PRIMARY KEY (person_id),
    FOREIGN KEY (person_id)    REFERENCES Person (person_id));


-- Anmeldung
-- *********

CREATE TABLE IF NOT EXISTS Anmeldung (
    anmeldung_id               INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    anmeldedatum               DATE          NOT NULL,
    abmeldedatum               DATE,
    schueler_id                INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (anmeldung_id),
    FOREIGN KEY (schueler_id)  REFERENCES Schueler (person_id));


-- Dispensation
-- ************

CREATE TABLE IF NOT EXISTS Dispensation (
    dispensation_id            INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    dispensationsbeginn        DATE          NOT NULL,
    dispensationsende          DATE,
    voraussichtliche_dauer     TEXT,
    grund                      TEXT          NOT NULL,
    schueler_id                INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (dispensation_id),
    FOREIGN KEY (schueler_id)  REFERENCES Schueler (person_id));


-- Code
-- ****

CREATE TABLE IF NOT EXISTS Code (
    code_id                    INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    discriminator              VARCHAR(20)   NOT NULL,
    kuerzel                    VARCHAR(5)    NOT NULL,
    beschreibung               VARCHAR(50)   NOT NULL,
    selektierbar               BOOLEAN       NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (code_id));


-- SchuelerCode
-- ************

CREATE TABLE IF NOT EXISTS SchuelerCode (
    code_id                    INT           NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));


-- Schueler_SchuelerCode
-- *********************

CREATE TABLE IF NOT EXISTS Schueler_SchuelerCode (
    person_id                  INT           NOT NULL,
    code_id                    INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, code_id),
    FOREIGN KEY (person_id)    REFERENCES Schueler (person_id),
    FOREIGN KEY (code_id)      REFERENCES SchuelerCode (code_id));


-- MitarbeiterCode
-- ***************

CREATE TABLE IF NOT EXISTS MitarbeiterCode (
    code_id                    INT           NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));


-- Mitarbeiter_MitarbeiterCode
-- ***************************

CREATE TABLE IF NOT EXISTS Mitarbeiter_MitarbeiterCode (
    person_id                  INT           NOT NULL,
    code_id                    INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, code_id),
    FOREIGN KEY (person_id)    REFERENCES Mitarbeiter (person_id),
    FOREIGN KEY (code_id)      REFERENCES MitarbeiterCode (code_id));


-- ElternmithilfeCode
-- ******************

CREATE TABLE IF NOT EXISTS ElternmithilfeCode (
    code_id                    INT           NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));


-- SemesterrechnungCode
-- ********************

CREATE TABLE IF NOT EXISTS SemesterrechnungCode (
    code_id                    INT           NOT NULL,
    PRIMARY KEY (code_id),
    FOREIGN KEY (code_id)      REFERENCES Code (code_id));


-- Kurstyp
-- *******

CREATE TABLE IF NOT EXISTS Kurstyp (
    kurstyp_id                 INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    bezeichnung                VARCHAR(50)   NOT NULL,
    selektierbar               BOOLEAN       NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurstyp_id));


-- Kursort
-- *******

CREATE TABLE IF NOT EXISTS Kursort (
    kursort_id                 INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    bezeichnung                VARCHAR(50)   NOT NULL,
    selektierbar               BOOLEAN       NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (kursort_id));


-- Semester
-- ********

CREATE TABLE IF NOT EXISTS Semester (
    semester_id                INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    schuljahr                  VARCHAR(9)    NOT NULL,
    semesterbezeichnung        VARCHAR(20)   NOT NULL,
    semesterbeginn             DATE          NOT NULL,
    semesterende               DATE          NOT NULL,
    ferienbeginn1              DATE          NOT NULL,
    ferienende1                DATE          NOT NULL,
    ferienbeginn2              DATE          NULL,
    ferienende2                DATE          NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (semester_id));


-- Kurs
-- ****

CREATE TABLE IF NOT EXISTS Kurs (
    kurs_id                    INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    semester_id                INT           NOT NULL,
    kurstyp_id                 INT           NOT NULL,
    altersbereich              VARCHAR(20)   NOT NULL,
    stufe                      VARCHAR(30)   NOT NULL,
    wochentag                  VARCHAR(10)   NOT NULL,
    zeit_beginn                TIME          NOT NULL,
    zeit_ende                  TIME          NOT NULL,
    kursort_id                 INT           NOT NULL,
    bemerkungen                VARCHAR(100)  NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified               TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurs_id),
    FOREIGN KEY (semester_id)  REFERENCES Semester (semester_id),
    FOREIGN KEY (kurstyp_id)   REFERENCES Kurstyp (kurstyp_id),
    FOREIGN KEY (kursort_id)   REFERENCES Kursort (kursort_id));


-- Kurs_Lehrkraft
-- **************

CREATE TABLE IF NOT EXISTS Kurs_Lehrkraft (
    kurs_id                    INT           NOT NULL,
    person_id                  INT           NOT NULL,
    lehrkraefte_ORDER          INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    PRIMARY KEY (kurs_id, person_id),
    FOREIGN KEY (kurs_id)      REFERENCES Kurs (kurs_id),
    FOREIGN KEY (person_id)    REFERENCES Mitarbeiter (person_id));


-- Kursanmeldung
-- *************

CREATE TABLE IF NOT EXISTS Kursanmeldung (
    person_id                  INT           NOT NULL,
    kurs_id                    INT           NOT NULL,
    version                    INT           NOT NULL,
    anmeldedatum               DATE          NOT NULL,
    abmeldedatum               DATE,
    bemerkungen                VARCHAR(100),
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, kurs_id),
    FOREIGN KEY (person_id)    REFERENCES Schueler (person_id),
    FOREIGN KEY (kurs_id)      REFERENCES Kurs (kurs_id));


-- Maerchen
-- ********

CREATE TABLE IF NOT EXISTS Maerchen (
    maerchen_id                INT           NOT NULL AUTO_INCREMENT,
    version                    INT           NOT NULL,
    schuljahr                  VARCHAR(9)    NOT NULL,
    bezeichnung                VARCHAR(50)   NOT NULL,
    anzahl_vorstellungen       INT           NOT NULL,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (maerchen_id));


-- Maercheneinteilung
-- ******************

CREATE TABLE IF NOT EXISTS Maercheneinteilung (
    person_id                  INT           NOT NULL,
    maerchen_id                INT           NOT NULL,
    version                    INT           NOT NULL,
    gruppe                     VARCHAR(1)    NOT NULL,
    rolle_1                    VARCHAR(60)   NOT NULL,
    bilder_rolle_1             VARCHAR(60),
    rolle_2                    VARCHAR(60),
    bilder_rolle_2             VARCHAR(60),
    rolle_3                    VARCHAR(60),
    bilder_rolle_3             VARCHAR(60),
    elternmithilfe             VARCHAR(12),
    code_id                    INT,
    kuchen_vorstellung_1       BOOLEAN       NOT NULL,
    kuchen_vorstellung_2       BOOLEAN       NOT NULL,
    kuchen_vorstellung_3       BOOLEAN       NOT NULL,
    kuchen_vorstellung_4       BOOLEAN       NOT NULL,
    kuchen_vorstellung_5       BOOLEAN       NOT NULL,
    kuchen_vorstellung_6       BOOLEAN       NOT NULL,
    kuchen_vorstellung_7       BOOLEAN       NOT NULL,
    kuchen_vorstellung_8       BOOLEAN       NOT NULL,
    kuchen_vorstellung_9       BOOLEAN       NOT NULL,
    zusatzattribut             VARCHAR(30),
    bemerkungen                VARCHAR(100),
    drittperson_id             INT,
    creation_date              TIMESTAMP     NOT NULL,
    last_modified              TIMESTAMP     NOT NULL,
    PRIMARY KEY (person_id, maerchen_id),
    FOREIGN KEY (person_id)    REFERENCES Schueler (person_id),
    FOREIGN KEY (maerchen_id)  REFERENCES Maerchen (maerchen_id),
    FOREIGN KEY (code_id)      REFERENCES ElternmithilfeCode (code_id),
    FOREIGN KEY (drittperson_id) REFERENCES ElternmithilfeDrittperson (person_id));


-- Semesterrechnung
-- ****************

CREATE TABLE IF NOT EXISTS Semesterrechnung (
    semester_id                INT           NOT NULL,
    person_id                  INT           NOT NULL,
    version                    INT           NOT NULL,
    stipendium                 VARCHAR(13),
    gratiskinder               BOOLEAN       NOT NULL,
    rechnungsdatum_vorrechnung       DATE,
    ermaessigung_vorrechnung         DECIMAL(8,2)  NOT NULL,
    ermaessigungsgrund_vorrechnung   VARCHAR(50),
    zuschlag_vorrechnung             DECIMAL(8,2)  NOT NULL,
    zuschlagsgrund_vorrechnung       VARCHAR(50),
    anzahl_wochen_vorrechnung        INT           NOT NULL,
    wochenbetrag_vorrechnung         DECIMAL(7,2)  NOT NULL,
    datum_zahlung_1_vorrechnung      DATE,
    betrag_zahlung_1_vorrechnung     DECIMAL(8,2),
    datum_zahlung_2_vorrechnung      DATE,
    betrag_zahlung_2_vorrechnung     DECIMAL(8,2),
    datum_zahlung_3_vorrechnung      DATE,
    betrag_zahlung_3_vorrechnung     DECIMAL(8,2),
    rechnungsdatum_nachrechnung      DATE,
    ermaessigung_nachrechnung        DECIMAL(8,2)  NOT NULL,
    ermaessigungsgrund_nachrechnung  VARCHAR(50),
    zuschlag_nachrechnung            DECIMAL(8,2)  NOT NULL,
    zuschlagsgrund_nachrechnung      VARCHAR(50),
    anzahl_wochen_nachrechnung       INT           NOT NULL,
    wochenbetrag_nachrechnung        DECIMAL(7,2)  NOT NULL,
    datum_zahlung_1_nachrechnung     DATE,
    betrag_zahlung_1_nachrechnung    DECIMAL(8,2),
    datum_zahlung_2_nachrechnung     DATE,
    betrag_zahlung_2_nachrechnung    DECIMAL(8,2),
    datum_zahlung_3_nachrechnung     DATE,
    betrag_zahlung_3_nachrechnung    DECIMAL(8,2),
    code_id                          INT,
    bemerkungen                      TEXT,
    deleted                          BOOLEAN       NOT NULL,
    creation_date                    TIMESTAMP     NOT NULL,
    last_modified                    TIMESTAMP     NOT NULL,
    PRIMARY KEY (semester_id, person_id),
    FOREIGN KEY (semester_id)  REFERENCES Semester (semester_id),
    FOREIGN KEY (person_id)    REFERENCES Angehoeriger (person_id),
    FOREIGN KEY (code_id)      REFERENCES SemesterrechnungCode (code_id));
