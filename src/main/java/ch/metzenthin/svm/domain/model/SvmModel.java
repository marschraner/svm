package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    void loadSchuelerCodesAll();
    void loadMaerchenCodesAll();
    void loadLehrkraefteAll();
    void loadKursorteAll();
    void loadKurstypenAll();
    void loadSemestersAll();

    List<SchuelerCode> getSchuelerCodesAll();
    List<MaerchenCode> getMaerchenCodesAll();
    List<Lehrkraft> getLehrkraefteAll();
    List<Lehrkraft> getAktiveLehrkraefteAll();
    List<Kursort> getKursorteAll();
    List<Kurstyp> getKurstypenAll();
    List<Semester> getSemestersAll();
}
