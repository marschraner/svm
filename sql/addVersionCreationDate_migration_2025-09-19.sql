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

ALTER TABLE svm.Kurs_Lehrkraft RENAME COLUMN last_updated TO creation_date;

ALTER TABLE svm.Kursanmeldung ADD COLUMN version INT NOT NULL AFTER kurs_id;
ALTER TABLE svm.Kursanmeldung ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER bemerkungen;
ALTER TABLE svm.Kursanmeldung CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Kursanmeldung SET creation_date = last_modified;

ALTER TABLE svm.Person ADD COLUMN version INT NOT NULL AFTER person_id;
ALTER TABLE svm.Person ADD COLUMN creation_date TIMESTAMP NOT NULL AFTER adresse_id;
ALTER TABLE svm.Person CHANGE last_updated last_modified TIMESTAMP NOT NULL;
UPDATE svm.Person SET creation_date = last_modified;

-- Sub-Tabellen von Person
ALTER TABLE svm.Angehoeriger DROP COLUMN last_updated;
ALTER TABLE svm.ElternmithilfeDrittperson DROP COLUMN last_updated;
ALTER TABLE svm.Mitarbeiter DROP COLUMN last_updated;
ALTER TABLE svm.Schueler DROP COLUMN last_updated;
