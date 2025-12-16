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