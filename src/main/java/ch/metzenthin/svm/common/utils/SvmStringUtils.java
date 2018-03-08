package ch.metzenthin.svm.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schraner
 */
public class SvmStringUtils {

    private static final Pattern PERIOD_LINE_BREAK_PATTERN = Pattern.compile("([.,:;!?])([\\p{Blank}]*)(\\n+)([\\p{Blank}]*)(\\S)");
    private static final Pattern NO_PERIOD_LINE_BREAK_LOWER_CASE_PATTERN = Pattern.compile("([^.,:;!?\\s])([\\p{Blank}]*)(\\n+)([\\p{Blank}]*)([a-zäöüéè])");
    private static final Pattern NO_PERIOD_LINE_BREAK_UPPER_CASE_PATTERN = Pattern.compile("([^.,:;!?\\s])([\\p{Blank}]*)(\\n+)([\\p{Blank}]*)([0-9A-ZÄÖÜÉÈ\\p{Punct}])");

    public static String replaceLineBreaksBySemicolonOrPeriod(String text) {
        return replaceLineBreaksByCharsOrPeriod(text, ";");
    }

    public static String replaceLineBreaksByCommaOrPeriod(String text) {
        return replaceLineBreaksByCharsOrPeriod(text, ",");
    }

    private static String replaceLineBreaksByCharsOrPeriod(String text, String replacementForLowerCase) {

        if (text == null || !text.contains("\n")) {
            return text;
        }

        // Word1[.,:;!?]\nWord2 -> Word1[.,:;!?]Word2
        Matcher matcher = PERIOD_LINE_BREAK_PATTERN.matcher(text);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "$1 $5");
        }
        matcher.appendTail(stringBuffer);
        text = stringBuffer.toString();

        // Word1\nword2 -> Word1replacementForLowerCase word2
        matcher = NO_PERIOD_LINE_BREAK_LOWER_CASE_PATTERN.matcher(text);
        stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "$1" + replacementForLowerCase + " $5");
        }
        matcher.appendTail(stringBuffer);
        text = stringBuffer.toString();

        // Word1\nWord2 -> Word1. Word2
        matcher = NO_PERIOD_LINE_BREAK_UPPER_CASE_PATTERN.matcher(text);
        stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "$1. $5");
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static String replaceLineBreaksByHtmlBr(String text) {

        if (text == null || !text.contains("\n")) {
            return text;
        }

        return "<html>" + text.replace("\n", "<br>") + "</html>";
    }

}
