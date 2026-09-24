INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (20, 0, 'Mitarbeiter', 'FRAU', 'Milka', 'Muster',
            '2000-02-28', null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (21, 0, 'Mitarbeiter', 'FRAU', 'Monika', 'Kuster',
            '2000-01-01', null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (22, 0, 'Mitarbeiter', 'FRAU', 'Ruth', 'Meier',
            '1980-06-28', null, null, null, null, '2025-10-01', '2025-10-01');
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

INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (100, 0, 'Mitarbeiter', 'A1', 'Beschreibung A1', TRUE,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.MitarbeiterCode(code_id) VALUES (100);
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (101, 0, 'Mitarbeiter', 'A2', 'Beschreibung A2', FALSE,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.MitarbeiterCode(code_id) VALUES (101);
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (102, 0, 'Mitarbeiter', 'A3', 'Beschreibung A3', TRUE,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.MitarbeiterCode(code_id) VALUES (102);

INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 100, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 101, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 102, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (22, 101, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (23, 100, '2025-10-01');
