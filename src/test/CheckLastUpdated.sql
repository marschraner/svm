-- Replace all 'yyyy-mm-dd 00:00:00' '...'
select 'Adresse' as tablename, last_modified, adresse_id as id from svm.Adresse where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Anmeldung', last_modified, anmeldung_id from svm.Anmeldung where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Code', last_modified, code_id from svm.Code where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Dispensation', last_modified, dispensation_id from svm.Dispensation where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'ElternmithilfeCode', last_modified, code_id from svm.ElternmithilfeCode where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'ElternmithilfeDrittperson', last_modified, person_id from svm.ElternmithilfeDrittperson where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Kurs', last_modified, kurs_id from svm.Kurs where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Kurs_Lehrkraft', last_modified, kurs_id from svm.Kurs_Lehrkraft where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Kursanmeldung', last_modified, kurs_id from svm.Kursanmeldung where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Kursort', last_modified, kursort_id from svm.Kursort where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Kurstyp', last_modified, kurstyp_id from svm.Kurstyp where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Lektionsgebuehren', last_modified, lektionslaenge from svm.Lektionsgebuehren where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Maerchen', last_modified, maerchen_id from svm.Maerchen where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Maercheneinteilung', last_modified, maerchen_id from svm.Maercheneinteilung where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Mitarbeiter', last_modified, person_id from svm.Mitarbeiter where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Mitarbeiter_MitarbeiterCode', last_modified, person_id from svm.Mitarbeiter_MitarbeiterCode where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'MitarbeiterCode', last_modified, code_id from svm.MitarbeiterCode where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Person', last_modified, person_id from svm.Person where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Schueler', last_modified, person_id from svm.Schueler where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Schueler_SchuelerCode', last_modified, person_id from svm.Schueler_SchuelerCode where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'SchuelerCode', last_modified, code_id from svm.SchuelerCode where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Semester', last_modified, semester_id from svm.Semester where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'Semesterrechnung', last_modified, semester_id from svm.Semesterrechnung where last_modified > 'yyyy-mm-dd 00:00:00'
union
select 'SemesterrechnungCode', last_modified, code_id from svm.SemesterrechnungCode where last_modified > 'yyyy-mm-dd 00:00:00'
;