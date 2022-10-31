package cryptology.des;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HexFormat;

import cryptology.app.CryptologyApp;
import cryptology.app.FileType;
import cryptology.io.Out;
import cryptology.util.message.Message;

public class DESApplication extends CryptologyApp {
	private DES des = new DES();
	private Message key = new Message(getEncoding());

	public DESApplication() {
		/* Key manipulation commands */
		addCommand("key [bin | hex]", "Вывести текущий ключ", args -> {
			showText(key, args.isPresent() ? args.nextString() : null);
		});
		addCommand("key_set <key>", "Задать ключ как `key`", args -> key.set(args.asString()));
		addCommand("key_save [file]",
				"Сохранить текущий ключ в `file`. Если `file` не указан, будет использовано текущее время.",
				args -> save(args.isPresent() ? args.asString() : null, FileType.key, key));
		addCommand("key_load <path>", "Загрузить ключ из `path`", args -> {
			key.set(load(args.asString(), FileType.key));
		});

		/* Encryption / decryption commands */
		addCommand("encrypt", "Зашифровать текущее сообщение текущим ключом", () -> {
			des.encrypt(key.toString(), getMessage());
		});
		addCommand("decrypt", "Расшифровать текущее сообщение текущим ключом", () -> {
			des.decrypt(key.toString(), getMessage());
		});

		printHelpMenu();

		if (new File(pathTo(DEFAULT_KEY, FileType.key)).exists())
			key = load(DEFAULT_KEY, FileType.key);
	}

	@Override
	protected void showText(Message text, String showAs) {
		super.showText(text, showAs);
		if (text == key && showAs != null) {
			long hash = DES.hash(key.toString());
			String str = "";
			if (showAs.equals("bin")) {
				str = Long.toBinaryString(hash);
				while (str.length() < 64)
					str = "0" + str;
			} else if (showAs.equals("hex")) {
				var format = HexFormat.ofDelimiter(" ");
				ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
				buffer.putLong(hash);
				str = format.formatHex(buffer.array());
			}
			Out.print("\n64-битный хэш ключа: %s", str);
		}
	}
}
