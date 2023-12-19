package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    List<SchuelerCode> getSchuelerCodesAll();

    List<SchuelerCode> getSelektierbareSchuelerCodesAll();

    List<MitarbeiterCode> getMitarbeiterCodesAll();

    List<MitarbeiterCode> getSelektierbareMitarbeiterCodesAll();

    List<ElternmithilfeCode> getElternmithilfeCodesAll();

    List<ElternmithilfeCode> getSelektierbareElternmithilfeCodesAll();

    List<SemesterrechnungCode> getSemesterrechnungCodesAll();

    List<SemesterrechnungCode> getSelektierbareSemesterrechnungCodesAll();

    List<Mitarbeiter> getMitarbeitersAll();

    List<Mitarbeiter> getAktiveLehrkraefteAll();

    List<Kursort> getKursorteAll();

    List<Kursort> getSelektierbareKursorteAll();

    List<Kurstyp> getKurstypenAll();

    List<Kurstyp> getSelektierbareKurstypenAll();

    List<Semester> getSemestersAll();

    List<Maerchen> getMaerchensAll();

    List<Lektionsgebuehren> getLektionsgebuehrenAllList();
}
