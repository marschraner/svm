package ch.metzenthin.svm.ui.components;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @author Martin Schraner
 */
public class UiComponentsUtils {

    /**
     * Source: https://kahdev.wordpress.com/2011/10/30/java-specifying-the-column-widths-of-a-jtable-as-percentages/
     *
     * Set the width of the columns as percentages.
     *
     * @param table
     *            the {@link JTable} whose columns will be set
     * @param percentages
     *            the widths of the columns as percentages; note: this method
     *            does NOT verify that all percentages add up to 100% and for
     *            the columns to appear properly, it is recommended that the
     *            widths for ALL columns be specified
     */
    public static void setJTableWidthAsPercentages(JTable table, double... percentages) {
        final double factor = 10000;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth((int) (percentages[columnIndex] * factor));
        }
    }

}
