package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public class Schuljahre {

    public static int SCHULJAHR_VALID_MIN = 2000;
    public static int SCHULJAHR_VALID_MAX = 2049;

    public String[] getSchuljahre() {
        String[] schuljahre = new String[SCHULJAHR_VALID_MAX - SCHULJAHR_VALID_MIN + 1];
        for (int i = 0; i < SCHULJAHR_VALID_MAX - SCHULJAHR_VALID_MIN; i++) {
            int schuljahr1 = SCHULJAHR_VALID_MIN + i;
            int schuljahr2 = schuljahr1 + 1;
            schuljahre[i] = schuljahr1 + "/" + schuljahr2;
        }
        return schuljahre;
    }

}