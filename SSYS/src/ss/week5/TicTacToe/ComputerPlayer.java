package ss.week5.TicTacToe;

public class ComputerPlayer extends Player {
	private Strategy strategy;
	
	public ComputerPlayer(Mark mark, Strategy strategy) {
		super(strategy.getName() + "-" + mark.toString(), mark);
		this.strategy = strategy;
	}
	
	public ComputerPlayer(Mark mark) {
		super(new NaiveStrategy().getName() + "-" + mark.toString(), mark);
		this.strategy = new NaiveStrategy();
	}
	
	@Override
	public int determineMove(Board board) {
		return strategy.determineMove(board, getMark());
	}

}
