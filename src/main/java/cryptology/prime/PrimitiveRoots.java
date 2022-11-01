package cryptology.prime;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cryptology.io.Out;

public class PrimitiveRoots {
	
	public static List<BigInteger> computePrimeFactors(BigInteger p) {
		var factors = new ArrayList<BigInteger>();
		var phi = p.subtract(ONE);
		var n = phi;
		for (var i = TWO; i.multiply(i).compareTo(n) <= 0; i = i.add(ONE)) {
			if (n.mod(i).equals(ZERO)) {
				factors.add(i);
				while (n.mod(i).equals(ZERO))
					n = n.divide(i);
			}
		}

		if (n.compareTo(ONE) > 0)
			factors.add(n);
		Out.print("%s = %s", p, factors.stream().map(BigInteger::toString).collect(Collectors.joining(" * ")));
		return factors;
	}

	public static List<BigInteger> compute(BigInteger p, int maxRoots) {
		var roots = new ArrayList<BigInteger>();
		var factors = computePrimeFactors(p);
		var phi = p.subtract(ONE);

		int rootsComputed = 0;
		for (var res = TWO; res.compareTo(p.add(ONE)) < 0; res = res.add(ONE)) {
			boolean ok = true;
			for(var factor : factors) {
				ok &= !res.modPow(phi.divide(factor), p).equals(ONE);
				if (!ok)
					break;
			}

			if (ok) {
				roots.add(res);
				++rootsComputed;
				if (maxRoots > 0 && rootsComputed >= maxRoots)
					return roots;
			}
		}

		return Collections.emptyList();
	}

	public static List<BigInteger> compute(BigInteger p) {
		return compute(p, 1);
	}
}
