package TransportModule.transport_module;


import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;

import java.util.List;

public interface ITransportController {

    // מחזיר את כל הבקשות להובלות לשבוע הקרוב
    List<TransportDTO> getTransportNextWeek() throws Exception;

    // מחזיר את מסמכי רשימות המוצרים לפי מזהה הובלה
    List<ProductListDocumentDto> getPLDbyTransportID(String transportID) throws Exception;

    // מקצה נהג להובלה (מבחינת סטטוס ההובלה)
    void assignDriverTransport(String driverID, int transportID) throws Exception;
}