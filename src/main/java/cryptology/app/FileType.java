package cryptology.app;

public enum FileType {
	text(".txt"),
	key(".key");

	private final String fileExt;

	private FileType(String fileExt) {
		this.fileExt = fileExt;
	}

	public String extension() {
		return fileExt;
	}
}