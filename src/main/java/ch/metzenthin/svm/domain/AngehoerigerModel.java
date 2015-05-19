package ch.metzenthin.svm.domain;

/**
 * @author Hans Stamm
 */
public interface AngehoerigerModel extends PersonModel {
    String getBeruf();

    void setBeruf(String beruf);

    void save();
}
