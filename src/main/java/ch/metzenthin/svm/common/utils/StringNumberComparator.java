package ch.metzenthin.svm.common.utils;

import java.util.Comparator;

/**
 * @author Martin Schraner
 */
public class StringNumberComparator implements Comparator<String> {

    @Override
    public int compare(String string1, String string2) {

        // Zerlegung mit ein oder mehreren nicht Wortzeichen (d.h. weder Zahl noch Buchstabe) als Trenner
        String[] spl1 = string1.split("(\\W)+");
        String[] spl2 = string2.split("(\\W)+");

        // Wortweiser Vergleich
        int i = 0;
        while (i < spl1.length && i < spl2.length) {
            String ws1 = spl1[i];
            String ws2 = spl2[i];
            int res;
            if (isInt(ws1) && isInt(ws2)) {
                // Zahlen vergleichen wenn möglich
                res = asInt(ws1) - asInt(ws2);
            } else if (isInt(ws1)) {
                // Zahlen sollen nach Buchstaben stehen
                return 1;
            } else if (isInt(ws2)) {
                return -1;
            } else {
                res = ws1.compareTo(ws2);
            }
            if (res != 0) {
                return res;
            }
            i ++;
        }
        return 0;
    }

     private boolean isInt(String s) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException f) {
            return false;
        }
    }

    private int asInt(String s) {
        return Integer.parseInt(s);
    }

}
