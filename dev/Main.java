import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.TransportAlreadySentException;
import transport_module.*;

public class Main {
    public static void menu_message(boolean flag) {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        if (!flag)
            System.out.println("Press S to start and load data");
        System.out.println("1. Add new transport");
        System.out.println("2. Create new delivery document");
        System.out.println("3. Add product to document");
        System.out.println("4. Remove product from document");
        System.out.println("5. Add driver to transport");
        System.out.println("6. Attach document to transport");
        System.out.println("7. Send transport manually");
        System.out.println("8. Print Transport details");
        System.out.println("9. Print all available trucks");
        System.out.println("10. Print document details");
        System.out.println("11. Add new Truck");
        System.out.println("12. Add new Site");
        System.out.println("13. Add new Product");


        System.out.println("Press E to Exit.");


    }

    public static void init_all_data(Truck[] trucks, Map<String, Driver> drivers, Map<Integer, Product> products, Map<String, Site> sites) {

        // Create new driving license instances
        DrivingLicence C1 = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        DrivingLicence C = new DrivingLicence("Heavy track - maximum 32 tons", "C");
        DrivingLicence CE = new DrivingLicence("Super heavy track - maximum 60 tons", "CE");

        // Save all trucks random plate numbers
        Map<Integer, Integer> numbers = new HashMap<>();

        // Initialize all trucks
        for (int i = 0; i < 9; i++) {
            int number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000); // draw a number
            while (numbers.get(number) != null) // if number is already drawn, draw another one.
                number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
            numbers.put(number, number); // save the drawn number

            try {
                if (i % 3 == 0) {
                    trucks[i] = new Truck(C1, 12000, String.valueOf(number));
                } else if (i % 3 == 1) {
                    trucks[i] = new Truck(C, 32000, String.valueOf(number));
                } else {
                    trucks[i] = new Truck(CE, 60000, String.valueOf(number));
                }
            } catch (ATransportModuleException e) {
                System.out.println("Invalid input - try again this function");
                return;
            }
        }

        // Create list of sites
        List<Site> sitesList = Arrays.asList(
                new Site("Haifa", "North"), new Site("Acre", "North"), new Site("Kiryat Shmona", "North"),
                new Site("Tiberias", "North"), new Site("Nazareth", "North"), new Site("Tel Aviv", "Center"),
                new Site("Ramat Gan", "Center"), new Site("Petah Tikva", "Center"), new Site("Holon", "Center"),
                new Site("Herzliya", "Center"), new Site("Beer Sheva", "South"), new Site("Ashkelon", "South"),
                new Site("Eilat", "South"), new Site("Dimona", "South"), new Site("Sderot", "South"),
                new Site("Netanya", "Center"), new Site("Hadera", "Center"), new Site("Yokneam", "North"),
                new Site("Karmiel", "North"), new Site("Arad", "South")
        );

        // Add sites to map
        for (Site site : sitesList) {
            sites.put(site.getName(), site);
        }

        // Create drivers
        String[] driverNames = {
                "David Cohen", "Sara Levi", "Moshe Mizrahi", "Rachel Avraham", "Yossi Peretz",
                "Noa Biton", "Daniel Azulay", "Maya Shahar", "Avi Ben David", "Gal Itzhak",
                "Lior Sasson", "Nadav Koren", "Shira Oren", "Hila Golan", "Tomer Bar",
                "Yael Shimon", "Ronen Hadad", "Dana Mor", "Amir Malka", "Tal Tzur"
        };

        for (int i = 0; i < driverNames.length; i++) {
            ArrayList<DrivingLicence> licences = new ArrayList<>();
            if (i % 3 == 0) {
                licences.add(C1);
            } else if (i % 3 == 1) {
                licences.add(C);
            } else {
                licences.add(CE);
            }
            Driver driver = new Driver(driverNames[i], String.valueOf(300000000 + i), licences);
            drivers.put(driver.getId(), driver);
        }

