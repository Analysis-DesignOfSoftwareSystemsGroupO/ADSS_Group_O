package SupplierMoudleSource.Domain;

import SupplierMoudleSource.DTO.*;
import SupplierMoudleSource.Repository.AgreementRepository;
import SupplierMoudleSource.Repository.BranchesRepository;
import SupplierMoudleSource.Repository.OrderRepository;
import SupplierMoudleSource.Repository.SupplierRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class ConstantOrder {
    private Map<SuppliedItem, Integer> suppliedItems;
    private String dayOfWeek;
    private final Agreement agreement;
    private int totalPrice;


    public ConstantOrder(Agreement agreement, String dayOfWeek) {
        if (agreement == null) {
            throw new NullPointerException("Agreement is null");
        }
        this.agreement = agreement;
        this.suppliedItems = new HashMap<SuppliedItem, Integer>();
        this.dayOfWeek = dayOfWeek;
    }

    public ConstantOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        if (constantOrderDTO == null) {
            throw new NullPointerException("ConstantOrderDTO is null");
        }
        this.dayOfWeek = constantOrderDTO.getDayOfWeek();
        AgreementDTO agreementDTO = AgreementRepository.getInstance().getAgreement(constantOrderDTO.getBranchID(), constantOrderDTO.getSupplierID());
        BranchDTO branchDTO = BranchesRepository.getInstance().getBranch(constantOrderDTO.getBranchID());
        SupplierDTO supplierDTO = SupplierRepository.getInstance().getSupplier(constantOrderDTO.getSupplierID());

        this.agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
        this.suppliedItems = new HashMap<>();

        for (Map.Entry<SuppliedItemDTO, Integer> entry : constantOrderDTO.getSuppliedItems().entrySet()) {
            SuppliedItemDTO itemDTO = entry.getKey();
            int quantity = entry.getValue();

            Product product = new Product(
                    itemDTO.product.productID,
                    itemDTO.product.productName,
                    itemDTO.product.productManufacturer,
                    itemDTO.product.shelfLifeDays
            );

            SuppliedItem item = new SuppliedItem(itemDTO.suppliedItemPrice, product);
            this.suppliedItems.put(item, quantity);
        }

        this.totalPrice = this.getTotalPrice();
    }


    public void addItemToOrder(String itemId, int quantity) throws Exception {
        if (itemId == null || itemId.isEmpty() || quantity <= 0) {
            throw new NullPointerException("Product ID cannot be null or empty || Quantity cannot be less than 1");
        }

        for (SuppliedItem item : suppliedItems.keySet()) {
            if (item.getSuppliedItemID().equals(itemId)) {
                suppliedItems.put(item, quantity + suppliedItems.get(item));
                this.totalPrice = this.getTotalPrice();
                return;
            }
        }

        System.out.println("Trying to add item: " + itemId);
        for (SuppliedItem item : agreement.getSupplierItemsList()) {
            System.out.println("- Comparing to: " + item.getSuppliedItemID());
            if (item.getSuppliedItemID().equals(itemId)) {
                System.out.println("Match found – adding to order");
                suppliedItems.put(item, quantity);
                return;
            }
        }
        System.out.println("Item not found in agreement – throw exception");

        throw new Exception("Invalid item, " + itemId + " doesnt exist in the agreement, enter valid ID");
    }

    // returns total price of the order
    public int getTotalPrice() {
        int totalPrice = 0;
        List<Discount> discounts = agreement.getDiscounts();
        for (SuppliedItem item : suppliedItems.keySet()) {
            totalPrice += item.getSuppliedItemPrice() * suppliedItems.get(item);
            for (Discount discount : discounts) {
                if (discount.getSuppliedItem().equals(item) && suppliedItems.get(item) >= discount.getQuantity() ) {
                    totalPrice -= discount.getDiscount();
                }
            }
        }
        return totalPrice;
    }

    public ConstantOrderDTO getConstantOrderDTO() {
        Map<SuppliedItemDTO, Integer> suppliedItemsMap = new HashMap<>();
        for (SuppliedItem item : suppliedItems.keySet()) {
            suppliedItemsMap.put(item.getSuppliedItemDTO(), suppliedItems.get(item));
        }
        ConstantOrderDTO constantOrderDTO = new ConstantOrderDTO(this.agreement.getBranchID(), this.agreement.getSupplierID(), suppliedItemsMap, this.dayOfWeek);

        return constantOrderDTO;
    }

    public void closeConstantOrder() {
        OrderRepository.getInstance().closeConstantOrder(this);
    }

    @Override
    public String toString() {
        System.out.println("Total Price: " + this.totalPrice + "₪");
        System.out.println("Items: ");
        for (SuppliedItem item : suppliedItems.keySet()) {
            System.out.println("Item id: " + item.getSuppliedItemID() + ", Name: " + item.getProduct().getProductName() +
                    " price: " + item.getSuppliedItemPrice() + "₪");
            System.out.println("\tquantity: " + this.suppliedItems.get(item));
        }
        System.out.println("*********************************************************");
        return "";
    }

}