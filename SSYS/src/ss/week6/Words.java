package ss.week6;

import java.util.*;

public class Words {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();
		String[] words = sentence.split(" ");
		if (!words[0].equals("end")) {
			for (int i = 0; i < words.length; i++) {
				System.out.println("Word " + (i + 1) + " " + words[i]);
			}
		}
		in.close();
	}

}
