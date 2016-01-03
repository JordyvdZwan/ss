package ss.week6.voteMachine;

import java.util.*;

public class PartyList extends Observable {

	private List<String> partys;
	
	public PartyList() {
		partys = new ArrayList<String>();
	}
	
	public void addParty(String party) {
		System.out.println("party"); //TODO
		partys.add(party);
		notifyObservers("party");
	}
	
	public List<String> getParties() {
		return partys;
	}
	
	public boolean hasParty(String party) {
		return partys.contains(party);
	}
	
}
