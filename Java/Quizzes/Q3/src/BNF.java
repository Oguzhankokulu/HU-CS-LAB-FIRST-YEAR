import java.util.HashMap;
import java.util.Map;

public class BNF {
    public static void main(String[] args) throws Exception {
        String[] content = FileInput.readFile(args[0], false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[1], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
        Map<String, String[]> database = new HashMap<>(); // Create a map to store values according to their keys. I used Map to store data because every terminal value assigned to a key, I think map is the best option.

        for (String line : content) { // Foreach loop to traverse content array.
            String[] input = line.split("->");
            String [] values = input[1].split("\\|");
            database.put(input[0], values); // Store the input in database.
        }
        
        String[] start = database.get("S"); // Starting line.
        String output = calculateBNF(database, start);
        FileOutput.writeToFile(args[1], "(" + output + ")", true, false);
    }

    /**
     * The function `calculateBNF` recursively generates a Backus-Naur Form (BNF) string representation
     * based on a given database and starting array of strings.
     * 
     * @param database  The `database` parameter is a `Map` that stores a mapping of terminal symbols to
     * their corresponding non-terminal or terminal symbols.
     * @param start This is an array of strings representing the starting symbols for
     * the BNF (Backus-Naur Form) calculation. Each string in the `start` array is a terminal symbol or
     * a combination of terminal symbols that need to be expanded.
     * @return String   Return value represents the result of recursively
     * processing the input `start` array based on the provided `database` map. The method processes
     * each character in the input strings from the `start` array and either replaces it with the
     * corresponding value from the `database` map (if available) or keeps the character as is if no
     * mapping is found.
     */
    public static String calculateBNF(Map<String, String[]> database, String[] start){
        String outputStr = "";
        if (start.length == 0){ // Base case, if there is no string left.
        return "";
       }

        for (int i = 0; i < start.length; i++){ // For every values of a key, for example, for "A" it's ["a","aa"]
            String terminal = start[i];
            for (int j = 0; j < terminal.length(); j++){ // For every char in a value, for example both "a"s in "aa".
                char currentChar = terminal.charAt(j);
                String currentStr = String.valueOf(currentChar); // To make chars into a single string
                try{
                    String[] next = database.get(currentStr); // Try to get the values of the current String, if the String is terminal then this assignment throws an ERROR.
                    outputStr += "(" + calculateBNF(database, next) + ")"; // If it's non-terminal, then use recursive approach to shape output str. Use "()" for every non-terminal element.
                } catch (Exception e){ // If it's a terminal value then simply add it to output String.
                    outputStr += currentChar;
                    
                }
            }    
            if (i != start.length-1){ // If the values isn't the last then add "|" between values.
                outputStr += "|";
            }
        }
       
       return outputStr; // Return final String which is shaped recursively.
    }
}
