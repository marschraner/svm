package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.model.EmailAndNachnameAndVornameIfc;
import ch.metzenthin.svm.service.result.CallEmailClientResult;
import java.util.Iterator;

/**
 * @author Hans Stamm
 */
public interface EmailService {

  CallEmailClientResult callDefaultEmailClient(
      Iterator<? extends EmailAndNachnameAndVornameIfc> emailContactIterator, boolean blindkopien);
}
