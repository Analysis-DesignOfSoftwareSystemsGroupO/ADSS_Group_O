package DTO;

import SupplierMoudleSource.Domain.*;

import java.util.HashMap;
import java.util.List;

public class SupplierDTO {
    private String supplierID;
    private String supplierName;
    private BankDTO bank;
    private PaymentMethodDTO paymentMethod;
    private List<InformationContactDTO> informationContacts;
    private HashMap<String, SuppliedItemDTO> supplyProducts;
    private DeliveryDTO delivery;


    public SupplierDTO(String supplierID, BankDTO bank, PaymentMethodDTO paymentMethod, DeliveryDTO delivery,
                       List<InformationContactDTO> informationContacts, HashMap<String, SuppliedItemDTO> supplyProducts) {
        this.supplierID = supplierID;
        this.bank = bank;
        this.paymentMethod = paymentMethod;
        this.delivery = delivery;
        this.informationContacts = informationContacts;
        this.supplyProducts = supplyProducts;

    }

    public String getSupplierID() {
        return supplierID;
    }
}