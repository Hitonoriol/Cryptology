package cryptology.prime;

import java.math.BigInteger;

import cryptology.util.Rng;

public class PrimalityTest {
	public static boolean rabinMiller(BigInteger p, int t) {
		if (p.equals(BigInteger.TWO))
			return true;

		if (p.compareTo(BigInteger.TWO) < 0)
			return false;

		if (p.mod(BigInteger.TWO).equals(BigInteger.ZERO))
			return false;

		var s = p.subtract(BigInteger.ONE);
		int b = 0;
		while (s.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
			s = s.divide(BigInteger.TWO);
			++b;
		}

		for (int i = 0; i < t; ++i) {
			var a = Rng.nextBelow(p);
			var z = a.modPow(s, p);
			if (!z.equals(BigInteger.ONE)) {
				var j = 0;
				while (!z.equals(p.subtract(BigInteger.ONE))) {
					if (j == b - 1)
						return false;
					else {
						++j;
						z = z.modPow(BigInteger.TWO, p);
					}
				}
			}
		}
		return true;
	}
}
