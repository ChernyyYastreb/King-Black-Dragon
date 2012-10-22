package kbd.rsc;

public class Base64 { // ok i have to go the the bathroom brb

	private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();
	public static String encode(String str) {
		byte[] buffer = str.getBytes();
		int a = 0, i = 0, size = buffer.length;
		char[] ch = new char[((size + 2) / 3) * 4];
		while (i < size) {
			byte b01 = buffer[i++];
			byte b12 = i < size ? buffer[i++] : 0;
			byte b23 = i < size ? buffer[i++] : 0;
			int mask = 0x3F;
			ch[a++] = ALPHABET[(b01 >> 2) & mask];
			ch[a++] = ALPHABET[((b01 << 4) | ((b12 & 0xFF) >> 4)) & mask];
			ch[a++] = ALPHABET[((b12 << 2) | ((b23 & 0xFF) >> 6)) & mask];
			ch[a++] = ALPHABET[b23 & mask];
		}
		switch (size % 3) {
		case 1:
			ch[--a] = '=';
		case 2:
			ch[--a] = '=';
		}
		return new String(ch);
	}
}
