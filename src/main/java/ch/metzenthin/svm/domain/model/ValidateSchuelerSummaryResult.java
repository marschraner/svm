package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerSummaryResult extends SchuelerErfassenSaveResult {

    private final Schueler schueler;
    private final List<Schueler> geschwister;
    private final List<Schueler> andereSchueler;
    private final String identischeAdressen;
    private final String abweichendeAdressen;
    private final boolean isMutterNeu;
    private final boolean isVaterNeu;
    private final boolean isRechnungsempfaengerNeu;
    private final static String BESCHREIBUNG = "Summary"; // todo

    public ValidateSchuelerSummaryResult(Schueler schueler, List<Schueler> geschwister, List<Schueler> andereSchueler, String identischeAdressen, String abweichendeAdressen, boolean isMutterNeu, boolean isVaterNeu, boolean isRechnungsempfaengerNeu) {
        super(ValidateSchuelerCommand.Result.CHECK_GESCHWISTER_SCHUELER_RECHNUGSEMFPAENGER_COMMAND_FINISHED);
        this.schueler = schueler;
        this.geschwister = geschwister;
        this.andereSchueler = andereSchueler;
        this.identischeAdressen = identischeAdressen;
        this.abweichendeAdressen = abweichendeAdressen;
        this.isMutterNeu = isMutterNeu;
        this.isVaterNeu = isVaterNeu;
        this.isRechnungsempfaengerNeu = isRechnungsempfaengerNeu;
    }

    public String getBeschreibung() {
        return BESCHREIBUNG;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public List<Schueler> getGeschwister() {
        return geschwister;
    }

    public List<Schueler> getAndereSchueler() {
        return andereSchueler;
    }

    public String getIdentischeAdressen() {
        return identischeAdressen;
    }

    public String getAbweichendeAdressen() {
        return abweichendeAdressen;
    }

    public boolean isMutterNeu() {
        return isMutterNeu;
    }

    public boolean isVaterNeu() {
        return isVaterNeu;
    }

    public boolean isRechnungsempfaengerNeu() {
        return isRechnungsempfaengerNeu;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

}
