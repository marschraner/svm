-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm;


-- Delete
-- ******

DELETE FROM Semesterrechnung;
DELETE FROM Maercheneinteilung;
DELETE FROM Maerchen;
DELETE FROM Kursanmeldung;
DELETE FROM Kurs_Lehrkraft;
DELETE FROM Kurs;
DELETE FROM Semester;
DELETE FROM Kursort;
DELETE FROM Kurstyp;
DELETE FROM SemesterrechnungCode;
DELETE FROM ElternmithilfeCode;
DELETE FROM Schueler_SchuelerCode;
DELETE FROM SchuelerCode;
DELETE FROM Code;
DELETE FROM Dispensation;
DELETE FROM Anmeldung;
DELETE FROM Lehrkraft;
DELETE FROM Schueler;
DELETE FROM Angehoeriger;
DELETE FROM Person;
DELETE FROM Adresse;
DELETE FROM Lektionsgebuehren;


-- Lektionsgebuehren
-- *****************

INSERT INTO Lektionsgebuehren (lektionslaenge, betrag_1_kind, betrag_2_kinder, betrag_3_kinder, betrag_4_kinder, betrag_5_kinder, betrag_6_kinder) VALUES
    (50, 22.00, 20.00, 18.00, 17.00, 16.00, 15.00),
    (60, 24.00, 22.00, 20.00, 19.00, 18.00, 17.00),
    (75, 28.00, 25.00, 23.00, 21.00, 20.00, 19.00);

SELECT * FROM Lektionsgebuehren;


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
    (14, 'Neugasse', '33', '8005', 'Zürich'),
    (16, 'Sennhauserweg', '18', '8032', 'Zürich'),
    (17, 'Sennhauserweg', '18', '8032', 'Zürich'),
    (18, 'Kurhausstrasse', '7', '8032', 'Zürich'),
    (19, 'Kurhausstrasse', '7', '8032', 'Zürich'),
    (20, 'Pilatusstrasse', '19', '8712', 'Stäfa'),
    (21, 'Pilatusstrasse', '19', '8712', 'Stäfa'),
    (22, 'Pilatusstrasse', '19', '8712', 'Stäfa'),
    (23, 'Döltschiweg', '53', '8055', 'Zürich'),
    (24, 'Döltschiweg', '53', '8055', 'Zürich'),
    (25, 'Bergwiesenstrasse', '14', '8132', 'Ebmatingen'),
    (26, 'Bergwiesenstrasse', '14', '8132', 'Ebmatingen'),
    (27, 'Hofackerstrasse', '74', '8032', 'Zürich'),
    (28, 'Hofackerstrasse', '74', '8032', 'Zürich'),
    (29, 'Hadlaubsteig', '8', '8006', 'Zürich'),
    (30, 'Hadlaubsteig', '8', '8006', 'Zürich'),
    (31, 'Hadlaubsteig', '8', '8006', 'Zürich'),
    (32, 'Freiestrasse', '29', '8032', 'Zürich'),
    (33, 'Freiestrasse', '29', '8032', 'Zürich'),
    (34, 'Weidstrasse', '12', '8122', 'Binz'),
    (35, 'Weidstrasse', '12', '8122', 'Binz'),
    (36, 'Zelglistrasse', '8', '8127', 'Forch'),
    (37, 'Zelglistrasse', '8', '8127', 'Forch'),
    (38, 'Zelglistrasse', '6', '8127', 'Forch'),
    (39, 'Schwerzimattstrasse', '48', '8912', 'Obfelden'),
    (40, 'Schwerzimattstrasse', '48', '8912', 'Obfelden'),
    (41, 'Markusstrasse', '12', '8006', 'Zürich'),
    (42, 'Markusstrasse', '12', '8006', 'Zürich'),
    (43, 'Heugatterstrasse', '21', '8600', 'Dübendorf'),
    (44, 'Heugatterstrasse', '21', '8600', 'Dübendorf'),
    (45, 'Witellikerstrasse', '66', '8008', 'Zürich'),
    (46, 'Witellikerstrasse', '66', '8008', 'Zürich'),
    (47, 'Toggwilerstrasse', '177', '8706', 'Meilen'),
    (48, 'Tichelrütistrasse', '39', '8044', 'Gockhausen'),
    (49, 'Tichelrütistrasse', '39', '8044', 'Gockhausen');

