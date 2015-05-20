package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand extends GenericDaoCommand {

    private Schueler schueler;
    private Angehoeriger vater;
    private Angehoeriger mutter;
    private Angehoeriger rechnungsempfaenger;
    private boolean schülerBereitsErfasst;
    private boolean abweichenderRechnungsempfaenger;
    private String infoRechnungsempfaenger;
    private String infoIdentischeAdressen;
    private String infoAbweichendeAdressen;
    private String infoBereitsInDb;
    private String infoNeuErfasst;


    // Rechnungsempfänger identisch mit...
    // Separater Rechnungsempfänger
    // Adressen von .... identisch
    // Adressen von ... verschieden
    // Vater, Mutter, ... bereits in Datenbank vorhanden
    // Vater, .... wird neu angelegt
    // In DB erfasste Geschwister gefunden: ...
    // Keine andere Geschwister erfasst.


    public ValidateSchuelerCommand(Schueler schueler, Angehoeriger vater, Angehoeriger mutter, Angehoeriger rechnungsempfaenger) {
        this.schueler = schueler;
        this.vater = vater;
        this.mutter = mutter;
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    @Override
    public void execute() {

        if (checkIfSchuelerAlreadyInDb()) {
            return;
        }

        schueler.setVater(vater);
        schueler.setMutter(mutter);
        schueler.setRechnungsempfaenger(rechnungsempfaenger);

        checkRechnungsempfaenger();
        checkForIdenticalAdressen();
        checkIfAngehoerigeAlreadyInDb();

    }

    private void checkRechnungsempfaenger() {
        if (rechnungsempfaenger.isIdenticalWith(mutter)) {
            infoRechnungsempfaenger = "Rechnungsempfänger identisch mit Mutter";
            schueler.setRechnungsempfaenger(mutter);
            abweichenderRechnungsempfaenger = false;
        }
        else if (rechnungsempfaenger.isIdenticalWith(vater)) {
            infoRechnungsempfaenger = "Rechnungsempfänger identisch mit Vater";
            schueler.setRechnungsempfaenger(vater);
            abweichenderRechnungsempfaenger = false;
        }
        else {
            infoRechnungsempfaenger = "Separater Rechnungsempfänger";
            abweichenderRechnungsempfaenger = true;
        }
    }

    private boolean checkIfSchuelerAlreadyInDb() {
        return false;   //TODO
    }

    private void checkForIdenticalAdressen() {

        // 1. Alle 4 Adressen identisch
        if (abweichenderRechnungsempfaenger && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            vater.setAdresse(mutter.getAdresse());
            rechnungsempfaenger.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter, Vater und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "";
        }

        // 2. 3 Adressen identisch
        else if (schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            vater.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter und Vater haben identische Adressen";
            if (abweichenderRechnungsempfaenger) {
                infoAbweichendeAdressen = "Rechnungsempfänger hat abweichende Adresse";
            }
            else {
                infoAbweichendeAdressen = "";
            }
        }

        else if (abweichenderRechnungsempfaenger && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            rechnungsempfaenger.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Vater hat abweichende Adresse";
        }

        else if (abweichenderRechnungsempfaenger && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            schueler.setAdresse(vater.getAdresse());
            rechnungsempfaenger.setAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Schüler, Vater und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Mutter hat abweichende Adresse";
        }

        else if (abweichenderRechnungsempfaenger && mutter.getAdresse().isIdenticalWith(vater.getAdresse())
                && mutter.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            vater.setAdresse(mutter.getAdresse());
            rechnungsempfaenger.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter, Vater und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
        }

        // 3. 2 Adressen identisch
        else if (schueler.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler und Mutter haben identische Adressen";
            if (abweichenderRechnungsempfaenger) {
                infoAbweichendeAdressen = "Vater und Rechnungsempfänger haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Schüler und Vater haben identische Adressen";
            if (abweichenderRechnungsempfaenger) {
                infoAbweichendeAdressen = "Mutter und Rechnungsempfänger haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Mutter hat abweichende Adresse";
            }
        }

        else if (abweichenderRechnungsempfaenger && schueler.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            schueler.setAdresse(rechnungsempfaenger.getAdresse());
            infoIdentischeAdressen = "Schüler und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Mutter und Vater haben abweichende Adressen";
        }

        else if (mutter.getAdresse().isIdenticalWith(vater.getAdresse())) {
            vater.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter und Vater haben identische Adressen";
            if (abweichenderRechnungsempfaenger) {
                infoAbweichendeAdressen = "Schüler und Rechnungsempfänger haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
            }
        }

        else if (abweichenderRechnungsempfaenger && mutter.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            rechnungsempfaenger.setAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Schüler und Vater haben abweichende Adressen";
        }

        else if (abweichenderRechnungsempfaenger && vater.getAdresse().isIdenticalWith(rechnungsempfaenger.getAdresse())) {
            rechnungsempfaenger.setAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Vater und Rechnungsempfänger haben identische Adressen";
            infoAbweichendeAdressen = "Schüler und Mutter haben abweichende Adressen";
        }

        // 4. alle Adressen verschieden
        else {
           if (abweichenderRechnungsempfaenger) {
                infoAbweichendeAdressen = "Schüler, Mutter, Vater und Rechnungsempfänger haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler, Mutter und Vater haben abweichende Adressen";
            }
        }

    }

    private void checkIfAngehoerigeAlreadyInDb() {
        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);  //TDOD

//        Angehoeriger mutterFound = angehoerigerDao.findSpecificAngehoeriger(mutter);
//        Angehoeriger vaterFound = angehoerigerDao.findSpecificAngehoeriger(vater);
//        Angehoeriger rechnungsempfaengerFound = angehoerigerDao.findSpecificAngehoeriger(rechnungsempfaenger);
//
//        // 1. alle Angehörigen in DB
//        if (abweichenderRechnungsempfaenger && mutterFound != null && vaterFound != null && rechnungsempfaengerFound != null) {
//            mutter = mutterFound;
//            vater = vaterFound;
//            rechnungsempfaenger = rechnungsempfaengerFound;
//            infoBereitsInDb = "Mutter, Vater und Rechnungsempfänger bereits in Datenbank";
//            infoNeuErfasst = "Schüler wird neu erfasst";
//        }
//
//        // 2. 2 Angehörige schon in DB
//        else if (mutterFound != null && vaterFound != null) {
//            mutter = mutterFound;
//            vater = vaterFound;
//            infoBereitsInDb = "Mutter und Vater bereits in Datenbank";
//            if (abweichenderRechnungsempfaenger) {
//                infoNeuErfasst = "Schüler und Rechnungsempfänger werden neu erfasst";
//            }
//            else {
//                infoNeuErfasst = "Schüler wird neu erfasst";
//            }
//        }
//
//        else if (abweichenderRechnungsempfaenger && mutterFound != null && rechnungsempfaengerFound != null) {
//            mutter = mutterFound;
//            rechnungsempfaenger = rechnungsempfaengerFound;
//            infoBereitsInDb = "Mutter und Rechnungsempfänger bereits in Datenbank";
//            infoNeuErfasst = "Schüler und Vater werden neu erfasst";
//        }
//
//        else if (abweichenderRechnungsempfaenger && vaterFound != null && rechnungsempfaengerFound != null) {
//            vater = vaterFound;
//            rechnungsempfaenger = rechnungsempfaengerFound;
//            infoBereitsInDb = "Vater und Rechnungsempfänger bereits in Datenbank";
//            infoNeuErfasst = "Schüler und Mutter werden neu erfasst";
//        }
//
//        // 3. 1 Angehöriger schon in DB
//        else if (mutterFound != null) {
//            mutter = mutterFound;
//            infoBereitsInDb = "Mutter bereits in Datenbank";
//            if (abweichenderRechnungsempfaenger) {
//                infoNeuErfasst = "Schüler, Vater und Rechnungsempfänger werden neu erfasst";
//            }
//            else {
//                infoNeuErfasst = "Schüler und Vater werden neu erfasst";
//            }
//        }
//
//        else if (vaterFound != null) {
//            vater = vaterFound;
//            infoBereitsInDb = "Vater bereits in Datenbank";
//            if (abweichenderRechnungsempfaenger) {
//                infoNeuErfasst = "Schüler, Mutter und Rechnungsempfänger werden neu erfasst";
//            }
//            else {
//                infoNeuErfasst = "Schüler und Mutter werden neu erfasst";
//            }
//        }
//
//        else if (abweichenderRechnungsempfaenger && rechnungsempfaengerFound != null) {
//            rechnungsempfaenger = rechnungsempfaengerFound;
//            infoBereitsInDb = "Rechnungsempfänger bereits in Datenbank";
//            infoNeuErfasst = "Schüler, Mutter und Vater werden neu erfasst";
//        }
//
//        // 4. keine Angehörige in DB
//        else {
//            infoBereitsInDb = "";
//            if (abweichenderRechnungsempfaenger) {
//                infoNeuErfasst = "Schüler, Mutter, Vater und Rechnungsempfänger werden neu erfasst";
//            }
//            else {
//                infoNeuErfasst = "Schüler, Mutter und Vater werden neu erfasst";
//            }
//        }
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public boolean isSchülerBereitsErfasst() {
        return schülerBereitsErfasst;
    }

    public boolean isAbweichenderRechnungsempfaenger() {
        return abweichenderRechnungsempfaenger;
    }

    public String getInfoRechnungsempfaenger() {
        return infoRechnungsempfaenger;
    }

    public String getInfoIdentischeAdressen() {
        return infoIdentischeAdressen;
    }

    public String getInfoAbweichendeAdressen() {
        return infoAbweichendeAdressen;
    }

    public String getInfoBereitsInDb() {
        return infoBereitsInDb;
    }

    public String getInfoNeuErfasst() {
        return infoNeuErfasst;
    }
}

