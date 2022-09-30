package cryptology.substitution;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SloganCipher extends SubstitutionСipher {
	private String slogan;
	private String cipherAlphabet;

	public SloganCipher(String alphabet, String slogan) {
		super(alphabet);
		setSlogan(slogan);
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
		Set<String> letterSet = new LinkedHashSet<>();
		populate(letterSet, slogan);
		populate(letterSet, getAlphabet());
		cipherAlphabet = letterSet.stream().collect(Collectors.joining());
	}
	
	protected final void setCipherAlphabet(String cipherAlphabet) {
		this.cipherAlphabet = cipherAlphabet;
	}

	public String getSlogan() {
		return slogan;
	}

	private void populate(Set<String> letterSet, String letters) {
		for (int i = 0; i < letters.length(); ++i)
			letterSet.add(letters.substring(i, i + 1));
	}

	@Override
	public String getCipherAlphabet() {
		return cipherAlphabet;
	}

	private String slogan(String message, boolean decipher) {
		StringBuilder sb = new StringBuilder();
		String srcAlphabet = decipher ? this.cipherAlphabet : getAlphabet();
		String cipherAlphabet = decipher ? getAlphabet() : this.cipherAlphabet;
		for (int i = 0; i < message.length(); ++i) {
			sb.append(getLetter(cipherAlphabet, idxInAlphabet(message, srcAlphabet, i)));
		}
		return sb.toString();
	}

	@Override
	public String cipher(String text) {
		return slogan(text, false);
	}

	@Override
	public String decipher(String ciphertext) {
		return slogan(ciphertext, true);
	}
}
