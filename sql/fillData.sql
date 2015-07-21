-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm;


-- Delete
-- ******

DELETE FROM Kurs;
DELETE FROM Semester;
DELETE FROM Kursort;
DELETE FROM Kurstyp;
DELETE FROM Schueler_Code;
DELETE FROM Code;
DELETE FROM Dispensation;
DELETE FROM Anmeldung;
DELETE FROM Lehrkraft;
DELETE FROM Schueler;
DELETE FROM Angehoeriger;
DELETE FROM Person;
DELETE FROM Adresse;


-- Adresse
-- *******

INSERT INTO Adresse (adresse_id, strasse, hausnummer, plz, ort) VALUES 
    (1, 'Hintere Bergstrasse', '15', '8942', 'Oberrieden'),
    (2, 'Eidmattstrasse', '20', '8032', 'Zürich'),
    (3, 'Eidmattstrasse', '20', '8032', 'Zürich'),
    (4, 'Eidmattstrasse', '20', '8032', 'Zürich'),
    (5, 'Forchstrasse', '232', '8032', 'Zürich'),
    (6, 'Forchstrasse', '232', '8032', 'Zürich'),
    (7, 'Forchstrasse', '232', '8032', 'Zürich'),
    (8, 'Eidmattstrasse', '20', '8032', 'Zürich'),
    (9, 'Stauffacherstrasse', '222', '8004', 'Zürich'),
    (10, 'Friedberg', '3', '3380', 'Wangen an der Aare');

SELECT * FROM Adresse;


-- Person
-- ******
   
INSERT INTO Person (person_id, discriminator, anrede, vorname, nachname, geburtsdatum, festnetz, natel, email, adresse_id) VALUES 
    (1, 'Angehoeriger', 'FRAU', 'Käthi', 'Schraner', NULL, '044 720 85 51', NULL, 'hschraner@bluewin.ch', 1),
    (2, 'Angehoeriger', 'HERR', 'Martin', 'Schraner', NULL, '044 364 36 30', '079 273 77 20', 'marschraner@gmail.com', 2),
    (3, 'Angehoeriger', 'FRAU', 'Sibyll', 'Metzenthin', NULL, '044 364 36 30', NULL, 'billa.metz@bluewin.ch', 3),
    (4, 'Schueler', 'KEINE', 'Jonas', 'Metzenthin', '2014-06-24', '044 364 36 30', NULL, NULL, 4),
    (5, 'Angehoeriger', 'HERR', 'Kurt', 'Juchli', NULL, NULL, NULL, 'kurt.juchli@zuerich.ch', NULL),
    (6, 'Angehoeriger', 'FRAU', 'Eva', 'Juchli', NULL, NULL, '044 271 53 69', 'juchlischraner@gmail.com', 5),
    (7, 'Schueler', 'KEINE', 'Lilly', 'Juchli', '2008-01-13', '044 271 53 69', NULL, NULL, 6),
    (8, 'Schueler', 'KEINE', 'Anna', 'Juchli', '2010-03-05', '044 271 53 69', NULL, NULL, 7),
    (9, 'Lehrkraft', 'FRAU', 'Sibyll', 'Metzenthin', '1972-05-17', '044 364 36 30', NULL, 'billa.metz@bluewin.ch', 8),
    (10, 'Lehrkraft', 'FRAU', 'Sibylle', 'Schweizer', '1969-05-19', '043 322 00 08', '079 629 72 36', 'sibylle.schweizer@gmx.ch', 9),
    (11, 'Lehrkraft', 'FRAU', 'Franziska', 'Lüscher', '1962-04-25', '032 631 07 76', '076 378 07 76', 'ziska@bluewin.ch', 10);

SELECT * FROM Person;


-- Angehoeriger
-- ************

INSERT INTO Angehoeriger (person_id) VALUES
    (1),
    (2),
    (3),
    (5),
    (6);

SELECT * FROM Angehoeriger;


-- Schueler
-- ********

