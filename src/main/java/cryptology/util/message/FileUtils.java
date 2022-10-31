package cryptology.util.message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
	public static Message read(String filePath, Charset charset) {
		try {
			return new Message(Files.readAllBytes(Paths.get(filePath)), charset);
		} catch (IOException e) {
			e.printStackTrace();
			return new Message();
		}
	}

	public static Message read(String filePath) {
		return read(filePath, StandardCharsets.UTF_8);
	}

	public static void write(String filePath, Message contents) {
		try (var outputStream = new FileOutputStream(filePath)) {
			outputStream.write(contents.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
