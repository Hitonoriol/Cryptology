package cryptology.substitution;

import cryptology.util.AlphabetMatrix;

public class TrithemiusCipher extends SloganCipher {
	private AlphabetMatrix cipherTable;

	public TrithemiusCipher(String alphabet, String slogan) {
		super(alphabet, slogan);
		cipherTable = new AlphabetMatrix(super.getCipherAlphabet(), 6, 6);
	}

	@Override
	public String getCipherAlphabet() {
		return cipherTable.toString();
	}

	private String trithemius(String text, boolean decipher) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); ++i) {
			String letter = getLetter(text, i);
			sb.append(cipherTable.selectInColumn(letter, decipher));
		}
		return sb.toString();
	}

	@Override
	public String cipher(String text) {
		return trithemius(text, false);
	}

	@Override
	public String decipher(String ciphertext) {
		return trithemius(ciphertext, true);
	}
}
