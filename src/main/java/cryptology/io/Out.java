package cryptology.io;

public class Out {
	
	private static int lastTitleWidth = 0;
	public static void printTitle(String title) {
		String delim = "*".repeat(Math.max(50, title.length()));
		String padding = " ".repeat((delim.length() - title.length()) / 2);
		print("%s\n%s\n%s", delim, padding + title + padding, delim);
		lastTitleWidth = delim.length();
	}
	
	public static void sectionDelimiter() {
		print("%s\n\n\n", "*".repeat(lastTitleWidth));
	}
	
	public static void write(String format, Object... args) {
		System.out.print(String.format(format, args));
	}
	
	public static void print(String text) {
		System.out.println(text);
	}
	
	public static void print(String format, Object... args) {
		write(format, args);
		System.out.println();
	}
	
	public static void print() {
		System.out.println();
	}
	
	public static String setWidth(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}
}
