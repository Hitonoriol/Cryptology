package cryptology.substitution;

import cryptology.util.AlphabetMatrix;

public class PolybiusSquareCipher extends SubstitutionСipher {
	private AlphabetMatrix cipherSquare;

	public PolybiusSquareCipher(String alphabet) {
		super(alphabet);
		cipherSquare = new AlphabetMatrix(alphabet, 6, 6);
	}

	@Override
	public String getCipherAlphabet() {
		return cipherSquare.toString();
	}

	public AlphabetMatrix getCipherSquare() {
		return cipherSquare;
	}

	@Override
	public String cipher(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); ++i) {
			String letter = getLetter(text, i);
			sb.append(cipherSquare.getI(letter));
			sb.append(cipherSquare.getJ(letter));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	@Override
	public String decipher(String ciphertext) {
		StringBuilder sb = new StringBuilder();
		String bigrams[] = ciphertext.split(" ");
		for (String bigram : bigrams) {
			int i = Integer.parseInt(getLetter(bigram, 0));
			int j = Integer.parseInt(getLetter(bigram, 1));
			sb.append(cipherSquare.getLetter(i, j));
		}
		return sb.toString();
	}
}
