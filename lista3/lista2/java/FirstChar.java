public class FirstChar {
	public static String makeString(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		String result = new Character(Crypto.alphabet.charAt(a)).toString() + new Character(Crypto.alphabet.charAt(b)).toString() + new Character(Crypto.alphabet.charAt(c)).toString() + new Character(Crypto.alphabet.charAt(d)).toString() + new Character(Crypto.alphabet.charAt(e)).toString() + new Character(Crypto.alphabet.charAt(f)).toString() + new Character(Crypto.alphabet.charAt(g)).toString() + new Character(Crypto.alphabet.charAt(h)).toString() + new Character(Crypto.alphabet.charAt(i)).toString();
		return result;
	}
	public static String choose(int id, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
		String s = "";
		switch(id) {
			case 0:
				s = makeString(0, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 1:
				s = makeString(1, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 2:
				s = makeString(2, i1, i2, i3, i4, i5, i6, i7, i8);
				break;
			case 3:
				s = makeString(3, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 4:
				s = makeString(4, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 5:
				s = makeString(5, i1, i2, i3, i4, i5, i6, i7, i8);
				break;
			case 6:
				s = makeString(6, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 7:
				s = makeString(7, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 8:
				s = makeString(8, i1, i2, i3, i4, i5, i6, i7, i8);
				break;
			case 9:
				s = makeString(9, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 10:
				s = makeString(10, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 11:
				s = makeString(11, i1, i2, i3, i4, i5, i6, i7, i8);
				break;
			case 12:
				s = makeString(12, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 13:
				s = makeString(13, i1, i2, i3, i4, i5, i6, i7, i8);
				break;											
			case 14:
				s = makeString(14, i1, i2, i3, i4, i5, i6, i7, i8);
				break;
			case 15:
				s = makeString(15, i1, i2, i3, i4, i5, i6, i7, i8);
				break;				
		}
		return s;
	}
}