INSERT INTO svmtest.Semester(Semester.semester_id, version, schuljahr, semesterbezeichnung,
                             semesterbeginn, semesterende, ferienbeginn1, ferienende1,
                             ferienbeginn2, ferienende2, creation_date, last_modified)
    VALUES (10, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-01',
            '2026-02-15','2025-10-01', '2025-10-14', null, null,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date, last_modified)
    VALUES (20, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (20, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date, last_modified)
    VALUES (21, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id, bemerkungen)
    VALUES (21, 'M', null, null, 20, null);

INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (30, 0, 'Semesterrechnung', '1a', 'Handrechnung Test1',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SemesterrechnungCode (code_id) values (30);

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
    VALUES (10, 20, 0, null, 0, null, 0.00, null, 0.00, null,
            1, 0.00, null, null, null, null, null, null,
            null, 0.00, null, 0.00, null, 0, 0.00,
            null, null, null, null, null, null, 30,
            null, 0, '2025-10-01', '2025-10-01');