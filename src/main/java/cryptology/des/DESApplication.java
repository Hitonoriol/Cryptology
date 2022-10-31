package cryptology.des;

import cryptology.app.CryptologyApp;
import cryptology.io.Out;

public class DESApplication extends CryptologyApp {
	private DES des = new DES();

	public DESApplication() {
		/* Encryption / decryption commands */
		addCommand("encrypt", "Зашифровать текущее сообщение текущим ключом", () -> {
			des.encrypt(getKey().toString(), getMessage());
		});
		addCommand("decrypt", "Расшифровать текущее сообщение текущим ключом", () -> {
			des.decrypt(getKey().toString(), getMessage());
		});

		printHelpMenu();
		Out.print();
	}

}
