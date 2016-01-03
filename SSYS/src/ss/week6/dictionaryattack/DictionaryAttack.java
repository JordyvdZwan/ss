package ss.week6.dictionaryattack;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ss.week6.cards.Card;


public class DictionaryAttack {
	private Map<String, String> passwordMap;
	private Map<String, String> hashDictionary;

	/**
	 * Reads a password file. Each line of the password file has the form:
	 * username: encodedpassword
	 * 
	 * After calling this method, the passwordMap class variable should be
	 * filled withthe content of the file. The key for the map should be
	 * the username, and the password hash should be the content.
	 * @param filename
	 */
	public void readPasswords(String filename) throws IOException {
		try (Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));) {
			passwordMap = new HashMap<String, String>(); // <--- The key ---
			while (sc.hasNext()) {
				String[] split = new String[2];
				split = sc.nextLine().split(": ");
				passwordMap.put(split[0], split[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Given a password, return the MD5 hash of a password. The resulting
	 * hash (or sometimes called digest) should be hex-encoded in a String.
	 * @param password
	 * @return
	 */
	public String getPasswordHash(String password) {
		String hexCode = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] mdbytes = md.digest();

	        StringBuffer hexString = new StringBuffer();
	    	for (int i = 0; i < mdbytes.length; i++) {
	    		String hex = Integer.toHexString(0xff & mdbytes[i]);
	   	     	if (hex.length() == 1) {
	   	     		hexString.append('0');
	   	     	}
	   	     	hexString.append(hex);
	    	}			
			hexCode = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getStackTrace());
		}
		return hexCode;
//		byte[] bytelist = password.getBytes();
//		MessageDigest md = null;
//		String hexcode = null;
//		try {
//			md = MessageDigest.getInstance(password);    //.getInstance(password);
//			byte[] mD5hash = md.digest(bytelist);
//			StringBuilder sb = new StringBuilder(mD5hash.length * 2);
//			for (byte b : mD5hash) {
//				sb.append(String.format("%02x", b & 0xff));
//			}
//			hexcode = sb.toString();
//		} catch (NoSuchAlgorithmException e) {
//			return "spongebob";
//		}
//		return hexcode;
	}
	/**
	 * Checks the password for the user the password list. If the user
	 * does not exist, returns false.
	 * @param user
	 * @param password
	 * @return whether the password for that user was correct.
	 */
	public boolean checkPassword(String user, String password) {
		String encodedpassword = getPasswordHash(password);
		boolean result = false;
		if (passwordMap.containsKey(user) && passwordMap.get(user).equals(encodedpassword)) {
			result = true;
		}
		return result;
	}

	/**
	 * Reads a dictionary from file (one line per word) and use it to add
	 * entries to a dictionary that maps password hashes (hex-encoded) to
     * the original password.
	 * @param filename filename of the dictionary.
	 */
    public void addToHashDictionary(String filename) throws IOException {
		try (Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));) {
			passwordMap = new HashMap<String, String>(); // <--- The key ---
			while (sc.hasNext()) {
				String password = sc.next();
				passwordMap.put(getPasswordHash(password), password);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
    }
    	
    
	/**
	 * Do the dictionary attack.
	 */
	public void doDictionaryAttack() {
		try {
			hashDictionary = new HashMap<String, String>();
			passwordMap = new HashMap<String, String>();
			readPasswords("LeakedPasswords");
			addToHashDictionary("passwords");
			for (String user: passwordMap.keySet()) {
				System.out.printf("\n %s: %s", user, passwordMap.get(user));
			}
			for (String hash: hashDictionary.keySet()) {
				System.out.printf("\n %s: %s", hash, hashDictionary.get(hash));

			}
			for (String user: passwordMap.keySet()) {
				if (hashDictionary.keySet().contains(passwordMap.get(user))) {
					System.out.printf("\n %s: %s", user, hashDictionary.get(passwordMap.get(user)));
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		DictionaryAttack da = new DictionaryAttack();
		// To implement
		da.doDictionaryAttack();
	}

}
