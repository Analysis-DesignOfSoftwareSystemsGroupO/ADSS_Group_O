import java.sql.Driver;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;

import transport_module.*;

public class Main {
    public static void menu_message() {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        System.out.println("1. Add new transport");
        System.out.println("2. Create new delivery document");
        System.out.println("3. Add product to document");
        System.out.println("4. Remove product from document");
        System.out.println("5. Add driver to transport");
        System.out.println("6. Attach document to transport");
        System.out.println("7. Send transport");
        System.out.println("8. Print Transport details");
        System.out.println("9. Print all available trucks");
        System.out.println("10. Print document details");


        System.out.println("Press E to Exit.");


    }

    public static void init_all_data(Truck[] trucks, Map<String, Driver> drivers, Map<Integer, Product> products, Map<String, Site> sites) {

        // create new driving license instances
        DrivingLicence C1 = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        DrivingLicence C = new DrivingLicence("Heavy track - maximum 32 tons", "C");
        DrivingLicence CE = new DrivingLicence("Super heavy track - maximum 60 tons", "CE");

        // save all trucks random plate numbers
        Map<Integer, Integer> numbers = new HashMap<>();

        // init all trucks
        for (int i = 0; i < 9; i++) {

            int number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000); // draw a number
            while (numbers.get(number) != null) // if number is already drawed, draw another one.
                number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
            numbers.put(number, number); // save the drawed number for next time

            // create random trucks
            if (i % 3 == 0) {
                trucks[i] = new Truck(C1, 12000, String.valueOf(number));
            }
            if (i % 3 == 1) {
                trucks[i] = new Truck(C, 32000, String.valueOf(number));
            }
            if (i % 3 == 2) {
                trucks[i] = new Truck(CE, 60000, String.valueOf(number));
            }

        }
        // create sites list
        List<Site> siteslist = Arrays.asList(
                new Site("Haifa", "North"),
                new Site("Acre", "North"),
                new Site("Kiryat Shmona", "North"),
                new Site("Tiberias", "North"),
                new Site("Nazareth", "North"),
                new Site("Tel Aviv", "Center"),
                new Site("Ramat Gan", "Center"),
                new Site("Petah Tikva", "Center"),
                new Site("Holon", "Center"),
                new Site("Herzliya", "Center"),
                new Site("Beer Sheva", "South"),
                new Site("Ashkelon", "South"),
                new Site("Eilat", "South"),
                new Site("Dimona", "South"),
                new Site("Sderot", "South"),
                new Site("Netanya", "Center"),
                new Site("Hadera", "Center"),
                new Site("Yokneam", "North"),
                new Site("Karmiel", "North"),
                new Site("Arad", "South")
        );


        // add sites to sites list
        for (Site site : siteslist) {
            sites.put(site.getName(), site);
        }

    }


//********************************************************************************************************************** Case 1 - Add new transport

    /**
     * Add new Transport function
     */
    public static void AddNewTransport(Truck[] trucks, Map<Integer, Transport> transports, Map<String, Site> sites, Map<LocalDate, List<Transport>> transportsPerDate) {

        boolean allTrucksUnAvailabl = true; // boolean variable for
        Truck t = null; // variable for saving truck if available
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter date by format: DD/MM/YEAR");
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(dateStr, formatter);
        }
        catch (DateTimeParseException e){
            e.getMessage();
            return;
        }


        for (Truck truck : trucks) // check if there is an available truck.
            if (truck.getAvailablity(parsedDate)) { // if there is an avialable truck - stop searching
                allTrucksUnAvailabl = false;
                t = truck;
                break;
            }
        if (allTrucksUnAvailabl) { // if all trucks are not available - print message
            System.out.println("There is no available truck please check later");
            return;
        }

        // time input from user
        System.out.println("Please enter date by format: HH:MM");
        String timeStr = scanner.nextLine();

        // source site input from user
        System.out.println("Please enter source site name");
        String site_name = scanner.nextLine();
        if (sites.get(site_name) == null) { // check if there is a site in the system
            System.out.println("There is no site " + site_name + " please try again"); // if there is no site - message to user
            scanner.close();
            return;
        }


        try { // try to create new transport
            Transport transport = new Transport(dateStr, timeStr, t, sites.get(site_name)); // create new transport with
            transports.put(transport.getId(), transport); // if transport created - add it to transport list
            transportsPerDate.computeIfAbsent(parsedDate, k -> new ArrayList<>()); // if transport created - add it to transport date map
            transportsPerDate.get(parsedDate).add(transport); // add the transport to scheduale
        } catch (Exception e) {
            System.out.println(e.getMessage() + "please try again"); // if there is any problem with transport - message to user
        }
        scanner.close();


    }

