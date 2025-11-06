package ch.metzenthin.svm.service;

/**
 * @author Martin Schraner
 */
public interface KursService {

  boolean existsKurseByKursortId(int kursortId);

  boolean existsKurseByKurstypId(int kurstypId);
}
