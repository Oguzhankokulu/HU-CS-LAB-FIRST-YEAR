import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents an item in an inventory system. It uses generics to hold products of any type that extend the `Product` class.
 * 
 * @param <T> The type of product this Item holds. T must be a subclass of `Product`.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Item<T extends Product> {
    private T product; // The product associated with this item.

    /**
    * Constructs an Item object with a given product.
    * 
    * @param product The product associated with this item.
    */
    public Item(T product) {
        this.product = product;
    }

    // Setter and getters.
    public T getProduct() {
        return product;
    }

    public String getName() {
        return product.getName();
    }

    public void setName(String name) {
        product.setName(name);
    }

    public int getBarcode() {
        return product.getBarcode();
    }

    public void setBarcode(int barcode) {
        product.setBarcode(barcode);
    }

    public double getPrice() {
        return product.getPrice();
    }

    public void setPrice(double price) {
        product.setPrice(price);
    }

     /**
    * Gets a product-specific attribute based on the product type.
    * 
    * This method uses instanceof checks to determine the product type and then casts the product 
    * to the appropriate subclass to access the specific attribute.
    * 
    * @return The product-specific attribute value (e.g., "Author" for Book, "Color" for Toy, "Kind" for Stationery), 
    *         or null if the product type is not recognized.
    */
    public String getAttribute(){
        if (product instanceof Book){ // Check if product is a Book instance.
            return "Author";
        } else if (product instanceof Toy){ // Check if product is a Toy instance.
            return "Color";
        } else if (product instanceof Stationery){ // Check if product is a Stationery instance.
            return "Kind";
        } else {
            return null;
        }
    }

    /**
    * Gets the value of a product-specific attribute based on the product type.
    * 
    * This method uses instanceof checks to determine the product type and then casts the product 
    * to the appropriate subclass to access the specific attribute getter method.
    * 
    * @return The value of the product-specific attribute (e.g., author name for Book, color for Toy, kind for Stationery), 
    *         or null if the product type is not recognized.
    */
    public String getAttributeValue(){
        if (product instanceof Book) { // Check if product is a Book instance.
            return ((Book) product).getAuthor(); // Cast to Book and access author.
        } else if (product instanceof Toy) { // Check if product is a Toy instance.
            return ((Toy) product).getColor(); // Cast to Toy and access color.
        } else if (product instanceof Stationery) { // Check if product is a Stationery instance.
            return ((Stationery) product).getKind(); // Cast to Stationery and access kind.
        } else {
            return null;
        }
    }
    
    /**
    * Analyzes a line of input from a file, performs the specified operation ("ADD", "REMOVE", "SEARCHBYBARCODE", or "SEARCHBYNAME"),
    * and writes the results to a file.
    * 
    * This method uses a switch statement to handle different operations based on the first element in the input line.
    * 
    * @param line The line of input from a file
    * @param path path of output file
    * @param inventory arraylist of inventory which includes all items.
    */
    public static void analyzeInput(String line, String path, ArrayList<Item<?>> inventory){
        String[] commands = line.split("\t");
        // Declare the commands for after use.
        String operation = commands[0];
        String itemType, name, attribute;
        int barcode;
        double price;

        switch (operation){
            case "ADD":
            // Initialize the commands.
            itemType = commands[1];
            name = commands[2];
            attribute = commands[3];
            barcode = Integer.parseInt(commands[4]);
            price = Double.parseDouble(commands[5]);
            // Create the item and add it to inventory.
            Item<?> newItem = Item.createItem(itemType, name, attribute, barcode, price);
            inventory.add(newItem);
            break;
            case "REMOVE":
            barcode = Integer.parseInt(commands[1]); // Initialize the barcode.
            // Base string.
            String itemStr = "Item is not found.\n";
            // Check the inventory if the item is present, If it is then remove the item from inventory.
            for (Item<?> item : inventory){
                if(barcode == item.getBarcode()){
                    itemStr = "Item is removed.\n"; // Set the string
                    inventory.remove(item);
                    break;
                }
            }
            // Write to the output file.
            FileOutput.writeToFile(path, "REMOVE RESULTS:\n"+ itemStr +"------------------------------", true, true);
            break;
            case "SEARCHBYBARCODE":
            barcode = Integer.parseInt(commands[1]); // Initialize the barcode.
            // Base string.
            itemStr = "Item is not found.\n";
            // Check the inventory if the item is present, If it is then set itemStr to the item's informations.
            for (Item<?> item : inventory){
                if(barcode == item.getBarcode()){
                    itemStr = String.format("%s of the %s is %s. Its barcode is %d and its price is %s\n", item.getAttribute(), item.getName(), item.getAttributeValue(), item.getBarcode(), String.valueOf(item.getPrice()));
                    break;
                }
            }
            // Write to the output file.
            FileOutput.writeToFile(path, "SEARCH RESULTS:\n"+ itemStr +"------------------------------", true, true);
            break;
            case "SEARCHBYNAME":
            name = commands[1]; // Initialize the name.
            // Base string.
            itemStr = "Item is not found.\n";
            // Check the inventory if the item is present, If it is then set itemStr to the item's informations.
            for (Item<?> item : inventory){
                if(name.equals(item.getName())){
                    itemStr = String.format("%s of the %s is %s. Its barcode is %d and its price is %s\n", item.getAttribute(), item.getName(), item.getAttributeValue(), item.getBarcode(), String.valueOf(item.getPrice()));
                    break;
                }
            }
            // Write to the output file.
            FileOutput.writeToFile(path, "SEARCH RESULTS:\n"+ itemStr +"------------------------------", true, true);
            break;
            case "DISPLAY":
            // Write to the output file.
            FileOutput.writeToFile(path, "INVENTORY:", true, true);
            // Lambda expression to sort items according to their attribute (kind, color, author).
            Collections.sort(inventory, (i1, i2) -> i1.getAttribute().compareTo(i2.getAttribute()));
            // Write all the item's information to the output file.
            for (Item<?> item : inventory){
                itemStr = String.format("%s of the %s is %s. Its barcode is %d and its price is %s", item.getAttribute(), item.getName(), item.getAttributeValue(), item.getBarcode(), String.valueOf(item.getPrice()));
                FileOutput.writeToFile(path, itemStr, true, true);
            }
            // Last strip.
            FileOutput.writeToFile(path,"------------------------------", true, true);
            break;
            default:
            break;
        }
    }
    
    /**
     * This method takes the item type ("Book", "Toy", or "Stationery") and other relevant information (name, attribute, barcode, price) as arguments.
     * It then creates the appropriate product object (Book, Toy, or Stationery) and wraps it in an Item object.
     * 
     * @param itemType The type of item to create ("Book", "Toy", or "Stationery").
     * @param name The name of the item.
     * @param attribute A product-specific attribute (e.g., "Author" for Book, "Color" for Toy, "Kind" for Stationery).
     * @param barcode The barcode of the item.
     * @param price The price of the item.
     * @return An Item object containing the specified product information, or null if the item type is invalid.
     */
    public static Item<?> createItem(String itemType, String name, String attribute, int barcode, double price) {
        switch (itemType) {
          case "Book":
            return new Item<>(new Book(name, attribute, barcode, price));
          case "Toy":
            return new Item<>(new Toy(name, attribute, barcode, price));
          case "Stationery":
            return new Item<>(new Stationery(name, attribute, barcode, price));
          default:
            return null;
        }
      }
      
}
