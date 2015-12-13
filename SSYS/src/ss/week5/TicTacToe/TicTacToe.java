package ss.week5.TicTacToe;

/**
 * Executable class for the game Tic Tac Toe. The game can be played against the
 * computer. Lab assignment Module 2
 * 
 * @author Theo Ruys
 * @version $Revision: 1.4 $
 */
public class TicTacToe {
    public static void main(String[] args) {
    	Player player1;
    	Player player2;
    	if (args.length == 2) {
    		if (args[0] == "-N") {
    			player1 = new ComputerPlayer(Mark.XX, new NaiveStrategy());
    		} else if (args[0] == "-S") {
    			player1 = new ComputerPlayer(Mark.XX, new SmartStrategy());
    		} else {
    			player1 = new HumanPlayer(args[0], Mark.XX);
    		}
    		if (args[0] == "-N") {
    			player2 = new ComputerPlayer(Mark.OO, new NaiveStrategy());
    		} else if (args[1] == "-S") {
    			player2 = new ComputerPlayer(Mark.OO, new SmartStrategy());
    		} else {
    			player2 = new HumanPlayer(args[1], Mark.OO);;
    		}
    	} else {
	        player1 = new ComputerPlayer(Mark.XX, new NaiveStrategy());
	        player2 = new ComputerPlayer(Mark.OO, new SmartStrategy());
    	}
        Game game = new Game(player1, player2);
        game.start();
    }
}