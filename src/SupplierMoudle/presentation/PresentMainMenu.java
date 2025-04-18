package SupplierMoudle.presentation;

import java.util.Scanner;

public class MenuMain {

    private AgreementOptions agreementOptions;
    private OrderOptions orderOptions;
    private SupplierOptions supplierOptions;

    public MenuMain(){
        agreementOptions = new AgreementOptions(this);
        orderOptions = new OrderOptions(this);
        supplierOptions = new SupplierOptions(this);
    }

    public void runMainPresentation(){
        System.out.println("Welcome to SupplierMoudle !\n");
        System.out.println("1.Supplier options\n");
        System.out.println("2.Order options\n");
        System.out.println("3.Agreement options\n");
        System.out.println("4.Exit\n");
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
                    break;
                default:
                    throw new Exception();
            }
        }
        catch(Exception e){
            System.out.println("Please enter a valid option!");
            runMainPresentation();
        }

    }
}
