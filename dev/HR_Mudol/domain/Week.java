package HR_Mudol.domain;

import java.time.*;
import java.util.LinkedList;
import java.util.List;
import java.time.temporal.TemporalAdjusters;

/**
 * The Week class represents a work week containing multiple shifts.
 * It manages the creation, addition, and removal of shifts,
 * and tracks the deadline for employees to submit their availability constraints.
 */
public class Week {

    // List of all shifts scheduled for the week
    private List<Shift> shifts;

    // Deadline for submitting shift availability constraints
    private LocalDateTime constraintDeadline;

    /**
     * Default constructor.
     * Initializes all the shifts for a typical week (Sunday-Friday),
     * alternating between morning and evening shifts.
     * Also calculates the constraint submission deadline (Thursday at 12:00 PM).
     */
    public Week() {
        this.shifts = new LinkedList<>();

        Shift newShift;
        this.constraintDeadline = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.THURSDAY))
                .atTime(12, 0);

        WeekDay day = WeekDay.SUNDAY;
        ShiftType type = ShiftType.MORNING;

        // Create 11 shifts: 2 per weekday, 1 for Friday
        for (int i = 1; i < 12; i++) {
            if (i <= 2) {
                day = WeekDay.SUNDAY;
            } else if (i <= 4) {
                day = WeekDay.MONDAY;
            } else if (i <= 6) {
                day = WeekDay.TUESDAY;
            } else if (i <= 8) {
                day = WeekDay.WEDNESDAY;
            } else if (i <= 10) {
                day = WeekDay.THURSDAY;
            } else {
                day = WeekDay.FRIDAY;
            }

            // Alternate between morning and evening shifts
            if (i % 2 == 0) {
                type = ShiftType.EVENING;
            } else {
                type = ShiftType.MORNING;
            }

            newShift = new Shift(day, type);
            shifts.addLast(newShift);
        }
    }

    /**
     * Alternative constructor.
     * Allows manually setting a custom deadline for constraint submission.
     * Useful for testing purposes.
     * @param customDeadline the custom constraint submission deadline
     */
    public Week(LocalDateTime customDeadline) {
        this(); // Calls the default constructor to create shifts
        this.constraintDeadline = customDeadline;
    }

    /**
     * Adds a new shift to the week.
     * Only HR managers or employees already assigned to the shift are allowed to add.
     * @param caller The user attempting to add the shift.
     * @param shift The shift to be added.
     * @throws SecurityException if the caller is unauthorized.
     */
    public void addShift(User caller, Shift shift) {
        boolean isHR = caller.isManager();
        boolean isSelfAssigned = shift.getEmployees().contains(caller.getUser());

        if (!isHR && !isSelfAssigned) {
            throw new SecurityException("Only HR or employees assigned to the shift can add it.");
        }

        this.shifts.add(shift);
    }

    /**
     * Returns the list of all shifts in the week.
     * @return list of shifts
     */
    public List<Shift> getShifts() {
        return shifts;
    }

    /**
     * Retrieves a specific shift based on day and shift type (morning/evening).
     * @param day The day of the week.
     * @param type The type of shift.
     * @return the matching Shift, or null if not found
     */
    public Shift getAShift(WeekDay day, ShiftType type) {
        for (Shift s : this.shifts) {
            if (s.getDay() == day && s.getType() == type) {
                return s;
            }
        }
        return null;
    }

    /**
     * Checks whether the constraint submission period is still open.
     * @return true if the current time is before the deadline, otherwise false
     */
    public boolean isConstraintSubmissionOpen() {
        return LocalDateTime.now().isBefore(constraintDeadline);
    }

    /**
     * Returns the constraint submission deadline.
     * @return deadline date and time
     */
    public LocalDateTime getConstraintDeadline() {
        return constraintDeadline;
    }

    /**
     * Removes a shift from the week's schedule.
     * @param shift The shift to be removed.
     */
    public void removeShift(Shift shift) {
        this.shifts.remove(shift);
    }

    /**
     * Returns a string representation of the week's shifts.
     * @return string listing all shifts
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shifts:\n");
        for (Shift shift : shifts) {
            sb.append("- ").append(shift.toString()).append("\n");
        }
        return sb.toString();
    }
}
