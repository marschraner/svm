package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface MitarbeiterMitarbeiterCodeService extends ReferencedCodeService {

  List<MitarbeiterCode> findMitarbeiterCodesByMitarbeiterId(int mitarbeiterId);
}
