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

INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(201, 0, 'Tanzen Test2', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(202, 0, 'Tanzen Test1', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(203, 0, 'Tanzen Test1', TRUE, '2025-10-01', '2025-10-01');

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
            'MITTWOCH', '14:00:00', '15:00:00', 303, NULL,
            '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (403, 0, 102, 203, '3 - 4 J', 'Vorkindergarten',
            'FREITAG', '14:00:00', '15:00:00', 303, NULL,
            '2025-10-01', '2025-10-01');