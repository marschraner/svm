-- Tabellen mit Daten befüllen
-- ***************************

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source fillData.sql

USE svm;


-- Delete
-- ******

DELETE FROM Maercheneinteilung;
DELETE FROM Maerchen;
DELETE FROM Schueler_Kurs;
DELETE FROM Kurs_Lehrkraft;
DELETE FROM Kurs;
DELETE FROM Semester;
DELETE FROM Kursort;
DELETE FROM Kurstyp;
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


-- Adresse
-- *******

INSERT INTO Adresse (adresse_id, strasse, hausnummer, plz, ort) VALUES 
    (1, 'Hintere Bergstrasse', '8', '8942', 'Oberrieden'),
    (2, 'Eidmattstrasse', '2', '8032', 'Zürich'),
    (3, 'Eidmattstrasse', '2', '8032', 'Zürich'),
    (4, 'Eidmattstrasse', '2', '8032', 'Zürich'),
    (5, 'Forchstrasse', '23', '8032', 'Zürich'),
    (6, 'Forchstrasse', '23', '8032', 'Zürich'),
    (7, 'Forchstrasse', '23', '8032', 'Zürich'),
    (8, 'Eidmattstrasse', '2', '8032', 'Zürich'),
    (9, 'Stauffacherstrasse', '22', '8004', 'Zürich'),
    (10, 'Friedberg', '33', '3380', 'Wangen an der Aare'),
    (11, 'Zollikerstrasse', '18', '8008', 'Zürich'),
    (12, 'Im Rüteli', '77', '5401', 'Baden-Dättwil'),
    (13, 'Ceresstrasse', '19', '8008', 'Zürich'),
    (14, 'Neugasse', '333', '8005', 'Zürich'),
    (16, 'Sennhauserweg', '12', '8032', 'Zürich'),
    (17, 'Sennhauserweg', '12', '8032', 'Zürich'),
    (18, 'Kurhausstrasse', '88', '8032', 'Zürich'),
    (19, 'Kurhausstrasse', '88', '8032', 'Zürich'),
    (20, 'Pilatusstrasse', '99', '8712', 'Stäfa'),
    (21, 'Pilatusstrasse', '99', '8712', 'Stäfa'),
    (22, 'Pilatusstrasse', '99', '8712', 'Stäfa'),
    (23, 'Döltschiweg', '55', '8055', 'Zürich'),
    (24, 'Döltschiweg', '55', '8055', 'Zürich'),
    (25, 'Bergwiesenstrasse', '44', '8132', 'Ebmatingen'),
    (26, 'Bergwiesenstrasse', '44', '8132', 'Ebmatingen'),
    (27, 'Hofackerstrasse', '777', '8032', 'Zürich'),
    (28, 'Hofackerstrasse', '777', '8032', 'Zürich'),
    (29, 'Hadlaubsteig', '88', '8006', 'Zürich'),
    (30, 'Hadlaubsteig', '88', '8006', 'Zürich'),
    (31, 'Hadlaubsteig', '88', '8006', 'Zürich'),
    (32, 'Freiestrasse', '299', '8032', 'Zürich'),
    (33, 'Freiestrasse', '299', '8032', 'Zürich'),
    (34, 'Weidstrasse', '122', '8122', 'Binz'),
    (35, 'Weidstrasse', '122', '8122', 'Binz'),
    (36, 'Zelglistrasse', '866', '8127', 'Forch'),
    (37, 'Zelglistrasse', '866', '8127', 'Forch'),
    (38, 'Zelglistrasse', '867', '8127', 'Forch'),
    (39, 'Schwerzimattstrasse', '448', '8912', 'Obfelden'),
    (40, 'Schwerzimattstrasse', '448', '8912', 'Obfelden'),
    (41, 'Markusstrasse', '112', '8006', 'Zürich'),
    (42, 'Markusstrasse', '112', '8006', 'Zürich'),
    (43, 'Heugatterstrasse', '211', '8600', 'Dübendorf'),
    (44, 'Heugatterstrasse', '211', '8600', 'Dübendorf'),
    (45, 'Witellikerstrasse', '667', '8008', 'Zürich'),
    (46, 'Witellikerstrasse', '667', '8008', 'Zürich'),
    (47, 'Toggwilerstrasse', '877', '8706', 'Meilen'),
    (48, 'Tichelrütistrasse', '399', '8044', 'Gockhausen'),
    (49, 'Tichelrütistrasse', '399', '8044', 'Gockhausen');

SELECT * FROM Adresse;


-- Person
-- ******
   
