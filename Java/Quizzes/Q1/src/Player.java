/**
 * The Player class represents a player in a game with methods to set name, score, and simulate
 * throwing dice.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Player {
    private String name;
    private int score = 0;

    // Getter and setters.
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    
    /**
     * The `thrower` function takes an array of two dice numbers as input and returns a score based on
     * certain conditions.
     * 
     * @param diceNumbers The `thrower` method takes an array of two strings representing dice numbers
     * as input. The first element in the array is the number rolled on the first dice, and the second
     * element is the number rolled on the second dice.
     * @return int  Returns 0 if the player skipped the turn,
     *              Returns -1 if the player lose the game,
     *              Returns 1 for other conditions.
     */
    private int thrower(String[] diceNumbers) {
        int firstDice = Integer.parseInt(diceNumbers[0]);
        int secondDice = Integer.parseInt(diceNumbers[1]);

        if (firstDice == 0) {
            return 0;
        } else if (firstDice == 1 && secondDice == 1) {
            return -1;
        } else if (firstDice == 1 || secondDice == 1) {
            return 1;
        } else {
            score += firstDice + secondDice;
            return 1;
        }
    }

    /**
     * The `game` function processes dice throws for multiple players, updating their scores and
     * handling game over scenarios.
     * 
     * @param content   input dice throws.
     * @param numberOfPlayers   number of players who is playing the game.
     * @param players   a Player class type array whose objects represents players.
     * @param args  command line arguments for writing the output.
     */
    public static void game(String[] content, int numberOfPlayers, Player[] players, String[] args) {
        for (int i = 2; i < content.length; i++ ) { // Start at i=2 because 0 and 1 are player informations.
            String[] diceNumbers = content[i].split("-");
            // Consider the iteration started from 0 and when we divide it with numberOfPlayers we get the index of the current player.
            // For example, in the first dice throw we are at the line in content[2] i-2 = 0 and the remainder is 0 so the index of the first player.
            // in the 6th dice throw (consider we have 4 players) we are at the line in content[7] i-2 = 5 and the remainder is 1 so the index of the second player.
            int currentPlayerIndex = (i-2) % numberOfPlayers;
            switch (players[currentPlayerIndex].thrower(diceNumbers)) {
                case -1:
                    String messageOver = String.format("%s threw 1-1. Game over %s!", players[currentPlayerIndex].getName(), players[currentPlayerIndex].getName());    
                    FileOutput.writeToFile(args[1], messageOver, true, true);
                    for (int j = currentPlayerIndex; j < players.length - 1; j++) { // Slide the object of the player who lost the game to end of the array.
                        players[j] = players[j + 1];
                    }
                    numberOfPlayers--; // Decrease numberOfPlayers so while checking currentPlayerIndex the player who lost the game won't be checked.
                    if (numberOfPlayers == 1) { // If there is only one player, he/she won the game.
                        currentPlayerIndex = (i-2) % numberOfPlayers;
                        String messageWinner = String.format("%s is the winner of the game with the score of %d. Congratulations %s!", players[currentPlayerIndex].getName(), players[currentPlayerIndex].getScore(), players[currentPlayerIndex].getName());
                        FileOutput.writeToFile(args[1], messageWinner, true, false);
                    }
                    break;
                case 0:
                    String messageEqual = String.format("%s skipped the turn and %s’s score is %d.", players[currentPlayerIndex].getName(), players[currentPlayerIndex].getName(), players[currentPlayerIndex].getScore());
                    FileOutput.writeToFile(args[1], messageEqual, true, true);
                    break;
                case 1:
                    String messagePoint = String.format("%s threw %s-%s and %s’s score is %d.", players[currentPlayerIndex].getName(), diceNumbers[0], diceNumbers[1], players[currentPlayerIndex].getName(), players[currentPlayerIndex].getScore());
                    FileOutput.writeToFile(args[1], messagePoint, true, true);
                    break;

            }
        }
    }
    
}
