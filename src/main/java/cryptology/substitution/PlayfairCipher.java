package cryptology.substitution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cryptology.io.Out;
import cryptology.util.AlphabetMatrix;
import cryptology.util.AlphabetMatrix.Cell;
import cryptology.util.Bigram;

public class PlayfairCipher extends SloganCipher {
	private AlphabetMatrix cipherTable;
	private String placeholderLetter;

	public PlayfairCipher(String alphabet, String slogan, String placeholderLetter) {
		super(alphabet, slogan);
		this.placeholderLetter = placeholderLetter;
		cipherTable = new AlphabetMatrix(super.getCipherAlphabet(), 6, 6);
		if (cipherTable.getSize() < cipherTable.getWidth() * cipherTable.getHeight()) {
			String newAlphabet = super.getCipherAlphabet();
			for (int i = 0; i < Math.min(cipherTable.getSize(), 10); ++i) {
				newAlphabet += i;
			}
			setCipherAlphabet(newAlphabet);
			cipherTable.setAlphabet(newAlphabet);
		}
	}

	@Override
	public String getCipherAlphabet() {
		return cipherTable.toString();
	}

	private List<Bigram> createBigrams(String text) {
		List<Bigram> bigrams = new ArrayList<>();
		int length = text.length();
		for (int i = 0; i < length; i += 2) {
			boolean hasNext = i + 1 < length;
			Bigram bigram = new Bigram(getLetter(text, i), hasNext ? getLetter(text, i + 1) : placeholderLetter);

			if (bigram.isSame())
				bigram.setSecond(placeholderLetter);
			bigrams.add(bigram);
		}
		Out.print(Arrays.toString(bigrams.toArray()));
		return bigrams;
	}

	private Cell getOppositeVertex(Cell firstCell, Cell secondCell) {
		int delta = firstCell.j() - secondCell.j();
		return new Cell(firstCell.i(), firstCell.j() - delta);
	}

	public String playfair(String text, boolean decipher) {
		var bigrams = createBigrams(text);
		for (Bigram bigram : bigrams) {
			Cell firstCell = cipherTable.getCell(bigram.first());
			Cell secondCell = cipherTable.getCell(bigram.second());
			boolean inSameRow = firstCell.i() == secondCell.i();
			boolean inSameColumn = firstCell.j() == secondCell.j();

			/*
			 * 1. Если символы биграммы исходного текста встречаются в одной строке, то эти
			 * символы замещаются на символы, расположенные в ближайших столбцах справа от
			 * соответствующих символов. Если символ является последним в строке, то он
			 * заменяется на первый символ этой же строки.
			 */
			if (inSameRow) {
				bigram.setFirst(cipherTable.selectInRow(bigram.first(), decipher));
				bigram.setSecond(cipherTable.selectInRow(bigram.second(), decipher));
			}

			/*
			 * 2. Если символы биграммы исходного текста встречаются в одном столбце, то они
			 * преобразуются в символы того же столбца, находящимися непосредственно под
			 * ними. Если символ является нижним в столбце, то он заменяется на первый
			 * символ этого же столбца.
			 */
			else if (inSameColumn) {
				bigram.setFirst(cipherTable.selectInColumn(bigram.first(), decipher));
				bigram.setSecond(cipherTable.selectInColumn(bigram.second(), decipher));
			}

			/*
			 * 3. Если символы биграммы исходного текста находятся в разных столбцах и
			 * разных строках, то они заменяются на символы, находящиеся в тех же строках,
			 * но соответствующие другим углам прямоугольника.
			 */
			else if (!inSameRow && !inSameColumn) {
				Cell oppositeFirst = getOppositeVertex(firstCell, secondCell);
				Cell oppositeSecond = getOppositeVertex(secondCell, firstCell);
				bigram.set(cipherTable.getLetter(oppositeFirst), cipherTable.getLetter(oppositeSecond));
			}
		}
		return bigrams.stream().map(Bigram::toString).collect(Collectors.joining());
	}

	@Override
	public String cipher(String text) {
		return playfair(text, false);
	}

	@Override
	public String decipher(String text) {
		return playfair(text, true);
	}
}