SELECT * FROM Adresse;


-- Person
-- ******
   
INSERT INTO Person (person_id, discriminator, anrede, nachname, vorname, geburtsdatum, festnetz, natel, email, adresse_id) VALUES 
    (1, 'Angehoeriger', 'FRAU', 'Schraner', 'Käthi', NULL, '044 720 85 51', NULL, 'hschraner@bluewin.ch', 1),
    (2, 'Angehoeriger', 'HERR', 'Schraner', 'Martin', NULL, '044 364 36 30', '079 273 77 20', 'marschraner@gmail.com', 2),
    (3, 'Angehoeriger', 'FRAU', 'Metzenthin', 'Sibyll', NULL, '044 364 36 30', NULL, 'billa.metz@bluewin.ch', 3),
    (4, 'Schueler', 'KEINE', 'Metzenthin', 'Jonas', '2014-06-24', '044 364 36 30', NULL, NULL, 4),
    (5, 'Angehoeriger', 'HERR', 'Juchli', 'Kurt', NULL, NULL, NULL, 'kurt.juchli@zuerich.ch', NULL),
    (6, 'Angehoeriger', 'FRAU', 'Juchli', 'Eva', NULL, '044 271 53 69', '076 515 14 65', 'juchlischraner@gmail.com', 5),
    (7, 'Schueler', 'KEINE', 'Juchli', 'Lilly', '2008-01-13', '044 271 53 69', NULL, NULL, 6),
    (8, 'Schueler', 'KEINE', 'Juchli', 'Anna', '2010-03-05', '044 271 53 69', NULL, NULL, 7),
    (9, 'Lehrkraft', 'FRAU', 'Metzenthin', 'Sibyll', '1972-05-17', '044 364 36 30', NULL, 'billa.metz@bluewin.ch', 8),
    (10, 'Lehrkraft', 'FRAU', 'Schweizer', 'Sibylle', '1969-05-19', '043 322 00 08', '079 629 72 36', 'sibylle.schweizer@gmx.ch', 9),
    (11, 'Lehrkraft', 'FRAU', 'Lüscher', 'Franziska', '1962-04-25', '032 631 07 76', '076 378 07 76', 'ziska@bluewin.ch', 10),
    (12, 'Lehrkraft', 'FRAU', 'Höhn', 'Ursina', '1971-07-17', '043 499 02 20', '079 714 02 07', 'ursina.hoehn@bluewin.ch', 11),
    (13, 'Lehrkraft', 'FRAU', 'Hofmann', 'Simona', '1980-07-24', NULL, '079 478 87 05', 'hofmannsimona@gmail.com', 12),
    (14, 'Lehrkraft', 'FRAU', 'Fessler', 'Esther', '1981-06-19', '043 537 71 93', '076 449 39 63', 'esthifessler@gmail.com', 13),
    (15, 'Lehrkraft', 'FRAU', 'Dorigo', 'Sara', '1980-11-26', NULL, '076 566 14 95', 'saradorigo@gmx.ch', 14),
    (16, 'Schueler', 'KEINE', 'Annen', 'Maude', '2005-08-01', '044 271 90 18', NULL, NULL, 16),
    (17, 'Angehoeriger', 'FRAU', 'Annen', 'Sabine', NULL, '044 271 90 18', '079 771 36 16', 'annen@rogerfrei.com', 17),
    (18, 'Schueler', 'KEINE', 'Baudouin-Psaulme', 'Eléonore', '2004-10-08', NULL, NULL, NULL, 18),
    (19, 'Angehoeriger', 'FRAU', 'Baudouin-Psaulme', 'Claire', NULL, NULL, '079 150 90 46', 'claire.baudouin-psaulme@outlook.com', 19),
    (20, 'Schueler', 'KEINE', 'Beerli', 'Sitara', '2005-10-25', '043 344 54 19', NULL, NULL, 20),
    (21, 'Angehoeriger', 'FRAU', 'Beerli', 'Sibylle', NULL, '043 344 54 19', '079 274 84 85', 'sibylle.beerli@gmx.ch', 21),
    (22, 'Angehoeriger', 'HERR', 'Beerli', 'Roland', NULL, '043 344 54 19', '079 246 64 46', NULL, 22),
    (23, 'Schueler', 'KEINE', 'Bruseghini', 'Oriana', '2006-07-24', '044 450 25 34', NULL, NULL, 23),
    (24, 'Angehoeriger', 'FRAU', 'Bruseghini', 'Lisa', NULL, '044 450 25 34', '079 255 77 07', 'lisabruseghini@sunrise.ch', 24),
    (25, 'Schueler', 'KEINE', 'Doell', 'Isla', '2004-10-06', '044 577 00 64', NULL, NULL, 25),
    (26, 'Angehoeriger', 'FRAU', 'Doell', 'Rachel', NULL, '044 577 00 64', '078 862 51 34', 'racheldoel@hotmail.com', 26),
    (27, 'Schueler', 'KEINE', 'Glatt', 'Medea', '2005-07-28', '044 382 25 33', NULL, NULL, 27),
    (28, 'Angehoeriger', 'HERR', 'Glatt', 'Raoul', NULL, '044 382 25 33', '079 269 62 69', 'familie_glatt@bluewin.ch', 28),
    (29, 'Schueler', 'KEINE', 'Gramigna', 'Serge', '2005-11-07', '044 363 95 42', NULL, NULL, 29),
    (30, 'Angehoeriger', 'FRAU', 'Nicolier', 'Isabelle', NULL, '044 363 95 42', '079 446 50 87', 'isabelle.nicolier@bluewin.ch', 30),
    (31, 'Angehoeriger', 'HERR', 'Gramigna', 'Ralph', NULL, '044 363 95 42', '078 653 29 07', NULL, 31),
    (32, 'Schueler', 'KEINE', 'Koch Medina', 'Anaïs', '2005-10-24', '044 381 13 05', NULL, NULL, 32),
    (33, 'Angehoeriger', 'FRAU', 'Schim van der Loeff', 'Madelaine', NULL, '044 381 13 05', '079 354 63 23', 'madelaine.schirmvanderloeff@bluewin.ch', 33),
    (34, 'Schueler', 'KEINE', 'Mannweiler', 'Elena', '2005-05-15', '043 366 01 36', NULL, NULL, 34),
    (35, 'Angehoeriger', 'FRAU', 'Mannweiler', 'Petra', NULL, '043 366 01 36', '076 370 27 90', 'petra@mannweiler.ch', 35),
    (36, 'Schueler', 'KEINE', 'Müller', 'Jessica', '2006-02-16', '044 980 66 14', NULL, NULL, 36),
    (37, 'Angehoeriger', 'FRAU', 'Müller', 'Jane', NULL, '044 980 66 14', '078 648 06 82', 'jane.mueller@ggaw.ch', 37),
    (38, 'Angehoeriger', 'FRAU', 'Müller', 'Monica', NULL, '044 980 66 14', NULL, 'moni.mueller@ggaw.ch', 38),
    (39, 'Schueler', 'KEINE', 'Papiernik', 'Lena', '2006-01-27', '044 260 66 16', NULL, NULL, 39),
    (40, 'Angehoeriger', 'FRAU', 'Papiernik', 'Eliska', NULL, '044 260 66 16', '076 336 23 10', 'eliska@sunrise.ch', 40),
    (41, 'Schueler', 'KEINE', 'Rotzler', 'Lü Max', '2005-12-07', '044 364 54 33', NULL, NULL, 41),
    (42, 'Angehoeriger', 'HERR', 'Rotzler', 'Andreas', NULL, '044 364 54 33', '078 744 67 07', 'r.rodriguez@highspeed.ch', 42),
    (43, 'Schueler', 'KEINE', 'Stolarski', 'Kiara', '2005-06-29', '043 538 32 55', NULL, NULL, 43),
    (44, 'Angehoeriger', 'HERR', 'Stolarski', 'Michal', NULL, '043 538 32 55', '076 489 71 73', 'diafka@yahoo.fr', 44),
    (45, 'Schueler', 'KEINE', 'Baier', 'Ida', '2004-07-28', '043 810 04 20', NULL, NULL, 43),
    (46, 'Angehoeriger', 'FRAU', 'Baier', 'Nina', NULL, '043 810 04 20', '076 392 79 87', 'nina@baierbischofberger.ch', 46),
    (47, 'Angehoeriger', 'FRAU', 'Bischofberger', 'Christina', NULL, NULL, NULL, NULL, 47),
    (48, 'Schueler', 'KEINE', 'Da Silva Goulart', 'Maria', '2003-09-26', NULL, NULL, NULL, 48),
    (49, 'Angehoeriger', 'FRAU', 'Da Silva Goulart', 'Wilma', NULL, NULL, '079 796 19 22', 'm_clara_dara@hotmail.ch', 49);

