package ch.metzenthin.svm.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schraner
 */
public class SvmStringUtils {

  private static final Pattern PERIOD_LINE_BREAK_PATTERN =
      Pattern.compile("([.,:;!?])([ \\t]*)(\\n+)([ \\t]*)(\\S)");
  private static final Pattern NO_PERIOD_LINE_BREAK_LOWER_CASE_PATTERN =
      Pattern.compile("([^.,:;!?\\s])([ \\t]*)(\\n+)([ \\t]*)([a-zäöüéè])");
  private static final Pattern NO_PERIOD_LINE_BREAK_UPPER_CASE_PATTERN =
      Pattern.compile("([^.,:;!?\\s])([ \\t]*)(\\n+)([ \\t]*)([0-9A-ZÄÖÜÉÈ\\p{Punct}])");

  private SvmStringUtils() {}

  public static String replaceLineBreaksBySemicolonOrPeriod(String text) {
    return replaceLineBreaksByCharsOrPeriod(text, ";");
  }

  public static String replaceLineBreaksByCommaOrPeriod(String text) {
    return replaceLineBreaksByCharsOrPeriod(text, ",");
  }

  private static String replaceLineBreaksByCharsOrPeriod(
      String text, String replacementForLowerCase) {

    if (text == null || !text.contains("\n")) {
      return text;
    }

    // Word1[.,:;!?]\nWord2 -> Word1[.,:;!?]Word2
    Matcher matcher = PERIOD_LINE_BREAK_PATTERN.matcher(text);
    StringBuilder stringBuilder = new StringBuilder();
    while (matcher.find()) {
      matcher.appendReplacement(stringBuilder, "$1 $5");
    }
    matcher.appendTail(stringBuilder);
    text = stringBuilder.toString();

    // Word1\nword2 -> Word1replacementForLowerCase word2
    matcher = NO_PERIOD_LINE_BREAK_LOWER_CASE_PATTERN.matcher(text);
    stringBuilder = new StringBuilder();
    while (matcher.find()) {
      matcher.appendReplacement(stringBuilder, "$1" + replacementForLowerCase + " $5");
    }
    matcher.appendTail(stringBuilder);
    text = stringBuilder.toString();

    // Word1\nWord2 -> Word1. Word2
    matcher = NO_PERIOD_LINE_BREAK_UPPER_CASE_PATTERN.matcher(text);
    stringBuilder = new StringBuilder();
    while (matcher.find()) {
      matcher.appendReplacement(stringBuilder, "$1. $5");
    }
    matcher.appendTail(stringBuilder);
    text = stringBuilder.toString();

    // Allfällige verbleibende \n löschen
    return text.replace("\n", "");
  }

  public static String replaceLineBreaksByHtmlBr(String text) {

    if (text == null || !text.contains("\n")) {
      return text;
    }

    return "<html>" + text.replace("\n", "<br>") + "</html>";
  }

  @SuppressWarnings("java:S3776")
  public static List<String> splitStringIntoMultipleLines(
      String string, int maxLength, int maxLines) {

    List<String> lines = new ArrayList<>();

    if (string == null) {
      return lines;
    }

    // Alle "-" durch "- " ersetzen, damit Leerschlag-Trennung wirksam wird
    String stringTmp = string.replace("-", "- ");
    // Alle "/" durch "/ " ersetzen, damit Leerschlag-Trennung wirksam wird
    stringTmp = stringTmp.replace("/", "/ ");

    String[] stringSpl = stringTmp.split("\\s");
    StringBuilder line = new StringBuilder();
    int length = 0;
    int j = 0;
    for (int i = 0; i < stringSpl.length; i++) {
      if (line.isEmpty()) {
        line = new StringBuilder(stringSpl[i]);
      } else {
        line.append(" ").append(stringSpl[i]);
      }
      length += stringSpl[i].length() + 1; // + 1 wegen Leerzeichen
      if (i == stringSpl.length - 1 || length + stringSpl[i + 1].length() > maxLength) {
        // Maximal zulässige Anzahl Zeilen erreicht -> den Rest auch rausschreiben
        if (j == maxLines - 1) {
          for (int ii = i + 1; ii < stringSpl.length; ii++) {
            line.append(" ").append(stringSpl[ii]);
          }
        }
        // Alle "- " wieder durch "-" ersetzen
        line = new StringBuilder(line.toString().replace("- ", "-"));
        // Alle "/ " wieder durch "/" ersetzen
        line = new StringBuilder(line.toString().replace("/ ", "/"));
        // Jetzige Zeile schreiben und neue Zeile beginnen
        lines.add(line.toString());
        if (j == maxLines - 1) {
          return lines;
        }
        line = new StringBuilder();
        length = 0;
        j++;
      }
    }

    return lines;
  }
}
