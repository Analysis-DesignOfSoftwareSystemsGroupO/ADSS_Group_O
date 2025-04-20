package Presentation;

import java.util.Scanner;

public class PresentMainMenu {

    private final PresentAgreementOptions agreementOptions;
    private final PresentOrderOptions orderOptions;
    private final PresentSupplierOptions supplierOptions;

    public PresentMainMenu(){
        agreementOptions = new PresentAgreementOptions();
        orderOptions = new PresentOrderOptions();
        supplierOptions = new PresentSupplierOptions();
    }

    public void runMainPresentation(){
        while (true){
            System.out.println("Welcome to SupplierMoudle !");
            System.out.println("1.Supplier options");
            System.out.println("2.Order options");
            System.out.println("3.Agreement options");
            System.out.println("4.Exit");
            System.out.println("Please select an option: ");
            Scanner input = new Scanner(System.in);
            try{
                int option = input.nextInt();
                switch (option){
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
            }
            catch(Exception e){
                System.out.println("Please enter a valid option!");
            }
        }


    }
}
