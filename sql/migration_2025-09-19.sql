# noinspection SqlResolveForFile
# noinspection SqlWithoutWhereForFile
ALTER TABLE svm.Adresse ADD COLUMN version INT NOT NULL AFTER adresse_id;
ALTER TABLE svm.Adresse ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER ort;
ALTER TABLE svm.Adresse CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Adresse SET creation_date = last_modified;

ALTER TABLE svm.Anmeldung ADD COLUMN version INT NOT NULL AFTER anmeldung_id;
ALTER TABLE svm.Anmeldung ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER schueler_id;
ALTER TABLE svm.Anmeldung CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Anmeldung SET creation_date = last_modified;

ALTER TABLE svm.Code ADD COLUMN version INT NOT NULL AFTER code_id;
ALTER TABLE svm.Code ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER selektierbar;
ALTER TABLE svm.Code CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Code SET creation_date = last_modified;

-- Sub-Tabellen von Code
ALTER TABLE svm.ElternmithilfeCode DROP COLUMN last_updated;
ALTER TABLE svm.MitarbeiterCode DROP COLUMN last_updated;
ALTER TABLE svm.SchuelerCode DROP COLUMN last_updated;
ALTER TABLE svm.SemesterrechnungCode DROP COLUMN last_updated;

ALTER TABLE svm.Dispensation ADD COLUMN version INT NOT NULL AFTER dispensation_id;
ALTER TABLE svm.Dispensation ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER schueler_id;
ALTER TABLE svm.Dispensation CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Dispensation SET creation_date = last_modified;

ALTER TABLE svm.Kurs ADD COLUMN version INT NOT NULL AFTER kurs_id;
ALTER TABLE svm.Kurs ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER bemerkungen;
ALTER TABLE svm.Kurs CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Kurs SET creation_date = last_modified;

-- Zur Zeit mit @ManyToMany
ALTER TABLE svm.Kurs_Lehrkraft RENAME COLUMN last_updated TO creation_date;

ALTER TABLE svm.Kursanmeldung ADD COLUMN version INT NOT NULL AFTER kurs_id;
ALTER TABLE svm.Kursanmeldung ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER bemerkungen;
ALTER TABLE svm.Kursanmeldung CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Kursanmeldung SET creation_date = last_modified;

ALTER TABLE svm.Kursort ADD COLUMN version INT NOT NULL AFTER kursort_id;
ALTER TABLE svm.Kursort ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER selektierbar;
ALTER TABLE svm.Kursort CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Kursort SET creation_date = last_modified;

ALTER TABLE svm.Kurstyp ADD COLUMN version INT NOT NULL AFTER kurstyp_id;
ALTER TABLE svm.Kurstyp ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER selektierbar;
ALTER TABLE svm.Kurstyp CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Kurstyp SET creation_date = last_modified;

ALTER TABLE svm.Lektionsgebuehren ADD COLUMN version INT NOT NULL AFTER lektionslaenge;
ALTER TABLE svm.Lektionsgebuehren ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER betrag_6_kinder;
ALTER TABLE svm.Lektionsgebuehren CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Lektionsgebuehren SET creation_date = last_modified;

ALTER TABLE svm.Maerchen ADD COLUMN version INT NOT NULL AFTER maerchen_id;
ALTER TABLE svm.Maerchen ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER anzahl_vorstellungen;
ALTER TABLE svm.Maerchen CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Maerchen SET creation_date = last_modified;

ALTER TABLE svm.Maercheneinteilung ADD COLUMN version INT NOT NULL AFTER maerchen_id;
ALTER TABLE svm.Maercheneinteilung ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER drittperson_id;
ALTER TABLE svm.Maercheneinteilung CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Maercheneinteilung SET creation_date = last_modified;

-- Zur Zeit mit @ManyToMany
ALTER TABLE svm.Mitarbeiter_MitarbeiterCode RENAME COLUMN last_updated TO creation_date;

ALTER TABLE svm.Person ADD COLUMN version INT NOT NULL AFTER person_id;
ALTER TABLE svm.Person ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER adresse_id;
ALTER TABLE svm.Person CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Person SET creation_date = last_modified;

-- Sub-Tabellen von Person
ALTER TABLE svm.Angehoeriger DROP COLUMN last_updated;
ALTER TABLE svm.ElternmithilfeDrittperson DROP COLUMN last_updated;
ALTER TABLE svm.Mitarbeiter DROP COLUMN last_updated;
ALTER TABLE svm.Schueler DROP COLUMN last_updated;

ALTER TABLE svm.Semester ADD COLUMN version INT NOT NULL AFTER semester_id;
ALTER TABLE svm.Semester ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER ferienende2;
ALTER TABLE svm.Semester CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Semester SET creation_date = last_modified;

ALTER TABLE svm.Semesterrechnung ADD COLUMN version INT NOT NULL AFTER person_id;
ALTER TABLE svm.Semesterrechnung ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER deleted;
ALTER TABLE svm.Semesterrechnung CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Semesterrechnung SET creation_date = last_modified;
