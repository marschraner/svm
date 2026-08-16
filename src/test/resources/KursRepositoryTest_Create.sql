INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (101, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-18',
            '2026-02-07', '2025-10-06', '2025-10-18', '2025-12-22', '2026-01-03',
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (102, 0, '2025/2026', 'ZWEITES_SEMESTER', '2026-02-23',
            '2026-07-11', '2026-04-27', '2026-05-09', null, null,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (103, 0, '2024/2025', 'ERSTES_SEMESTER', '2024-08-19',
            '2025-02-01', '2024-10-07', '2024-10-19', '2024-12-23', '2025-01-04',
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (104, 0, '2024/2025', 'ZWEITES_SEMESTER', '2025-02-24',
            '2025-07-12', '2025-04-21', '2025-05-03', null, null,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(201, 0, 'Tanzen Test2', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(202, 0, 'Tanzen Test1', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(203, 0, 'Tanzen Test3 Bezeichnung mit mehr als 22 Zeichen', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(204, 0, 'Tanzen Test4', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES (301, 0, 'Saal Test2', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES (302, 0, 'Saal Test1', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES (303, 0, 'Saal Test3', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (401, 0, 101, 201, '3 - 4 J', 'Vorkindergarten',
            'MONTAG', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (402, 0, 102, 203, '3 - 4 J', 'Vorkindergarten',
            'MITTWOCH', '14:00:00', '15:00:00', 303, 'Bemerkung mit;Strichpunkt',
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (403, 0, 102, 204, '3 - 4 J', 'Vorkindergarten',
            'FREITAG', '14:00:00', '15:00:00', 303, NULL,
            '2025-10-01', '2025-10-01');
-- Kurse im Semester 103
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (404, 0, 103, 201, '3 - 4 J', 'Vorkindergarten',
            'MITTWOCH', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (405, 0, 103, 202, '5 - 6 J', 'Kindergarten',
            'FREITAG', '14:00:00', '15:00:00', 302, NULL,
            '2025-10-01', '2025-10-01');
-- Kurse im Semester 104 (gleiche, wie im Semester 103)
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (406, 0, 104, 201, '3 - 4 J', 'Vorkindergarten',
            'MITTWOCH', '14:00:00', '15:00:00', 301, NULL,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (407, 0, 104, 202, '5 - 6 J', 'Kindergarten',
            'FREITAG', '14:00:00', '15:00:00', 302, NULL,
            '2025-10-01', '2025-10-01');

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
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (503, 0, 'Angehoeriger', 'FRAU', 'Martha', 'Meier',
            null, null, null, null, null,
            '2026-02-01', '2026-02-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (503, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (504, 0, 'Schueler', 'KEINE', 'Franz', 'Meier',
            null, null, null, null, null,
            '2026-02-01', '2026-02-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (504, 'M', null, null, 503, null);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (505, 0, 'Angehoeriger', 'HERR', 'Jürg', 'Müller',
            null, null, null, null, null,
            '2026-02-01', '2026-02-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (505, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (506, 0, 'Schueler', 'KEINE', 'Luzia', 'Müller',
            null, null, null, null, null,
            '2026-02-01', '2026-02-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (506, 'W', null, null, 505, null);

INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (601, 0, '2024-01-01', null, 502,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (602, 0, '2024-01-01', null, 504,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Anmeldung(anmeldung_id, version, anmeldedatum, abmeldedatum, schueler_id,
                              creation_date, last_modified)
    VALUES (603, 0, '2024-01-01', null, 506,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (502, 401, 0, '2025-01-01', null, '',
            '2025-10-01','2025-10-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (504, 403, 0, '2026-01-01', null, '',
            '2026-02-01','2026-02-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (506, 403, 0, '2026-01-01', null, '',
            '2026-02-01','2026-02-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (502, 404, 0, '2024-01-01', null, '',
            '2025-10-01','2025-10-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (504, 404, 0, '2024-01-01', null, '',
            '2026-02-01','2026-02-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (504, 405, 0, '2024-01-01', null, '',
            '2026-02-01','2026-02-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (506, 405, 0, '2024-01-01', null, '',
            '2026-02-01','2026-02-01');
INSERT INTO svmtest.Kursanmeldung(person_id, kurs_id, version, anmeldedatum, abmeldedatum,
                                  bemerkungen, creation_date, last_modified)
    VALUES (502, 405, 0, '2024-01-01', '2024-12-31', '',
            '2026-02-01','2026-02-01');

INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (507, 0, 'Mitarbeiter', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (508, 0, 'Mitarbeiter', 'FRAU', 'Monika', 'Kuster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (509, 0, 'Mitarbeiter', 'FRAU', 'Ruth', 'Meier',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (510, 0, 'Mitarbeiter', 'FRAU', 'Lea', 'Kummer',
            null, null, null, null, null, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (507, null, null, TRUE, null, null, TRUE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (508, null, null, TRUE, null, null, TRUE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (509, null, null, TRUE, null, null, TRUE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (510, null, null, TRUE, null, null, TRUE);

INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (401, 510, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (402, 507, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (403, 509, 1, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (403, 508, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (404, 507, 1, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (404, 508, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (405, 509, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (405, 510, 1, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (406, 507, 1, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (406, 508, 0, '2025-10-01');
INSERT INTO svmtest.Kurs_Lehrkraft(kurs_id, person_id, lehrkraefte_ORDER, creation_date)
    VALUES (407, 507, 0, '2025-10-01');

INSERT INTO svmtest.Lektionsgebuehren(id, lektionslaenge, version, betrag_1_kind, betrag_2_kinder,
                                      betrag_3_kinder, betrag_4_kinder, betrag_5_kinder,
                                      betrag_6_kinder, creation_date, last_modified)
    VALUES (1, 50, 0, 50.00, 40.00, 30.00, 20.00, 10.00, 1.00,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Lektionsgebuehren(id, lektionslaenge, version, betrag_1_kind, betrag_2_kinder,
                                      betrag_3_kinder, betrag_4_kinder, betrag_5_kinder,
                                      betrag_6_kinder, creation_date, last_modified)
    VALUES (2, 60, 0, 60.00, 50.00, 40.00, 30.00, 20.00, 10.00,
            '2025-10-01', '2025-10-01');
