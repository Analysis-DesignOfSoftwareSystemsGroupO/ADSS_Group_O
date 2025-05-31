package SupplierMoudleSource.Presentation;

import java.util.Scanner;
import SupplierMoudleSource.DataBase.LoadData.LoadData;

import static SupplierMoudleSource.DataBase.DatabaseInitializer.createSupplierTables;
import static SupplierMoudleSource.DataBase.DatabaseInitializer.dropAllTables;

public class PresentMainMenu {

    private final PresentAgreementOptions agreementOptions;
    private final PresentOrderOptions orderOptions;
    private final PresentSupplierOptions supplierOptions;

    public PresentMainMenu() {
        agreementOptions = new PresentAgreementOptions();
        orderOptions = new PresentOrderOptions();
        supplierOptions = new PresentSupplierOptions();
    }

    public void runMainPresentation() throws Exception {
        while (true) {
            Scanner input = new Scanner(System.in);
            boolean outer = true;
            while (outer) { //load data
                System.out.println("Welcome to Supplier Module !");
                System.out.println("1.Load Data");
                System.out.println("2.Empty Data");
                System.out.println("3.Existing Data");
                System.out.println("4.Exit");
                System.out.println("Enter your choice");
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        dropAllTables();
                        createSupplierTables();
                        LoadData loadData = new LoadData();
                        loadData.LoadData();
                        outer = false;
                        break;
                    case 2:
                        dropAllTables();
                        createSupplierTables();
                        outer = false;
                        break;
                    case 3:
                        outer = false;
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice !");
                        break;

                }
            }
            while (true) {
                System.out.println("1.Supplier options");
                System.out.println("2.Order options");
                System.out.println("3.Agreement options");
                System.out.println("4.Exit");
                System.out.println("Please select an option: ");

                try {
                    int option = input.nextInt();
                    switch (option) {
                        case 1:
                            this.supplierOptions.runSupplierMenu();
                            break;
                        case 2:
                            this.orderOptions.runOrderMenu();
                            break;
                        case 3:
                            this.agreementOptions.runAgreementMenu();
                            break;
                        case 4:
                            return;
                        default:
                            throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid option!");
                }
            }

        }
    }
}
