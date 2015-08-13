package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    void loadSchuelerCodesAll();
    void loadElternmithilfeCodesAll();
    void loadLehrkraefteAll();
    void loadKursorteAll();
    void loadKurstypenAll();
    void loadSemestersAll();
    void loadMaerchensAll();

    List<SchuelerCode> getSchuelerCodesAll();
    List<ElternmithilfeCode> getElternmithilfeCodesAll();
    List<Lehrkraft> getLehrkraefteAll();
    List<Lehrkraft> getAktiveLehrkraefteAll();
    List<Kursort> getKursorteAll();
    List<Kurstyp> getKurstypenAll();
    List<Semester> getSemestersAll();
    List<Maerchen> getMaerchensAll();
}
