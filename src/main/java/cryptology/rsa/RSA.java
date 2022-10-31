package cryptology.rsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import cryptology.io.Out;
import cryptology.prime.PrimeGenerator;
import cryptology.util.Rng;
import cryptology.util.message.Message;

public class RSA {
	private Keys keys = new Keys();

	public Message encrypt(Message msg) {
		try (var cipherBytes = new ByteArrayOutputStream()) {
			for (var b : msg.getBytes())
				cipherBytes.write(encrypt(b));
			msg.set(cipherBytes.toByteArray());
			return msg;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Message decrypt(Message msg) {
		var plainBytes = new ByteArrayOutputStream();
		byte cipherByte[] = new byte[keys.getSize() / 8];

		try (var cipherBytes = new ByteArrayInputStream(msg.getBytes())) {
			while (cipherBytes.read(cipherByte) != -1)
				plainBytes.write(decrypt(new BigInteger(cipherByte)));
			msg.set(plainBytes.toByteArray());
			return msg;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] encrypt(byte b) {
		if (!keys.containsPublic())
			throw new RuntimeException("Trying to encrypt a message without a public key");

		var m = BigInteger.valueOf(b & 0xFF);
		if (m.compareTo(keys.n) > 0)
			throw new RuntimeException("Input byte value is greater than modulo");

		return m.modPow(keys.e, keys.n).toByteArray();
	}

	private byte decrypt(BigInteger c) {
		if (!keys.containsPrivate())
			throw new RuntimeException("Trying to decrypt a message without a private key");

		if (c.compareTo(keys.n) > 0)
			throw new RuntimeException("Input cipher block value is greater than modulo");

		return (byte) (c.modPow(keys.d, keys.n).byteValue());
	}

	public void generateKeys(int bits) {
		keys.generate(bits);
	}

	public Keys getKeys() {
		return keys;
	}

	public static class Keys {
		private BigInteger n;
		private BigInteger e;
		private BigInteger d;

		public Keys() {}

		public void generate(int bits) {
			var p = PrimeGenerator.generate(bits / 2);
			var q = PrimeGenerator.generate(bits / 2);

			Out.print("Сгенерированы проcтые множители:");
			Out.print("  p = %s (%d бит)", p, p.bitLength());
			Out.print("  q = %s (%d бит)", q, q.bitLength());

			n = p.multiply(q);
			/* phi = (p - 1) * (q - 1) */
			var phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

			e = phi;
			while (!phi.gcd(e).equals(BigInteger.ONE))
				e = Rng.nextBelow(phi);

			d = e.modInverse(phi);

			Out.print("Сгенерированы компоненты публичного (e, n) и приватного (d, n) ключей:");
			Out.print("  n = %s (%d бит)", n, n.bitLength());
			Out.print("  e = %s (%d бит)", e, e.bitLength());
			Out.print("  d = %s (%d бит)", d, d.bitLength());
		}

		public int getSize() {
			return nextPow2(n.bitLength());
		}

		public void setPublic(BigInteger e, BigInteger n) {
			this.e = e;
			this.n = n;
		}

		public void setPrivate(BigInteger d, BigInteger n) {
			this.d = d;
			this.n = n;
		}
		
		public void printKeys() {
			printPublic();
			printPrivate();
		}
		
		public void printPublic() {
			Out.print("Публичный ключ:");
			Out.print("  e = %s (%d бит)", e, e.bitLength());
			Out.print("  n = %s (%d бит)", n, n.bitLength());
		}
		
		public void printPrivate() {
			Out.print("Приватный ключ:");
			Out.print("  d = %s (%d бит)", d, d.bitLength());
			Out.print("  n = %s (%d бит)", n, n.bitLength());
		}

		public boolean containsPublic() {
			return e != null && n != null;
		}

		public boolean containsPrivate() {
			return d != null && n != null;
		}

		public BigInteger getN() {
			return n;
		}

		public BigInteger getE() {
			return e;
		}

		public BigInteger getD() {
			return d;
		}

		private static int nextPow2(final int x) {
			int power = 1;
			while (power < x)
				power *= 2;
			return power;
		}
	}
}
