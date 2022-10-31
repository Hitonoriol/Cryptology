package cryptology.prime;

import java.math.BigInteger;

import cryptology.io.Out;
import cryptology.util.Rng;

public class PrimeGenerator {
	public static BigInteger generate(int bits, int t, boolean verbose) {
		var i = 0;
		BigInteger num;
		do {
			++i;
			num = Rng.nextBits(bits).setBit(0);
		} while (!PrimalityTest.rabinMiller(num, t));

		if (verbose)
			Out.print("Сгенерировано простое число в %d итераций по %d проверок простоты", i, t);

		return num;
	}
	
	public static BigInteger generate(int bits, int t) {
		return generate(bits, t, false);
	}
	
	public static BigInteger generate(int bits) {
		return generate(bits, 10);
	}
}
