package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternteil;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface MaercheneinteilungErfassenModel extends Model {
    Gruppe getGruppe();
    String getRolle1();
    String getBilderRolle1();
    String getRolle2();
    String getBilderRolle2();
    String getRolle3();
    String getBilderRolle3();

    Elternteil getElternmithilfe();
    ElternmithilfeCode getElternmithilfeCode();
    Boolean isKuchenVorstellung1();
    Boolean isKuchenVorstellung2();
    Boolean isKuchenVorstellung3();
    Boolean isKuchenVorstellung4();
    Boolean isKuchenVorstellung5();
    Boolean isKuchenVorstellung6();
    Boolean isKuchenVorstellung7();
    Boolean isKuchenVorstellung8();
    Boolean isKuchenVorstellung9();
    String getZusatzattribut();
    String getBemerkungen();
    Maerchen getMaerchen();

    void setMaerchen(Maerchen maerchen);
    void setGruppe(Gruppe gruppe) throws SvmRequiredException;
    void setRolle1(String rolle1) throws SvmValidationException;
    void setBilderRolle1(String bilderRolle1) throws SvmValidationException;
    void setRolle2(String rolle2) throws SvmValidationException;
    void setBilderRolle2(String bilderRolle2) throws SvmValidationException;
    void setRolle3(String rolle3) throws SvmValidationException;
    void setBilderRolle3(String bilderRolle3) throws SvmValidationException;

    void setElternmithilfe(Elternteil elternmithilfe);
    void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode);
    void setKuchenVorstellung1(Boolean isSelected);
    void setKuchenVorstellung2(Boolean isSelected);
    void setKuchenVorstellung3(Boolean isSelected);
    void setKuchenVorstellung4(Boolean isSelected);
    void setKuchenVorstellung5(Boolean isSelected);
    void setKuchenVorstellung6(Boolean isSelected);
    void setKuchenVorstellung7(Boolean isSelected);
    void setKuchenVorstellung8(Boolean isSelected);
    void setKuchenVorstellung9(Boolean isSelected);
    void setZusatzattribut(String zusatzattribut) throws SvmValidationException;
    void setBemerkungen(String bemerkungen) throws SvmValidationException;
    void setMaercheneinteilungOrigin(Maercheneinteilung maercheneinteilungOrigin);

    Maerchen getMaerchenInit(List<Maerchen> selectableMaerchens);
    boolean checkIfMaerchenIsInPast();
    Elternteil[] getSelectableElternmithilfen(SchuelerDatenblattModel schuelerDatenblattModel);
    ElternmithilfeCode[] getSelectableElternmithilfeCodes(SvmModel svmModel);
    boolean checkIfElternmithilfeHasEmail(SchuelerDatenblattModel schuelerDatenblattModel);
    boolean checkIfElternmithilfeHasTelefon(SchuelerDatenblattModel schuelerDatenblattModel);
    Schueler findGeschwisterElternmithilfeBereitsErfasst(SchuelerDatenblattModel schuelerDatenblattModel);
    void speichern(MaercheneinteilungenTableModel maercheneinteilungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
