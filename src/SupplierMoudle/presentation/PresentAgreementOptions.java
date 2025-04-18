package SupplierMoudle.presentation;

import java.util.Scanner;

public class AgreementOptions {
    private MenuMain menuMain;
    public AgreementOptions(MenuMain menu) {
        this.menuMain = menu;
    }
    public void runAgreementMenu() {
        System.out.println("Pick Agreement Options:\n");
        System.out.println("1.View all Agreements");
        System.out.println("2.Add new agreement");
        System.out.println("3.Remove agreement");
        System.out.println("4.Edit agreement");
        System.out.println("5.Return to main menu");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                    case 1:
                        //TODO: domain function: view all agreements
                        break;
                    case 2:
                        //TODO: domain function to add a new Agreement
                        break;
                    case 3:
                        //TODO: domain function to remove an agreement
                        break;
                    case 4:
                        Scanner editAgreement = new Scanner(System.in);
                        System.out.println("Enter Agreement ID");
                        String agreementID = editAgreement.nextLine();
                        //TODO: domain func check if the agreement exist

                        System.out.println("Enter Agreement Description");
                        String agreementDescription = editAgreement.nextLine();
                        //add more details if necessary
                        //possible to add a for loop that edits each items discount
                        break;
                    case 5:
                        menuMain.runMainPresentation();
                        break;
                    default:
                        throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid option !\n");
            this.runAgreementMenu();
        }
    }
}
