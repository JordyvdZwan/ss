package ss.week5.TicTacToe;

public class ComputerPlayer extends Player {
	private Strategy strategy;
	
	public ComputerPlayer(Mark mark, Strategy strategy) {
		super(strategy.getName() + "-" + mark.toString(), mark);
		this.setStrategy(strategy);
	}
	
	public ComputerPlayer(Mark mark) {
		super(new NaiveStrategy().getName() + "-" + mark.toString(), mark);
		this.setStrategy(new NaiveStrategy());
	}
	
	@Override
	public int determineMove(Board board) {
		return getStrategy().determineMove(board, getMark());
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

}
