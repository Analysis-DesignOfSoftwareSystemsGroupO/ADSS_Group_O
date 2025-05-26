package dto;

import SupplierMoudleSource.Domain.*;

import java.util.HashMap;
import java.util.List;

public class SupplierDTO {
    private String supplierID;
    private String supplierName;
    private BankDTO bank;
    private PaymentMethod paymentMethod;
    private List<InformationContactDTO> informationContacts;
    private HashMap<String, SuppliedItemDTO> supplyProducts;
    private DeliveryDTO delivery;


    public SupplierDTO(String supplierID, BankDTO bank, PaymentMethod paymentMethod, DeliveryDTO delivery,
                       List<InformationContactDTO> informationContacts, HashMap<String, SuppliedItemDTO> supplyProducts) {

    }
}
