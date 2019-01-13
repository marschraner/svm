-- Replace all 'yyyy-mm-dd 00:00:00' '...'
select 'Adresse' as tablename, last_updated, adresse_id as id from Adresse where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Angehoeriger', last_updated, person_id from Angehoeriger where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Anmeldung', last_updated, anmeldung_id from Anmeldung where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Code', last_updated, code_id from Code where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Dispensation', last_updated, dispensation_id from Dispensation where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'ElternmithilfeCode', last_updated, code_id from ElternmithilfeCode where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'ElternmithilfeDrittperson', last_updated, person_id from ElternmithilfeDrittperson where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Kurs', last_updated, kurs_id from Kurs where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Kurs_Lehrkraft', last_updated, kurs_id from Kurs_Lehrkraft where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Kursanmeldung', last_updated, kurs_id from Kursanmeldung where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Kursort', last_updated, kursort_id from Kursort where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Kurstyp', last_updated, kurstyp_id from Kurstyp where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Lektionsgebuehren', last_updated, lektionslaenge from Lektionsgebuehren where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Maerchen', last_updated, maerchen_id from Maerchen where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Maercheneinteilung', last_updated, maerchen_id from Maercheneinteilung where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Mitarbeiter', last_updated, person_id from Mitarbeiter where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Mitarbeiter_MitarbeiterCode', last_updated, person_id from Mitarbeiter_MitarbeiterCode where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'MitarbeiterCode', last_updated, code_id from MitarbeiterCode where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Person', last_updated, person_id from Person where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Schueler', last_updated, person_id from Schueler where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Schueler_SchuelerCode', last_updated, person_id from Schueler_SchuelerCode where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'SchuelerCode', last_updated, code_id from SchuelerCode where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Semester', last_updated, semester_id from Semester where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'Semesterrechnung', last_updated, semester_id from Semesterrechnung where last_updated > 'yyyy-mm-dd 00:00:00'
union
select 'SemesterrechnungCode', last_updated, code_id from SemesterrechnungCode where last_updated > 'yyyy-mm-dd 00:00:00'
;