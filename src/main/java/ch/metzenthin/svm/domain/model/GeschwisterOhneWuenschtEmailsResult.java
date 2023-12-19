package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Schueler;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.GESCHWISTER_OHNE_WUENSCHT_EMAILS;

/**
 * @author Martin Schraner
 */
public class GeschwisterOhneWuenschtEmailsResult extends SchuelerErfassenSaveResult {

    private final Schueler geschwister;
    private final boolean gemeinsameMutter;

    public GeschwisterOhneWuenschtEmailsResult(Schueler geschwister, boolean gemeinsameMutter) {
        super(GESCHWISTER_OHNE_WUENSCHT_EMAILS);
        this.geschwister = geschwister;
        this.gemeinsameMutter = gemeinsameMutter;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public String getErrorMessage() {
        String mutterOderVater = (gemeinsameMutter) ? "die Mutter" : "den Vater";
        return "Bitte \"Wünscht E-Mails\" für " + mutterOderVater + " selektieren!\n\n" +
                "Wäre \"Wünscht E-Mails\" nicht selektiert, hätte dies zur Folge, dass für das " +
                "Geschwister " + geschwister.getVorname() + " " + geschwister.getNachname() + "\nfür " +
                "keinen Elternteil \"Wünscht E-Mails\" selektiert ist, was nicht zulässig ist.";
    }
}
