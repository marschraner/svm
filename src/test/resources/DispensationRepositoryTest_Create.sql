INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id,
                           creation_date, last_modified)
    VALUES (101, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (101, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id,
                           creation_date, last_modified)
    VALUES (102, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (102, 'M', null, null, 101, null);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id,
                           creation_date, last_modified)
    VALUES (103, 0, 'Angehoeriger', 'FRAU', 'Marie', 'Kuster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (103, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id,
                           creation_date, last_modified)
    VALUES (104, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (104, 'M', null, null, 103, null);

INSERT INTO svmtest.Dispensation(dispensation_id, version, dispensationsbeginn, dispensationsende,
                                 voraussichtliche_dauer, grund, schueler_id,
                                 creation_date, last_modified)
    VALUES (201, 0, '2026-03-01', '2026-03-31',
            'unbekannt', 'Fuss gebrochen', 102,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Dispensation(dispensation_id, version, dispensationsbeginn, dispensationsende,
                                 voraussichtliche_dauer, grund, schueler_id,
                                 creation_date, last_modified)
    VALUES (202, 0, '2026-04-01', '2026-04-30',
            '3 Wochen', 'unbekannt', 102,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Dispensation(dispensation_id, version, dispensationsbeginn, dispensationsende,
                                 voraussichtliche_dauer, grund, schueler_id,
                                 creation_date, last_modified)
    VALUES (203, 0, '2026-03-01', '2026-03-31',
            '3 Wochen', 'Hand gebrochen', 104,
            '2025-10-01', '2025-10-01');
