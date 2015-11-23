package ch.metzenthin.svm.domain.model;

/**
 * @author Martin Schraner
 */
public class NachnameGratiskindFormatter implements Formatter<String> {

    @Override
    public String format(String name) {

        if (name == null) {
            return null;
        }

        // Gratiskind oder (nicht) gratis weglassen
        //name = name.replaceAll("\\s+Gratiskind", "");

        return name.replaceAll("\\s+([Nn]icht\\s+)?[Gg]ratis(kind|kinder)?", "");
    }

}
