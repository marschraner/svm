package ch.metzenthin.svm.common.utils;

import java.util.Collection;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmListUtils {

    private SvmListUtils() {
    }

    /**
     * Replaces list elements of listToBeUpdated by elements of updatedSublist.
     *
     * @param listToBeUpdated Original list, the elements of will be replaced by the elements of updatedSublist.
     * @param updatedSublist The elements of the original list will be replaced by the elements of this collection.
     * @param <T> Generic type of lists
     */
    public static <T> void updateList(List<T> listToBeUpdated, Collection<T> updatedSublist) {
        for (T updatedListEntry : updatedSublist) {
            int index = listToBeUpdated.indexOf(updatedListEntry);
            if (index >= 0) {
                listToBeUpdated.set(index, updatedListEntry);
            }
        }
    }

}