SELECT * FROM Person;


-- Angehoeriger
-- ************

INSERT INTO Angehoeriger (person_id) VALUES
    (1),
    (2),
    (3),
    (5),
    (6),
    (17),
    (19),
    (21),
    (22),
    (24),
    (26),
    (28),
    (30),
    (31),
    (33),
    (35),
    (37),
    (38),
    (40),
    (42),
    (44),
    (46),
    (47),
    (49);

SELECT * FROM Angehoeriger;


-- Schueler
-- ********

INSERT INTO Schueler (person_id, geschlecht, mutter_id, vater_id, rechnungsempfaenger_id, bemerkungen) VALUES
    (4, 'M', 3, 2, 3, NULL),
    (7, 'W', 6, 5, 1, 'Grosse Schwester von Anna'),
    (8, 'W', 6, 5, 1, 'Grosse Schwester von Feller'),
    (16, 'W', 17, NULL, 17, NULL),
    (18, 'W', 19, NULL, 19, NULL),
    (20, 'W', 21, 22, 21, NULL),
    (23, 'W', 24, NULL, 24, NULL),
    (25, 'W', 26, NULL, 26, NULL),
    (27, 'W', NULL, 28, 28, NULL),
    (29, 'M', 30, 31, 30, NULL),
    (32, 'W', 33, NULL, 33, NULL),
    (34, 'W', 35, NULL, 35, NULL),
    (36, 'W', 37, NULL, 38, NULL),
    (39, 'W', 40, NULL, 40, NULL),
    (41, 'M', NULL, 42, 42, NULL),
    (43, 'W', NULL, 44, 44, NULL),
    (45, 'W', 46, NULL, 47, NULL),
    (48, 'W', 49, NULL, 49, NULL);

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
    (3, '2014-01-01', NULL, 8),
    (16, '2012-11-01', NULL, 16),
    (18, '2015-03-01', NULL, 18),
    (20, '2010-09-01', NULL, 20),
    (23, '2011-01-01', NULL, 23),
    (25, '2012-03-01', NULL, 25),
    (27, '2011-02-01', NULL, 27),
    (29, '2010-09-01', NULL, 29),
    (32, '2013-09-01', NULL, 32),
    (34, '2014-03-01', NULL, 34),
    (36, '2009-09-01', NULL, 36),
    (39, '2011-05-01', NULL, 39),
    (41, '2012-09-01', NULL, 41),
    (43, '2009-03-01', NULL, 43),
    (45, '2013-10-01', NULL, 45),
    (48, '2013-10-01', NULL, 48);

