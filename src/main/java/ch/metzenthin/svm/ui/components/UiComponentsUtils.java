package ch.metzenthin.svm.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

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
    public static void setJTableColumnWidthAsPercentages(JTable table, double... percentages) {
        final double factor = 10000;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth((int) (percentages[columnIndex] * factor));
        }
    }

    // Source: http://stackoverflow.com/questions/18378506/set-a-column-width-of-jtable-according-to-the-text-in-a-header
    public static void setJTableColumnWidthAccordingToCellContentAndHeader(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                // note some extra padding
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            TableCellRenderer headerRenderer = columnModel.getColumn(column).getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = columnModel.getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            width = Math.max(width, headerComp.getPreferredSize().width);
            columnModel.getColumn(column).setPreferredWidth(width);
        }
        DefaultTableCellRenderer stringRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
        stringRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
    }


}