INSERT INTO Schueler (person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id, bemerkungen) VALUES
    (4, 'M', 2, 3, 3, NULL),
    (7, 'W', 5, 6, 1, 'Grosse Schwester von Anna'),
    (8, 'W', 5, 6, 1, 'Grosse Schwester von Feller');

SELECT * FROM Schueler;


-- Lehrkraft
-- *********
INSERT INTO Lehrkraft (person_id, ahvnummer, vertretungsmoeglichkeiten, aktiv) VALUES
    (9, '756.9620.8222.54', NULL, 1),
    (10, '756.3201.3214.21', 'Mi, Sa', 1),
    (11, '756.8923.1873.08', NULL, 1);

SELECT * FROM Lehrkraft;


-- Anmeldung
-- *********

INSERT INTO Anmeldung (anmeldung_id, anmeldedatum, abmeldedatum, schueler_id) VALUES
    (1, '2015-05-09', NULL, 4),
    (2, '2013-01-01', NULL, 7),
    (3, '2014-01-01', NULL, 8);

SELECT * FROM Anmeldung;


-- Dispensation
-- ************

INSERT INTO Dispensation (dispensation_id, dispensationsbeginn, dispensationsende, voraussichtliche_dauer, grund, schueler_id) VALUES
    (1, '2015-05-09', '2017-08-23', NULL, 'Noch zu klein', 4),
    (7, '2014-02-10', '2014-06-02', NULL, 'Beinbruch', 7);

SELECT * FROM Dispensation;


-- Code
-- ****

INSERT INTO Code (code_id, kuerzel, beschreibung) VALUES
    (1, 'c', 'Casting'),
    (2, 'j', 'Jugendtheater');

SELECT * FROM Code;


-- Schueler_Code
-- *************

INSERT INTO Schueler_Code (schueler_id, code_id) VALUES
    (7, 2),
    (8, 1);

SELECT * FROM Schueler_Code;


-- Kurstyp
-- *******

INSERT INTO Kurstyp (kurstyp_id, bezeichnung) VALUES
    (1, 'Rhythmik-Darstellendes Spiel'),
    (2, 'Theaterspiel'),
    (3, 'Jugendtheater'),
    (4, 'Tanzen'),
    (5, 'Akrobatik'),
    (6, 'Artistik');

SELECT * FROM Kurstyp;


-- Kursort
-- *******

INSERT INTO Kursort (kursort_id, bezeichnung) VALUES
    (1, 'Saal A'),
    (2, 'Saal B'),
    (3, 'Studio S'),
    (4, 'Schulhaus Hofacker C');

SELECT * FROM Kursort;


-- Semester
-- ********

INSERT INTO Semester (semester_id, schuljahr, semesterbezeichnung, semesterbeginn, semesterende, anzahl_schulwochen) VALUES
    (1, '2014/2015', 'ERSTES_SEMESTER', '2014-08-18', '2015-02-07', 18),
    (2, '2014/2015', 'ZWEITES_SEMESTER', '2015-02-23', '2015-07-11', 18),
    (3, '2015/2016', 'ERSTES_SEMESTER', '2015-08-17', '2016-02-06', 18),
    (4, '2015/2016', 'ZWEITES_SEMESTER', '2016-02-22', '2016-07-09', 18);

SELECT * FROM Semester;


-- Kurs
-- ****

INSERT INTO Kurs (kurs_id, semester_id, kurstyp_id, altersbereich, stufe, wochentag, zeit_beginn, zeit_ende, kursort_id, lehrkraft1_id, lehrkraft2_id, bemerkungen) VALUES
    (1, 4, 1, '3 - 4 J', 'Vorkindergarten', 'MONTAG', '10:00:00', '10:50:00', 1, 10, NULL, NULL),
    (2, 4, 1, '4 1/2 - 5 J', '1. Kindergarten', 'DIENSTAG', '14:00:00', '14:50:00', 1, 11, NULL, NULL),
    (3, 4, 6, '9 - 11 J', '5 - 6', 'MONTAG', '16:40:00', '17:30:00', 1, 9, 10, NULL); 

SELECT * FROM Kurs;
