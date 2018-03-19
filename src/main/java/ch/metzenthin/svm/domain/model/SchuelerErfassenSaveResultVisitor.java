package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public interface SchuelerErfassenSaveResultVisitor {
    void visit(ValidateSchuelerSummaryResult validateSchuelerSummaryResult);
    void visit(AngehoerigerMehrereEintraegePassenResult angehoerigerMehrereEintraegePassenResult);
    void visit(AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult);
    void visit(AngehoerigerEinEintragGleicherNameAndereAttributeResult angehoerigerEinEintragGleicherNameAndereAttributeResult);
    void visit(AngehoerigerEinEintragPasstResult angehoerigerEinEintragPasstResult);
    void visit(SchuelerBereitsInDatenbankResult schuelerBereitsInDatenbankResult);
    void visit(SchuelerErfassenSaveOkResult schuelerErfassenSaveOkResult);
    void visit(SchuelerErfassenUnerwarteterFehlerResult schuelerErfassenUnerwarteterFehlerResult);
    void visit(DrittpersonIdentischMitElternteilResult drittpersonIdentischMitElternteilResult);
    void visit(AbbrechenResult abbrechenResult);
}
