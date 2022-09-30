package cryptology.substitution;

public abstract class SubstitutionСipher {
	private String alphabet;

	public SubstitutionСipher(String alphabet) {
		this.alphabet = alphabet;
	}

	protected void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public String getAlphabet() {
		return alphabet;
	}
	
	protected final void appendToAlphabet(String suffix) {
		alphabet += suffix;
	}

	protected static String getLetter(String text, int letterIdx) {
		return text.substring(letterIdx, letterIdx + 1);
	}

	protected static int idxInAlphabet(String message, String alphabet, int letterIdx) {
		return alphabet.indexOf(getLetter(message, letterIdx));
	}

	protected int idxInAlphabet(String message, int letterIdx) {
		return idxInAlphabet(message, alphabet, letterIdx);
	}

	public abstract String getCipherAlphabet();

	public abstract String cipher(String text);

	public abstract String decipher(String ciphertext);

	public enum Alphabet {
		English("abcdefghijklmnopqrstuvwxyz"),
		Ukrainian("абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"),
		Russian("абвгдеёжзийклмнопрстуфхцчшщъыьэюя");

		private String letters;

		Alphabet(String letters) {
			this.letters = letters;
		}

		public String getLetters() {
			return letters;
		}
	}
}
