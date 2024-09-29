/**
 * This class represents a MachineSlot object which defines slots in a vending machine.
 * It holds information about the productName, price, calorie,
 * protein, carb, fat and number within that slot.
 * Fills the slots with products based on their information.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class MachineSlot {
    private String productName = "";
    private int price = 999;
    private int calorie = -999;
    private double protein, carb, fat;
    private int numberOfProduct = 0;


    // Getter and setters.
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCalorie() {
        return this.calorie;
    }
    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
    public double getProtein() {
        return this.protein;
    }
    public void setProtein(double protein) {
        this.protein = protein;
    }
    public double getCarb() {
        return this.carb;
    }
    public void setCarb(double carb) {
        this.carb = carb;
    }
    public double getFat() {
        return this.fat;
    }
    public void setFat(double fat) {
        this.fat = fat;
    }
    public int getNumberOfProduct() {
        return this.numberOfProduct;
    }
    public void setNumberOfProduct(int number) {
        this.numberOfProduct = number;
    }

    private void reset() {
        this.productName = "";
        this.price = 999;
        this.calorie = -999;
        this.protein = 0;
        this.carb = 0;
        this.fat = 0;
        this.numberOfProduct = 0;
    }
 
    
    /** 
     * This method assign the input product's values to the slot.
     * 
     * @param line  String line that consists of the input product's values.
     */
    private void assign(String line) {
        // Split input line at \t and assign variables.
        String[] values = line.split("\t");
        productName = values[0];
        price = Integer.parseInt(values[1]);
       // Split at white space and assign macro values. 
        String[] macros = values[2].split(" ");
        protein = Double.parseDouble(macros[0]);
        carb = Double.parseDouble(macros[1]);
        fat = Double.parseDouble(macros[2]);
        // Calculate total calorie.
        double totalCalorie = (4*protein) + (4*carb) + (9*fat);
        calorie = (int) Math.round(totalCalorie);
    }

    
    /**
     * This method fills the slots of the machine. 
     * There is 2 conditions (at least one of them must satisfy) for the filling process to be succesfull:
     * 1- There must be an empty slot.
     * 2- There must be a slot filled with the same type of the input and there must be enough place to put it in the slot.
     * 
     * @param line  String line that consists of the input values.
     * @param slots A MachineSlot array that consists of slots which we will be filling.
     * @param output Output argument to print the outputs.
     * @return int  Returns 1 if the filling process is done successfully, returns 0 if it is not, returns -1 if the machine is full.
     */
    public static int fill(String line, MachineSlot[] slots, String output) {
        // Traverse through the slots and try to fill the product.
        for (int i = 0; i < slots.length; i++) {
                if ((slots[i].productName.equals(line.split("\t")[0])) && (slots[i].numberOfProduct < 10)) { // If the product type that is in the slot, matches the product in the input and there is enough place to put.
                    ++slots[i].numberOfProduct;
                    return 1;
                } else if (slots[i].numberOfProduct == 0) { // If the slot is empty.
                    // definer() method changes the slot object's variables according to input. 
                    slots[i].assign(line);
                    ++slots[i].numberOfProduct;
                    return 1;
                }
        }
        
        // Check whether if all the slots are completely full or there is no slot available.
        // If full, return -1. If not but there is no slot left with that product type, return 0.  
        int count = 0;
        for (int i = 0; i < slots.length; i++) {  
            if (slots[i].numberOfProduct == 10) {
                ++count;
            }
        }
        FileOutput.writeToFile(output, "INFO: There is no available place to put "  + line.split("\t")[0], true, true);
        if (count == 24) {
            FileOutput.writeToFile(output, "INFO: The machine is full!", true, true);
            return -1;     
        }
        return 0;
    }
    

    
    /** 
     * This method takes the purchase of the customer.
     * Only 1, 5, 10, 20, 50, 100 and 200 TL accepted.
     * Input consists of the information about cash, choice and value.
     * 
     * @param line String line that consists customer's purchase informations.
     * @param slots A MachineSlot array that represents the slots of the machine.
     * @param output Output argument to print the outputs.
     * @return int  If there is an error returns -1, if not returns 1.
     */
    public static int purchase(String line, MachineSlot[] slots, String output) {
        FileOutput.writeToFile(output, "INPUT: " + line, true, true);
        String choice = line.split("\t")[2];
        String[] cash = (line.split("\t")[1]).split(" ");
        int order = Integer.parseInt(line.split("\t")[3] );
        
        // Check if the given cash is valid or not. If it is not valid, there will be an info message and the cash will be returned.
        String[] money = {"1", "5", "10", "20", "50", "100", "200"};
        int totalMoney = 0;
        for (String i : cash) {
            boolean isAcceptable = false;
            for (String j : money) {
                if (i.equals(j)) { // If the cash is acceptable.
                    totalMoney += Integer.parseInt(j);
                    isAcceptable = true;
                    break;
                }
            }
            if (!isAcceptable) { // If the cash is not acceptable.
                FileOutput.writeToFile(output, "INFO:" + i + " TL cash is not valid.", true, true);
            }
        }

        switch (choice) {
            case "NUMBER":
                if (order > slots.length) { // If the number of the order is out of the machine's index.
                    FileOutput.writeToFile(output, "INFO: Number cannot be accepted. Please try again with another number.", true, true);
                    FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    return -1;
                } else if (slots[order].numberOfProduct == 0) { // If the slot is empty.
                    FileOutput.writeToFile(output, "INFO: This slot is empty, your money will be returned.", true, true);
                    FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    return -1;
                }
                if (slots[order].price <= totalMoney) { // If the given cash is enough to buy that product.
                    FileOutput.writeToFile(output, "PURCHASE: You have bought one " + slots[order].productName, true, true);
                    int change = totalMoney - slots[order].price;
                    slots[order].numberOfProduct--;
                    if (slots[order].numberOfProduct == 0) { // If the slot is empty after the purchase, reset the slot's data.
                        slots[order].reset();
                    }
                    FileOutput.writeToFile(output, "RETURN: Returning your change: " + change + " TL", true, true);
                    return 1;
                } else {
                    FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                    FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    return -1;
                }
            case "CALORIE":
                for (MachineSlot slot : slots) {
                    if (slot.calorie >= (order - 5) && slot.calorie <= (order + 5) && slot.price <= totalMoney) { // If the calorie of the slot is in the desired range and the given money is enough.
                        FileOutput.writeToFile(output, "PURCHASE: You have bought one " + slot.productName, true, true);
                        int change = totalMoney - slot.price;
                        slot.numberOfProduct--;
                        if (slot.numberOfProduct == 0) { // If the slot is empty after the purchase, reset the slot's data.
                            slot.reset();
                        }
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + change + " TL", true, true);
                        return 1;
                    } else if (slot.calorie >= (order - 5) && slot.calorie <= (order + 5) && slot.price > totalMoney) { // If product found in desired range but money is not enough.
                        FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                        return -1;
                    }
                }
                FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
                FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                return -1;
            case "PROTEIN":
                for (MachineSlot slot : slots) {
                    if (slot.protein >= (order - 5) && slot.protein <= (order + 5) && slot.price <= totalMoney) { // If the protein of the slot is in the desired range and the given money is enough.
                        FileOutput.writeToFile(output, "PURCHASE: You have bought one " + slot.productName, true, true);
                        int change = totalMoney - slot.price;
                        slot.numberOfProduct--;
                        if (slot.numberOfProduct == 0) { // If the slot is empty after the purchase, reset the slot's data.
                            slot.reset();
                        }
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + change + " TL", true, true);
                        return 1;
                    } else if (slot.protein >= (order - 5) && slot.protein <= (order + 5) && slot.price > totalMoney) { // If product found in desired range but money is not enough.
                        FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                        return -1;
                    }
                }
                FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
                FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                return -1;
            case "CARB":
                for (MachineSlot slot : slots) {
                    if (slot.carb >= (order - 5) && slot.carb <= (order + 5) && slot.price <= totalMoney) { // If the carb of the slot is in the desired range and the given money is enough.
                        FileOutput.writeToFile(output, "PURCHASE: You have bought one " + slot.productName, true, true);
                        int change = totalMoney - slot.price;
                        slot.numberOfProduct--;
                        if (slot.numberOfProduct == 0) { // If the slot is empty after the purchase, reset the slot's data.
                            slot.reset();
                        }
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + change + " TL", true, true);
                        return 1;
                    } else if (slot.carb >= (order - 5) && slot.carb <= (order + 5) && slot.price > totalMoney) { // If product found in desired range but money is not enough.
                        FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                        return -1;
                    }
                }
                FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
                FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                return -1;
            case "FAT":
                for (MachineSlot slot : slots) {
                    if (slot.fat >= (order - 5) && slot.fat <= (order + 5) && slot.price <= totalMoney) { // If the fat of the slot is in the desired range and the given money is enough.
                        FileOutput.writeToFile(output, "PURCHASE: You have bought one " + slot.productName, true, true);
                        int change = totalMoney - slot.price;
                        slot.numberOfProduct--;
                        if (slot.numberOfProduct == 0) { // If the slot is empty after the purchase, reset the slot's data.
                            slot.reset();
                        }
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + change + " TL", true, true);
                        return 1;
                    } else if (slot.fat >= (order - 5) && slot.fat <= (order + 5) && slot.price > totalMoney) { // If product found in desired range but money is not enough.
                        FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                        return -1;
                    }
                }
                FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
                FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                return -1;
            default:
                FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
                FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                return -1;
        }
        
    }
    
}
