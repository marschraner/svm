package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    List<Code> getCodesAll();
}
