package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.model.EmailAndNachnameAndVornameIfc;
import ch.metzenthin.svm.service.result.CallEmailClientResult;
import ch.metzenthin.svm.service.result.CheckEmailResult;
import java.util.Iterator;

/**
 * @author Hans Stamm
 */
public interface EmailService {

  CheckEmailResult checkEmailAdressen(
      Iterator<? extends EmailAndNachnameAndVornameIfc> emailAndNachnameAndVornameIterator);

  CallEmailClientResult callDefaultEmailClient(
      Iterator<? extends EmailAndNachnameAndVornameIfc> emailContactIterator, boolean blindkopien);
}
