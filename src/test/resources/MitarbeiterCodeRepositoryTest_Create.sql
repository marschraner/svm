INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (1, 0, 'Mitarbeiter', 'MA', 'Märchen Test1',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.MitarbeiterCode (code_id) values (1);
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (2, 0, 'Mitarbeiter', 'MB', 'Märchen Test2',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.MitarbeiterCode (code_id) values (2);