        // Create products (supermarket style)
        String[] productNames = {
                "Milk", "Cheese", "Yogurt", "Butter", "Eggs", "Chicken Breast", "Ground Beef", "Salmon Fillet", "Tuna Cans", "Hot Dogs",
                "Frozen Pizza", "Frozen Vegetables", "Frozen French Fries", "Ice Cream", "Apple", "Banana", "Orange", "Grapes", "Watermelon", "Strawberries",
                "Tomato", "Cucumber", "Carrot", "Potato", "Onion", "Garlic", "Lettuce", "Cabbage", "Broccoli", "Cauliflower",
                "Pasta", "Rice", "Flour", "Sugar", "Salt", "Black Pepper", "Olive Oil", "Canola Oil", "Vinegar", "Soy Sauce",
                "Ketchup", "Mayonnaise", "Mustard", "Peanut Butter", "Jam", "Honey", "Cornflakes", "Oatmeal", "Chocolate Spread", "Cookies",
                "Chocolate Bar", "Chips", "Pretzels", "Popcorn", "Nuts", "Almonds", "Cashews", "Sunflower Seeds", "Coffee", "Tea",
                "Instant Coffee", "Green Tea", "Juice", "Mineral Water", "Soda", "Energy Drink", "Beer", "Red Wine", "White Wine", "Whiskey",
                "Laundry Detergent", "Fabric Softener", "Dish Soap", "Surface Cleaner", "Bleach", "Sponges", "Toilet Paper", "Paper Towels", "Tissues", "Garbage Bags",
                "Shampoo", "Conditioner", "Soap Bar", "Shower Gel", "Toothpaste", "Toothbrush", "Razor Blades", "Deodorant", "Body Lotion", "Hand Cream",
                "Diapers", "Baby Wipes", "Baby Formula", "Cat Food", "Dog Food", "Bird Seeds", "Pet Shampoo", "Plastic Plates", "Plastic Cups", "Aluminum Foil",
                "Baking Paper", "Sandwich Bags", "Batteries", "Light Bulbs", "Matches", "Lighter", "Umbrella", "Slippers", "Broom", "Mop"
        };

        for (int i = 0; i < productNames.length; i++) {
            int weight = ThreadLocalRandom.current().nextInt(1, 6); // random 1-5 kg
            products.put(i + 1, new Product(i + 1, productNames[i], weight));
        }

    }


//********************************************************************************************************************** Case 1 - Add new transport


//********************************************************************************************************************** Case 2 - Create new delivery document

    /**
     * a function that creates new ProductListDocument to new destination
     * input - documents map, sites list
     */
    public static void createNewDoc(Map<Integer, ProductListDocument> documents, Map<String, Site> sites, Scanner scanner) {
        // destination name input from user
        System.out.println("Please enter destination name:");

        String siteName = scanner.nextLine();
        // check if site is in system
        if (sites.get(siteName) == null) { // if there is no site
            System.out.println("There is no site " + siteName + " please try again"); // if there is no site - message to user


            return;
        }
        // date input from user
        System.out.println("Please enter date by format: DD/MM/YYYY");
        String dateStr = scanner.nextLine();


        try {
            ProductListDocument document = new ProductListDocument(sites.get(siteName), dateStr); // create new document
            documents.put(document.getId(), document); // add a document to documents list

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }


    }


//********************************************************************************************************************** Case 3 - Add product to document

    /**
     * a function that attach a document to transport
     * input - products list, documents list
     */
    public static void AddProductToDocument(Map<Integer, Product> products, Map<Integer, ProductListDocument> documents, Scanner scanner) {
        // document id input from user

        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        if (documents.get(documentId) == null) { // if document is not in system - message to user
            System.out.println("There is no document " + documentId + " Please try again");

            return;
        }
        // get the document from data base
        ProductListDocument document = documents.get(documentId);
        // ask for product id from user
        System.out.println("Please enter Product Catalog Number: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        if (products.get(id) == null) { // if product is not in data base - message to user
            System.out.println("Product number " + id + " not in System - please try again.");

            return;
        }
        // ask for amount from user
        System.out.println("enter the amount you want to load: ");
        int amount = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        try {
            document.addProduct(products.get(id), amount); // add product to document
        } catch (ATransportModuleException e) {
            System.out.println(e.getMessage());
            return;
        }

    }

    //********************************************************************************************************************** Case 4 - Remove product from document

    /**
     * delete product from document
     */
    public static void RemoveProductFromDocument(Map<Integer, Product> products, Map<Integer, ProductListDocument> documents, Scanner scanner) {
        // ask for document id from user

        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        if (documents.get(documentId) == null) { // if ddocument not in the data base
            System.out.println("There is no document " + documentId + " Please try again"); // message to user

            return;
        }
        ProductListDocument document = documents.get(documentId); // get the document from data

        // ask for document id from user
        System.out.println("Please enter Product Catalog Number: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        if (products.get(id) == null) { // if product is not in data base
            System.out.println("Product number " + id + " not in System - please try again.");

            return;
        }
        // ask for amount to remove from document
        System.out.println("enter the amount you want to remove: ");
        int amount = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        // close scanner
        try {
            document.reduceAmountFromProduct(products.get(id), amount); // try to remove item from document
        } catch (ATransportModuleException e) {
            System.out.println(e.getMessage());
        }


    }

    //****************************************************************************************************************** Case 5 - Add driver to transport

    /**
     * add driver to transport
     */
    public static void AddDriverToTransport(Map<String, Driver> drivers, Map<Integer, Transport> transports, Scanner scanner) {


    }
    //****************************************************************************************************************** Case 6 -  Attach document to transport

    /**
     * a function that attaches a document to transport
     */
    public static void AttachDocumentToTransport(Map<Integer, ProductListDocument> documents, Map<Integer, Transport> transports, Scanner scanner) {


    }

    //********************************************************************************************************************** Case 7 - Send transport

    /**
     * a function that can send transport manual
     */
    public static void sendTransportManual(Map<Integer, Transport> transports, Scanner scanner) throws ATransportModuleException{


    }


