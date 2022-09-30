package cryptology.util;

import cryptology.io.Out;

public class AlphabetMatrix {
	private int width, height;
	private String alphabet;

	/*
	 * May contain null cells. This doesn't break anything as long as there are
	 * non-null elements in every row & column.
	 */
	public AlphabetMatrix(String alphabet, int width, int height) {
		this.width = width;
		this.height = height;
		this.alphabet = alphabet;
	}
	
	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	private int getIdx(final int i, final int j) {
		return j + i * width;
	}

	private int getJ(final int idx) {
		return idx % width;
	}

	private int getI(final int idx) {
		return idx / width;
	}

	public int getJ(String letter) {
		return getJ(alphabet.indexOf(letter));
	}

	public int getI(String letter) {
		return getI(alphabet.indexOf(letter));
	}

	public String getLetter(final int i, final int j) {
		final int idx = getIdx(i, j);
		if (idx < getSize())
			return alphabet.substring(idx, idx + 1);
		else
			return null;
	}
	
	public String getLetter(Cell cell) {
		return getLetter(cell.i, cell.j);
	}
	
	public Cell getCell(String letter) {
		return new Cell(getI(letter), getJ(letter));
	}

	public String selectInRow(String letter, boolean toTheLeft) {
		Cell cell = getCell(letter);
		int direction = toTheLeft ? -1 : 1;
		int lj = Math.floorMod(cell.j() + direction, getWidth());
		String newLetter = getLetter(cell.i, lj);
		if (newLetter == null) {
			if (!toTheLeft)
				return getLetter(cell.i, 0);
			else {
				int j = lj - 1;
				while ((newLetter = getLetter(cell.i, lj)) == null) {
					j = Math.floorMod(j - 1, width);
				}
				return getLetter(cell.i, j);
			}
		}
		return newLetter;
	}
	
	public String selectToTheLeft(String letter) {
		return selectInRow(letter, true);
	}
	
	public String selectToTheRight(String letter) {
		return selectInRow(letter, false);
	}

	public String selectInColumn(String letter, boolean above) {
		Cell cell = getCell(letter);
		int direction = above ? -1 : 1;
		int li = Math.floorMod(cell.i + direction, getHeight());
		String newLetter = getLetter(li, cell.j);
		return newLetter != null ? newLetter : getLetter(above ? (getHeight() - 2) : 0, cell.j);
	}

	public String selectAbove(String letter) {
		return selectInColumn(letter, true);
	}

	public String selectBelow(String letter) {
		return selectInColumn(letter, false);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getLowestI(int j) {
		int i = getHeight() - 1;
		while (i > 0) {
			if (!hasCell(i, j))
				break;
		}
		return i;
	}

	public boolean hasCell(final int i, final int j) {
		return getLetter(i, j) != null;
	}

	public int getSize() {
		return alphabet.length();
	}

	@Override
	public String toString() {
		int width = 3;
		StringBuilder sb = new StringBuilder();
		sb.append(Out.setWidth("", width));
		for (int i = 0; i < this.width; ++i)
			sb.append(Out.setWidth((i + 1) + "", width));
		sb.append("\n");

		for (int i = 0; i < height; ++i) {
			sb.append(Out.setWidth((i + 1) + " ", width));
			for (int j = 0; j < this.width; ++j) {
				String letter = getLetter(i, j);
				sb.append(Out.setWidth((letter != null ? letter : "-") + "", width));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static record Cell(int i, int j) {
	}
}
