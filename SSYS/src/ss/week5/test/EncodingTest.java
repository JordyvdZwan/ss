package ss.week5.test;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.*;

/**
 * A simple class that experiments with the Hex encoding
 * of the Apache Commons Codec library.
 *
 */
public class EncodingTest {
    public static void main(String[] args) throws DecoderException {
        System.out.println(" ");
        System.out.println("Encoding Hello Big World (Hex) ");
    	String input = "Hello Big World";
        System.out.println(">> " + Hex.encodeHexString(input.getBytes()));
        
        byte[] byteArray = Hex.encodeHexString(input.getBytes()).getBytes();
       
        System.out.println(" ");
        System.out.println("Trying to decode Hello Big World (Hex)");
        System.out.println(">> " + new String(byteArray));
        System.out.println(">> " + Hex.decodeHex(new String(byteArray).toCharArray()));
       
        System.out.println(" ");
        System.out.println("trying to encode Hello Worlds (Base64)");
        input = "Hello World";
        System.out.println(">> " + Base64.encodeBase64(input.getBytes()));
        
        System.out.println(" ");
        System.out.println("(Base64) encoding: ");
        input = "010203040506";
        System.out.println(input);
        System.out.println(">> " + Base64.encodeBase64(Hex.decodeHex(input.toCharArray()).toString().getBytes()));
    	
        System.out.println(" ");
        System.out.println("testing to decode: U29mdHdhcmUgU3lzdGVtcw==  (Base64)");
        input = "U29mdHdhcmUgU3lzdGVtcw==";
    	System.out.println(">> " + Base64.decodeBase64(input));
    	
        System.out.println(" ");
        System.out.println("testing a, aa, aaa, etc. (Base64)");
    	System.out.println(">> " + Base64.encodeBase64String(new String("a").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aa").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aaa").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aaaa").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aaaaa").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aaaaaa").getBytes()));
    	System.out.println(">> " + Base64.encodeBase64String(new String("aaaaaaa").getBytes()));
    }
}
