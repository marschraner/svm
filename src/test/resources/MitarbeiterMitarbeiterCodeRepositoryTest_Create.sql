INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (20, 0, 'Mitarbeiter', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Mitarbeiter(person_id, ahvnummer, ibannummer, lehrkraft,
                                vertretungsmoeglichkeiten, bemerkungen, aktiv)
    VALUES (20, null, null, 1, null, null, 1);

INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (30, 0, 'Mitarbeiter', 'M', 'Märchen',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (31, 0, 'Mitarbeiter', 'L', 'Licht',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (32, 0, 'Mitarbeiter', 'S', 'Schminken',TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.MitarbeiterCode (code_id) values (30);
INSERT INTO svmtest.MitarbeiterCode (code_id) values (31);
INSERT INTO svmtest.MitarbeiterCode (code_id) values (32);

INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 30, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 31, '2025-10-01');
INSERT INTO svmtest.Mitarbeiter_MitarbeiterCode(person_id, code_id, creation_date)
    VALUES (20, 32, '2025-10-01');