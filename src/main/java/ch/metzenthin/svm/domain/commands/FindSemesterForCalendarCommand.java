package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindSemesterForCalendarCommand implements Command {

    // input
    private final Calendar calendar;
    private final List<Semester> erfassteSemester;

    // output
    private Semester currentSemester;   // null, falls calendar in den Ferien zwischen zwei Semestern
    private Semester nextSemester;
    private Semester previousSemester;

    public FindSemesterForCalendarCommand(Calendar calendar, List<Semester> erfassteSemester) {
        this.calendar = calendar;
        this.erfassteSemester = erfassteSemester;
    }

    public FindSemesterForCalendarCommand(List<Semester> erfassteSemester) {
        // heutiges Datum
        calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.erfassteSemester = erfassteSemester;
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void execute() {
        if (erfassteSemester.isEmpty()) {
            return;
        }
        Collections.sort(erfassteSemester);  // ältestes Semester zuerst
        for (int i = 0; i < erfassteSemester.size(); i++) {
            if ((erfassteSemester.get(i).getSemesterbeginn().before(calendar) || erfassteSemester.get(i).getSemesterbeginn().equals(calendar))
                    && (erfassteSemester.get(i).getSemesterende().after(calendar) || erfassteSemester.get(i).getSemesterende().equals(calendar))) {
                // calendar liegt innerhalb eines Semesters
                currentSemester = erfassteSemester.get(i);
                nextSemester = (i > 0 ? erfassteSemester.get(i - 1) : null);
                previousSemester = (i + 1 < erfassteSemester.size() ? erfassteSemester.get(i + 1) : null);
                return;
            } else if (i + 1 < erfassteSemester.size() && erfassteSemester.get(i + 1).getSemesterende().before(calendar) && erfassteSemester.get(i).getSemesterbeginn().after(calendar)) {
                // calendar liegt in den Ferien zwischen zwei Semestern
                currentSemester = null;
                nextSemester = erfassteSemester.get(i);
                previousSemester = erfassteSemester.get(i + 1);
                return;
            }
        }
        // Keiner der beiden Fälle gefunden -> calendar liegt vor oder nach allen erfassten Semestern
        if (erfassteSemester.get(0).getSemesterende().before(calendar)) {
            currentSemester = null;
            previousSemester = erfassteSemester.get(0);
            nextSemester = null;
        } else {
            currentSemester = null;
            previousSemester = null;
            nextSemester = erfassteSemester.get(erfassteSemester.size() - 1);
        }
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public Semester getNextSemester() {
        return nextSemester;
    }

    public Semester getPreviousSemester() {
        return previousSemester;
    }
}
