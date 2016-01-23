package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungBearbeitenModel extends SemesterrechnungModel {

    String getRechnungsempfaengerAnrede();
    String getRechnungsempfaengerNachname();
    String getRechnungsempfaengerStrasseNr();
    String getRechnungsempfaengerNatel();
    String getRechnungsempfaengerFestnetz();
    String getRechnungsempfaengerPlz();
    String getRechnungsempfaengerVorname();
    String getRechnungsempfaengerOrt();
    String getRechnungsempfaengerEmail();
    String getRechnungsempfaengerSchuelersVorrechnung();
    String getRechnungsempfaengerKurseVorrechnung();
    String getSechsJahresRabattVorrechnung();
    String getRabattFaktor();
    String getRechnungsbetragVorrechnung();
    String getRechnungsempfaengerSchuelersNachrechnung();
    String getRechnungsempfaengerKurseNachrechnung();
    String getSechsJahresRabattNachrechnung();
    String getRechnungsbetragNachrechnung();
    String getRestbetrag();

    void setSemesterrechnungOrigin(Semesterrechnung semesterrechnungOrigin);

    boolean checkIfRechnungsempfaengerHasEmail();
    CallDefaultEmailClientCommand.Result callEmailClient();
    void calculateWochenbetrag(Rechnungstyp rechnungstyp);
    void speichern(SemesterrechnungenTableModel semesterrechnungenTableModel);
}
