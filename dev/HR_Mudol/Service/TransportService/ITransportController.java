package HR_Mudol.Service.TransportService;


import HR_Mudol.DTO.TransportReqDTO;
import HR_Mudol.DTO.PLDDTO;
import java.util.List;

public interface ITransportController {

    // מחזיר את כל הבקשות להובלות לשבוע הקרוב
    List<TransportReqDTO> getTransportNextWeek();

    // מחזיר את מסמכי רשימות המוצרים לפי מזהה הובלה
    List<PLDDTO> getPLDbyTransportID(String transportID);

    // מקצה נהג למשאית (משייך נהג למשאית בהובלה מסוימת)
    void setDriverToTruck(String driverID, String transportID);

    // מקצה נהג להובלה (מבחינת סטטוס ההובלה)
    void assignDriverTransport(String driverID, String transportID);
}
