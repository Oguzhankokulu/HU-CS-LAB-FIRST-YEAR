public class Main {
    public static void main(String[] args) throws Exception {
        String[] productContent = FileInput.readFile(args[0], false, false); // Reads the file line by line as it is without discarding or trimming anything and stores it in string array namely content.
        String[] purchaseContent = FileInput.readFile(args[1], false, false);
        FileOutput.writeToFile(args[2], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
        
        // Create a new 2D MachineSlot array and initialize all the objects.
        MachineSlot[] slots = new MachineSlot[24];
        for (int i = 0; i < slots.length; i++) {
                slots[i] = new MachineSlot();
        }
        
        // Fill the slots. If all the slots are full, stop.
        for (String line : productContent) {
            int i = MachineSlot.fill(line, slots, args[2]);
            if (i == -1) {
                break;
            }
        }
        
        // Starting status of the machine.
        FileOutput.writeToFile(args[2], "-----Gym Meal Machine-----", true, true);
        int column = 0; // column var for counting columns.
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].getProductName() != "") { // If the slot is not empty.
                String slotInfo = String.format("%s(%d, %d)___", slots[i].getProductName(), slots[i].getCalorie(), slots[i].getNumberOfProduct());
                FileOutput.writeToFile(args[2], slotInfo, true, false);
                column++;
            } else {
                FileOutput.writeToFile(args[2], "___(0, 0)___", true, false);
                column++;
            }
            if (column == 4) { // If we printed the 4 column, pass to new line.
                FileOutput.writeToFile(args[2], "", true, true);
                column = 0;
            }
        }
        FileOutput.writeToFile(args[2], "----------", true, true);
        
        // Handle the purchases.
        for (String line : purchaseContent) {
            MachineSlot.purchase(line, slots, args[2]);
        }
        
        // Ending status of the machine.
        FileOutput.writeToFile(args[2], "-----Gym Meal Machine-----", true, true);
        column = 0; // column var for counting columns
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].getProductName() != "") { // If the slot is not empty.
                String slotInfo = String.format("%s(%d, %d)___", slots[i].getProductName(), slots[i].getCalorie(), slots[i].getNumberOfProduct());
                FileOutput.writeToFile(args[2], slotInfo, true, false);
                column++;
            } else {
                FileOutput.writeToFile(args[2], "___(0, 0)___", true, false);
                column++;
            }
            if (column == 4) { // If we printed the 4 column, pass to new line.
                FileOutput.writeToFile(args[2], "", true, true);
                column = 0;
            }
        }
        FileOutput.writeToFile(args[2], "----------", true, true);
           
    }
}