//********************************************************************************************************************** Case 8 - Print Transport details

    /**
     * a function that prints transport by its id
     */
    public static void printTransport(Map<Integer, Transport> transports, Scanner scanner) {
        // ask for input from user
        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        Transport transport = transports.get(transport_id); // search for transport in database

        if (transport == null) { // if transport not in database - message to user
            System.out.println("Transport number " + transport_id + " is not in system - please try again");
            return;
        }
        System.out.println(transports.get(transport_id)); // print transport to screen

    }


//********************************************************************************************************************** Case 9 - Print all available trucks

    /**
     * a function that prints all availalbe trucks
     */
    public static void printAllAvailableTrukcs(Truck[] trucks, Scanner scanner) {


    }

    //********************************************************************************************************************** Case 10 - Print document details

    /**
     * a function that prints all document details by its id
     */
    public static void PrintDocumentDetails(Map<Integer, ProductListDocument> documents, Scanner scanner) {
        // ask for input from user
        System.out.println("Please enter document number"); // print document data
        Integer documentId = scanner.nextInt(); // document id from user
        scanner.nextLine(); // Clear the newline

        if (documents.get(documentId) == null) { // if document not in database
            System.out.println("There is no document " + documentId + " Please try again"); // send message to user

            return;
        }

        System.out.println(documents.get(documentId)); // print document details to screen

    }

    //********************************************************************************************************************** send Transport function every 1 minute
    public static void sendTransport(Map<LocalDate, List<Transport>> transportsPerDate) {

        List<Transport> currlist = transportsPerDate.get(LocalDate.now()); // save the list of all transport of current date
        if (currlist != null) { // if there are transport of current date.
            Iterator<Transport> iterator = currlist.iterator();
            while (iterator.hasNext()) {
                Transport transport = iterator.next();
                if (transport.getDeparture_time().isAfter(LocalTime.now())) {
                    try {
                        transport.sendTransport();
                    } catch (TransportAlreadySentException e) {

                    } catch (ATransportModuleException e1) {
                        System.out.println(e1.getMessage());
                    }
                    if (transport.isSent()) {
                        iterator.remove(); // Legal removal
                    }
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
        boolean isInit = false;

        // Run every 1 minute the sendTransport function
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendTransport(transportsPerDate);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);

        while (running) {
            menu_message(isInit);
            String input = scanner.nextLine();

            switch (input) {
                case "S": {
                    if (!isInit) {
                        init_all_data(trucks, drivers, products, sites);
                        System.out.println("Data is set");
                    }

                }
                /// Add new transport
                case "1": {
                    System.out.println("s");
                    break;

                }
                case "2": {
                    /// create a new document
                    createNewDoc(documents, sites, scanner);
                    break;

                }
                case "3": {
                    /// add product to document
                    AddProductToDocument(products, documents, scanner);
                    break;
                }
                case "4": {
                    /// Remove Product From document
                    RemoveProductFromDocument(products, documents, scanner);
                    break;
                }
                case "5": {
                    /// add driver to transport
                    AddDriverToTransport(drivers, transports, scanner);


                    break;
                }
                case "6": {
                    /// attach document to delivery
                    AttachDocumentToTransport(documents, transports, scanner);
                    break;

                }
                case "7": {
                    /// send transport manual

                    break;

                }
                case "8": {
                    /// print transport details

                    printTransport(transports, scanner);
                    break;

                }
                case "9": {
                    /// print all available trucks

                    printAllAvailableTrukcs(trucks, scanner);
                    break;

                }
                case "10": {
                    /// print document details
                    PrintDocumentDetails(documents, scanner);
                    break;


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
