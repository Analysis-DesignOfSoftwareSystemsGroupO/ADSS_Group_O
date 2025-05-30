package DTO;

import SupplierMoudleSource.Domain.*;

import java.util.ArrayList;
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


    public SupplierDTO(String supplierID,String supplierName, BankDTO bank, PaymentMethodDTO paymentMethod, DeliveryDTO delivery,
                       InformationContactDTO informationContacts, HashMap<String, SuppliedItemDTO> supplyProducts) {
        this.supplierName = supplierName;
        this.supplierID = supplierID;
        this.bank = bank;
        this.paymentMethod = paymentMethod;
        this.delivery = delivery;
        this.informationContacts = new ArrayList<>();
        this.informationContacts.add(informationContacts);
        this.supplyProducts = supplyProducts;

    }

    public SupplierDTO(String supplierID,String supplierName, BankDTO bank, PaymentMethodDTO paymentMethod, DeliveryDTO delivery,
                       List<InformationContactDTO> informationContacts, HashMap<String, SuppliedItemDTO> supplyProducts) {
        this.supplierName = supplierName;
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

    public String getSupplierName() {
        return this.supplierName;
    }

    public BankDTO getBank() {
        return this.bank;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return this.paymentMethod;
    }
    public DeliveryDTO getDelivery() {
        return this.delivery;
    }
    public List<InformationContactDTO> getInformationContacts() {
        return this.informationContacts;
    }
    public HashMap<String, SuppliedItemDTO> getSupplyProducts() {
        return this.supplyProducts;
    }

    public void setName(String newName) {
        this.supplierName = newName;
    }

    public void setBank(BankDTO bankDTO) {
        this.bank = bankDTO;
    }

    public void setDeliveryMethod(DeliveryDTO deliveryDTO) {
        this.delivery = deliveryDTO;
    }
}