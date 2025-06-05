package HR_Mudol.DTO;

public class EmploymentContractDTO {
    private int minDayShift;
    private int minEveningShift;
    private int sickDays;
    private int daysOff;
    private int ownerId;

    public EmploymentContractDTO(int minDayShift, int minEveningShift, int sickDays, int daysOff, int ownerId) {
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
    public int getOwnerId() { return ownerId; }
}

