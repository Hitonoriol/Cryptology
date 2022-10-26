package cryptology.util.text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
	public static Text read(String filePath, Charset charset) {
		try {
			return new Text(Files.readAllBytes(Paths.get(filePath)), charset);
		} catch (IOException e) {
			e.printStackTrace();
			return new Text();
		}
	}

	public static Text read(String filePath) {
		return read(filePath, StandardCharsets.UTF_8);
	}

	public static void write(String filePath, Text contents) {
		try (var outputStream = new FileOutputStream(filePath)) {
			outputStream.write(contents.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
