-- Semester
INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (101, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-18',
            '2026-02-07', '2025-10-06', '2025-10-18', '2025-12-22', '2026-01-03',
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (102, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-18',
            '2026-02-07', '2025-10-06', '2025-10-18', '2025-12-22', '2026-01-03',
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (103, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-18',
            '2026-02-07', '2025-10-06', '2025-10-18', '2025-12-22', '2026-01-03',
            '2025-10-01', '2025-10-01');

-- Kurse mit Kurstyp und Kursort
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date,
                            last_modified)
    VALUES(201, 0, 'Tanzen Test1', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date,
                            last_modified)
    VALUES (301, 0, 'Saal Test1', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date,
                         last_modified)
    VALUES (401, 0, 101, 201, '3 - 4 J', 'Vorkindergarten',
            'MONTAG', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date,
                         last_modified)
    VALUES (402, 0, 102, 201, '3 - 4 J', 'Vorkindergarten',
            'MONTAG', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date,
                         last_modified)
    VALUES (403, 0, 103, 201, '3 - 4 J', 'Vorkindergarten',
            'MONTAG', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date,
                         last_modified)
    VALUES (404, 0, 103, 201, '3 - 4 J', 'Vorkindergarten',
            'MITTWOCH', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');

-- Angehöriger (Rechnungsempfänger) und Schüler mit einer Kursanmeldung für das ganze Semester
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (501, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (501, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (502, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (502, 'M', null, null, 501, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (601, 0, '2025-01-01', null, 502, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (502, 401, 0, '2025-01-01', null, null,
            '2025-10-01','2025-10-01');

-- Angehöriger (Rechnungsempfänger) und Schüler
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (503, 0, 'Angehoeriger', 'FRAU', 'Anna', 'Müsterli',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (503, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (504, 0, 'Schueler', 'KEINE', 'Pedro', 'Müsterli',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (504, 'M', null, null, 503, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (602, 0, '2025-01-01', null, 504, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (504, 402, 0, '2025-01-01', null, null,
            '2025-10-01','2025-10-01');

-- Angehöriger (Rechnungsempfänger) und zwei Schüler mit Kursanmeldungen innerhalb des Semesters
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (505, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (505, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (506, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (506, 'M', null, null, 505, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (603, 0, '2025-01-01', null, 506, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
VALUES (506, 403, 0, '2025-01-01', '2026-01-31', null,
        '2025-10-01','2025-10-01');

INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (507, 0, 'Schueler', 'KEINE', 'Peter', 'Muster 2',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (507, 'M', null, null, 505, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (604, 0, '2025-01-01', null, 507, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (507, 404, 0, '2025-01-01', '2025-12-31', null,
            '2025-10-01','2025-10-01');


INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (701, 0, 'Semesterrechnung', '1a', 'Handrechnung Test1',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SemesterrechnungCode (code_id) values (701);

INSERT INTO svmtest.Semesterrechnung(semester_id, person_id, version, stipendium, gratiskinder,
                                     rechnungsdatum_vorrechnung, ermaessigung_vorrechnung,
                                     ermaessigungsgrund_vorrechnung, zuschlag_vorrechnung,
                                     zuschlagsgrund_vorrechnung, anzahl_wochen_vorrechnung,
                                     wochenbetrag_vorrechnung, datum_zahlung_1_vorrechnung,
                                     betrag_zahlung_1_vorrechnung, datum_zahlung_2_vorrechnung,
                                     betrag_zahlung_2_vorrechnung, datum_zahlung_3_vorrechnung,
                                     betrag_zahlung_3_vorrechnung, rechnungsdatum_nachrechnung,
                                     ermaessigung_nachrechnung, ermaessigungsgrund_nachrechnung,
                                     zuschlag_nachrechnung, zuschlagsgrund_nachrechnung,
                                     anzahl_wochen_nachrechnung, wochenbetrag_nachrechnung,
                                     datum_zahlung_1_nachrechnung, betrag_zahlung_1_nachrechnung,
                                     datum_zahlung_2_nachrechnung, betrag_zahlung_2_nachrechnung,
                                     datum_zahlung_3_nachrechnung, betrag_zahlung_3_nachrechnung,
                                     code_id, bemerkungen, deleted, creation_date, last_modified)
    VALUES (101, 501, 0, null, 0, null, 0.00, null, 0.00, null,
            1, 0.00, null, null, null, null, null, null,
            null, 0.00, null, 0.00, null, 0, 0.00,
            null, null, null, null, null, null, 701,
            null, 0, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semesterrechnung(semester_id, person_id, version, stipendium, gratiskinder,
                                     rechnungsdatum_vorrechnung, ermaessigung_vorrechnung,
                                     ermaessigungsgrund_vorrechnung, zuschlag_vorrechnung,
                                     zuschlagsgrund_vorrechnung, anzahl_wochen_vorrechnung,
                                     wochenbetrag_vorrechnung, datum_zahlung_1_vorrechnung,
                                     betrag_zahlung_1_vorrechnung, datum_zahlung_2_vorrechnung,
                                     betrag_zahlung_2_vorrechnung, datum_zahlung_3_vorrechnung,
                                     betrag_zahlung_3_vorrechnung, rechnungsdatum_nachrechnung,
                                     ermaessigung_nachrechnung, ermaessigungsgrund_nachrechnung,
                                     zuschlag_nachrechnung, zuschlagsgrund_nachrechnung,
                                     anzahl_wochen_nachrechnung, wochenbetrag_nachrechnung,
                                     datum_zahlung_1_nachrechnung, betrag_zahlung_1_nachrechnung,
                                     datum_zahlung_2_nachrechnung, betrag_zahlung_2_nachrechnung,
                                     datum_zahlung_3_nachrechnung, betrag_zahlung_3_nachrechnung,
                                     code_id, bemerkungen, deleted, creation_date, last_modified)
    VALUES (102, 503, 0, null, 0, '2025-02-01', 0.00, null, 0.00, null,
            18, 0.00, null, null, null, null, null, null,
            '2025-02-01', 0.00, null, 0.00, null, 19, 0.00,
            null, null, null, null, null, null, 701,
            null, 0, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semesterrechnung(semester_id, person_id, version, stipendium, gratiskinder,
                                     rechnungsdatum_vorrechnung, ermaessigung_vorrechnung,
                                     ermaessigungsgrund_vorrechnung, zuschlag_vorrechnung,
                                     zuschlagsgrund_vorrechnung, anzahl_wochen_vorrechnung,
                                     wochenbetrag_vorrechnung, datum_zahlung_1_vorrechnung,
                                     betrag_zahlung_1_vorrechnung, datum_zahlung_2_vorrechnung,
                                     betrag_zahlung_2_vorrechnung, datum_zahlung_3_vorrechnung,
                                     betrag_zahlung_3_vorrechnung, rechnungsdatum_nachrechnung,
                                     ermaessigung_nachrechnung, ermaessigungsgrund_nachrechnung,
                                     zuschlag_nachrechnung, zuschlagsgrund_nachrechnung,
                                     anzahl_wochen_nachrechnung, wochenbetrag_nachrechnung,
                                     datum_zahlung_1_nachrechnung, betrag_zahlung_1_nachrechnung,
                                     datum_zahlung_2_nachrechnung, betrag_zahlung_2_nachrechnung,
                                     datum_zahlung_3_nachrechnung, betrag_zahlung_3_nachrechnung,
                                     code_id, bemerkungen, deleted, creation_date, last_modified)
    VALUES (103, 505, 0, null, 0, null, 0.00, null, 0.00, null,
            1, 0.00, null, null, null, null, null, null,
            null, 0.00, null, 0.00, null, 0, 0.00,
            null, null, null, null, null, null, 701,
            null, 0, '2025-10-01', '2025-10-01');