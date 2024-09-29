import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] itemContent = FileInput.readFile(args[0], false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        String[] decorationContent = FileInput.readFile(args[1], false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[2], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
        
        ArrayList<ClassroomBuilder> classrooms = new ArrayList<>();
        ArrayList<Decoration> decorations = new ArrayList<>();
        ArrayList<Classroom> endClassrooms = new ArrayList<>(); // Classroom type arraylist, which the subclasses will be inserted. It provides Polymorphism.
        int totalPrice = 0;

        for (String line : itemContent) { // Foreach loop to traverse itemContent array.
            Builder.itemBuild(line, classrooms, decorations);
        }  

        for (String line :decorationContent){ // Foreach loop to traverse decorationContent array.
            endClassrooms.add(Builder.built(line, classrooms, decorations)); // Built final versions of classrooms according to input by using Builder Design Pattern.
        }

        for (int i = 0; i < endClassrooms.size(); i++){
                totalPrice += (int) endClassrooms.get(i).calculatePrice(args[2]);  // Calculating prices and doing implementation for different subclasses by using Polymorphism.
        }
        FileOutput.writeToFile(args[2],  "Total price is: "+ totalPrice +"TL.", true, false);   // Final printing of totalPrice.
    }
}