SELECT * FROM Anmeldung;


-- Dispensation
-- ************

INSERT INTO Dispensation (dispensation_id, dispensationsbeginn, dispensationsende, voraussichtliche_dauer, grund, schueler_id) VALUES
    (1, '2015-05-09', '2017-08-23', NULL, 'Noch zu klein', 4),
    (7, '2014-02-10', '2014-06-02', NULL, 'Beinbruch', 7);

SELECT * FROM Dispensation;


-- Code
-- ****

INSERT INTO Code (code_id, discriminator, kuerzel, beschreibung, selektierbar) VALUES
    (1, 'Schueler', 'c', 'Casting', 1),
    (2, 'Schueler', 'j', 'Jugendtheater', 1),
    (3, 'Elternmithilfe', 'b', 'Buffet', 1),
    (4, 'Elternmithilfe', 'g', 'Garderobe', 1),
    (5, 'Semesterrechnung', '1', 'Stipendium', 1),
    (6, 'Semesterrechnung', '2', 'Handrechnung', 1);

SELECT * FROM Code;


-- SchuelerCode
-- ************

INSERT INTO SchuelerCode (code_id) VALUES
    (1),
    (2);

SELECT * FROM SchuelerCode;


-- Schueler_SchuelerCode
-- *********************

INSERT INTO Schueler_SchuelerCode (person_id, code_id) VALUES
    (7, 2),
    (8, 1);

