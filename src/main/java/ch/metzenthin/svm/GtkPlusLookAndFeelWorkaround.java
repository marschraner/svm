package ch.metzenthin.svm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Hans Stamm
 */
public class GtkPlusLookAndFeelWorkaround {
    /*
     * This is free and unencumbered software released into the public domain.
     *
     * Anyone is free to copy, modify, publish, use, compile, sell, or
     * distribute this software, either in source code form or as a compiled
     * binary, for any purpose, commercial or non-commercial, and by any
     * means.
     *
     * In jurisdictions that recognize copyright laws, the author or authors
     * of this software dedicate any and all copyright interest in the
     * software to the public domain. We make this dedication for the benefit
     * of the public at large and to the detriment of our heirs and
     * successors. We intend this dedication to be an overt act of
     * relinquishment in perpetuity of all present and future rights to this
     * software under copyright law.
     *
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
     * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
     * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
     * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
     * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
     * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
     * OTHER DEALINGS IN THE SOFTWARE.
     *
     * For more information, please refer to <http://unlicense.org/>
     */
    /**
     * Swing menus are looking pretty bad on Linux when the GTK LaF is used (See
     * bug #6925412). It will most likely never be fixed anytime soon so this
     * method provides a workaround for it. It uses reflection to change the GTK
     * style objects of Swing so popup menu borders have a minimum thickness of
     * 1 and menu separators have a minimum vertical thickness of 1.
     */
    private static final Logger LOGGER = LogManager.getLogger(GtkPlusLookAndFeelWorkaround.class);

    public static void installGtkPopupBugWorkaround() {
        // Get current look-and-feel implementation class
        LookAndFeel laf = UIManager.getLookAndFeel();
        Class<?> lafClass = laf.getClass();

        // Do nothing when not using the problematic LaF
        if (!lafClass.getName().equals(
                "com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) return;

        // We do reflection from here on. Failure is silently ignored. The
        // workaround is simply not installed when something goes wrong here
        try {
            // Access the GTK style factory
            Field field = lafClass.getDeclaredField("styleFactory");
            field.setAccessible(true);
            Object styleFactory = field.get(laf);
            field.setAccessible(false);

            // Fix the horizontal and vertical thickness of popup menu style
            Object style = getGtkStyle(styleFactory, new JPopupMenu(),
                    "POPUP_MENU");
            fixGtkThickness(style, "yThickness");
            fixGtkThickness(style, "xThickness");

            // Fix the vertical thickness of the popup menu separator style
            style = getGtkStyle(styleFactory, new JSeparator(),
                    "POPUP_MENU_SEPARATOR");
            fixGtkThickness(style, "yThickness");
        } catch (Exception exception) {
            // Silently ignored. Workaround can't be applied.
            LOGGER.warn("Um den GTK+-Look-And-Feel-Workaround zu verwenden, muss SVM mit " +
                    "\"java --add-opens java.desktop/com.sun.java.swing.plaf.gtk=ALL-UNNAMED -jar <SVM.jar>\" " +
                    "gestartet werden!");
        }
    }
    /**
     * Called internally by installGtkPopupBugWorkaround to fix the thickness
     * of a GTK style field by setting it to a minimum value of 1.
     *
     * @param style     The GTK style object.
     * @param fieldName The field name.
     * @throws Exception When reflection fails.
     */
    private static void fixGtkThickness(Object style, String fieldName)
            throws Exception {
        Field field = style.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(style, Math.max(1, field.getInt(style)));
        field.setAccessible(false);
    }

    /**
     * Called internally by installGtkPopupBugWorkaround. Returns a specific
     * GTK style object.
     *
     * @param styleFactory The GTK style factory.
     * @param component    The target component of the style.
     * @param regionName   The name of the target region of the style.
     * @return The GTK style.
     * @throws Exception When reflection fails.
     */
    private static Object getGtkStyle(Object styleFactory,
                                      JComponent component, String regionName) throws Exception {
        // Create the region object
        Class<?> regionClass = Class.forName("javax.swing.plaf.synth.Region");
        Field field = regionClass.getField(regionName);
        Object region = field.get(regionClass);

        // Get and return the style
        Class<?> styleFactoryClass = styleFactory.getClass();
        Method method = styleFactoryClass.getMethod("getStyle",
                JComponent.class, regionClass);
        method.setAccessible(true);
        @SuppressWarnings("JavaReflectionInvocation") Object style = method.invoke(styleFactory, component, region);
        method.setAccessible(false);
        return style;
    }
}
