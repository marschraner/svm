INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (20, 0, 'Mitarbeiter', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (21, 0, 'Mitarbeiter', 'FRAU', 'Monika', 'Kuster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (22, 0, 'Mitarbeiter', 'FRAU', 'Ruth', 'Meier',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (23, 0, 'Mitarbeiter', 'FRAU', 'Lea', 'Kummer',
            null, null, null, null, null, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (20, null, null, TRUE, null, null, TRUE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (21, null, null, TRUE, null, null, FALSE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (22, null, null, FALSE, null, null, TRUE);
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (23, null, null, TRUE, null, null, TRUE);

