package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveSemesterResult {
  SEMESTER_BEREITS_ERFASST,
  SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER,
  SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT,
  SPEICHERN_ERFOLGREICH
}
