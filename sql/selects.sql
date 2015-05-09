-- DB-Abfragen
-- ***********

-- Als svm auszuführen.

-- mysql -u svm -psvm 
-- mysql> source selects.sql

USE svm

SELECT PersSchue.vorname AS 'Vorname', 
    PersSchue.nachname AS 'Nachname',
    PersSchue.geburtsdatum AS 'Geburtsdatum',
    Adr.strasse AS 'Strasse',
    Adr.hausnummer AS 'Nr',
    Adr.plz AS 'PLZ',
    Adr.ort AS 'Ort',
    PersVater.vorname AS 'Vorn. Vater',
    PersMutter.vorname AS 'Vorn. Mutter',
    Schue.anmeldedatum AS 'Anmeldedatum',
    PersRechn.vorname AS 'Vorn. Rechnungsemp.',
    Schue.bemerkungen AS 'Bemerkungen'
    FROM Schueler Schue
    INNER JOIN Person PersSchue ON PersSchue.person_id = Schue.person_id
    INNER JOIN Adresse Adr ON Adr.adresse_id = PersSchue.adresse_id
    INNER JOIN Angehoeriger Vater ON Vater.person_id = Schue.vater_id
    INNER JOIN Person PersVater ON PersVater.person_id = Vater.person_id
    INNER JOIN Angehoeriger Mutter ON Mutter.person_id = Schue.mutter_id
    INNER JOIN Person PersMutter ON PersMutter.person_id = Mutter.person_id
    INNER JOIN Angehoeriger Rechn ON Rechn.person_id = Schue.rechnungsempfaenger_id
    INNER JOIN Person PersRechn ON PersRechn.person_id = Rechn.person_id;

    
