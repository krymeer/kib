import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Random;
import java.lang.System;

class Run implements Runnable {
	private int id;
	public Run(int id) {
		this.id = id;
	}
	public void run() {
		int time = 0;
		int x = Crypto.alphabet.length()-1;
		for (int i1 = x; i1 >= 0; i1--) {	
			for (int i2 = x; i2 >= 0; i2--) {
				for (int i3 = x; i3 >= 0; i3--) {
					for (int i4 = x; i4 >= 0; i4--) {
						for (int i5 = x; i5 >= 0; i5--) {
							for (int i6 = x; i6 >= 0; i6--) {
								for (int i7 = x; i7 >= 0; i7--) {
									String s = "";
									s = FirstChar.choose(id, i1, i2, i3, i4, i5, i6, i7);
									if (!Crypto.done) {
										try {
											String key = s + Crypto.suffix;
											long elapsed = (System.currentTimeMillis()-Crypto.timeStart)/1000;
											if (elapsed % 60 == 0 && elapsed > 0 && id == 1 && time != (elapsed/60)) {
												time = (int)(elapsed/60);
												System.out.println("Time elapsed: " + (time) + " min / number of checked keys: " + Crypto.total + " / one of checked keys: " + key);
											}
											byte[] keyBytes = Crypto.toByteArray(key);
											SecretKeySpec secret = new SecretKeySpec(keyBytes,"AES");
											Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
											cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(Crypto.iv));
											byte[] result = cipher.doFinal(Crypto.ciphertext);
											String p = DatatypeConverter.printHexBinary(result);
											Crypto.total++;
											if (Crypto.isStringValid(p)) {
												System.out.println("Message found!\n\n"+p);
												Crypto.done = true;
												long timeOut = System.currentTimeMillis();
												System.out.println("\nTime elapsed: " + (timeOut-Crypto.timeStart)/1000 + "s / n = " + Crypto.total + " / key = " + key + "\n");
												return;
											} else {
												continue;
											}
										} catch (BadPaddingException ex) {
											continue;
										} catch (Exception ex) {
											ex.printStackTrace();
											continue;
										}
									} else {
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}


public class Crypto {
	public static boolean done = false;
	public static byte[] iv, ciphertext;
	public static String suffix = "";
	public static String alphabet = "0123456789abcdef";
	public static int total;
	public static long timeStart = System.currentTimeMillis();
	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
	public static String makeString(int a, int b, int c, int d, int e, int f, int g, int h) {
		String result = new Character(alphabet.charAt(a)).toString() + new Character(alphabet.charAt(b)).toString() + new Character(alphabet.charAt(c)).toString() + new Character(alphabet.charAt(d)).toString() + new Character(alphabet.charAt(e)).toString() + new Character(alphabet.charAt(f)).toString() + new Character(alphabet.charAt(g)).toString() + new Character(alphabet.charAt(h)).toString();
		return result;
	}
	public static boolean isStringValid(String s) {
		int i = 0;
		if (s.length() <= 1) {
			return false;
		}
		String result = "";
		while (i < s.length()) {
			String hex = s.substring(i, i+2);
			int h = Integer.parseInt(hex, 16);
			if (h < 128) {
				result += (char)h;
				i += 2;
			} else if (h >= 128 && h <= 255) {
				char c = SpecialChars.check(s.substring(i, i+4));
				if (c == '0') {
					return false;
				} else {
					result += c;
				}
				i += 4;
			}
		}
		System.out.println("\n" + result.trim());
		return true;
	}
	public static void main (String[] args) throws Exception {
		total = 0;
		int thr = 16;
		if (args.length != 3) {
			System.out.println("\nError! 3 parameters required:\n- iv;\n- suffix of key;\n- ciphertext.\n");
			return;
		}
		iv = toByteArray(args[0]);
		suffix = args[1];
		ciphertext = DatatypeConverter.parseBase64Binary(args[2]);
		Runnable[] runners = new Runnable[thr];
		Thread[] threads = new Thread[thr];
		for (int i = 0; i < runners.length; i++) {
			runners[i] = new Run(i);
		}
		for (int i = 0; i < runners.length; i++) {
			threads[i] = new Thread(runners[i]);
		}
		for (int i = 0; i < runners.length; i++) {
			threads[i].start();
		}
	}
}