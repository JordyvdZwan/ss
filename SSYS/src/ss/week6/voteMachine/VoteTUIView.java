package ss.week6.voteMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;

public class VoteTUIView implements VoteView {

	VoteMachine voteMachine;
	
	public VoteTUIView(VoteMachine voteMachine) {
		this.voteMachine = voteMachine;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		boolean doorgaan = true;
		Scanner in = new Scanner(System.in);
		while (doorgaan) {
			System.out.println("What do you want to do next?");
			System.out.println("VOTE [party] , ADD PARTY [party] , VOTES , PARTIES , EXIT , HELP");
			String input = in.nextLine();
			Scanner read = new Scanner(input);
			List<String> words = new ArrayList<String>();
			int counter = 0;
			while (read.hasNext()) {
				words.add(counter, read.next());
				counter++;
			}
			if (words.size() == 3) {
				if (words.get(0).equals("ADD") && words.get(1).equals("PARTY")) {
					voteMachine.addParty(words.get(2));
				} else {
					System.out.println("Error: invalid three words input!");
				}
			} else if (words.size() == 2) {
				if (words.get(0).equals("VOTE")) {
					voteMachine.vote(words.get(1));
				} else {
					System.out.println("Error: invalid two words input!");
				}
			} else if (words.size() == 1) {
				if (words.get(0).equals("votes")) {
					showVotes(voteMachine.getVotes());
				} else if (words.get(0).equals("parties")) {
					showParties(voteMachine.getParties());
				} else if (words.get(0).equals("exit")) {
					doorgaan = false;
					System.out.println("Shutting down...");
				} else 	if (words.get(0).equals("help")) {
					System.out.println("Enter one of the following commands to execute a method");
					System.out.println("VOTE [party] , ADD PARTY [party] , VOTES , PARTIES , EXIT , HELP");
				} else {
					System.out.println("Error: invalid one word input!");
				}
			} else {
				System.out.println("Error: no input!");
			}
			read.close();
		}
		in.close();
	}
	
	public void inputError() {
		System.out.println("Error: input was invalid!");
	}
	
	public void inputError(String message) {
		System.out.printf("Error: %s!", message);
	}

	@Override
	public void showVotes(Map<String, Integer> votes) {
		System.out.println("--------------- Votes ---------------");
		for (String party: votes.keySet()) {
			System.out.printf("\nParty:__________%s  Votes: %s ", party, votes.get(party));
		}
		System.out.println("\n---------------  End  ---------------");
	}

	@Override
	public void showParties(List<String> parties) {
		System.out.println("-------------- parties --------------");
		for (String party: parties) {
			System.out.printf("\nParty:__________%s", party);
		}
		System.out.println("\n---------------  End  ---------------");
	}

	@Override
	public void showError(String message) {
		System.out.printf("ERROR: %s", message);
	}

}
