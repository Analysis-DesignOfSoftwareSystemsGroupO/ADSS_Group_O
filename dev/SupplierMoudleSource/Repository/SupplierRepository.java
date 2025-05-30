package SupplierMoudleSource.Repository;

import DTO.*;
import SupplierMoudleSource.DAO.SupplierDAO;
import SupplierMoudleSource.Domain.*;

import java.sql.SQLException;
import java.util.*;

public class SupplierRepository {
    private Map<String, SupplierDTO> suppliers;

    private SupplierDAO supplierDAO = new SupplierDAO();


    //singleton database
    private static SupplierRepository supplierRepository = null;
    public static SupplierRepository getInstance() {
        if (supplierRepository == null) {
            supplierRepository = new SupplierRepository();
        }
        return supplierRepository;
    }
    private SupplierRepository(){
        suppliers = new HashMap<>();
    }


    /**
     *Supplier Data Base Functions:
     */
    public void addSupplier(String supplierName, PaymentMethod supplierPaymentMethod,
                            BankDTO bank, InformationContactDTO informationContactDTO, Delivery deliveryWay) {
        supplierDAO.addSupplier(supplierName, bank, supplierPaymentMethod.getPaymentMethodDTO(), deliveryWay.getDeliveryDTO(), informationContactDTO);
    }



    public SupplierDTO getSupplier(String supplierID) throws Exception {
        if (supplierID == null) {
            throw new NullPointerException("supplier does not exist");
        }
        if (suppliers.containsKey(supplierID)) {
            return suppliers.get(supplierID);
        }
        else {
            try {
                SupplierDTO supplierDTO = supplierDAO.getSupplier(supplierID);
                suppliers.put(supplierID, supplierDTO);
                return supplierDTO;

            } catch (SQLException e) {
                throw new Exception("Supplier does not exist");
            }

        }

    }

    public List<Supplier> getAllSuppliers() {
        List<SupplierDTO> supplierDTOS = new ArrayList<>();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            supplierDTOS = supplierDAO.getAllSuppliers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (SupplierDTO supplierDTO : supplierDTOS){
            suppliers.add(new Supplier(supplierDTO));
        }
        return suppliers;
    }


    public void removeSupplier(String supplierID) throws Exception {
        if (suppliers.containsKey(supplierID)){
            suppliers.remove(supplierID);
        }
        try {
            supplierDAO.removeSupplier(supplierID);
        }
        catch (Exception e){
            throw new Exception("Supplier does not exist");
        }
    }


    public void addProduct(SuppliedItemDTO suppliedItemDTO, String supplierId) throws Exception {
        if (suppliers.containsKey(supplierId)){
            suppliers.get(supplierId).getSupplyProducts().put(suppliedItemDTO.product.productID, suppliedItemDTO);
        }
        supplierDAO.addProduct(supplierId, suppliedItemDTO);
    }

    public void editInformationCotact(String supplierid, InformationContactDTO informationContactDTO) throws Exception {

        if (suppliers.containsKey(supplierid)){
            SupplierDTO supplierDTO = suppliers.get(supplierid);
            List<InformationContactDTO> contacts = supplierDTO.getInformationContacts();

            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getContactName().equals(informationContactDTO.getContactName())) {
                    contacts.set(i, informationContactDTO);  // <-- Replace in the list
                    break;
                }
            }
        }

        try {
            supplierDAO.editInformationCotact(supplierid, informationContactDTO);
        } catch (Exception e) {
            throw new Exception("Failed to update contact in DAO", e);
        }


    }

    public void editBankInformation(String supplierId, BankDTO bankDTO) throws Exception {
        if (suppliers.containsKey(supplierId)){
            SupplierDTO supplierDTO = suppliers.get(supplierId);
            supplierDTO.setBank(bankDTO);
        }
        try {
            supplierDAO.editBank(supplierId, bankDTO);

        }catch (Exception e){
            throw new Exception("SupplierDoes Not Exist");
        }
    }

    public void editDeliveryMethod(String supplierID, Delivery delivery) throws Exception {
        if (suppliers.containsKey(supplierID)){
            SupplierDTO supplierDTO = suppliers.get(supplierID);
            supplierDTO.setDeliveryMethod(delivery.getDeliveryDTO());
        }
    }

    public void updateSupplierName(String supplierID, String newName) throws Exception {
        if (suppliers.containsKey(supplierID)){
            SupplierDTO supplierDTO = suppliers.get(supplierID);
            supplierDTO.setName(newName);
        }try {
            supplierDAO.editSupplierName(supplierID, newName);

        }catch (Exception e){
            throw new Exception("SupplierDoes Not Exist");
        }

    }

    public void addNewInformationContact(String supplierId, InformationContactDTO informationContactDTO) throws Exception {
        if (suppliers.containsKey(supplierId)){
            for (InformationContactDTO informationContactDTO1 :suppliers.get(supplierId).getInformationContacts()){
                if (informationContactDTO1.getContactName().equals(informationContactDTO.getContactName())) {
                    throw new Exception("Contact already exists");
                }
            }
            suppliers.get(supplierId).getInformationContacts().add(informationContactDTO);
        }
        try {
            supplierDAO.addInformationContact(supplierId, informationContactDTO);

        }catch (Exception e){
            throw new Exception("SupplierDoes Not Exist");
        }
    }




}