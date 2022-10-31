package cryptology.app;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import org.apache.commons.lang3.mutable.MutableInt;

import consoleapp.ConsoleApplication;
import cryptology.des.DES;
import cryptology.io.Out;
import cryptology.util.message.FileUtils;
import cryptology.util.message.Message;

public abstract class CryptologyApp extends ConsoleApplication {
	private static final String ROOT_DIR = "data";
	private static final String DEFAULT_MSG = "msg", DEFAULT_KEY = "key";
	private Charset encoding = StandardCharsets.UTF_8;
	private Message msg = new Message(encoding), key = new Message(encoding);

	public CryptologyApp() {
		/* Message / key manipulation commands */
		addCommand("msg [bin | hex]", "Вывести текущее сообщение", args -> {
			showText(msg, args.isPresent() ? args.nextString() : null);
		});
		addCommand("key [bin | hex]", "Вывести текущий ключ", args -> {
			showText(key, args.isPresent() ? args.nextString() : null);
		});
		addCommand("msg_set <text>", "Задать сообщение как `text`", args -> setMessage(args.asString()));
		addCommand("key_set <key>", "Задать ключ как `key`", args -> setKey(args.asString()));
		addCommand("msg_load <path>", "Загрузить сообщение из `path`", args -> {
			msg.set(load(args.asString(), FileType.text));
		});
		addCommand("key_load <path>", "Загрузить ключ из `path`", args -> {
			key.set(load(args.asString(), FileType.key));
		});
		addCommand("msg_save [file]",
				"Сохранить текущее сообщение в `file`. Если `file` не указан, будет использовано текущее время.",
				args -> save(args.isPresent() ? args.asString() : null, FileType.text, msg));
		addCommand("key_save [file]",
				"Сохранить текущий ключ в `file`. Если `file` не указан, будет использовано текущее время.",
				args -> save(args.isPresent() ? args.asString() : null, FileType.key, key));

		/* Encoding manipulation commands */
		addCommand("encoding", "Вывести имя текущей кодировки", () -> Out.print(encoding.displayName()));
		addCommand("encoding_set <name>", "Задать кодировку по имени `name`", args -> {
			setEncoding(Charset.forName(args.asString()));
		});

		addCommand("ls", "Вывести все файлы сообщений и ключей", this::listFiles);
		setHelpMenuMsg("Список команд:");
		setAfterInput(System.lineSeparator());
		setAfterOutput(System.lineSeparator());

		new File(ROOT_DIR).mkdirs();
		if (new File(pathTo(DEFAULT_MSG, FileType.text)).exists())
			msg = load(DEFAULT_MSG, FileType.text);
		if (new File(pathTo(DEFAULT_KEY, FileType.key)).exists())
			key = load(DEFAULT_KEY, FileType.key);
	}

	protected Message load(String filePath, FileType type) {
		var path = pathTo(filePath, type);
		var text = FileUtils.read(path, encoding);
		Out.print("Файл с %s загружен из `%s`.",
				type == FileType.key ? "ключом" : "сообщением", path);
		return text;
	}

	protected void save(String filePath, FileType type, Message text) {
		if (filePath == null)
			filePath = System.currentTimeMillis() + "";
		filePath = pathTo(filePath, type);
		FileUtils.write(filePath, text);
		Out.print("Файл с %s сохранен в `%s`.",
				type == FileType.key ? "ключом" : "сообщением", filePath);
	}

	private void showText(Message text, String showAs) {
		if (showAs == null)
			Out.print(text.toString());
		else {
			switch (showAs) {
			case "hex":
				Out.print(text.toHexString());
				break;
			case "bin":
				Out.print(text.toBinString());
				break;
			default:
				break;
			}
		}
		Out.print("[%d символов, %d байт]", text.characterCount(), text.byteCount());
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

	protected Message getMessage() {
		return msg;
	}

	protected void setMessage(String msg) {
		this.msg.set(msg);
	}

	protected Message getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key.set(key);
	}

	private void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	private void listFiles() {
		var root = Paths.get(ROOT_DIR);
		Out.print("Все ключи:");
		list(root, FileType.key);
		Out.print("\nВсе сообщения:");
		list(root, FileType.text);
	}

	private void list(Path root, FileType type) {
		try {
			MutableInt i = new MutableInt(0);
			Files.walk(root)
					.map(Path::toString)
					.filter(path -> path.endsWith(type.extension()))
					.forEach(path -> {
						Out.write(path + "    ");
						if (i.incrementAndGet() % 2 == 0)
							Out.print();
					});
			Out.print();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String pathTo(String file, FileType type) {
		return ROOT_DIR + File.separator + file + type.extension();
	}
}
