package cryptology.substitution;

import cryptology.util.StringUtils;

public class CaesarCipher extends SubstitutionСipher {
	private String cipherAlphabet;
	private int shift;

	public CaesarCipher(String alphabet, int shift) {
		super(alphabet);
		setShift(shift);
	}

	public void setShift(int shift) {
		this.shift = shift;
		cipherAlphabet = StringUtils.shift(getAlphabet(), shift);
	}
	
	public int getShift() {
		return shift;
	}

	@Override
	public String getCipherAlphabet() {
		return cipherAlphabet;
	}

	private String caesar(String message, boolean decipher) {
		StringBuilder sb = new StringBuilder();
		String srcAlphabet = getAlphabet();
		int srcAlphabetLength = srcAlphabet.length();
		for (int i = 0, srcIdx, shiftedIdx; i < message.length(); ++i) {
			srcIdx = srcAlphabet.indexOf(message.substring(i, i + 1));
			shiftedIdx = Math.floorMod(srcIdx + (decipher ? -shift : shift), srcAlphabetLength);
			sb.append(srcAlphabet.substring(shiftedIdx, shiftedIdx + 1));
		}
		return sb.toString();
	}

	@Override
	public String cipher(String message) {
		return caesar(message, false);
	}

	@Override
	public String decipher(String message) {
		return caesar(message, true);
	}
}
