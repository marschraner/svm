INSERT INTO svmtest.Maerchen(maerchen_id, version, schuljahr, bezeichnung, anzahl_vorstellungen,
                             creation_date, last_modified)
    VALUES (10, 0, '2025/2026', 'Rumpelstilzchen', 8,
            '2025-10-01', '2025-10-01');

INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (20, 0, 'Angehoeriger', 'FRAU', 'Milka', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Angehoeriger(person_id, wuenscht_emails)
    VALUES (20, 0);
INSERT INTO svmtest.Person(person_id, version, discriminator, anrede, vorname, nachname,
                           geburtsdatum, festnetz, natel, email, adresse_id, creation_date,
                           last_modified)
    VALUES (21, 0, 'Schueler', 'KEINE', 'Peter', 'Muster',
            null, null, null, null, null, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.Schueler(person_id, geschlecht, vater_id, mutter_id, rechnungsempfaenger_id,
                             bemerkungen)
    VALUES (21, 'M', null, null, 20, null);

INSERT INTO svmtest.Code(code_id, version, discriminator, kuerzel, beschreibung, selektierbar,
                         creation_date, last_modified)
    VALUES (30, 0, 'Elternmithilfe', 'ba', 'Buffet Test1',TRUE, '2025-10-01', '2025-10-01');
INSERT INTO svmtest.ElternmithilfeCode (code_id) values (30);

INSERT INTO svmtest.Maercheneinteilung(person_id, maerchen_id, version, gruppe, rolle_1,
                                       bilder_rolle_1, rolle_2, bilder_rolle_2, rolle_3,
                                       bilder_rolle_3, elternmithilfe, code_id,
                                       kuchen_vorstellung_1, kuchen_vorstellung_2,
                                       kuchen_vorstellung_3, kuchen_vorstellung_4,
                                       kuchen_vorstellung_5, kuchen_vorstellung_6,
                                       kuchen_vorstellung_7, kuchen_vorstellung_8,
                                       kuchen_vorstellung_9, zusatzattribut, bemerkungen,
                                       drittperson_id, creation_date, last_modified)
    VALUES (21, 10, 0, 'A', 'Maus', null, null, null, null, null, 'VATER',
            30, 1, 0, 0, 0, 0, 0, 1, 0, 0,
            null, null, null, '2025-10-01', '2025-10-01');