package ss.week6.voteMachine;

import java.util.*;

public class VoteList extends Observable {
	private Map<String, Integer> votes;
	
	public VoteList() {
		votes = new HashMap<String, Integer>();
	}
	
	public void addVote(String party) {
		if (votes.containsKey(party)) {
			votes.put(party, votes.get(party) + 1);
		} else {
			votes.put(party, 1);
		}
		setChanged();
		notifyObservers("vote");
	}
	
	public Map<String, Integer> getVotes() {
		return votes;
	}

}
