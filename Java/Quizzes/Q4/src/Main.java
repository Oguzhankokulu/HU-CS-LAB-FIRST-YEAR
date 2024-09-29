import java.util.ArrayList;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        String[] content = FileInput.readFile(args[0], false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[1], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
        ArrayList<Item<?>> inventory = new ArrayList<>(); // Create an arraylist to store items.
        for (String line : content){
            Item.analyzeInput(line, args[1], inventory);
        }
    }

}