SELECT * FROM Schueler_SchuelerCode;


-- ElternmithilfeCode
-- ******************

INSERT INTO ElternmithilfeCode (code_id) VALUES
    (3),
    (4);

SELECT * FROM ElternmithilfeCode;


-- SemesterrechnungCode
-- ********************

INSERT INTO SemesterrechnungCode (code_id) VALUES
    (5),
    (6);

SELECT * FROM SemesterrechnungCode;


-- Kurstyp
-- *******

INSERT INTO Kurstyp (kurstyp_id, bezeichnung, selektierbar) VALUES
    (1, 'Rhythmik-Darstellendes Spiel', 1),
    (2, 'Theaterspiel', 1),
    (3, 'Jugendtheater', 1),
    (4, 'Tanzen', 1),
    (5, 'Akrobatik', 1),
    (6, 'Artistik', 1);

SELECT * FROM Kurstyp;


-- Kursort
-- *******

INSERT INTO Kursort (kursort_id, bezeichnung, selektierbar) VALUES
    (1, 'Saal A', 1),
    (2, 'Saal B', 1),
    (3, 'Studio S', 1),
    (4, 'Schulhaus Hofacker C', 1);

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
    (18, 3, 2, '6 1/2 - 8 J', '1. - 2. Klasse', 'DONNERSTAG', '15:50:00', '16:40:00', 1, NULL),
    (19, 2, 2, '9 - 10 J', '3. - 4. Klasse', 'DIENSTAG', '16:40:00', '17:30:00', 1, NULL),
    (20, 2, 2, '9 - 10 J', '3. - 4. Klasse', 'MITTWOCH', '16:40:00', '17:30:00', 1, NULL),
    (21, 2, 2, '11 - 12 J', '5. - 6. Klasse', 'MITTWOCH', '17:30:00', '18:20:00', 1, NULL),
    (22, 3, 2, '11 - 12 J', '5. - 6. Klasse', 'MITTWOCH', '17:30:00', '18:20:00', 1, NULL); 

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
    (18, 11, 0),
    (19, 11, 0),
    (20, 11, 0),
    (21, 11, 0),
    (22, 11, 0);

SELECT * FROM Kurs_Lehrkraft;


-- Kursanmeldung
-- **************

INSERT INTO Kursanmeldung (person_id, kurs_id, abmeldung_per_ende_semester, bemerkungen) VALUES
    (7, 13, 0, '2 Wochen später angefangen'),
    (7, 14, 1, NULL),
    (7, 18, 0, NULL),
    (8, 7, 0, NULL),
    (8, 8, 0, NULL),
    (8, 6, 0, NULL),
    (16, 20, 0, NULL),
    (18, 20, 0, NULL),
    (20, 20, 0, NULL),
    (23, 20, 0, NULL),
    (25, 20, 0, NULL),
    (27, 20, 0, NULL),
    (29, 20, 0, NULL),
    (32, 20, 0, NULL),
    (34, 20, 0, NULL),
    (36, 20, 0, NULL),
    (39, 20, 0, NULL),
    (41, 20, 0, NULL),
    (43, 20, 0, NULL),
    (45, 21, 0, NULL),
    (48, 21, 0, NULL),
    (48, 22, 0, NULL);

