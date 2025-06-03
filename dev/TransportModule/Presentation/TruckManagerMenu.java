package TransportModule.Presentation;
import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.TruckDto;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 *  UI menu class for interacting with truck management features.
 * Responsible for displaying a menu and delegating user actions to the TruckManagerService.
 */
public class TruckManagerMenu {

    // Service layer that handles truck-related business logic
    private final TruckControllerPL truckController;
    private final BookingControllerPL bookingControllerPL;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs the TruckManagerMenu with the provided service instance.
     */
    public TruckManagerMenu() throws Exception{
        this.truckController = new TruckControllerPL();
        this.bookingControllerPL = new BookingControllerPL();
    }

    private void addTruck(){
        System.out.println("Enter truck plate number:");
        String plate = scanner.nextLine();

        System.out.println("Enter max weight:");
        int maxWeight = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter driving licence code:");
        String licence = scanner.nextLine();

        try { // try to add new truck to system
            truckController.addTruck(plate, maxWeight, licence);
            System.out.println("Truck has added successfully");
        }
        catch (Exception e){ // catch the Exception if threw
            System.out.println(e.getMessage());
        }

    }
    private void printTrucksArrayOfDTO(List<TruckDto> trucks){
        if (trucks == null)
            return;
        for (TruckDto currTruck : trucks) {
            System.out.println("Truck number: " + currTruck.getPlateNumber() + " Licence " + currTruck.getLiceenceReq());
            System.out.println(currTruck.getWeight() + "/" + currTruck.getMaxWeight() + " weight");
        }
    }

    private void showAllTrucks(){
        try {
            // Get from controller all trucks

            List<TruckDto> trucks = truckController.getAllTrucks();

            // for each truck - print its details
            printTrucksArrayOfDTO(trucks);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    private void deleteTruck(){
        System.out.println("Enter plate number:");
        String plate = scanner.nextLine();
        try{
            truckController.deleteTruck(plate);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    private void showWeeklyTransports(){
        try {
            List<TransportDTO> transportDTOList = bookingControllerPL.getWeeklyTransportsRequests();

            for (TransportDTO transportReq : transportDTOList) // print all weekly transports
                if(Objects.equals(transportReq.getTruckPN(), "-1")) // show weekly transports with no trucks
                    System.out.println(printTransportDTO(transportReq));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String printTransportDTO(TransportDTO dto){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Transport number: ").append(dto.getId()).append("\n").append("\t");
            stringBuilder.append("From: ").append(dto.getSiteName()).append("\n").append("\t");
            stringBuilder.append("At date: ").append(dto.getDate()).append("\n").append("\t");
            stringBuilder.append("Leaves at: ").append(dto.getDepartureTime()).append("\n").append("\t");
            stringBuilder.append("Total weight: ").append(dto.getMaxWeight()).append("\n").append("\t");
            stringBuilder.append("Driver: ");
            if(Objects.equals(dto.getDriverID(),"-1"))
                stringBuilder.append(" No driver assigned to Transport");
            else
                stringBuilder.append("id number - ").append(dto.getId());
            stringBuilder.append("\n\t");
            stringBuilder.append("Truck: ");
            if(Objects.equals(dto.getTruckPN(),"-1"))
                stringBuilder.append(" No Truck assigned to Transport");
            else
                stringBuilder.append("Truck's Plate number - ").append(dto.getId());
            stringBuilder.append("\n\t");
            if(dto.isSent())
                stringBuilder.append("wait to be sent.\n");
            else
                stringBuilder.append("already left.\n");

            return stringBuilder.toString();
    }

    private void attachTruckToTransport(){

        System.out.println("Please enter transport id");
        String transportId = scanner.nextLine();

        System.out.println("Please enter truck plate number");
        String truckPlt = scanner.nextLine();

        // Try to attach truck
        try {
            truckController.attachTruck(transportId, truckPlt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }


    /**
     * Displays the truck manager menu and handles user input.
     * Supported actions: add truck, list all trucks, show available trucks on date, remove truck, and exit.
     */
    public void showMenu() {
        boolean running = true;
        while (running) {
            // Print menu options
            System.out.println("1. Add new Truck");
            System.out.println("2. Show all Trucks");
            System.out.println("3. Remove Truck");
            System.out.println("4. Show Weekly Transports");
            System.out.println("5. Attach Truck to transport");
            System.out.println("E. Exit");
            String input = scanner.nextLine();

            try {
                switch (input) {

                    case "1" -> addTruck();

                    case "2" -> showAllTrucks();

                    case "3" -> deleteTruck();

                    case "4" -> showWeeklyTransports();

                    case "5" -> attachTruckToTransport();


                    case "E", "e" -> {
                        running = false;
                        System.out.println("Goodbye.");
                    }

                    // Handle invalid input
                    default -> System.out.println("Invalid input.");
                }
            } catch (Exception e) {
                // Catch and report any runtime exceptions
                System.out.println("Error: " + e.getMessage()); // Show error message
            }
        }
    }
}
