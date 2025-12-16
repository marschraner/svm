package ch.metzenthin.svm.service;

/**
 * @author Martin Schraner
 */
public interface KursService {

  boolean existsKursByKursortId(int kursortId);

  boolean existsKursByKurstypId(int kurstypId);

  boolean existsKursByLektionslaenge(int lektionslaenge);

  boolean existsKursBySemesterId(int semesterId);
}
