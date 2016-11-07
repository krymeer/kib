import java.util.Random;

public class Chars {
	public static String makeKey() {
		String alphabet = "0123456789abcdef", p = "";
		Random random = new Random();
		for (int i = 0; i < 64; i++) {
			p += alphabet.charAt(random.nextInt(16));
		}
		return p;
	}
	public static String utf8String(String s) {
		int i = 0;
		String result = "";
		while (i < s.length()) {
			String hex = s.substring(i, i+2);
			int h = Integer.parseInt(hex, 16);
			if (h < 128) {
				result += (char)h;
				i += 2;
			} else if (h >= 128 && h <= 255) {
				char c = check(s.substring(i, i+4));
				if (c == '0') {
					return "";
				} else {
					result += c;
				}
				i += 4;
			}
		}
		return result.trim();
	}
	public static char check(String s) {
		char c = '0';
		switch(s) {
			case "C484":
				c = 'Ą';
				break;
			case "C485":
				c = 'ą';
				break;
			case "C486":
				c = 'Ć';
				break;
			case "C487":
				c = 'ć';
				break;
			case "C498":
				c = 'Ę';
				break;
			case "C499":
				c = 'ę';
				break;
			case "C581":
				c = 'Ł';
				break;
			case "C582":
				c = 'ł';
				break;			
			case "C583":
				c = 'Ń';
				break;
			case "C584":
				c = 'ń';
				break;
			case "C393":
				c = 'Ó';
				break;	
			case "C3B3":
				c = 'ó';
				break;
			case "C59A":
				c = 'Ś';
				break;
			case "C59B":
				c = 'ś';
				break;
			case "C5B9":
				c = 'Ź';
				break;
			case "C5BA":
				c = 'ź';
				break;
			case "C5BB":
				c = 'Ż';
				break;
			case "C5BC":
				c = 'ż';
				break;
			default:
				c = '0';
		}
		return c;
	}
}