//********************************************************************************************************************** Case 2 - Create new delivery document

    /** a function that creates new ProductListDocument to new destination
     * input - documents map, sites list
     */
    public static void createNewDoc(Map<Integer, ProductListDocument> documents, Map<String, Site> sites) {
        // destination name input from user
        System.out.println("Please enter destination name:");
        Scanner scanner = new Scanner(System.in);
        String siteName = scanner.nextLine();
        // check if site is in system
        if (sites.get(siteName) == null) { // if there is no site
            System.out.println("There is no site " + siteName + " please try again"); // if there is no site - message to user
            scanner.close();

            return;
        }
        // date input from user
        System.out.println("Please enter date by format: DD/MM/YEAR");
        String dateStr = scanner.nextLine();
        scanner.close();

        try {
            ProductListDocument document = new ProductListDocument(sites.get(siteName),dateStr); // create new document
            documents.put(document.getId(), document); // add a document to documents list

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }


    }


//********************************************************************************************************************** Case 3 - Add product to document

    /** a function that attach a document to transport
     * input - products list, documents list*/
    public static void AddProductToDocument(Map<Integer, Product> products, Map<Integer, ProductListDocument> documents) {
        // document id input from user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt();
        if (documents.get(documentId) == null) { // if document is not in system - message to user
            System.out.println("There is no document " + documentId + " Please try again");
            scanner.close();
            return;
        }
        // get the document from data base
        ProductListDocument document = documents.get(documentId);
        // ask for product id from user
        System.out.println("Please enter Product Catalog Number: ");
        int id = scanner.nextInt();
        if (products.get(id) == null) { // if product is not in data base - message to user
            System.out.println("Product number "+id+" not in System - please try again.");
            scanner.close();
            return;
        }
        // ask for amount from user
        System.out.println("enter the amount you want to load: ");
        int amount = scanner.nextInt();
        document.addProduct(products.get(id), amount); // add product to document
        scanner.close();
    }

    //********************************************************************************************************************** Case 4 - Remove product from document

    /** delete product from document*/
    public static void RemoveProductFromDocument(Map<Integer, Product> products, Map<Integer, ProductListDocument> documents) {
        // ask for document id from user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt();
        if (documents.get(documentId) == null) { // if ddocument not in the data base
            System.out.println("There is no document " + documentId + " Please try again"); // message to user
            scanner.close();
            return;
        }
        ProductListDocument document = documents.get(documentId); // get the document from data

        // ask for document id from user
        System.out.println("Please enter Product Catalog Number: ");
        int id = scanner.nextInt();
        if (products.get(id) == null) { // if product is not in data base
            System.out.println("Product number "+id+" not in System - please try again.");
            scanner.close();
            return;
        }
         // ask for amount to remove from document
        System.out.println("enter the amount you want to remove: ");
        int amount = scanner.nextInt();
        scanner.close(); // close scanner
        document.reduceAmountFromProduct(products.get(id), amount); // try to remove item from document


    }

    //****************************************************************************************************************** Case 5 - Add driver to transport

    /**
     * add driver to transport
     */
    public static void AddDriverToTransport(Map<String, Driver> drivers, Map<Integer, Transport> transports) {
        // ask for drivers id input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter driver's id");
        String id = scanner.nextLine();
        Driver driver = drivers.get(id); // search for driver in database
        if (driver == null) { // if driver is not in system
            System.out.println("Driver "+id +" is not in system - please try again"); // message to user
            scanner.close();
            return;
        }
        // ask for transport id from user
        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        Transport transport = transports.get(transport_id);// get transport from database by transport id
        if (transport == null) { // if transport is not in system
            System.out.println("Transport number " + transport_id+ " is not in system - please try again");
            scanner.close();
            return;
        }
        transport.addDriver((transport_module.Driver) driver); // try to add driver to transport
        scanner.close();


    }
    //****************************************************************************************************************** Case 6 -  Attach document to transport

    /** a function that attaches a document to transport*/
    public static void AttachDocumentToTransport(Map<Integer, ProductListDocument> documents, Map<Integer, Transport> transports) {
        // ask for transport id from user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        Transport transport = transports.get(transport_id); // search for transport id in database
        if (transport == null) { // if transport not in database - message to user
            System.out.println("Transport number " + transport_id+ " is not in system - please try again");
            scanner.close();
            return;
        }
        // ask for document id from user
        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt(); // search document in database
        if (documents.get(documentId) == null) {
            System.out.println("There is no document " + documentId + " Please try again"); // if document not in database - message to user
            scanner.close();
            return;
        }
        try { // try to load document to transport
            transport.loadByDocument(documents.get(documentId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //********************************************************************************************************************** Case 7 - Send transport

    /** a function that can send transport manual*/
    public static void sendTransportManual(Map<Integer, Transport> transports) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter Transport number"); // ask for input from user
        int transport_id = scanner.nextInt();
        Transport transport = transports.get(transport_id); // search transport in database
        scanner.close();
        if (transport == null) { // if transport not in data base - message to user
            System.out.println("Transport number " + transport_id+ " is not in system - please try again");
            return;
        }
        transport.sendTransport();


    }


//********************************************************************************************************************** Case 8 - Print Transport details

    /** a function that prints transport by its id*/
    public static void printTransport(Map<Integer, Transport> transports) {
        Scanner scanner = new Scanner(System.in); // ask for input from user
        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        Transport transport = transports.get(transport_id); // search for transport in database
        scanner.close();
        if (transport == null) { // if transport not in database - message to user
            System.out.println("Transport number " + transport_id+ " is not in system - please try again");
            return;
        }
        System.out.println(transports.get(transport_id)); // print transport to screen

    }


//********************************************************************************************************************** Case 9 - Print all available trucks

    /** a function that prints all availalbe trucks*/
    public static void printAllAvailableTrukcs(Truck[] trucks) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter date by format: DD/MM/YEAR"); // ask for date from user
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(dateStr, formatter);
        }
        catch (DateTimeParseException e){
            e.getMessage();
            return;
        }
        int count = 0;
        for (Truck truck : trucks) {
            if (truck.getAvailablity(parsedDate)) {
                System.out.println(truck);

                count++;
            }
        }
        if (count == 0)
            System.out.println("There is no available truck in system.");


    }

    //********************************************************************************************************************** Case 10 - Print document details
    public static void PrintDocumentDetails(Map<Integer, ProductListDocument> documents) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt();
        if (documents.get(documentId) == null) {
            System.out.println("There is no document " + documentId + " Please try again");
            scanner.close();
            return;
        }

        System.out.println(documents.get(documentId));

    }

    public static void sendTransport(Map<LocalDate, List<Transport>> transportsPerDate) {

        List<Transport> currlist = transportsPerDate.get(LocalDate.now());
        if (currlist != null) {
            for (Transport transport : currlist) { // for each transport in list
                if (transport.getDeparture_time().isAfter(LocalTime.now())) { // if its time to send the transport
                    transport.sendTransport(); // send transport
                    if (transport.isSent()) // if transport sent successfully
                        currlist.remove(transport); // remove transport from list
                }
            }
        }

    }

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Truck[] trucks = new Truck[9];
        Map<String, Driver> drivers = new HashMap<>();
        Map<Integer, Transport> transports = new HashMap<>();
        Map<LocalDate, List<Transport>> transportsPerDate = new HashMap<>();
        Map<Integer, ProductListDocument> documents = new HashMap<>();
        Map<Integer, Product> products = new HashMap<>();
        Map<String, Site> sites = new HashMap<>();

        init_all_data(trucks,drivers,products,sites);

        // Run every 1 minute the sendTransport function
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendTransport(transportsPerDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES);

        while (running) {
            menu_message();
            String input = scanner.nextLine();

            switch (input) {
                /// Add new transport
                case "1": {
                    AddNewTransport(trucks, transports, sites, transportsPerDate);
                    break;

                }
                case "2": {
                    /// create a new document
                    createNewDoc(documents, sites);
                    break;

                }
                case "3": {
                    /// add product to document
                    AddProductToDocument(products, documents);
                    break;
                }
                case "4": {
                    /// Remove Product From document
                    RemoveProductFromDocument(products, documents);
                    break;
                }
                case "5": {
                    /// add driver to transport
                    AddDriverToTransport(drivers, transports);


                    break;
                }
                case "6": {
                    /// attach document to delivery
                    AttachDocumentToTransport(documents, transports);
                    break;

                }
                case "7": {
                    /// send transport manual

                    sendTransportManual(transports);

                    break;

                }
                case "8": {
                    /// print transport details

                    printTransport(transports);
                    break;

                }
                case "9": {
                    /// print all available trucks

                    printAllAvailableTrukcs(trucks);
                    break;

                }
                case "10": {
                    /// print document details
                    PrintDocumentDetails(documents);


                }
                case "E": {
                    System.out.println("Thank you! have a nice day.");
                    running = false;
                    break;
                }
                default: {
                    System.out.println("Wrong input! Please try again.");
                    break;
                }

            }

        }
        scanner.close();


    }

}
