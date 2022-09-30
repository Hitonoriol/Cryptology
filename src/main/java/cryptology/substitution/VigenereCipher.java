package cryptology.substitution;

import cryptology.util.AlphabetMatrix;
import cryptology.util.StringUtils;

public class VigenereCipher extends SubstitutionСipher {
	private AlphabetMatrix trithTable;
	private String key;

	public VigenereCipher(String alphabet, String key) {
		super(alphabet);
		createTable(alphabet);
		this.key = key;
	}

	private void createTable(String alphabet) {
		StringBuilder sb = new StringBuilder();
		int alphLen = alphabet.length();
		for (int i = 0; i < alphLen; ++i)
			sb.append(StringUtils.shift(alphabet, i));
		trithTable = new AlphabetMatrix(sb.toString(), alphLen, alphLen);
	}

	@Override
	public String getCipherAlphabet() {
		return trithTable.toString();
	}

	public String getKey() {
		return key;
	}
	
	public VigenereCipher setKey(String key) {
		this.key = key;
		return this;
	}

	@Override
	protected int idxInAlphabet(String message, int letterIdx) {
		return getAlphabet().indexOf(getLetter(message, Math.floorMod(letterIdx, message.length())));
	}

	private String vigenere(String text, boolean decipher) {
		StringBuilder sb = new StringBuilder();
		int sign = decipher ? -1 : 1;
		String alphabet = getAlphabet();
		int alphLen = alphabet.length();
		for (int i = 0, idx; i < text.length(); ++i) {
			idx = Math.floorMod(idxInAlphabet(text, i) + sign * idxInAlphabet(key, i), alphLen);
			sb.append(getLetter(alphabet, idx));
		}
		return sb.toString();
	}

	@Override
	public String cipher(String text) {
		return vigenere(text, false);
	}

	@Override
	public String decipher(String ciphertext) {
		return vigenere(ciphertext, true);
	}
}
