-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm


-- Delete
-- ******

DELETE FROM Schueler;
DELETE FROM Angehoeriger;
DELETE FROM Person;
DELETE FROM Adresse;


-- Adresse
-- *******

INSERT INTO Adresse (adresse_id, strasse, hausnummer, plz, ort, festnetz) VALUES 
    (1, 'Hintere Bergstrasse', 15, 8942, 'Oberrieden', '044 720 85 51'),
    (2, 'Eidmattstrasse', 20, 8032, 'Zürich', '044 364 36 30'),
    (3, 'Forchstrasse', 232, 8032, 'Zürich', '044 271 53 69');

SELECT * FROM Adresse;


-- Person
-- ******
   
INSERT INTO Person (person_id, discriminator, anrede, vorname, nachname, geburtsdatum, natel, email, adresse_id) VALUES 
    (1, 'Angehoeriger', 'Frau', 'Käthi', 'Schraner', NULL, NULL, 'hschraner@bluewin.ch', 1),
    (2, 'Angehoeriger', 'Herr', 'Martin', 'Schraner', NULL, '079 273 77 20', 'marschraner@gmail.ch', 2),
    (3, 'Angehoeriger', 'Frau', 'Sibyll', 'Metzenthin', NULL, NULL, 'billa.metz@bluewin.ch', 2),
    (4, 'Schueler', NULL, 'Jonas', 'Metzenthin', '2014-06-24', NULL, NULL, 2),
    (5, 'Angehoeriger', 'Herr', 'Kurt', 'Juchli', NULL, NULL, 'kurt.juchli@zuerich.ch', 3),
    (6, 'Angehoeriger', 'Frau', 'Eva', 'Juchli', NULL, NULL, 'juchlischraner@gmail.com', 3),
    (7, 'Schueler', NULL, 'Lilly', 'Juchli', '2008-01-13', NULL, NULL, 3),
    (8, 'Schueler', NULL, 'Anna', 'Juchli', '2010-03-05', NULL, NULL, 3);



SELECT * FROM Person;


-- Angehoeriger
-- ************

INSERT INTO Angehoeriger (person_id, elternrolle, rechnungsempfaenger, beruf) VALUES
    (1, NULL, 1, NULL),
    (2, 'Vater', 0, 'Wissenschaftlicher Mitarbeiter'),
    (3, 'Mutter', 1, 'Sportlehrerin'),
    (5, 'Vater', 0, 'Jurist'),
    (6, 'Mutter', 0, NULL);

SELECT * FROM Angehoeriger;


-- Schueler
-- ********

INSERT INTO Schueler (person_id, anmeldedatum, abmeldedatum, vater_id, mutter_id, rechnungsempfaenger_id, dispensationsbeginn, dispensationsende, bemerkungen) VALUES
    (4, '2015-05-09', NULL, 2, 3, 3, '2015-05-09', '2017-08-23', 'Eigentlich noch viel zu klein'),
    (7, '2014-01-01', NULL, 5, 6, 1, NULL, NULL, 'Grosse Schwester von Anna'),
    (8, '2014-01-01', NULL, 5, 6, 1, NULL, NULL, 'Grosse Schwester von Feller');

SELECT * FROM Schueler;



