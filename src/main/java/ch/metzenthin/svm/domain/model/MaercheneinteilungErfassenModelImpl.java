package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Elternmithilfe;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DetermineMaerchenInitCommand;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateMaercheneinteilungCommand;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungErfassenModelImpl extends PersonModelImpl implements MaercheneinteilungErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(MaercheneinteilungErfassenModelImpl.class);
    private static final ElternmithilfeCode ELTERNMITHILFE_CODE_KEINER = new ElternmithilfeCode();
    private static final String ROLLE_NICHT_GESETZT = "Rolle nicht gesetzt";

    private final Maercheneinteilung maercheneinteilung = new Maercheneinteilung();
    private Maercheneinteilung maercheneinteilungOrigin;
    private ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode();
    private final ElternmithilfeDrittperson elternmithilfeDrittperson = new ElternmithilfeDrittperson();

    @Override
    Person getPerson() {
        return elternmithilfeDrittperson;
    }

    @Override
    public void setMaercheneinteilungOrigin(Maercheneinteilung maercheneinteilungOrigin) {
        this.maercheneinteilungOrigin = maercheneinteilungOrigin;
    }

    @Override
    public Maerchen getMaerchen() {
        return maercheneinteilung.getMaerchen();
    }

    @Override
    public void setMaerchen(Maerchen maerchen) {
        Maerchen oldValue = maercheneinteilung.getMaerchen();
        maercheneinteilung.setMaerchen(maerchen);
        firePropertyChange(Field.MAERCHEN, oldValue, maercheneinteilung.getMaerchen());
    }

    @Override
    public Gruppe getGruppe() {
        return maercheneinteilung.getGruppe();
    }

    @Override
    public void setGruppe(Gruppe gruppe) throws SvmRequiredException {
        Gruppe oldValue = maercheneinteilung.getGruppe();
        maercheneinteilung.setGruppe(gruppe);
        firePropertyChange(Field.GRUPPE, oldValue, maercheneinteilung.getGruppe());
        if (gruppe == null) {
            invalidate();
            throw new SvmRequiredException(Field.GRUPPE);
        }
    }

    private final StringModelAttribute rolle1ModelAttribute = new StringModelAttribute(
            this,
            Field.ROLLE1, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getRolle1();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setRolle1(value);
                }
            }
    );

    @Override
    public String getRolle1() {
        return rolle1ModelAttribute.getValue();
    }

    @Override
    public void setRolle1(String rolle1) throws SvmValidationException {
        rolle1ModelAttribute.setNewValue(true, rolle1, isBulkUpdate());
    }

    private final StringModelAttribute bilderRolle1ModelAttribute = new StringModelAttribute(
            this,
            Field.BILDER_ROLLE1, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getBilderRolle1();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setBilderRolle1(value);
                }
            }
    );

    @Override
    public String getBilderRolle1() {
        return bilderRolle1ModelAttribute.getValue();
    }

    @Override
    public void setBilderRolle1(String bilderRolle1) throws SvmValidationException {
        bilderRolle1ModelAttribute.setNewValue(false, bilderRolle1, isBulkUpdate());
    }

    private final StringModelAttribute rolle2ModelAttribute = new StringModelAttribute(
            this,
            Field.ROLLE2, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getRolle2();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setRolle2(value);
                }
            }
    );

    @Override
    public String getRolle2() {
        return rolle2ModelAttribute.getValue();
    }

    @Override
    public void setRolle2(String rolle2) throws SvmValidationException {
        rolle2ModelAttribute.setNewValue(false, rolle2, isBulkUpdate());
    }

    private final StringModelAttribute bilderRolle2ModelAttribute = new StringModelAttribute(
            this,
            Field.BILDER_ROLLE2, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getBilderRolle2();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setBilderRolle2(value);
                }
            }
    );

    @Override
    public String getBilderRolle2() {
        return bilderRolle2ModelAttribute.getValue();
    }

    @Override
    public void setBilderRolle2(String bilderRolle2) throws SvmValidationException {
        bilderRolle2ModelAttribute.setNewValue(false, bilderRolle2, isBulkUpdate());
    }

    private final StringModelAttribute rolle3ModelAttribute = new StringModelAttribute(
            this,
            Field.ROLLE3, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getRolle3();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setRolle3(value);
                }
            }
    );

    @Override
    public String getRolle3() {
        return rolle3ModelAttribute.getValue();
    }

    @Override
    public void setRolle3(String rolle3) throws SvmValidationException {
        rolle3ModelAttribute.setNewValue(false, rolle3, isBulkUpdate());
    }

    private final StringModelAttribute bilderRolle3ModelAttribute = new StringModelAttribute(
            this,
            Field.BILDER_ROLLE3, 1, 60,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getBilderRolle3();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setBilderRolle3(value);
                }
            }
    );

    @Override
    public String getBilderRolle3() {
        return bilderRolle3ModelAttribute.getValue();
    }

    @Override
    public void setBilderRolle3(String bilderRolle3) throws SvmValidationException {
        bilderRolle3ModelAttribute.setNewValue(false, bilderRolle3, isBulkUpdate());
    }

    @Override
    public Maerchen getMaerchenInit(List<Maerchen> selectableMaerchens) {
        DetermineMaerchenInitCommand determineMaerchenInitCommand = new DetermineMaerchenInitCommand(selectableMaerchens);
        determineMaerchenInitCommand.execute();
        return determineMaerchenInitCommand.getMaerchenInit();
    }

    @Override
    public boolean checkIfMaerchenIsInPast() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) == Calendar.JANUARY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr1Maerchen = Integer.parseInt(maercheneinteilung.getMaerchen().getSchuljahr().substring(0, 4));
        return schuljahr1Maerchen < schuljahr1;
    }

    @Override
    public Elternmithilfe[] getSelectableElternmithilfen(SchuelerDatenblattModel schuelerDatenblattModel) {
        List<Elternmithilfe> selectableElternmithilfenList = new ArrayList<>();
        selectableElternmithilfenList.add(Elternmithilfe.KEINER);
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        if (schueler.getMutter() != null && schueler.getMutter().getAdresse() != null) {
            selectableElternmithilfenList.add(Elternmithilfe.MUTTER);
        }
        if (schueler.getVater() != null && schueler.getVater().getAdresse() != null) {
            selectableElternmithilfenList.add(Elternmithilfe.VATER);
        }
        selectableElternmithilfenList.add(Elternmithilfe.DRITTPERSON);
        return selectableElternmithilfenList.toArray(new Elternmithilfe[0]);
    }

    @Override
    public Elternmithilfe getElternmithilfe() {
        return maercheneinteilung.getElternmithilfe();
    }

    @Override
    public void setElternmithilfe(Elternmithilfe elternmithilfe) {
        if (elternmithilfe == Elternmithilfe.KEINER) {
            elternmithilfe = null;
        }
        Elternmithilfe oldValue = maercheneinteilung.getElternmithilfe();
        maercheneinteilung.setElternmithilfe(elternmithilfe);
        firePropertyChange(Field.ELTERNMITHILFE, oldValue, maercheneinteilung.getElternmithilfe());
    }

    @Override
    public ElternmithilfeCode[] getSelectableElternmithilfeCodes(SvmModel svmModel) {
        List<ElternmithilfeCode> elternmithilfeCodeList = svmModel.getSelektierbareElternmithilfeCodesAll();
        // ElternmithilfeCode darf auch leer sein
        if (!elternmithilfeCodeList.get(0).isIdenticalWith(ELTERNMITHILFE_CODE_KEINER)) {
            elternmithilfeCodeList.add(0, ELTERNMITHILFE_CODE_KEINER);
        }
        if (maercheneinteilungOrigin != null && maercheneinteilungOrigin.getElternmithilfeCode() != null) {
            // Beim Bearbeiten muss ggf auch ein nicht mehr selektierbarer Elternmithilfe-Code angezeigt werden können
            boolean found = false;
            for (ElternmithilfeCode elternmithilfeCode1 : elternmithilfeCodeList) {
                if (maercheneinteilungOrigin.getElternmithilfeCode().isIdenticalWith(elternmithilfeCode1)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                elternmithilfeCodeList.add(maercheneinteilungOrigin.getElternmithilfeCode());
            }
        }
        return elternmithilfeCodeList.toArray(new ElternmithilfeCode[0]);
    }

    @Override
    public ElternmithilfeCode getElternmithilfeCode() {
        return elternmithilfeCode;
    }

    @Override
    public void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
        if (elternmithilfeCode == ELTERNMITHILFE_CODE_KEINER) {
            elternmithilfeCode = null;
        }
        ElternmithilfeCode oldValue = this.elternmithilfeCode;
        this.elternmithilfeCode = elternmithilfeCode;
        firePropertyChange(Field.ELTERNMITHILFE_CODE, oldValue, this.elternmithilfeCode);
    }

    @Override
    public void setKuchenVorstellung1(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung1();
        maercheneinteilung.setKuchenVorstellung1(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG1, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung1() {
        return maercheneinteilung.getKuchenVorstellung1();
    }

    @Override
    public void setKuchenVorstellung2(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung2();
        maercheneinteilung.setKuchenVorstellung2(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG2, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung2() {
        return maercheneinteilung.getKuchenVorstellung2();
    }

    @Override
    public void setKuchenVorstellung3(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung3();
        maercheneinteilung.setKuchenVorstellung3(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG3, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung3() {
        return maercheneinteilung.getKuchenVorstellung3();
    }

    @Override
    public void setKuchenVorstellung4(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung4();
        maercheneinteilung.setKuchenVorstellung4(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG4, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung4() {
        return maercheneinteilung.getKuchenVorstellung4();
    }

    @Override
    public void setKuchenVorstellung5(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung5();
        maercheneinteilung.setKuchenVorstellung5(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG5, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung5() {
        return maercheneinteilung.getKuchenVorstellung5();
    }

    @Override
    public void setKuchenVorstellung6(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung6();
        maercheneinteilung.setKuchenVorstellung6(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG6, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung6() {
        return maercheneinteilung.getKuchenVorstellung6();
    }

    @Override
    public void setKuchenVorstellung7(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung7();
        maercheneinteilung.setKuchenVorstellung7(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG7, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung7() {
        return maercheneinteilung.getKuchenVorstellung7();
    }

    @Override
    public void setKuchenVorstellung8(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung8();
        maercheneinteilung.setKuchenVorstellung8(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG8, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung8() {
        return maercheneinteilung.getKuchenVorstellung8();
    }

    @Override
    public void setKuchenVorstellung9(Boolean isSelected) {
        Boolean oldValue = maercheneinteilung.getKuchenVorstellung9();
        maercheneinteilung.setKuchenVorstellung9(isSelected);
        firePropertyChange(Field.KUCHEN_VORSTELLUNG9, oldValue, isSelected);
    }

    @Override
    public Boolean isKuchenVorstellung9() {
        return maercheneinteilung.getKuchenVorstellung9();
    }

    private final StringModelAttribute zusatzattributModelAttribute = new StringModelAttribute(
            this,
            Field.ZUSATZATTRIBUT_MAERCHEN, 1, 30,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getZusatzattribut();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setZusatzattribut(value);
                }
            }
    );

    @Override
    public String getZusatzattribut() {
        return zusatzattributModelAttribute.getValue();
    }

    @Override
    public void setZusatzattribut(String zusatzattribut) throws SvmValidationException {
        zusatzattributModelAttribute.setNewValue(false, zusatzattribut, isBulkUpdate());
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 2, 100,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maercheneinteilung.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    maercheneinteilung.setBemerkungen(value);
                }
            }
    );

    @Override
    public String getBemerkungen() {
        return bemerkungenModelAttribute.getValue();
    }

    @Override
    public void setBemerkungen(String bemerkungen) throws SvmValidationException {
        bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
    }

    @Override
    public void setAnrede(Anrede anrede) {
        Anrede oldValue = getPerson().getAnrede();
        getPerson().setAnrede(anrede);
        firePropertyChange(Field.ANREDE, oldValue, getPerson().getAnrede());
    }

    @Override
    public Maerchen[] getSelectableMaerchenMaercheneinteilungOrigin() {
        return new Maerchen[]{maercheneinteilungOrigin.getMaerchen()};
    }

    @Override
    public boolean checkIfElternmithilfeHasEmail(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER
                && !checkNotEmpty(schueler.getMutter().getEmail())) {
            return false;
        } else return maercheneinteilung.getElternmithilfe() != Elternmithilfe.VATER
                || checkNotEmpty(schueler.getVater().getEmail());
    }

    @Override
    public boolean checkIfElternmithilfeHasTelefon(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER
                && !checkNotEmpty(schueler.getMutter().getFestnetz())
                && !checkNotEmpty(schueler.getMutter().getNatel())) {
            return false;
        } else return maercheneinteilung.getElternmithilfe() != Elternmithilfe.VATER
                || checkNotEmpty(schueler.getVater().getFestnetz())
                || checkNotEmpty(schueler.getVater().getNatel());
    }

    @Override
    public List<Maercheneinteilung> findMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand checkElternmithilfeBereitsBeiGeschwisterErfasstCommand = new CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand(schueler, getMaerchen());
        checkElternmithilfeBereitsBeiGeschwisterErfasstCommand.execute();
        return checkElternmithilfeBereitsBeiGeschwisterErfasstCommand
                .getMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe();
    }

    @Override
    public void speichern(MaercheneinteilungenTableModel maercheneinteilungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        maercheneinteilung.setSchueler(schuelerDatenblattModel.getSchueler());
        ElternmithilfeCode elternmithilfeCodeToBeSaved = elternmithilfeCode;
        if (elternmithilfeCode != null && elternmithilfeCode.isIdenticalWith(ELTERNMITHILFE_CODE_KEINER)) {
            elternmithilfeCodeToBeSaved = null;
        }
        ElternmithilfeDrittperson elternmithilfeDrittpersonToBeSaved = elternmithilfeDrittperson;
        Adresse elternmithilfeAdresseToBeSaved = getAdresse();
        if (maercheneinteilung.getElternmithilfe() != Elternmithilfe.DRITTPERSON) {
            elternmithilfeDrittpersonToBeSaved = null;
            elternmithilfeAdresseToBeSaved = null;
        }
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateMaercheneinteilungCommand saveOrUpdateMaercheneinteilungCommand
                = new SaveOrUpdateMaercheneinteilungCommand(maercheneinteilung,
                elternmithilfeCodeToBeSaved,
                elternmithilfeDrittpersonToBeSaved,
                elternmithilfeAdresseToBeSaved,
                maercheneinteilungOrigin,
                schuelerDatenblattModel.getSchueler().getSortedMaercheneinteilungen());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaercheneinteilungCommand);
        // TableData mit von der Datenbank upgedateter Maercheneinteilung updaten
        maercheneinteilungenTableModel.getMaercheneinteilungenTableData().setMaercheneinteilungen(
                schuelerDatenblattModel.getSchueler().getSortedMaercheneinteilungen());
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initializeCompleted() {
        if (maercheneinteilungOrigin != null) {
            setBulkUpdate(true);
            try {
                setMaerchen(maercheneinteilungOrigin.getMaerchen());
                setGruppe(maercheneinteilungOrigin.getGruppe());
                setRolle1(maercheneinteilungOrigin.getRolle1());
                setBilderRolle1(maercheneinteilungOrigin.getBilderRolle1());
                setRolle2(maercheneinteilungOrigin.getRolle2());
                setBilderRolle2(maercheneinteilungOrigin.getBilderRolle2());
                setRolle3(maercheneinteilungOrigin.getRolle3());
                setBilderRolle3(maercheneinteilungOrigin.getBilderRolle3());
                setElternmithilfe(maercheneinteilungOrigin.getElternmithilfe());
                setElternmithilfeCode(maercheneinteilungOrigin.getElternmithilfeCode());
                setKuchenVorstellung1(!maercheneinteilungOrigin.getKuchenVorstellung1()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung1(maercheneinteilungOrigin.getKuchenVorstellung1());
                setKuchenVorstellung2(!maercheneinteilungOrigin.getKuchenVorstellung2()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung2(maercheneinteilungOrigin.getKuchenVorstellung2());
                setKuchenVorstellung3(!maercheneinteilungOrigin.getKuchenVorstellung3()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung3(maercheneinteilungOrigin.getKuchenVorstellung3());
                setKuchenVorstellung4(!maercheneinteilungOrigin.getKuchenVorstellung4()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung4(maercheneinteilungOrigin.getKuchenVorstellung4());
                setKuchenVorstellung5(!maercheneinteilungOrigin.getKuchenVorstellung5()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung5(maercheneinteilungOrigin.getKuchenVorstellung5());
                setKuchenVorstellung6(!maercheneinteilungOrigin.getKuchenVorstellung6()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung6(maercheneinteilungOrigin.getKuchenVorstellung6());
                setKuchenVorstellung7(!maercheneinteilungOrigin.getKuchenVorstellung7()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung7(maercheneinteilungOrigin.getKuchenVorstellung7());
                setKuchenVorstellung8(!maercheneinteilungOrigin.getKuchenVorstellung8()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung8(maercheneinteilungOrigin.getKuchenVorstellung8());
                setKuchenVorstellung9(!maercheneinteilungOrigin.getKuchenVorstellung9()); // damit PropertyChange ausgelöst wird!
                setKuchenVorstellung9(maercheneinteilungOrigin.getKuchenVorstellung9());
                setZusatzattribut(maercheneinteilungOrigin.getZusatzattribut());
                setBemerkungen(maercheneinteilungOrigin.getBemerkungen());
                ElternmithilfeDrittperson eltermithilfeDrittpersonOrigin = maercheneinteilungOrigin.getElternmithilfeDrittperson();
                if (eltermithilfeDrittpersonOrigin != null) {
                    setAnrede(eltermithilfeDrittpersonOrigin.getAnrede());
                    setNachname(eltermithilfeDrittpersonOrigin.getNachname());
                    setVorname(eltermithilfeDrittpersonOrigin.getVorname());
                    if (eltermithilfeDrittpersonOrigin.getAdresse() != null) {
                        setStrasseHausnummer(eltermithilfeDrittpersonOrigin.getAdresse().getStrasseHausnummer());
                        setPlz(eltermithilfeDrittpersonOrigin.getAdresse().getPlz());
                        setOrt(eltermithilfeDrittpersonOrigin.getAdresse().getOrt());
                    }
                    setFestnetz(eltermithilfeDrittpersonOrigin.getFestnetz());
                    setNatel(eltermithilfeDrittpersonOrigin.getNatel());
                    setEmail(eltermithilfeDrittpersonOrigin.getEmail());
                }
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean isCompleted() {
        return !(isSetBilderRolle1() && !isSetRolle1())
                && !(isSetBilderRolle2() && !isSetRolle2())
                && !(isSetBilderRolle3() && !isSetRolle3())
                && !(isSetRolle3() && !isSetRolle2())
                && !(isSetRolle2() && !isSetRolle1())
                && !(isSetElternmithilfe() && !isSetElternmithilfeCode())
                && !(isSetElternmithilfeCode() && !isSetElternmithilfe())
                && !(isSetElternmithilfeDrittperson() && !isSetAnschriftElternmithilfeDrittperson());
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void doValidate() throws SvmValidationException {
        super.doValidate();
        if (isSetBilderRolle1() && !isSetRolle1()) {
            throw new SvmValidationException(3001, ROLLE_NICHT_GESETZT, Field.ROLLE1);
        }
        if (isSetBilderRolle2() && !isSetRolle2()) {
            throw new SvmValidationException(3002, ROLLE_NICHT_GESETZT, Field.ROLLE2);
        }
        if (isSetBilderRolle3() && !isSetRolle3()) {
            throw new SvmValidationException(3003, ROLLE_NICHT_GESETZT, Field.ROLLE3);
        }
        if (isSetRolle3() && !isSetRolle2()) {
            throw new SvmValidationException(3004, ROLLE_NICHT_GESETZT, Field.ROLLE2);
        }
        if (isSetRolle2() && !isSetRolle1()) {
            throw new SvmValidationException(3005, ROLLE_NICHT_GESETZT, Field.ROLLE1);
        }
        if (isSetElternmithilfe() && !isSetElternmithilfeCode()) {
            throw new SvmValidationException(3007, "Kein Eltern-Mithilfe-Code ausgewählt", Field.ELTERNMITHILFE_CODE);
        }
        if (isSetElternmithilfeCode() && !isSetElternmithilfe()) {
            throw new SvmValidationException(3008, "Keine Eltern-Mithilfe ausgewählt", Field.ELTERNMITHILFE);
        }
        if (isSetElternmithilfeDrittperson() && !isSetAnschriftElternmithilfeDrittperson()) {
            throw new SvmValidationException(3009, "Unvollständige Anschrift", Field.ANREDE);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSetRolle1() {
        return maercheneinteilung.getRolle1() != null;
    }

    private boolean isSetBilderRolle1() {
        return maercheneinteilung.getBilderRolle1() != null;
    }

    private boolean isSetRolle2() {
        return maercheneinteilung.getRolle2() != null;
    }

    private boolean isSetBilderRolle2() {
        return maercheneinteilung.getBilderRolle2() != null;
    }

    private boolean isSetRolle3() {
        return maercheneinteilung.getRolle3() != null;
    }

    private boolean isSetBilderRolle3() {
        return maercheneinteilung.getBilderRolle3() != null;
    }

    private boolean isSetElternmithilfe() {
        return maercheneinteilung.getElternmithilfe() != null;
    }

    private boolean isSetElternmithilfeCode() {
        return elternmithilfeCode != null && !elternmithilfeCode.isIdenticalWith(ELTERNMITHILFE_CODE_KEINER);
    }

    private boolean isSetElternmithilfeDrittperson() {
        return maercheneinteilung.getElternmithilfe() == Elternmithilfe.DRITTPERSON;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSetAnschriftElternmithilfeDrittperson() {
        return getAdresse() != null
                && elternmithilfeDrittperson.getAnrede() != null
                && checkNotEmpty(elternmithilfeDrittperson.getNachname())
                && checkNotEmpty(elternmithilfeDrittperson.getVorname())
                && checkNotEmpty(getAdresse().getStrasseHausnummer())
                && checkNotEmpty(getAdresse().getPlz())
                && checkNotEmpty(getAdresse().getOrt());
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
