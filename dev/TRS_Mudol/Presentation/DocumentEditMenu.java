package Presentation;

import Transport_Module_Exceptions.*;
import transport_module.*;

import java.util.Map;
import java.util.Scanner;

/**
 * Console menu for editing ProductListDocument objects.
 * Allows users to add products, remove product quantities, and view document details.
 */
public class DocumentEditMenu {

    // Maps for accessing documents and products by ID and name respectively
    private final Map<Integer, ProductListDocument> documents;
    private final Map<String, Product> products;  // assumes you have products by name

    /**
     * Constructs the menu with access to existing documents and products.
     * @param documents a map of document IDs to ProductListDocument objects
     * @param products a map of product names to Product objects
     */
    public DocumentEditMenu(Map<Integer, ProductListDocument> documents, Map<String, Product> products) {
        this.documents = documents;
        this.products = products;
    }

    /**
     * Displays the document editing menu and handles user interaction.
     * Allows adding/removing products and displaying document content.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display the menu options
            System.out.println("\n=== Document Editing Menu ===");
            System.out.println("1. Add product to document");
            System.out.println("2. Remove amount from product");
            System.out.println("3. Show document");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            try{
            // Handle user selection
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
            catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a specified quantity of a product to a selected document.
     * Prompts the user for document ID, product name, and quantity.
     *
     * @param scanner the scanner used for reading user input
     */
    private void addProduct(Scanner scanner) throws ATransportModuleException{
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            // Retrieve the document
            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                throw new InvalidDocumentException("Document not found.");
            }

            // Retrieve the product
            System.out.println("Enter product name:");
            String productName = scanner.nextLine();
            Product product = products.get(productName);
            if (product == null) {
                throw new InvalidProductException("Product not found.");

            }

            // Get quantity to add
            System.out.println("Enter quantity to add:");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Add product to document
            doc.addProduct(product, quantity);
        } catch (ATransportModuleException e) {
            System.out.println("Failed to add product: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Reduces the quantity of a product in a selected document.
     * Prompts the user for document ID, product name, and quantity to remove.
     *
     * @param scanner the scanner used for reading user input
     */
    private void removeProductAmount(Scanner scanner) throws ATransportModuleException{
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            // Retrieve the document
            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                throw new InvalidDocumentException("Document not found.");
            }

            // Retrieve the product
            System.out.println("Enter product name:");
            String productName = scanner.nextLine();
            Product product = products.get(productName);
            if (product == null) {
                throw new InvalidProductException("Product not found.");
            }

            // Get amount to remove
            System.out.println("Enter quantity to remove:");
            int amount = Integer.parseInt(scanner.nextLine());

            // Remove amount from product in document
            doc.reduceAmountFromProduct(product, amount);
        } catch (ATransportModuleException e) {
            System.out.println("Failed to remove product: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the content of a selected document.
     * Prompts the user for document ID and prints the document details.
     *
     * @param scanner the scanner used for reading user input
     */
    private void showDocument(Scanner scanner) throws ATransportModuleException{
        try {
            System.out.println("Enter document ID:");
            int docId = Integer.parseInt(scanner.nextLine());

            // Retrieve the document
            ProductListDocument doc = documents.get(docId);
            if (doc == null) {
                throw new InvalidDocumentException("Document not found.");

            }

            // Display the document content (assumes toString is overridden)
            System.out.println(doc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
