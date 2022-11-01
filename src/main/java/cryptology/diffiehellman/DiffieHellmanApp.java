package cryptology.diffiehellman;

import java.math.BigInteger;
import java.util.Arrays;

import consoleapp.ConsoleApplication;
import cryptology.io.Out;
import cryptology.prime.PrimalityTest;
import cryptology.prime.PrimeGenerator;
import cryptology.prime.PrimitiveRoots;

public class DiffieHellmanApp extends ConsoleApplication {
	private DiffieHellman diffieHellman = new DiffieHellman();

	public DiffieHellmanApp() {
		addCommand("gen <bits> [t=10]",
				"Cгенерировать простое число в n бит и t (если не указано, то 10) проверок простоты",
				args -> {
					var startTime = System.currentTimeMillis();
					var n = PrimeGenerator.generate(args.nextInt(), args.hasNext() ? args.nextInt() : 10, true);
					Out.print("%s (%d бит)", n, n.bitLength());
					Out.print("Потрачено %.8f секунд", (System.currentTimeMillis() - startTime) / 1000.0);
				});
		addCommand("prime_range <from> <to>",
				"Hайти все простые числа в диапазоне",
				args -> {
					var n = new BigInteger(args.nextString());
					var end = new BigInteger(args.nextString());
					
					long primes = 0;
					for (; n.compareTo(end) < 0; n = n.add(BigInteger.ONE)) {
						if (PrimalityTest.rabinMiller(n, 10)) {
							Out.write("%s ", n);
							++primes;
						}
					}
					Out.print("\n[%d чисел]", primes);
				});
		addCommand("prim_roots <n> [max = 100]",
				"Найти не больше `max` (если не указано, то 100) первообразных корней числа n",
				args -> {
					var startTime = System.currentTimeMillis();
					var roots = Arrays.toString(PrimitiveRoots.compute(new BigInteger(args.nextString()),
							args.hasNext() ? args.nextInt() : 100).toArray());
					Out.print("%s\nПотрачено %.8f секунд", roots, (System.currentTimeMillis() - startTime) / 1000.0);
				});
		addCommand("dh_sim <N> [t=10]",
				"Cимулировать обмен ключами по схеме Диффи-Хеллмана с длиной числа n в N бит",
				args -> {
					var startTime = System.currentTimeMillis();
					diffieHellman.setN(PrimeGenerator.generate(args.nextInt(), args.hasNext() ? args.nextInt() : 10, true));
					Out.print();
					diffieHellman.simulate();
					Out.print("Потрачено %.8f секунд", (System.currentTimeMillis() - startTime) / 1000.0);
				});
		addCommand("dh_set <n>",
				"Cимулировать обмен ключами по схеме Диффи-Хеллмана с заданным числом n",
				args -> {
					var startTime = System.currentTimeMillis();
					diffieHellman.setN(new BigInteger(args.nextString()));
					diffieHellman.simulate();
					Out.print("Потрачено %.8f секунд", (System.currentTimeMillis() - startTime) / 1000.0);
				});
		setHelpMenuMsg("Список команд:");
		setAfterInput(System.lineSeparator());
		setAfterOutput(System.lineSeparator());
		printHelpMenu();
	}

	@Override
	public void printHelpMenu() {
		super.printHelpMenu();
		Out.print();
	}
}
