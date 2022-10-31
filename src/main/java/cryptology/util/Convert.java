package cryptology.util;

import cryptology.util.message.Message;

public class Convert {
	public static byte[] binToBytes(String bin) {
		byte[] ciphertextBytes = new byte[bin.length() / 8];
		for (int j = 0; j < ciphertextBytes.length; j++) {
			String temp = bin.substring(0, 8);
			byte b = (byte) Integer.parseInt(temp, 2);
			ciphertextBytes[j] = b;
			bin = bin.substring(8);
		}
		return ciphertextBytes;
	}

	public static String bytesToBin(Message utf) {
		byte[] bytes = utf.getBytes();
		String bin = "";
		for (int i = 0; i < bytes.length; i++) {
			int value = bytes[i];
			for (int j = 0; j < 8; j++) {
				bin += ((value & 128) == 0 ? 0 : 1);
				value <<= 1;
			}
		}
		return bin;
	}
}
