package HR_Mudol.DTO;

public class EmploymentContractDTO {
    private int minDayShift;
    private int minEveningShift;
    private int sickDays;
    private int daysOff;
    private long ownerId;

    public EmploymentContractDTO(int minDayShift, int minEveningShift, int sickDays, int daysOff, long ownerId) {
        this.minDayShift = minDayShift;
        this.minEveningShift = minEveningShift;
        this.sickDays = sickDays;
        this.daysOff = daysOff;
        this.ownerId = ownerId;
    }

    public int getMinDayShift() { return minDayShift; }
    public int getMinEveningShift() { return minEveningShift; }
    public int getSickDays() { return sickDays; }
    public int getDaysOff() { return daysOff; }
    public long getOwnerId() { return ownerId; }
}

