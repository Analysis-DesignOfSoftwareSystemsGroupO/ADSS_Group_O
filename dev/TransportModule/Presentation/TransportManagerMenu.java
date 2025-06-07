package TransportModule.Presentation;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;


/**
 * UI menu for the Transport Manager.
 * Provides a text-based menu to manage transport operations such as
 * creating transports, assigning drivers, attaching documents, and handling delays.
 */

public class TransportManagerMenu {

    private final TransportManagerControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);

    public TransportManagerMenu() throws Exception {
        this.controller = new TransportManagerControllerPL();
    }

    public void showMenu() {
        boolean running = true;
        while (running) {

            System.out.println("\nWelcome to Transport Manager Menu.");
            System.out.println("1. Show all next week Transports.");
            System.out.println("2. Show all next week Transports With no Trucks.");
            System.out.println("3. Show all next week Transports With no Driver.");
            System.out.println("4. Remove Transport.");
            System.out.println("E. Exit.");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> ShowAllNextWeekTransports();
                case "2" -> ShowAllNextWeekTransportsWithNoTrucks();
                case "3" -> ShowAllNextWeekTransportsWithNoDrivers();
                case "4" -> RemoveTransportById();
                case "E", "e" -> running = false;
                default -> System.out.println("Invalid option, try again.\n");
            }
        }
    }


    private void ShowAllNextWeekTransports() {
        try {
            PrintTransportDTOList(controller.getNextWeekTransports());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ShowAllNextWeekTransportsWithNoTrucks() {
        try {
            PrintTransportDTOList(controller.getNextWeekTransportsWithNoTrucks());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void ShowAllNextWeekTransportsWithNoDrivers() {
        try {
            PrintTransportDTOList(controller.getNextWeekTransportsWithNoDrivers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void PrintTransportDTOList(List<TransportDTO> list) {
        for (TransportDTO dto : list) {
            try {
                System.out.println(printTransportDTO(dto));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private String getPLDInfo(int transportId)throws Exception{

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("To: ").append("\n").append("\t");
        List<ProductListDocumentDto> DTOS =  controller.getAllPLDSByTransportId(transportId);
        for (ProductListDocumentDto dto: DTOS){
            stringBuilder.append("\t").append(dto.getSiteDes()).append(" - Approximated arrival time at: ").append(dto.getApproximatedArrivalTime()).append("\n").append("\t");
        }
        return stringBuilder.toString();

    }

    private String printTransportDTO(TransportDTO dto) throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transport number: ").append(dto.getId()).append("\n").append("\t");
        stringBuilder.append("From: ").append(dto.getSiteName()).append("\n").append("\t");
        stringBuilder.append("At date: ").append(dto.getDate()).append("\n").append("\t");
        stringBuilder.append("Leaves at: ").append(dto.getDepartureTime()).append("\n").append("\t");
        stringBuilder.append(getPLDInfo(dto.getId()));
        stringBuilder.append("Total weight: ").append(dto.getMaxWeight()).append("\n").append("\t");
        stringBuilder.append("Driver: ");
        if(dto.getDriverID()== null)
            stringBuilder.append(" No driver assigned to Transport");
        else
            stringBuilder.append("id number - ").append(dto.getDriverID());
        stringBuilder.append("\n\t");
        stringBuilder.append("Truck: ");
        if(dto.getTruckPN() ==null)
            stringBuilder.append(" No Truck assigned to Transport");
        else
            stringBuilder.append("Truck's Plate number - ").append(dto.getTruckPN());
        stringBuilder.append("\n\t");
        if(!dto.isSent())
            stringBuilder.append("wait to be sent.\n");
        else
            stringBuilder.append("already left.\n");

        return stringBuilder.toString();
    }

    private void RemoveTransportById(){
        System.out.println("Please enter Transport id you want to delete: ");
        String transportId = scanner.nextLine();
        try{
            controller.removeTransportById(transportId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}