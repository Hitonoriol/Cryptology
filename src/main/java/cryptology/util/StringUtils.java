package cryptology.util;

public class StringUtils {
	public static String shift(String str, int shiftBy) {
		if (shiftBy == 0)
			return str;

		String out = "";
		int srcLength = str.length();
		for (int i = 0, newLetterPos; i < srcLength; ++i) {
			newLetterPos = Math.floorMod(i + shiftBy, srcLength);
			out += str.substring(newLetterPos, newLetterPos + 1);
		}
		return out;
	}
}
