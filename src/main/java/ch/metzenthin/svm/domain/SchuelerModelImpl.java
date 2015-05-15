package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.commands.CommandInvoker;
import ch.metzenthin.svm.commands.SaveAdresseCommand;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import ch.metzenthin.svm.model.entities.Person;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends AbstractModel implements SchuelerModel {

    private Person schueler; // todo $$$ Schueler
    private Adresse adresse;

    SchuelerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        schueler = new Angehoeriger(); // todo $$$ Schueler
        adresse = new Adresse();
    }

    @Override
    public String getNachname() {
        return schueler.getNachname();
    }

    private boolean checkNachname() {
        return checkNotEmpty(schueler.getNachname());
    }

    @Override
    public String getVorname() {
        return schueler.getVorname();
    }

    private boolean checkVorname() {
        return checkNotEmpty(schueler.getVorname());
    }

    @Override
    public void setNachname(String nachname) {
        schueler.setNachname(nachname);
    }

    @Override
    public void setVorname(String vorname) {
        schueler.setVorname(vorname);
    }

    @Override
    public String getStrasse() {
        return adresse.getStrasse();
    }

    private boolean checkStrasse() {
        return checkNotEmpty(adresse.getStrasse());
    }

    @Override
    public Integer getPlz() {
        return adresse.getPlz();
    }

    private boolean checkPlz() {
        return (adresse.getPlz() != null) && (adresse.getPlz() > 0);
    }

    @Override
    public String getOrt() {
        return adresse.getOrt();
    }

    private boolean checkOrt() {
        return checkNotEmpty(adresse.getOrt());
    }

    @Override
    public void setStrasse(String strasse) {
        adresse.setStrasse(strasse);
    }

    @Override
    public void setPlz(Integer plz) {
        adresse.setPlz(plz);
    }

    @Override
    public void setPlz(String plzString) {
        Integer plz = null;
        if (checkNumber(plzString)) {
            plz = toInteger(plzString);
        }
        setPlz(plz);
    }

    @Override
    public void setOrt(String ort) {
        adresse.setOrt(ort);
    }

    @Override
    public boolean isValid() {
            return checkNachname() && checkVorname() && checkStrasse() && checkPlz() && checkOrt();
    }

    @Override
    public void save() {
        SaveAdresseCommand adresseInsertCommand = new SaveAdresseCommand(adresse); // todo $$$ SaveSchuelerCommand?
        getCommandInvoker().executeCommand(adresseInsertCommand);
    }

}
