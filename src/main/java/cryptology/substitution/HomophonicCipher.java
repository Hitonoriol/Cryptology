package cryptology.substitution;

import cryptology.util.HomophoneTable;

public class HomophonicCipher extends SubstitutionСipher {
	private HomophoneTable hTable;

	public HomophonicCipher(String alphabet, long seed) {
		super(alphabet);
		hTable = new HomophoneTable(alphabet, seed);
	}

	@Override
	public String getCipherAlphabet() {
		return hTable.toString();
	}

	@Override
	public String cipher(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); ++i)
			sb.append(hTable.getHomophone(getLetter(text, i)) + " ");
		return sb.toString();
	}

	@Override
	public String decipher(String ciphertext) {
		StringBuilder sb = new StringBuilder();
		String homophones[] = ciphertext.split(" ");
		for (var homophone : homophones)
			sb.append(hTable.getLetter(homophone));
		return sb.toString();
	}

}
