package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    void loadAll();
    void loadSchuelerCodesAll();
    void loadElternmithilfeCodesAll();
    void loadSemesterrechnungCodesAll();
    void loadLehrkraefteAll();
    void loadKursorteAll();
    void loadKurstypenAll();
    void loadSemestersAll();
    void loadMaerchensAll();
    void loadLektionsgebuehrenAll();

    List<SchuelerCode> getSchuelerCodesAll();
    List<SchuelerCode> getSelektierbareSchuelerCodesAll();
    List<ElternmithilfeCode> getElternmithilfeCodesAll();
    List<ElternmithilfeCode> getSelektierbareElternmithilfeCodesAll();
    List<SemesterrechnungCode> getSemesterrechnungCodesAll();
    List<SemesterrechnungCode> getSelektierbareSemesterrechnungCodesAll();
    List<Lehrkraft> getLehrkraefteAll();
    List<Lehrkraft> getAktiveLehrkraefteAll();
    List<Kursort> getKursorteAll();
    List<Kursort> getSelektierbareKursorteAll();
    List<Kurstyp> getKurstypenAll();
    List<Kurstyp> getSelektierbareKurstypenAll();
    List<Semester> getSemestersAll();
    List<Maerchen> getMaerchensAll();
    List<Lektionsgebuehren> getLektionsgebuehrenAllList();
    Map<Integer, BigDecimal[]> getLektionsgebuehrenAllMap();
}