INSERT INTO Person (person_id, discriminator, anrede, nachname, vorname, geburtsdatum, festnetz, natel, email, adresse_id) VALUES 
    (1, 'Angehoeriger', 'FRAU', 'Schwarz', 'Käthi', NULL, '044 720 33 44', NULL, 'hschwarz@bluewin.ch', 1),
    (2, 'Angehoeriger', 'HERR', 'Schwarz', 'Martin', NULL, '044 364 22 33', '079 273 22 33', 'marschwarz@gmail.com', 2),
    (3, 'Angehoeriger', 'FRAU', 'Meierhans', 'Sibyll', NULL, '044 364 22 33', NULL, 'sibyll.meierhans@bluewin.ch', 3),
    (4, 'Schueler', 'KEINE', 'Meierhans', 'Jonas', '2014-06-24', '044 364 22 33', NULL, NULL, 4),
    (5, 'Angehoeriger', 'HERR', 'Jucker', 'Kurt', NULL, NULL, NULL, 'kurt.jucker@zuerich.ch', NULL),
    (6, 'Angehoeriger', 'FRAU', 'Jucker', 'Eva', NULL, '044 271 88 77', '076 515 14 65', 'juckerschwarz@gmail.com', 5),
    (7, 'Schueler', 'KEINE', 'Jucker', 'Lilly', '2008-01-13', '044 271 88 77', NULL, NULL, 6),
    (8, 'Schueler', 'KEINE', 'Jucker', 'Anna', '2010-03-05', '044 271 88 77', NULL, NULL, 7),
    (9, 'Lehrkraft', 'FRAU', 'Meierhans', 'Sibyll', '1972-05-17', '044 364 22 33', NULL, 'sibyll.meierhans@bluewin.ch', 8),
    (10, 'Lehrkraft', 'FRAU', 'Zürcher', 'Sibylle', '1969-05-19', '043 322 11 22', '079 629 22 44', 'sibylle.zürcher@gmx.ch', 9),
    (11, 'Lehrkraft', 'FRAU', 'Locher', 'Franziska', '1962-04-25', '032 631 66 55', '076 378 66 55', 'franziska.locher@bluewin.ch', 10),
    (12, 'Lehrkraft', 'FRAU', 'Hofer', 'Ursina', '1971-07-17', '043 499 20 22', '079 714 89 89', 'ursina.hofer@bluewin.ch', 11),
    (13, 'Lehrkraft', 'FRAU', 'Hasler', 'Simona', '1980-07-24', NULL, '079 478 55 44', 'haslersimona@gmail.com', 12),
    (14, 'Lehrkraft', 'FRAU', 'Fischer', 'Esther', '1981-06-19', '043 537 21 21', '076 449 93 93', 'esthifischer@gmail.com', 13),
    (15, 'Lehrkraft', 'FRAU', 'Dättwyler', 'Sara', '1980-11-26', NULL, '076 566 54 54', 'saradaettwyler@gmx.ch', 14),
    (16, 'Schueler', 'KEINE', 'Amann', 'Maude', '2005-08-01', '044 271 33 33', NULL, NULL, 16),
    (17, 'Angehoeriger', 'FRAU', 'Amann', 'Sabine', NULL, '044 271 33 33', '079 771 18 18', 'amann@bluewin.ch', 17),
    (18, 'Schueler', 'KEINE', 'Braun-Pfister', 'Eléonore', '2004-10-08', NULL, NULL, NULL, 18),
    (19, 'Angehoeriger', 'FRAU', 'Braun-Pfister', 'Claire', NULL, NULL, '079 150 19 19', 'claire.braun-pfister@outlook.com', 19),
    (20, 'Schueler', 'KEINE', 'Brand', 'Sitara', '2005-10-25', '043 344 11 11', NULL, NULL, 20),
    (21, 'Angehoeriger', 'FRAU', 'Brand', 'Sibylle', NULL, '043 344 11 11', '079 274 12 12', 'sibylle.brand@gmx.ch', 21),
    (22, 'Angehoeriger', 'HERR', 'Brand', 'Roland', NULL, '043 344 11 11', '079 246 98 98', NULL, 22),
    (23, 'Schueler', 'KEINE', 'Baumann', 'Oriana', '2006-07-24', '044 450 99 88', NULL, NULL, 23),
    (24, 'Angehoeriger', 'FRAU', 'Baumann', 'Lisa', NULL, '044 450 99 88', '079 255 22 02', 'lisabaumann@sunrise.ch', 24),
    (25, 'Schueler', 'KEINE', 'Dietrich', 'Isla', '2004-10-06', '044 577 99 33', NULL, NULL, 25),
    (26, 'Angehoeriger', 'FRAU', 'Dietrich', 'Rachel', NULL, '044 577 99 33', '078 862 08 08', 'racheldietrich@hotmail.com', 26),
    (27, 'Schueler', 'KEINE', 'Graf', 'Medea', '2005-07-28', '044 382 09 09', NULL, NULL, 27),
    (28, 'Angehoeriger', 'HERR', 'Graf', 'Raoul', NULL, '044 382 09 09', '079 269 63 63', 'familie_graf@bluewin.ch', 28),
    (29, 'Schueler', 'KEINE', 'Gremlich', 'Serge', '2005-11-07', '044 363 78 78', NULL, NULL, 29),
    (30, 'Angehoeriger', 'FRAU', 'Neuenschwander', 'Isabelle', NULL, '044 363 78 78', '079 446 50 87', 'isabelle.neuenschwander@bluewin.ch', 30),
    (31, 'Angehoeriger', 'HERR', 'Gremlich', 'Ralph', NULL, '044 363 78 78', '078 653 00 00', NULL, 31),
    (32, 'Schueler', 'KEINE', 'Koller Messerli', 'Anaïs', '2005-10-24', '044 381 86 86', NULL, NULL, 32),
    (33, 'Angehoeriger', 'FRAU', 'Schwarzenegger', 'Madelaine', NULL, '044 381 86 86', '079 354 44 44', 'madelaine.schwarzenegger@bluewin.ch', 33),
    (34, 'Schueler', 'KEINE', 'Mühlemann', 'Elena', '2005-05-15', '043 366 54 54', NULL, NULL, 34),
    (35, 'Angehoeriger', 'FRAU', 'Mühlemann', 'Petra', NULL, '043 366 54 54', '076 370 45 45', 'petra@muehlemann.ch', 35),
    (36, 'Schueler', 'KEINE', 'Moser', 'Jessica', '2006-02-16', '044 980 63 63', NULL, NULL, 36),
    (37, 'Angehoeriger', 'FRAU', 'Moser', 'Jane', NULL, '044 980 63 63', '078 648 39 39', 'jane.moser@ggaw.ch', 37),
    (38, 'Angehoeriger', 'FRAU', 'Moser', 'Monica', NULL, '044 980 63 63', NULL, 'moni.moser@ggaw.ch', 38),
    (39, 'Schueler', 'KEINE', 'Pfenninger', 'Lena', '2006-01-27', '044 260 31 31', NULL, NULL, 39),
    (40, 'Angehoeriger', 'FRAU', 'Pfenninger', 'Eliska', NULL, '044 260 31 31', '076 336 92 29', 'pfenninger@sunrise.ch', 40),
    (41, 'Schueler', 'KEINE', 'Richter', 'Lü Max', '2005-12-07', '044 364 76 76', NULL, NULL, 41),
    (42, 'Angehoeriger', 'HERR', 'Richter', 'Andreas', NULL, '044 364 76 76', '078 744 05 06', 'a.richter@highspeed.ch', 42),
    (43, 'Schueler', 'KEINE', 'Seidel', 'Kiara', '2005-06-29', '043 538 24 24', NULL, NULL, 43),
    (44, 'Angehoeriger', 'HERR', 'Seidel', 'Michal', NULL, '043 538 24 24', '076 489 37 37', 'mseidel@yahoo.fr', 44),
    (45, 'Schueler', 'KEINE', 'Blatter', 'Ida', '2004-07-28', '043 810 20 20', NULL, NULL, 43),
    (46, 'Angehoeriger', 'FRAU', 'Blatter', 'Nina', NULL, '043 810 20 20', '076 392 12 34', 'nina@blatter.ch', 46),
    (47, 'Angehoeriger', 'FRAU', 'Bolliger', 'Christina', NULL, NULL, NULL, NULL, 47),
    (48, 'Schueler', 'KEINE', 'Kuster', 'Maria', '2003-09-26', NULL, NULL, NULL, 48),
    (49, 'Angehoeriger', 'FRAU', 'Kuster', 'Wilma', NULL, NULL, '079 796 56 78', 'wkuster@hotmail.ch', 49);

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
    (4, 'Elternmithilfe', 'g', 'Garderobe', 1);

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
-- ************

INSERT INTO ElternmithilfeCode (code_id) VALUES
    (3),
    (4);

SELECT * FROM ElternmithilfeCode;


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
    (21, 2, 2, '11 - 12 J', '5. - 6. Klasse', 'MITTWOCH', '17:30:00', '18:20:00', 1, NULL); 

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
    (21, 11, 0);

SELECT * FROM Kurs_Lehrkraft;


-- Schueler_Kurs
-- *************

INSERT INTO Schueler_Kurs (person_id, kurs_id) VALUES
    (7, 13),
    (7, 14),
    (7, 18),
    (8, 7),
    (8, 8),
    (8, 6),
    (16, 20),
    (18, 20),
    (20, 20),
    (23, 20),
    (25, 20),
    (27, 20),
    (29, 20),
    (32, 20),
    (34, 20),
    (36, 20),
    (39, 20),
    (41, 20),
    (43, 20),
    (45, 21),
    (48, 21);

SELECT * FROM Schueler_Kurs;


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
    (8, 3, 'B', 'Schulkind 6', '2, 3', 'Waldtier Hase 2', '4', NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, 'Eltern-Mithilfe bei Lilly Jucker erfasst.');

SELECT * FROM Maercheneinteilung;
