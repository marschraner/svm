-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm;


-- Delete
-- ******

DELETE FROM Dispensation;
DELETE FROM Anmeldung;
DELETE FROM Schueler;
DELETE FROM Angehoeriger;
DELETE FROM Person;
DELETE FROM Adresse;


-- Adresse
-- *******

INSERT INTO Adresse (adresse_id, strasse, hausnummer, plz, ort, festnetz) VALUES 
    (1, 'Hintere Bergstrasse', '15', '8942', 'Oberrieden', '044 720 85 51'),
    (2, 'Eidmattstrasse', '20', '8032', 'Zürich', '044 364 36 30'),
    (3, 'Forchstrasse', '232', '8032', 'Zürich', '044 271 53 69');

SELECT * FROM Adresse;


-- Person
-- ******
   
INSERT INTO Person (person_id, discriminator, anrede, vorname, nachname, geburtsdatum, natel, email, adresse_id) VALUES 
    (1, 'Angehoeriger', 'FRAU', 'Käthi', 'Schraner', NULL, NULL, 'hschraner@bluewin.ch', 1),
    (2, 'Angehoeriger', 'HERR', 'Martin', 'Schraner', NULL, '079 273 77 20', 'marschraner@gmail.com', 2),
    (3, 'Angehoeriger', 'FRAU', 'Sibyll', 'Metzenthin', NULL, NULL, 'billa.metz@bluewin.ch', 2),
    (4, 'Schueler', 'KEINE', 'Jonas', 'Metzenthin', '2014-06-24', NULL, NULL, 2),
    (5, 'Angehoeriger', 'HERR', 'Kurt', 'Juchli', NULL, NULL, 'kurt.juchli@zuerich.ch', NULL),
    (6, 'Angehoeriger', 'FRAU', 'Eva', 'Juchli', NULL, NULL, 'juchlischraner@gmail.com', 3),
    (7, 'Schueler', 'KEINE', 'Lilly', 'Juchli', '2008-01-13', NULL, NULL, 3),
    (8, 'Schueler', 'KEINE', 'Anna', 'Juchli', '2010-03-05', NULL, NULL, 3);

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
    (1, '2015-05-09', '2017-08-23', NULL, 'Noch zu klein', 4);

SELECT * FROM Dispensation;



