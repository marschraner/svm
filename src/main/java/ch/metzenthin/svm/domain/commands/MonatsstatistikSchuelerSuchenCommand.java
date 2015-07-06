package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.MonatsstatistikModel;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikSchuelerSuchenCommand extends GenericDaoCommand {

    private static final Logger LOGGER = Logger.getLogger(MonatsstatistikSchuelerSuchenCommand.class);

    // input
    private MonatsstatistikModel.AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen;
    private Calendar monatJahr;
    private StringBuilder selectStatementSb;
    private TypedQuery<Schueler> typedQuery;

    // output
    private List<Schueler> schuelerFound;

    public MonatsstatistikSchuelerSuchenCommand(MonatsstatistikModel monatsstatistikModel) {
        this.anAbmeldungenDispensationen = monatsstatistikModel.getAnAbmeldungenDispensationen();
        this.monatJahr = monatsstatistikModel.getMonatJahr();
    }

    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select s from Schueler s where");

        // Query erzeugen
        createQuery();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }

        // Sortierung
        selectStatementSb.append(" order by s.nachname, s.vorname, s.adresse.ort, s.adresse.strasse");

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = entityManager.createQuery(selectStatementSb.toString(), Schueler.class);

        // Suchparameter setzen
        setParameterStatistikMonat();

        schuelerFound = typedQuery.getResultList();
    }
    
    private void createQuery() {
        if (anAbmeldungenDispensationen == MonatsstatistikModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN) {
            selectStatementSb.append(" exists (select anm from Anmeldung anm join anm.schueler sch where anm.anmeldedatum >= :statistikMonatBeginn and anm.anmeldedatum < :statistikMonatEnde and s.personId = sch.personId) and");
        }
        if (anAbmeldungenDispensationen == MonatsstatistikModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN) {
            selectStatementSb.append(" exists (select anm from Anmeldung anm join anm.schueler sch where anm.abmeldedatum >= :statistikMonatBeginn and anm.abmeldedatum < :statistikMonatEnde and s.personId = sch.personId) and");
        }
        if (anAbmeldungenDispensationen == MonatsstatistikModel.AnAbmeldungenDispensationenSelected.DISPENSATIONEN) {
            selectStatementSb.append(" exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsbeginn < :statistikMonatEnde and dis.dispensationsende >= :statistikMonatBeginn and s.personId = sch.personId) and");
        }
    }

    private void setParameterStatistikMonat() {
        if (selectStatementSb.toString().contains(":statistikMonatBeginn")) {
            Calendar statistikMonatBeginn = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), 1);
            typedQuery.setParameter("statistikMonatBeginn", statistikMonatBeginn);
        }
        if (selectStatementSb.toString().contains(":statistikMonatEnde")) {
            Calendar statistikMonatEnde;
            if (monatJahr.get(Calendar.MONTH) == Calendar.DECEMBER) {
                statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
            } else {
                statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH) + 1, 1);
            }
            typedQuery.setParameter("statistikMonatEnde", statistikMonatEnde);
        }
    }

    public List<Schueler> getSchuelerFound() {
        return schuelerFound;
    }
}
