package cryptology.diffiehellman;

import java.math.BigInteger;

import cryptology.io.Out;
import cryptology.prime.PrimeGenerator;
import cryptology.prime.PrimitiveRoots;

public class DiffieHellman {
	private BigInteger n, g;
	private Person a;
	private Person b;

	DiffieHellman() {}
	
	public DiffieHellman(BigInteger n) {
		setN(n);
	}
	
	public DiffieHellman(int bits, int t) {
		setN(PrimeGenerator.generate(bits, t, true));
	}
	
	public void setN(BigInteger n) {
		this.n = n;
		Out.print("  n = %s (%d бит)", n, n.bitLength());
		Out.print("Находим первый первообразный корень...");
		g = PrimitiveRoots.compute(n).get(0);
		Out.print("  g = %s", g);
		a = new Person("A", n, g);
		b = new Person("B", n, g);
	}
	
	public BigInteger getN() {
		return n;
	}

	public void simulate() {
		Out.print("Обмен ключами по схеме Диффи-Хеллмана между абонентами:\n%s%s", a, b);
		a.exchangeWith(b);
		Out.print("Обмен успешен (оба абонента обладают одинаковым ключом K): %b", a.keyEquals(b));
	}

	public static class Person {
		private String name;
		private BigInteger n;
		private BigInteger x, y;
		private BigInteger key;

		public Person(String name, BigInteger n, BigInteger g) {
			this.name = name;
			this.n = n;
			x = PrimeGenerator.generate(n.bitLength());
			y = g.modPow(x, n);
		}

		public void exchangeWith(Person peer) {
			sendPublic(peer);
			peer.sendPublic(this);
		}

		public boolean keyEquals(Person peer) {
			return key.equals(peer.key);
		}

		private void sendPublic(Person peer) {
			Out.print("Абонент %s отправляет публичное число Y абоненту %s...", name, peer.name);
			peer.receivePublic(this);
		}

		private void receivePublic(Person peer) {
			key = peer.y.modPow(x, n);
			Out.print("%s: Получено открытое число Y от абонента %s:", name, peer.name);
			Out.print("  Y[%s] = %s (%d бит)", peer.name, peer.y, peer.y.bitLength());
			Out.print("  Вычислен ключ K = %s (%d бит)", key, key.bitLength());
		}

		@Override
		public String toString() {
			return String.format(
					"Абонент %s\n"
							+ "  Секретное значение X = %s (%d бит)\n"
							+ "  Открытое значение Y = %s (%d бит)\n",
					name, x, x.bitLength(), y, y.bitLength());
		}
	}
}
