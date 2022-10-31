package cryptology.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.mutable.MutableInt;

import consoleapp.ConsoleApplication;
import cryptology.io.Out;
import cryptology.util.message.FileUtils;
import cryptology.util.message.Message;

public abstract class CryptologyApp extends ConsoleApplication {
	protected static final String ROOT_DIR = "data";
	protected static final String DEFAULT_MSG = "msg", DEFAULT_KEY = "key";

	private Charset encoding = StandardCharsets.UTF_8;
	private Message msg = new Message(encoding);

	public CryptologyApp() {
		/* Message / key manipulation commands */
		addCommand("msg [bin | hex]", "Вывести текущее сообщение", args -> {
			showText(msg, args.isPresent() ? args.nextString() : null);
		});
		addCommand("msg_set <text>", "Задать сообщение как `text`", args -> setMessage(args.asString()));
		addCommand("msg_load <path>", "Загрузить сообщение из `path`", args -> {
			msg.set(load(args.asString(), FileType.text));
		});
		addCommand("msg_save [file]",
				"Сохранить текущее сообщение в `file`. Если `file` не указан, будет использовано текущее время.",
				args -> save(args.isPresent() ? args.asString() : null, FileType.text, msg));

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
	}

	protected Message load(String filePath, FileType type) {
		var path = pathTo(filePath, type);
		var text = FileUtils.read(path, encoding);
		Out.print("%s-файл загружен из `%s`.", type, path);
		return text;
	}

	protected void save(String filePath, FileType type, Message text) {
		if (filePath == null)
			filePath = System.currentTimeMillis() + "";
		filePath = pathTo(filePath, type);
		FileUtils.write(filePath, text);
		Out.print("%s-файл сохранен в `%s`.", type, filePath);
	}

	protected void showText(Message text, String showAs) {
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
	}

	protected Message getMessage() {
		return msg;
	}

	protected void setMessage(String msg) {
		this.msg.set(msg);
	}

	protected Charset getEncoding() {
		return encoding;
	}

	protected void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}

	private void listFiles() {
		var root = Paths.get(ROOT_DIR);
		for (var type : FileType.values()) {
			Out.print("Все %s файлы:", type.extension());
			list(root, type);
		}
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

	@Override
	public void printHelpMenu() {
		super.printHelpMenu();
		Out.print();
	}
}
