package cryptology.util;

import java.math.BigInteger;
import java.util.Random;

import cryptology.io.Out;

public class Rng {
	private static final Random random = new Random();

	public static int nextInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}

	public static BigInteger nextBelow(BigInteger n) {
		var length = n.bitLength();
		BigInteger num;
		do {
			num = new BigInteger(length, random);
		} while (num.compareTo(n) >= 0 || num.compareTo(BigInteger.ZERO) < 0);
		return num;
	}

	public static BigInteger nextBits(int numBits) {
		BigInteger n;
		do {
			n = new BigInteger(numBits, random);
		} while (n.bitLength() != numBits);
		return n;
	}
}
