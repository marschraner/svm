package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMitarbeiterCommand extends GenericDaoCommand {

    // input
    private Mitarbeiter mitarbeiter;
    private Adresse adresse;
    private Mitarbeiter mitarbeiterOrigin;
    private List<Mitarbeiter> bereitsErfassteLehrkraefte;


    public SaveOrUpdateMitarbeiterCommand(Mitarbeiter mitarbeiter, Adresse adresse, Mitarbeiter mitarbeiterOrigin, List<Mitarbeiter> bereitsErfassteLehrkraefte) {
        this.mitarbeiter = mitarbeiter;
        this.adresse = adresse;
        this.mitarbeiterOrigin = mitarbeiterOrigin;
        this.bereitsErfassteLehrkraefte = bereitsErfassteLehrkraefte;
    }

    @Override
    public void execute() {
        mitarbeiter.setAdresse(adresse);
        MitarbeiterDao mitarbeiterDao = new MitarbeiterDao(entityManager);
        if (mitarbeiterOrigin != null) {
            // Update von codeOrigin mit Werten von code
            mitarbeiterOrigin.copyAttributesFrom(mitarbeiter);
            mitarbeiterOrigin.getAdresse().copyAttributesFrom(mitarbeiter.getAdresse());
            mitarbeiterDao.save(mitarbeiterOrigin);
        } else {
            Mitarbeiter mitarbeiterSaved = mitarbeiterDao.save(mitarbeiter);
            bereitsErfassteLehrkraefte.add(mitarbeiterSaved);
        }
        Collections.sort(bereitsErfassteLehrkraefte);
    }

}
