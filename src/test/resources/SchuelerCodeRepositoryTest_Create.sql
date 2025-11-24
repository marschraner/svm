INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (1, 0, 'Schueler', 'va', 'Versand Test1',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SchuelerCode (code_id) values (1);
INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (2, 0, 'Schueler', 'vb', 'Versand Test2',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.SchuelerCode (code_id) values (2);
