INSERT INTO svmtest.Semester(semester_id, version, schuljahr, semesterbezeichnung, semesterbeginn,
                             semesterende, ferienbeginn1, ferienende1, ferienbeginn2, ferienende2,
                             creation_date, last_modified)
    VALUES (1, 0, '2025/2026', 'ERSTES_SEMESTER', '2025-08-18',
            '2026-02-07', '2025-10-06', '2025-10-18', '2025-12-22', '2026-01-03',
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurstyp(kurstyp_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES(11, 0, 'Tanzen', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES (51, 0, 'Saal Test2', TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Kursort(kursort_id, version, bezeichnung, selektierbar, creation_date, last_modified)
    VALUES (52, 0, 'Saal Test1', TRUE, '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Kurs(kurs_id, version, semester_id, kurstyp_id, altersbereich, stufe, wochentag,
                         zeit_beginn, zeit_ende, kursort_id, bemerkungen, creation_date, last_modified)
    VALUES (101, 0, 1, 11, '3 - 4 J', 'Vorkindergarten',
            'Montag', '14:00:00', '15:00:00', 51, NULL,
            '2025-10-01', '2025-10-01');