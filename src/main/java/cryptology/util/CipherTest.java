package cryptology.util;

import static cryptology.io.Out.printTitle;
import static cryptology.io.Out.sectionDelimiter;
import static java.lang.System.lineSeparator;

import java.util.function.Consumer;

import cryptology.io.Out;
import cryptology.substitution.SubstitutionСipher;

public class CipherTest<T extends SubstitutionСipher> {
	private String cipherName;
	private T cipher;
	private Consumer<T> optionalAction;
	private String testMessage;

	public CipherTest(String cipherName, T cipher, Consumer<T> optionalAction) {
		this.cipherName = cipherName;
		this.cipher = cipher;
		this.optionalAction = optionalAction;
	}

	public CipherTest<T> setTestMessage(String message) {
		testMessage = message;
		return this;
	}

	public void run() {
		printTitle(cipherName);
		Out.print("Исходный алфавит: %s", cipher.getAlphabet());
		String cipherAlphabet = cipher.getCipherAlphabet();
		Out.print("Алфавит замены: %s%s",
				cipherAlphabet.contains("\n") ? lineSeparator() : "", cipherAlphabet);

		if (optionalAction != null)
			optionalAction.accept(cipher);
		Out.print();

		String ciphertext = cipher.cipher(testMessage);
		Out.print("Результат шифрования: %s", ciphertext);
		Out.print("Результат дешифрования: %s", cipher.decipher(ciphertext));
		sectionDelimiter();
	}

	public static <T extends SubstitutionСipher> CipherTest<?> cipher(String name, T cipher,
			Consumer<T> optionalAction) {
		return new CipherTest<>(name, cipher, optionalAction);
	}

	public static <T extends SubstitutionСipher> CipherTest<?> cipher(String name, T cipher) {
		return cipher(name, cipher, null);
	}
}