SELECT * FROM Kursanmeldung;


-- Maerchen
-- ********

INSERT INTO Maerchen (maerchen_id, schuljahr, bezeichnung, anzahl_vorstellungen) VALUES
    (1, '2013/2014', 'Froschkönig', 8),
    (2, '2014/2015', 'Aschenputtel', 8),
    (3, '2015/2016', 'Rumpelstilzchen', 8);

SELECT * FROM Maerchen;


-- Maercheneinteilung
-- *****************

INSERT INTO Maercheneinteilung (
    person_id, 
    maerchen_id,
    gruppe,
    rolle_1,
    bilder_rolle_1,
    rolle_2,
    bilder_rolle_2,
    rolle_3,
    bilder_rolle_3,
    elternmithilfe,
    code_id,
    kuchen_vorstellung_1,
    kuchen_vorstellung_2,
    kuchen_vorstellung_3,
    kuchen_vorstellung_4,
    kuchen_vorstellung_5,
    kuchen_vorstellung_6,
    kuchen_vorstellung_7,
    kuchen_vorstellung_8,
    kuchen_vorstellung_9,
    zusatzattribut,
    bemerkungen) VALUES
    (7, 2, 'A', 'Aschenputtel Mina', '1, v3, 3, v4a, v4b, 4, v5b, 5, v6, 6', NULL, NULL, NULL, NULL, 'MUTTER', 3, 0, 1, 0, 0, 1, 0, 1, 0, 0, NULL, NULL),
    (7, 3, 'B', 'Erzähltaube 2', '3, v4, 4, v5b, 5, v6, 6', NULL, NULL, NULL, NULL, 'MUTTER', 4, 1, 0, 1, 0, 0, 1, 0, 0, 0, NULL, NULL),
    (8, 3, 'B', 'Schulkind 6', '2, 3', 'Waldtier Hase 2', '4', NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, 'Eltern-Mithilfe bei Lilly Juchli erfasst.');

SELECT * FROM Maercheneinteilung;


-- Semesterrechnung
-- ****************

INSERT INTO Semesterrechnung (
    semester_id,
    person_id,
    stipendium,
    gratiskinder,
    rechnungsdatum_vorrechnung,
    ermaessigung_vorrechnung,
    ermaessigungsgrund_vorrechnung,
    zuschlag_vorrechnung,
    zuschlagsgrund_vorrechnung,
    anzahl_wochen_vorrechnung,
    wochenbetrag_vorrechnung,
    rechnungsdatum_nachrechnung,
    ermaessigung_nachrechnung,
    ermaessigungsgrund_nachrechnung,
    zuschlag_nachrechnung,
    zuschlagsgrund_nachrechnung,
    anzahl_wochen_nachrechnung,
    wochenbetrag_nachrechnung, 
    datum_zahlung_1,
    betrag_zahlung_1,
    datum_zahlung_2,
    betrag_zahlung_2,
    datum_zahlung_3,
    betrag_zahlung_3,
    code_id,
    bemerkungen) VALUES
    (2, 1, NULL, 0, '2015-02-15', NULL, NULL, NULL, NULL, 18, 40.00, '2015-04-20', NULL, NULL, NULL, NULL, 18, 40.00, '2015-03-21', 720.00, NULL, NULL, NULL, NULL, NULL, NULL),
    (3, 1, NULL, 0, '2015-07-30', NULL, NULL, NULL, NULL, 18, 40.00, '2015-09-01', NULL, NULL, NULL, NULL, 18, 40.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (2, 17, NULL, 0, '2015-02-15', NULL, NULL, NULL, NULL, 18, 20.00, '2015-04-20', NULL, NULL, NULL, NULL, 18, 20.00, '2015-03-21', 180.00, '2015-04-08', 180.00, NULL, NULL, NULL, NULL),
    (3, 19, NULL, 0, '2015-07-30', 20.00, 'Gutschrift vom letzten Semester', NULL, NULL, 18, 20.00, '2015-09-01', 20.00, 'Gutschrift vom letzten Semester', NULL, NULL, 18, 25.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SELECT * FROM Semesterrechnung;
