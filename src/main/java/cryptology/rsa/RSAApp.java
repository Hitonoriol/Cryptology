package cryptology.rsa;

import java.math.BigInteger;

import cryptology.app.CryptologyApp;
import cryptology.app.FileType;
import cryptology.io.Out;
import cryptology.util.message.Message;

public class RSAApp extends CryptologyApp {
	private RSA rsa = new RSA();

	public RSAApp() {
		addCommand("genkeys <bits>",
				"Сгенерировать публичный и приватный ключи указанной размерности",
				args -> rsa.generateKeys(args.nextInt()));

		addCommand("encrypt",
				"Зашифровать текущее сообщение текущим публичным ключом",
				() -> {
					Out.print("Шифруем сообщение %d-битным публичным ключом...", rsa.getKeys().getSize());
					rsa.encrypt(getMessage());
					Out.print("Сообщение зашифровано!");
				});
		addCommand("decrypt",
				"Расшифровать текущее сообщение текущим приватным ключом",
				() -> {
					Out.print("Расшифровываем сообщение %d-битным приватным ключом...", rsa.getKeys().getSize());
					rsa.decrypt(getMessage());
					Out.print("Сообщение расшифровано!");
				});

		addCommand("keys_save <path>",
				"Сохранить публичный и приватный ключи в файлы <path>.pub и <path>.prv",
				args -> saveKeys(args.isPresent() ? args.asString() : null));
		addCommand("keys_load <path>",
				"Загрузить публичный и приватный ключи из файлов <path>.pub и <path>.prv",
				args -> loadKeys(args.isPresent() ? args.asString() : null));
		
		addCommand("show_pub", "Показать публичный ключ", () -> rsa.getKeys().printPublic());
		addCommand("show_prv", "Показать приватный ключ", () -> rsa.getKeys().printPrivate());
		addCommand("show_keys", "Показать публичный и приватный ключи", () -> rsa.getKeys().printKeys());

		printHelpMenu();
	}

	private void saveKeys(String fileName) {
		var prv = new Message(rsa.getKeys().getD() + " " + rsa.getKeys().getN());
		var pub = new Message(rsa.getKeys().getE() + " " + rsa.getKeys().getN());
		save(fileName, FileType.privateKey, prv);
		save(fileName, FileType.publicKey, pub);
	}

	private void loadKeys(String fileName) {
		var prv = load(fileName, FileType.privateKey).toString().split(" ");
		var pub = load(fileName, FileType.publicKey).toString().split(" ");
		var keys = rsa.getKeys();
		keys.setPrivate(new BigInteger(prv[0]), new BigInteger(prv[1]));
		keys.setPublic(new BigInteger(pub[0]), new BigInteger(pub[1]));

	}
}
