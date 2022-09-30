package cryptology.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.random.RandomDataGenerator;

public class HomophoneTable {
	private RandomDataGenerator rng = new RandomDataGenerator();
	private Map<String, List<String>> homophones = new LinkedHashMap<>();

	public HomophoneTable(String alphabet, long seed) {
		rng.reSeed(seed);
		populate(alphabet, 2);
	}

	private String stringify(int n) {
		String str = Integer.toString(n);
		return str.length() >= 3 ? str : "0".repeat(3 - str.length()) + str;
	}

	private void populate(String alphabet, int homophoneNum) {
		var usedNums = new HashSet<String>();
		for (int i = 0; i < alphabet.length(); ++i) {
			var set = new ArrayList<String>();
			homophones.put(alphabet.substring(i, i + 1), set);
			for (int n = 0; n < homophoneNum;) {
				String num = stringify(rng.nextInt(1, 999));
				if (usedNums.add(num)) {
					set.add(num);
					++n;
				}
			}
		}
	}

	public String getHomophone(String letter) {
		var list = homophones.get(letter);
		return list.get(rng.nextInt(0, list.size() - 1));
	}
	
	public String getLetter(String homophone) {
		for (var entry : homophones.entrySet()) {
			if (entry.getValue().contains(homophone))
				return entry.getKey();
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		homophones.forEach((letter, hList) -> {
			sb.append(letter + ": ");
			sb.append(Arrays.toString(hList.toArray()));
			sb.append("\n");
		});
		return sb.toString();
	}
}
