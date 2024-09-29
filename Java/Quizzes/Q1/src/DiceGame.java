public class DiceGame {
    public static void main(String[] args) throws Exception {
        String[] content = FileInput.readFile(args[0], false, false); // Reads the file line by line as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[1], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.
        
        int numberOfPlayers = Integer.parseInt(content[0]);
        String[] playerNames = content[1].split(",");
        
        Player[] players = new Player[numberOfPlayers];
        for (int i = 0; i < players.length; i++) { // Create objects for each player.
            players[i] = new Player();
            players[i].setName(playerNames[i]);
        }
        
        Player.game(content, numberOfPlayers, players, args);
        
        
    }
}
