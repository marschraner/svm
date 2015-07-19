package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LehrkraftDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLehrkraftCommand extends GenericDaoCommand {

    // input
    private Lehrkraft lehrkraft;
    private Adresse adresse;
    private Lehrkraft lehrkraftOrigin;
    private List<Lehrkraft> bereitsErfassteLehrkraefte;


    public SaveOrUpdateLehrkraftCommand(Lehrkraft lehrkraft, Adresse adresse, Lehrkraft lehrkraftOrigin, List<Lehrkraft> bereitsErfassteLehrkraefte) {
        this.lehrkraft = lehrkraft;
        this.adresse = adresse;
        this.lehrkraftOrigin = lehrkraftOrigin;
        this.bereitsErfassteLehrkraefte = bereitsErfassteLehrkraefte;
    }

    @Override
    public void execute() {
        lehrkraft.setAdresse(adresse);
        LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);
        if (lehrkraftOrigin != null) {
            // Update von codeOrigin mit Werten von code
            lehrkraftOrigin.copyAttributesFrom(lehrkraft);
            lehrkraftOrigin.getAdresse().copyAttributesFrom(lehrkraft.getAdresse());
            lehrkraftDao.save(lehrkraftOrigin);
        } else {
            Lehrkraft lehrkraftSaved = lehrkraftDao.save(lehrkraft);
            bereitsErfassteLehrkraefte.add(lehrkraftSaved);
        }
        Collections.sort(bereitsErfassteLehrkraefte);
    }

}
