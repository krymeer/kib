public class FirstChar {
	public static String choose(int id, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
		String s = "";
		switch(id) {
			case 0:
				s = Crypto.makeString(0, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 1:
				s = Crypto.makeString(1, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 2:
				s = Crypto.makeString(2, i1, i2, i3, i4, i5, i6, i7);
				break;
			case 3:
				s = Crypto.makeString(3, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 4:
				s = Crypto.makeString(4, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 5:
				s = Crypto.makeString(5, i1, i2, i3, i4, i5, i6, i7);
				break;
			case 6:
				s = Crypto.makeString(6, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 7:
				s = Crypto.makeString(7, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 8:
				s = Crypto.makeString(8, i1, i2, i3, i4, i5, i6, i7);
				break;
			case 9:
				s = Crypto.makeString(9, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 10:
				s = Crypto.makeString(10, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 11:
				s = Crypto.makeString(11, i1, i2, i3, i4, i5, i6, i7);
				break;
			case 12:
				s = Crypto.makeString(12, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 13:
				s = Crypto.makeString(13, i1, i2, i3, i4, i5, i6, i7);
				break;											
			case 14:
				s = Crypto.makeString(14, i1, i2, i3, i4, i5, i6, i7);
				break;
			case 15:
				s = Crypto.makeString(15, i1, i2, i3, i4, i5, i6, i7);
				break;				
		}
		return s;
	}
}