package ss.week6.voteMachine;
import java.util.*;
public class VoteMachine {

	private PartyList partyList;
	private VoteList voteList;
	private VoteView voteView = new VoteGUIView(this);
	
	public VoteMachine() {
		partyList = new PartyList();
		voteList = new VoteList();
		partyList.addObserver(voteView);
		voteList.addObserver(voteView);
	}

	public static void main(String[] args) {
		VoteMachine voteMachine = new VoteMachine();
		voteMachine.start();
	}
	
	public void start() {
		voteView.start();
	}
	
	public List<String> getParties() {
		return partyList.getParties();
	}
	
	public Map<String, Integer> getVotes() {
		return voteList.getVotes();
	}
	
	public void addParty(String party) {
		partyList.addParty(party);
	}
	
	public void vote(String party) {
		voteList.addVote(party);
	}
	
}
