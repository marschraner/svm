-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm;


-- Delete
-- ******

DELETE FROM Schueler_Kurs;
DELETE FROM Kurs_Lehrkraft;
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
    (10, 'Friedberg', '3', '3380', 'Wangen an der Aare'),
    (11, 'Zollikerstrasse', '81', '8008', 'Zürich'),
    (12, 'Im Rüteli', '7', '5401', 'Baden-Dättwil'),
    (13, 'Ceresstrasse', '11', '8008', 'Zürich'),
    (14, 'Neugasse', '33', '8005', 'Zürich');

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
    (11, 'Lehrkraft', 'FRAU', 'Franziska', 'Lüscher', '1962-04-25', '032 631 07 76', '076 378 07 76', 'ziska@bluewin.ch', 10),
    (12, 'Lehrkraft', 'FRAU', 'Ursina', 'Höhn', '1971-07-17', '043 499 02 20', '079 714 02 07', 'ursina.hoehn@bluewin.ch', 11),
    (13, 'Lehrkraft', 'FRAU', 'Simona', 'Hofmann', '1980-07-24', NULL, '079 478 87 05', 'hofmannsimona@gmail.com', 12),
    (14, 'Lehrkraft', 'FRAU', 'Esther', 'Fessler', '1981-06-19', '043 537 71 93', '076 449 39 63', 'esthifessler@gmail.com', 13),
    (15, 'Lehrkraft', 'FRAU', 'Sara', 'Dorigo', '1980-11-26', NULL, '076 566 14 95', 'saradorigo@gmx.ch', 14);

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
    (11, '756.8923.1873.08', NULL, 1),
    (12, '756.3501.3214.21', NULL, 1),
    (13, '756.8433.1873.08', NULL, 1),
    (14, '756.1232.1812.12', NULL, 1),
    (15, '756.2433.2373.47', NULL, 1);

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


-- SchuelerCode
-- ****

INSERT INTO Code (code_id, kuerzel, beschreibung) VALUES
    (1, 'c', 'Casting'),
    (2, 'j', 'Jugendtheater');

SELECT * FROM Code;


-- Schueler_Code
-- *************

INSERT INTO Schueler_Code (person_id, code_id) VALUES
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
    (3, '2015/2016', 'ERSTES_SEMESTER', '2015-08-17', '2016-02-06', 18);

SELECT * FROM Semester;


-- Kurs
-- ****

INSERT INTO Kurs (kurs_id, semester_id, kurstyp_id, altersbereich, stufe, wochentag, zeit_beginn, zeit_ende, kursort_id, bemerkungen) VALUES
    (1, 1, 1, '4 1/2 - 5 J', '1. Kindergarten', 'FREITAG', '14:00:00', '14:50:00', 1, NULL),
    (2, 2, 1, '5 - 5 1/2 J', '1. Kindergarten', 'FREITAG', '14:00:00', '14:50:00', 1, NULL),
    (3, 3, 1, '4 1/2 - 5 J', '1. Kindergarten', 'FREITAG', '14:00:00', '14:50:00', 1, NULL),
    (4, 1, 1, '5 1/2 - 6 J', '2. Kindergarten', 'FREITAG', '14:50:00', '15:40:00', 1, NULL),
    (5, 2, 1, '6 - 6 1/2 J', '2. Kindergarten', 'FREITAG', '14:50:00', '15:40:00', 1, NULL),
    (6, 3, 1, '5 1/2 - 6 J', '2. Kindergarten', 'FREITAG', '14:50:00', '15:40:00', 1, NULL),
    (7, 1, 1, '4 1/2 - 5 J', '1. Kindergarten', 'FREITAG', '15:40:00', '16:30:00', 1, NULL),
    (8, 2, 1, '5 - 5 1/2 J', '1. Kindergarten', 'FREITAG', '15:40:00', '16:30:00', 1, NULL),
    (9, 3, 1, '4 1/2 - 5 J', '1. Kindergarten', 'FREITAG', '15:40:00', '16:30:00', 1, NULL),
    (10, 1, 1, '5 1/2 - 6 J', '2. Kindergarten', 'FREITAG', '16:40:00', '17:30:00', 1, NULL),
    (11, 2, 1, '6 - 6 1/2 J', '2. Kindergarten', 'FREITAG', '16:40:00', '17:30:00', 1, NULL),
    (12, 3, 1, '5 1/2 - 6 J', '2. Kindergarten', 'FREITAG', '16:40:00', '17:30:00', 1, NULL),
    (13, 1, 5, '6 1/2 - 8 J', '1 - 2 (ab 1. Kl)', 'FREITAG', '15:50:00', '16:40:00', 3, NULL),
    (14, 2, 5, '7 - 8 J', '1 - 2 (ab 1. Kl)', 'FREITAG', '15:50:00', '16:40:00', 3, '(T. Gut / F. Vogel)'),
    (15, 3, 5, '6 1/2 - 8 J', '1 - 2 (ab 1. Kl)', 'FREITAG', '15:50:00', '16:40:00', 3, NULL),
    (16, 2, 6, '9 - 11 J', '2 - 3', 'MONTAG', '16:50:00', '17:40:00', 4, NULL),
    (17, 3, 6, '9 - 11 J', '2 - 3', 'MONTAG', '16:40:00', '17:30:00', 4, NULL),
    (18, 3, 2, '6 1/2 - 8 J', '1 - 2. Klasse', 'DONNERSTAG', '15:50:00', '16:40:00', 1, NULL); 

SELECT * FROM Kurs;


-- Kurs_Lehrkraft
-- **************

INSERT INTO Kurs_Lehrkraft (kurs_id, person_id, lehrkraefte_ORDER) VALUES
    (1, 15, 0),
    (2, 15, 0),
    (3, 15, 0),
    (4, 15, 0),
    (5, 15, 0),
    (6, 15, 0),
    (7, 15, 0),
    (8, 15, 0),
    (9, 15, 0),
    (10, 15, 0),
    (11, 15, 0),
    (12, 15, 0),
    (13, 14, 0),
    (14, 14, 0),
    (15, 14, 0),
    (16, 9, 0),
    (16, 13, 1),
    (17, 9, 0),
    (17, 13, 1),
    (18, 11, 0);

SELECT * FROM Kurs_Lehrkraft;


-- Schueler_Kurs
-- *************

INSERT INTO Schueler_Kurs (person_id, kurs_id) VALUES
    (7, 13),
    (7, 14),
    (7, 18),
    (8, 7),
    (8, 8),
    (8, 6);

SELECT * FROM Schueler_Kurs;
