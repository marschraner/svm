INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (101, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-01', '2026-02-15',
            '2025-10-01', '2025-10-14', null, null,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (102, 0, '2025/2026', 'ZWEITES_SEMESTER', '2026-02-23',
            '2026-07-11', '2026-04-27', '2026-05-09', null, null,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id,
                           creation_date, last_modified)
    VALUES (201, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (201, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date, last_modified)
    VALUES (202, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (202, 'M', null, null, 201, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (301,0, '2025-08-01', null, 202, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (401, 0, 'Semesterrechnung', '1a', 'Handrechnung Test1',
            TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SemesterrechnungCode (code_id) values (401);

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
    VALUES (101, 201, 0, null, 0, null, 0.00,
            null, 0.00, null, 27, 0.00,
            null, null, null, null,
            null, null, null, 0.00,
            null, 0.00, null, 27, 0.00,
            null, null, null, null,
            null, null, 401, null, 0,
            '2025-10-01', '2025-10-01');


INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date,
                            last_modified)
    VALUES(501, 0, 'Tanzen Test1', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date,
                            last_modified)
    VALUES (601, 0, 'Saal Test1', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen,
                         creation_date, last_modified)
    VALUES (701, 0, 102, 501, '5 J', 'Kindergarten', 'DIENSTAG',
            '10:00:00', '11:00:00', 601, null,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen,
                         creation_date, last_modified)
    VALUES (702, 0, 102, 501, '5 J', 'Kindergarten', 'MITTWOCH',
            '10:00:00', '11:00:00', 601, null,
            '2025-10-01', '2025-10-01');
