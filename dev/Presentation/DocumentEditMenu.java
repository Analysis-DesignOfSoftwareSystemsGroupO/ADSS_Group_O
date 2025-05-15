package Presentation;

import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.util.Map;
import java.util.Scanner;

/**
 * Console menu to edit ProductListDocument contents (add/remove products).
 */
public class DocumentEditMenu {

    private final Map<Integer, ProductListDocument> documents;
    private final Map<String, Product> products;  // assumes you have products by name

    public DocumentEditMenu(Map<Integer, ProductListDocument> documents, Map<String, Product> products) {
        this.documents = documents;
        this.products = products;
    }

    /**
     * Starts the editing menu loop.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Document Editing Menu ===");
            System.out.println("1. Add product to document");
            System.out.println("2. Remove amount from product");
            System.out.println("3. Show document");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> addProduct(scanner);
                case "2" -> removeProductAmount(scanner);
                case "3" -> showDocument(scanner);
                case "E", "e" -> {
                    System.out.println("Exiting document editor.");
                    running = false;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void addProduct(Scanner scanner) {
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                System.out.println("Document not found.");
                return;
            }

            System.out.println("Enter product name:");
            String productName = scanner.nextLine();
            Product product = products.get(productName);
            if (product == null) {
                System.out.println("Product not found.");
                return;
            }

            System.out.println("Enter quantity to add:");
            int quantity = Integer.parseInt(scanner.nextLine());

            doc.addProduct(product, quantity);
        } catch (ATransportModuleException e) {
            System.out.println("Failed to add product: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeProductAmount(Scanner scanner) {
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                System.out.println("Document not found.");
                return;
            }

            System.out.println("Enter product name:");
            String productName = scanner.nextLine();
            Product product = products.get(productName);
            if (product == null) {
                System.out.println("Product not found.");
                return;
            }

            System.out.println("Enter quantity to remove:");
            int amount = Integer.parseInt(scanner.nextLine());

            doc.reduceAmountFromProduct(product, amount);
        } catch (ATransportModuleException e) {
            System.out.println("Failed to remove product: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showDocument(Scanner scanner) {
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                System.out.println("Document not found.");
                return;
            }

            System.out.println(doc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
