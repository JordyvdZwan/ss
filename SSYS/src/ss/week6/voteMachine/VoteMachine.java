package ss.week6.voteMachine;
import java.util.*;
public class VoteMachine {

	private PartyList partyList;
	private VoteList voteList;
	private VoteView voteView = new VoteTUIView(this);
	
	public VoteMachine() {
		partyList = new PartyList();
		voteList = new VoteList();
	}

	public static void main(String[] args) {
		VoteMachine voteMachine = new VoteMachine();
		voteMachine.start();
	}
	
	public void start() {
		partyList.addObserver(voteView);
		voteList.addObserver(voteView);
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
//package ss.week6.voteMachine;
//
//import java.util.List;
//import java.util.Map;
//
////import ss.week6.voteMachine.gui.VoteGUIView;
//
//public class VoteMachine {
//	
//	private VoteList votelist;
//	private PartyList partylist;
//	VoteView v;
//	
//	public static void main(String args[]){
//		VoteMachine vm = new VoteMachine();
//		vm.start();
//	}
//	
//	public VoteMachine(){
//		votelist = new VoteList();
//		partylist = new PartyList();
//	}
//	
//	public void start(){
//		v = new VoteGUIView(this);
//		votelist.addObserver(v);
//		partylist.addObserver(v);
//		v.start();
//	};
//	
//	public void addParty(String name){
//		partylist.addParty(name);
//	}
//	
//	public void addVote(String party){
//		if (partylist.getParties().contains(party)) votelist.addVote(party);
//		else v.showError("Party not found");;
//	}
//	
//	public List<String> getParties(){
//		return partylist.getParties();
//	}
//	
//	public Map<String, Integer> getVotes(){
//		return votelist.getVotes();
//	}
//}