package cryptology.des;

import cryptology.io.Out;
import cryptology.util.Convert;
import cryptology.util.text.Text;

public class DES {
	private static final int BLOCK_LENGTH = 64;
	private static final int KEY_LENGTH = 64;
	private static final int ROUNDS = 16;

	private static final int[] PC1 = {
			57, 49, 41, 33, 25, 17, 9,
			1, 58, 50, 42, 34, 26, 18,
			10, 2, 59, 51, 43, 35, 27,
			19, 11, 3, 60, 52, 44, 36,
			63, 55, 47, 39, 31, 23, 15,
			7, 62, 54, 46, 38, 30, 22,
			14, 6, 61, 53, 45, 37, 29,
			21, 13, 5, 28, 20, 12, 4
	};

	private static final int[] KEY_SHIFTS = {
			1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};

	private static final int[] PC2 = {
			14, 17, 11, 24, 1, 5,
			3, 28, 15, 6, 21, 10,
			23, 19, 12, 4, 26, 8,
			16, 7, 27, 20, 13, 2,
			41, 52, 31, 37, 47, 55,
			30, 40, 51, 45, 33, 48,
			44, 49, 39, 56, 34, 53,
			46, 42, 50, 36, 29, 32
	};

	private static final int[][] s1 = {
			{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
			{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
			{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
			{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
	};

	private static final int[][] s2 = {
			{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
			{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
			{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
			{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
	};

	private static final int[][] s3 = {
			{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
			{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
			{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
			{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
	};

	private static final int[][] s4 = {
			{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
			{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
			{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
			{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
	};

	private static final int[][] s5 = {
			{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
			{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
			{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
			{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
	};

	private static final int[][] s6 = {
			{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
			{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
			{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
			{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
	};

	private static final int[][] s7 = {
			{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
			{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
			{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
			{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
	};

	private static final int[][] s8 = {
			{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
			{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
			{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
			{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
	};

	private static final int[][][] s = { s1, s2, s3, s4, s5, s6, s7, s8 };

	private static final int[] E = {
			32, 1, 2, 3, 4, 5,
			4, 5, 6, 7, 8, 9,
			8, 9, 10, 11, 12, 13,
			12, 13, 14, 15, 16, 17,
			16, 17, 18, 19, 20, 21,
			20, 21, 22, 23, 24, 25,
			24, 25, 26, 27, 28, 29,
			28, 29, 30, 31, 32, 1
	};

	private static final int[] P = {
			16, 7, 20, 21,
			29, 12, 28, 17,
			1, 15, 23, 26,
			5, 18, 31, 10,
			2, 8, 24, 14,
			32, 27, 3, 9,
			19, 13, 30, 6,
			22, 11, 4, 25
	};

	private static final int[] IP = {
			58, 50, 42, 34, 26, 18, 10, 2,
			60, 52, 44, 36, 28, 20, 12, 4,
			62, 54, 46, 38, 30, 22, 14, 6,
			64, 56, 48, 40, 32, 24, 16, 8,
			57, 49, 41, 33, 25, 17, 9, 1,
			59, 51, 43, 35, 27, 19, 11, 3,
			61, 53, 45, 37, 29, 21, 13, 5,
			63, 55, 47, 39, 31, 23, 15, 7
	};

	private static final int[] IPi = {
			40, 8, 48, 16, 56, 24, 64, 32,
			39, 7, 47, 15, 55, 23, 63, 31,
			38, 6, 46, 14, 54, 22, 62, 30,
			37, 5, 45, 13, 53, 21, 61, 29,
			36, 4, 44, 12, 52, 20, 60, 28,
			35, 3, 43, 11, 51, 19, 59, 27,
			34, 2, 42, 10, 50, 18, 58, 26,
			33, 1, 41, 9, 49, 17, 57, 25
	};

	private int maxPreviewRounds = 3;

	private long[] K = new long[ROUNDS];
	private int previewRounds;

	public DES() {}

	public Text encrypt(String key, Text plaintext) {
		return processText(Operation.Encrypt, key, plaintext);
	}

	public Text decrypt(String key, Text plaintext) {
		return processText(Operation.Decrypt, key, plaintext).trim();
	}

	private Text processText(Operation operation, String key, Text plaintext) {
		var startTime = System.currentTimeMillis();
		previewRounds = maxPreviewRounds;
		String textBits = Convert.bytesToBin(plaintext);
		buildKeySchedule(hash(key));

		/* Pad the input text if needed */
		int remainder = textBits.length() % BLOCK_LENGTH;
		if (remainder != 0) {
			Out.print("Дополняем входной текст %d нулями до размера кратного блоку...", BLOCK_LENGTH - remainder);
			for (int i = 0; i < (BLOCK_LENGTH - remainder); i++)
				textBits = "0" + textBits;
		}

		/* Create blocks from the input bit string */
		String[] inputBitBlocks = new String[textBits.length() / BLOCK_LENGTH];
		for (int i = 0, offset = 0; i < inputBitBlocks.length; i++) {
			inputBitBlocks[i] = textBits.substring(offset, offset + BLOCK_LENGTH);
			offset += BLOCK_LENGTH;
		}

		Out.print("%s %d блоков (%d байт) текста...",
				operation == Operation.Encrypt ? "Шифруем" : "Дешифруем",
				inputBitBlocks.length, textBits.length() / 8);

		/* Process the blocks */
		String outputBits = "";
		for (int i = 0; i < inputBitBlocks.length; i++)
			outputBits += processBlock(operation, inputBitBlocks[i]);

		/* Clear round keys */
		for (int i = 0; i < ROUNDS; i++)
			K[i] = 0;

		Out.print("Готово! %s за %.3f секунд.",
				operation == Operation.Encrypt ? "Зашифровано" : "Расшифровано",
				(System.currentTimeMillis() - startTime) / 1000f);
		plaintext.set(Convert.binToBytes(outputBits));
		return plaintext;
	}

	private String processBlock(Operation operation, String binBlock) {
		int length = binBlock.length();
		if (length != BLOCK_LENGTH)
			throw new RuntimeException("Invalid block length");

		/* Initial permutation */
		String out = "";
		for (int i = 0; i < IP.length; i++)
			out = out + binBlock.charAt(IP[i] - 1);

		String mL = out.substring(0, 32);
		String mR = out.substring(32);

		for (int i = 0, ki; i < ROUNDS; i++) {
			// 48-bit round key
			ki = operation == Operation.Encrypt ? (i) : (15 - i);
			String roundKey = Long.toBinaryString(K[ki]);
			while (roundKey.length() < 48)
				roundKey = "0" + roundKey;

			// Get 32-bit result from f with m1 and ki
			String fResult = f(mR, roundKey);

			// XOR m0 and f
			long f = Long.parseLong(fResult, 2);
			long cmL = Long.parseLong(mL, 2);

			long m2 = cmL ^ f;
			String m2String = Long.toBinaryString(m2);

			while (m2String.length() < 32)
				m2String = "0" + m2String;

			if (previewRounds > 0) {
				Out.print("Раунд #%d", i + 1);
				Out.print("[L%d: %s] [R%1$d: %s]", i + 1, mL, mR);
				Out.print("K%d = %s (48 бит); f(K%1$d) = %s (32 бита);", i + 1, roundKey, fResult);
				Out.print("L%1$d = R%2$d; R%1$d = L%2$d (+) f(K%2$d);", i + 2, i + 1);
				Out.print("[L%d: %s] [R%1$d: %s]", i + 2, mR, m2String);
				Out.print("");
			}

			mL = mR;
			mR = m2String;

			if (previewRounds-- == 0)
				Out.print("...\n");
		}

		String in = mR + mL;
		String output = "";
		for (int i = 0; i < IPi.length; i++)
			output = output + in.charAt(IPi[i] - 1);

		return output;
	}

	public String encryptBlock(String plaintextBlock) {
		return processBlock(Operation.Encrypt, plaintextBlock);
	}

	public String decryptBlock(String plaintextBlock) {
		return processBlock(Operation.Decrypt, plaintextBlock);
	}

	/**
	 * Prepare 16 48-bit round keys from 64-bit key
	 */
	private void buildKeySchedule(long key) {
		String binKey = Long.toBinaryString(key);
		while (binKey.length() < KEY_LENGTH)
			binKey = "0" + binKey;

		// PC1 (64 -> 56 bit)
		String binKeyPC1 = "";
		for (int i = 0; i < PC1.length; i++)
			binKeyPC1 = binKeyPC1 + binKey.charAt(PC1[i] - 1);

		// Split permuted string in half | 56/2 = 28
		String sL = binKeyPC1.substring(0, 28);
		String sR = binKeyPC1.substring(28);
		int iL = Integer.parseInt(sL, 2);
		int iR = Integer.parseInt(sR, 2);

		for (int i = 0; i < ROUNDS; i++) {
			// Perform left shifts according to key shift array
			iL = Integer.rotateLeft(iL, KEY_SHIFTS[i]);
			iR = Integer.rotateLeft(iR, KEY_SHIFTS[i]);

			// Merge the two halves
			long merged = ((long) iL << 28) + iR;

			// 56-bit merged
			String sMerged = Long.toBinaryString(merged);
			while (sMerged.length() < 56)
				sMerged = "0" + sMerged;

			// Apply PC2 (56 -> 48 bit)
			String binKeyPC2 = "";
			for (int j = 0; j < PC2.length; j++)
				binKeyPC2 = binKeyPC2 + sMerged.charAt(PC2[j] - 1);

			K[i] = Long.parseLong(binKeyPC2, 2);
		}
	}

	/**
	 * mi : 32-bit message half-block binary string
	 * key : 48-bit round key binary string
	 * Returns 32-bit output binary string
	 */
	public static String f(String mi, String key) {
		// Expansion function E
		String ei = "";
		for (int i = 0; i < E.length; i++)
			ei = ei + mi.charAt(E[i] - 1);

		long m = Long.parseLong(ei, 2);
		long k = Long.parseLong(key, 2);

		// XOR expanded message block and key block (48 bits)
		Long result = m ^ k;

		String bin = Long.toBinaryString(result);
		while (bin.length() < 48)
			bin = "0" + bin;

		// Split into eight 6-bit strings
		String[] sin = new String[8];
		for (int i = 0; i < 8; i++) {
			sin[i] = bin.substring(0, 6);
			bin = bin.substring(6);
		}

		// Do S-Box calculations
		String[] sout = new String[8];
		for (int i = 0; i < 8; i++) {
			int[][] curS = s[i];
			String cur = sin[i];

			// Do S-Box lookup
			int row = Integer.parseInt(cur.charAt(0) + "" + cur.charAt(5), 2);
			int col = Integer.parseInt(cur.substring(1, 5), 2);
			sout[i] = Integer.toBinaryString(curS[row][col]);

			while (sout[i].length() < 4)
				sout[i] = "0" + sout[i];
		}

		// Merge S-Box outputs into one 32-bit string
		String merged = "";
		for (int i = 0; i < 8; i++)
			merged = merged + sout[i];

		// Apply Permutation P
		String mergedP = "";
		for (int i = 0; i < P.length; i++)
			mergedP = mergedP + merged.charAt(P[i] - 1);
		return mergedP;
	}

	public static long hash(String string) {
		long h = 1125899906842597L;
		int len = string.length();

		for (int i = 0; i < len; i++) {
			h = 31 * h + string.charAt(i);
		}
		return h;
	}

	public void setMaxPreviewRounds(int rounds) {
		this.maxPreviewRounds = rounds;
	}
	
	private enum Operation {
		Encrypt,
		Decrypt;
	}
}
