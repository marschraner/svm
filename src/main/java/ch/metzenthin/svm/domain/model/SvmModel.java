package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    void reloadCodesAll();
    void reloadLehrkraefteAll();

    List<Code> getCodesAll();
    List<Lehrkraft> getLehrkraefteAll();
}
