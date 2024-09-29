import java.util.ArrayList;
import java.util.Locale;


/**
 * This class reads commands from a file, processes them to
 * manage bus bookings, and outputs results to another file.
 * It holds no fields but instead runs the system via its methods.
 * 
 * @author Oguzhan
 * @version 1.2
 */
public class BookingSystem {
    public static void main(String[] args) throws Exception {
        if (args.length > 2 || args.length < 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            System.exit(0);
        }
        
        String[] content = FileInput.readFile(args[0], true, true); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[1], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
    
        // Create a new arrayList to store bus objects.
        ArrayList<Bus> buses = new ArrayList<>();

        if (content.length == 0) { // If the input is empty file.
            runZ_REPORT("Z_REPORT", args[1], buses, true);
        }
        
        for (String line : content) { // For every line in the input.
            if (line == content[content.length-1] && !(line.split("\t")[0].equals("Z_REPORT"))) { // If the last line isn't Z_REPORT command.
                analyzeCommand(line, args[1], buses, false); // Analyze command and then runZ_REPORT.
                runZ_REPORT("Z_REPORT", args[1], buses, true);
            } else if (line == content[content.length-1] && line.split("\t")[0].equals("Z_REPORT")) { // If the last line is Z_REPORT command.
                analyzeCommand(line, args[1], buses, true);
            } else {
                analyzeCommand(line, args[1], buses, false);
            }
        }
    
    }

    
    /** 
     * This method analyzes given input command, and then executes if the command is valid.
     * It will give an ERROR if the command is NOT valid.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     * @param isLast    Boolean value to determine if the input is the last line.
     */
    public static void analyzeCommand(String commandLine, String path, ArrayList<Bus> busList, boolean isLast) {
        FileOutput.writeToFile(path, "COMMAND: "+ commandLine, true, true); // Writes the COMMAND: info.
        String command = commandLine.split("\t")[0];
        switch (command) { // If command doesn't match any valid type, it will write an ERROR.
            case "Z_REPORT":
                runZ_REPORT(commandLine, path, busList, isLast);
                break;
            case "INIT_VOYAGE":
                runINIT_VOYAGE(commandLine, path, busList);
                break;
            case "SELL_TICKET":
                runSELL_TICKET(commandLine, path, busList);
                break;
            case "REFUND_TICKET":
                runREFUND_TICKET(commandLine, path, busList);
                break;
            case "PRINT_VOYAGE":
                runPRINT_VOYAGE(commandLine, path, busList, false);
                break;
            case "CANCEL_VOYAGE":
                runCANCEL_VOYAGE(commandLine, path, busList);
                break;

            default: FileOutput.writeToFile(path, "ERROR: There is no command namely "+ command +"!", true, true);
        }
    }

    
    /** 
     * This method executes the Z_REPORT command by printing every bus information
     * in ID order.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     * @param isLast    Boolean value to determine if the input is the last line.
     */
    public static void runZ_REPORT(String commandLine, String path, ArrayList<Bus> busList, boolean isLast){
        if (commandLine.split("\t").length > 1){ // If there is something more other than Z_REPORT command, it's invalid usage.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
            return;
        }
        
        FileOutput.writeToFile(path, "Z Report:", true, true);

        busList.sort((bus1, bus2) -> { // Lambda expression to sort busList arrayList according to bus ID's.
            Integer id1 = bus1.getId();
            Integer id2 = bus2.getId();
            return id1.compareTo(id2);
        });

        String formattedStr = "";
        if (busList.size()>0) { // If there is a voyage to write.
            for (Bus bus : busList) {
                FileOutput.writeToFile(path, "----------------", true, true);
                String command = "PRINT_VOYAGE\t" + Integer.toString(bus.getId()); // Command line to execute runPRINT_VOYAGE method as desired.
                runPRINT_VOYAGE(command, path, busList, false);
            }
        } else {
            formattedStr += "----------------\nNo Voyages Available!\n";
        }
        formattedStr += "----------------";
        if (!isLast) { // If it's not the last line, add \n character.
            FileOutput.writeToFile(path, formattedStr, true, true);
        } else { // If not, don't add \n.
            FileOutput.writeToFile(path, formattedStr, true, false);
        }

    }

    
    /** 
     * This method initializes voyages. It will write ERROR messages if the given command is not valid.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     */
    public static void runINIT_VOYAGE(String commandLine, String path, ArrayList<Bus> busList){
        try { // It will catch any ERROR which includes index errors mostly.
            String[] commands = commandLine.split("\t");
            String formattedStr;
            String type = commands[1];
            int id = Integer.parseInt(commands[2]);
            if (id <= 0) {
                FileOutput.writeToFile(path, "ERROR: "+ id +" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }
            
            if (checkID(id, busList)) { // If there is a voyage with given ID.
                FileOutput.writeToFile(path, "ERROR: There is already a voyage with ID of "+ id +"!", true, true);
                return;
            }

            String from = commands[3];
            String to = commands[4];
            int rows = Integer.parseInt(commands[5]);
            if (rows <= 0) {
                FileOutput.writeToFile(path, "ERROR: "+ rows +" is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
                return;
            }
            double price = Double.parseDouble(commands[6]); 
            if (price <= 0) {
                FileOutput.writeToFile(path, "ERROR: "+ (int) price +" is not a positive number, price must be a positive number!", true, true);
                return;
            }
            switch (type) {
                case "Minibus":
                    if (commands.length > 7) { // If there is more element than necessery in the input.
                        FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                        return;
                    }
                    Minibus temp1 = new Minibus(type, id, from, to, rows, price); // Initialize a Minibus object and assign it to a temporary variable.
                    busList.add(temp1);
                    // Used Locale.US to get "." for floats in output.
                    formattedStr = String.format(Locale.US, "Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.",
                    id, from, to, price, temp1.getSeatSize());
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    break;
                case "Standard":
                    if (commands.length > 8) { // If there is more element than necessery in the input.
                        FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                        return;
                    }
                    double refundCut = Double.parseDouble(commands[7]);
                    if (refundCut < 0) {
                        FileOutput.writeToFile(path, "ERROR: "+ (int) refundCut +" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                        return;
                    }
                    StandardBus temp2 = new StandardBus(type, id, from, to, rows, price, refundCut);
                    busList.add(temp2);
                    // Used Locale.US to get "." for floats in output.
                    formattedStr = String.format(Locale.US, "Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.",
                    id, from, to, price, temp2.getSeatSize(), (int) refundCut );
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    break;
                case "Premium":
                    if (commands.length > 9) { // If there is more element than necessery in the input.
                        FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                        return;
                    }
                    refundCut = Double.parseDouble(commands[7]);
                    if (refundCut < 0) {
                        FileOutput.writeToFile(path, "ERROR: "+ (int) refundCut +" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                        return;
                    }
                    double premiumFee = Double.parseDouble(commands[8]);
                    if (premiumFee < 0) {
                        FileOutput.writeToFile(path, "ERROR: "+ (int) premiumFee +" is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                        return;
                    }
                    PremiumBus temp3 = new PremiumBus(type, id, from, to, rows, price, refundCut, premiumFee);
                    busList.add(temp3);
                    // Used Locale.US to get "." for floats in output.
                    formattedStr = String.format(Locale.US, "Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.",
                    id, from, to, price, (temp3.getSeatSize()*2)/3, temp3.getPremiumPrice(), temp3.getSeatSize()/3, (int) refundCut );
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    break;
                default: // If the input type is not matching any valid type.
                FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);

            }
        } catch (Exception a){ // It will catch mostly index errors due to giving less input values than necessary.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
        }
    }

    
    /** 
     * This method executes the process of selling a ticket. If the command is invalid, it will write
     * an ERROR message.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     */
    public static void runSELL_TICKET(String commandLine, String path, ArrayList<Bus> busList) {
        try { // It will catch any ERROR which includes index errors mostly.
            String[] commands = commandLine.split("\t");
            if (commands.length > 3) { // If there is more element than necessery in the input.
                FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
                return;
            }
            int id = Integer.parseInt(commands[1]);
            if (id <= 0) {
                FileOutput.writeToFile(path, "ERROR: "+ id +" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }
            String[] seats = commands[2].split("_");
            if (!checkID(id, busList)){ // If there is no voyage with the given ID.
                FileOutput.writeToFile(path, "ERROR: There is no voyage with ID of "+ id +"!", true, true);
                return;
            }
            
            for (String str : seats) {
                if (!isPositiveInt(str)) { // If the command is not a positive integer.
                    FileOutput.writeToFile(path, "ERROR: "+ str +" is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }
            }
            
            String seatStr = seats[0];
            if (seats.length > 1) { // Formatting seat numbers information.
                for (int i = 1; i < seats.length; i++) {
                    seatStr += "-" + seats[i];
                }
            }

            for (Bus bus : busList){
                if (bus.getId() == id) {
                    double tempRev = bus.getRevenue();
                    String[] tempPlan = bus.getBusPlan().clone();
                    for (String seat : seats) { // For error handling.
                        int seatNumber = Integer.parseInt(seat);
                        if (seatNumber > bus.getSeatSize()) {
                            FileOutput.writeToFile(path, "ERROR: There is no such a seat!", true, true);
                            return;
                        }

                        if (tempPlan[seatNumber-1] == "X") {
                            FileOutput.writeToFile(path, "ERROR: One or more seats already sold!", true, true);
                            return;
                        }
                    }
                    for (String seat : seats) {
                        int seatNumber = Integer.parseInt(seat);
                        bus.sellTicket(seatNumber);
                    }
                    // Revenue calculated by substracting the initial revenue from the current one.
                    String formattedStr = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", seatStr, bus.getId(), bus.getFrom(), bus.getTo(), (bus.getRevenue() - tempRev));
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    break;
                }
            }
        } catch (Exception a){ // It will catch mostly index errors due to giving less input values than necessary.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
        }
    }

    
    /** 
     * This method executes the process of refunding a ticket. If the command is invalid, it will write
     * an ERROR message.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     */
    public static void runREFUND_TICKET(String commandLine, String path, ArrayList<Bus> busList) {
        try { // It will catch any ERROR which includes index errors mostly.
            String[] commands = commandLine.split("\t");
            if (commands.length > 3) { // If there is more element than necessery in the input.
                FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
                return;
            }
            int id = Integer.parseInt(commands[1]);
            if (id <= 0) {
                FileOutput.writeToFile(path, "ERROR: "+ id +" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }
            String[] seats = commands[2].split("_");
            if (!checkID(id, busList)){ // If there is no voyage with the given ID.
                FileOutput.writeToFile(path, "ERROR: There is no voyage with ID of "+ id +"!", true, true);
                return;
            }
            
            for (String str : seats) {
                if (!isPositiveInt(str)) { // If the command is not a positive integer.
                    FileOutput.writeToFile(path, "ERROR: "+ str +" is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }
            }

            String seatStr = seats[0];
            if (seats.length > 1) { // Formatting seat numbers information.
                for (int i = 1; i < seats.length; i++) {
                    seatStr += "-" + seats[i];
                }
            }

            for (Bus bus : busList){
                // If the bus type is Premium or Standard and matches with input id
                if (bus.getId() == id && (bus.getBusType().equals("Standard") || bus.getBusType().equals("Premium"))) {
                    RefundProtocol refundable = (RefundProtocol) bus; // Downcast to use refund method with the help of Polymorphism.
                    double tempRev = bus.getRevenue();
                    String[] tempPlan = bus.getBusPlan().clone();
                    for (String seat : seats) { // For ERROR handling.
                        int seatNumber = Integer.parseInt(seat);
                        if (seatNumber > bus.getSeatSize()) {
                            FileOutput.writeToFile(path, "ERROR: There is no such a seat!", true, true);
                            return;
                        }

                        if (tempPlan[seatNumber-1] == "*") {
                            FileOutput.writeToFile(path, "ERROR: One or more seats are already empty!", true, true);
                            return;
                        }
                    }
                    for (String seat : seats) {
                        int seatNumber = Integer.parseInt(seat);
                        refundable.refund(seatNumber); // Use the method of RefundProtocol interface even with different classes.
                    }
                    // Used Locale.US to get "." for floats in output.
                    String formattedStr = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.", seatStr, bus.getId(), bus.getFrom(), bus.getTo(), (tempRev - bus.getRevenue()));
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    break;
                } else if (bus.getId() == id) { // If the given ID matches but it's type Minibus.
                    FileOutput.writeToFile(path, "ERROR: Minibus tickets are not refundable!", true, true);
                }
            }
        } catch (Exception a) { // It will catch mostly index errors due to giving less input values than necessary.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
        }
    }

    
    /** 
     * This method prints the voyage information. It will print the differences for cancelled voyage if it's specified in parameters.
     * If the command is invalid, it will write an ERROR message.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     * @param isCancel Boolean value to check if it will print information about a cancelled voyage.
     */
    public static void runPRINT_VOYAGE(String commandLine, String path, ArrayList<Bus> busList, boolean isCancel) {
        try { // It will catch any ERROR which includes index errors mostly.
            String[] commands = commandLine.split("\t");
            if (commands.length > 2) { // If there is more element than necessery in the input.
                FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
                return;
            }
            
            if (!isPositiveInt(commands[1])) { // If the command is not a positive integer.
                FileOutput.writeToFile(path, "ERROR: "+ commands[1] +" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }

            if (!checkID(Integer.parseInt(commands[1]), busList)) { // If there is no voyage with input ID.
                FileOutput.writeToFile(path, "ERROR: There is no voyage with ID of "+ commands[1] +"!", true, true);
                return;
            }

            String formattedStr = "";
            String voyage = commands[1];
            for (Bus bus : busList) {
                if (bus.getId() == Integer.parseInt(voyage)) {
                    formattedStr += String.format("Voyage %d\n%s-%s\n", bus.getId(), bus.getFrom(), bus.getTo()); // Voyage info.
                    switch (bus.getBusType()) { // To check type of the bus, so it will arrange the order of seats by using busPlan.
                        case "Minibus":
                            for (int j = 0; j < bus.getSeatSize(); j+=2) { // It will print seats as two seats in each row.
                                // The information about the seat (sold or not) is in the busPlan.
                                formattedStr += String.format("%s %s\n",  bus.getBusPlan()[j], bus.getBusPlan()[j+1]);
                            }
                            if (!isCancel){ // If it's not a cancelled voyage then it will also print the revenue.
                                formattedStr += String.format(Locale.US, "Revenue: %.2f\n", bus.getRevenue());
                                break;
                            } else { // Else, it will print revenue as 0.00 because there is no refund in Minibus.
                                formattedStr += "Revenue: 0.00";
                                break;
                            }
                        case "Standard":
                            for (int j = 0; j < bus.getSeatSize(); j+=4) { // It will print seats as four seats in each row, seats seperated from the middle with a line.
                                formattedStr += String.format("%s %s | %s %s\n",  bus.getBusPlan()[j], bus.getBusPlan()[j+1], bus.getBusPlan()[j+2], bus.getBusPlan()[j+3]);
                            }
                            if (!isCancel){ // If it's not a cancelled voyage then it will also print the revenue.
                                formattedStr += String.format(Locale.US, "Revenue: %.2f\n", bus.getRevenue());
                                break;
                            } else {
                                StandardBus tempBus = (StandardBus) bus; // Downcasting to StandardBus to get RefundRevenue. 
                                // Used Locale.US to get "." for floats in output.
                                formattedStr += String.format(Locale.US, "Revenue: %.2f\n", tempBus.getRefundRevenue());;
                                break;
                            }
                        case "Premium":
                            for (int j = 0; j < bus.getSeatSize(); j+=3) { // It will print seats as three seats in each row, seats seperated from right of the left seat by a line.
                                formattedStr += String.format("%s | %s %s\n",  bus.getBusPlan()[j], bus.getBusPlan()[j+1], bus.getBusPlan()[j+2]);
                            }
                            if (!isCancel){ // If it's not a cancelled voyage then it will also print the revenue.
                                formattedStr += String.format(Locale.US, "Revenue: %.2f\n", bus.getRevenue());
                                break;
                            } else {
                                PremiumBus tempBus = (PremiumBus) bus; // Downcasting to PremiumBus to get RefundRevenue. 
                                // Used Locale.US to get "." for floats in output.
                                formattedStr += String.format(Locale.US, "Revenue: %.2f\n", tempBus.getRefundRevenue());;
                                break;
                            }
                    }
                }
            }
            FileOutput.writeToFile(path, formattedStr, true, false);
        } catch (Exception a) { // It will catch mostly index errors due to giving less input values than necessary.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
        }
    }

    
    /** 
     * This method cancels a voyage and removes it's information from the system.
     * If the command is invalid, it will write an ERROR message.
     * 
     * @param commandLine   String line that consists of the command input.
     * @param path  Path of the output file.
     * @param busList  An ArrayList that consists of the Bus objects.
     */
    public static void runCANCEL_VOYAGE(String commandLine, String path, ArrayList<Bus> busList) {
        try{ // It will catch any ERROR which includes index errors mostly.
            String[] commands = commandLine.split("\t");
            if (commands.length > 2) { // If there is more element than necessery in the input.
                FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
                return;
            }
            
            if (!isPositiveInt(commands[1])) { // If the command is not a positive integer.
                FileOutput.writeToFile(path, "ERROR: "+ commands[1] +" is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }

            if (!checkID(Integer.parseInt(commands[1]), busList)) { // If there is no voyage with input ID.
                FileOutput.writeToFile(path, "ERROR: There is no voyage with ID of "+ commands[1] +"!", true, true);
                return;
            }

            String voyage = commands[1];
            
            for (int i = 0; i < busList.size(); i++) {
                if (busList.get(i).getId() == Integer.parseInt(voyage)) {
                    String formattedStr = String.format("Voyage %s was successfully cancelled!\nVoyage details can be found below:", voyage);
                    FileOutput.writeToFile(path, formattedStr, true, true);
                    runPRINT_VOYAGE(commandLine, path, busList, true); // Prints the voyage in cancel mode.
                    busList.remove(i); // Remove the voyage from the database.
                }                   
            }
            
        } catch (Exception a) { // It will catch mostly index errors due to giving less input values than necessary.
            FileOutput.writeToFile(path, "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
        }
    }

    
    /** 
     * This method runs in other command executors as a helper.
     * It will check if the given ID is in the database or not.
     * 
     *  @param id    ID of the voyage which will be checked.
     * @param busList  An ArrayList that consists of the Bus objects.
     * @return boolean If it's found in the database it will return true, if not it will return false.
     */
    public static boolean checkID(int id, ArrayList<Bus> busList){
        boolean isFound = false;
        for (Bus bus : busList){
            if (id == bus.getId()){
                isFound = true;
            }
        }
        return isFound;
    }

    
   /**
    * This method checks if a given string can be converted to a positive integer.
    * 
    * @param    command Input value which will be checked if it can be converted to a positive integer.
    * @return   boolean If it can be converted to a positive integer it will return true, if not it will return false.
    */
    public static boolean isPositiveInt(String command){
        boolean isPositiveInt = true;
        try { // It will try to convert the input value to integer, If it fails this will catch.
            if (Integer.parseInt(command) <= 0) { // If converting to integer is successful but it's not a positive integer.
                isPositiveInt = false;
            }
        } catch (Exception e) {
            isPositiveInt = false;
        }
        return isPositiveInt;
    }
}
