package HR_Mudol.DTO;

public class ShiftDTO {
    private int shiftID;
    private String day;
    private String type;
    private String status;
    private int shiftManagerId;

    public ShiftDTO(int shiftID, String day, String type, String status, int shiftManagerId) {
        this.shiftID = shiftID;
        this.day = day;
        this.type = type;
        this.status = status;
        this.shiftManagerId = shiftManagerId;
    }

    public int getShiftID() { return shiftID; }
    public String getDay() { return day; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getShiftManagerId() { return shiftManagerId; }
}
