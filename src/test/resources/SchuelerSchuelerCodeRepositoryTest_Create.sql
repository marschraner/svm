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
    VALUES (30, 0, 'Schueler', 'M', 'Märchen',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SchuelerCode (code_id) values (30);

INSERT INTO svmtest.Schueler_SchuelerCode(person_id, code_id, creation_date)
    VALUES (21, 30, '2025-10-01');