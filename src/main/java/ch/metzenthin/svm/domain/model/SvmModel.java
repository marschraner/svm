package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    void loadCodesAll();
    void loadLehrkraefteAll();
    void loadKursorteAll();
    void loadKurstypenAll();
    void loadSemestersAll();

    List<Code> getCodesAll();
    List<Lehrkraft> getLehrkraefteAll();
    List<Kursort> getKursorteAll();
    List<Kurstyp> getKurstypenAll();
    List<Semester> getSemestersAll();
}
