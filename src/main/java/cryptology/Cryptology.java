package cryptology;

import static cryptology.io.Out.printTitle;
import static cryptology.io.Out.sectionDelimiter;

import cryptology.io.Out;
import cryptology.substitution.CaesarCipher;
import cryptology.substitution.HomophonicCipher;
import cryptology.substitution.PlayfairCipher;
import cryptology.substitution.PolybiusSquareCipher;
import cryptology.substitution.SloganCipher;
import cryptology.substitution.SubstitutionСipher;
import cryptology.substitution.SubstitutionСipher.Alphabet;
import cryptology.substitution.TrithemiusCipher;
import cryptology.substitution.VigenereCipher;

public class Cryptology {
	
	public static void main(String args[]) {
		final String alphabet = Alphabet.Russian.getLetters();
		final String message = "тестовыйтекст";
		String ciphertext = "";
		
		var caesar = new CaesarCipher(alphabet, 9);
		printTitle("Шифр Цезаря");
		Out.print("Исходный алфавит: %s", caesar.getAlphabet());
		Out.print("Aлфавит со сдвигом %d: %s\n", caesar.getShift(), caesar.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = caesar.cipher(message));
		Out.print("Результат дешифрования: %s", caesar.decipher(ciphertext));
		sectionDelimiter();
		
		var slogan = new SloganCipher(alphabet, "индустрия");
		printTitle("Лозунговый шифр");
		Out.print("Исходный алфавит: %s", slogan.getAlphabet());
		Out.print("Лозунг: %s", slogan.getSlogan());
		Out.print("Aлфавит с лозугом: %s\n", slogan.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = slogan.cipher(message));
		Out.print("Результат дешифрования: %s", slogan.decipher(ciphertext));
		sectionDelimiter();
		
		var polybius = new PolybiusSquareCipher(alphabet);
		printTitle("Полибианский квадрат");
		Out.print("Исходный алфавит: %s", polybius.getAlphabet());
		Out.print("Квадрат:\n%s", polybius.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = polybius.cipher(message));
		Out.print("Результат дешифрования: %s", polybius.decipher(ciphertext));
		sectionDelimiter();
		
		var trithemius = new TrithemiusCipher(alphabet, "автомобиль");
		printTitle("Шифр Трисемуса");
		Out.print("Исходный алфавит: %s", trithemius.getAlphabet());
		Out.print("Лозунг: %s", trithemius.getSlogan());
		Out.print("Квадрат:\n%s", trithemius.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = trithemius.cipher(message));
		Out.print("Результат дешифрования: %s", trithemius.decipher(ciphertext));
		sectionDelimiter();
		
		var playfair = new PlayfairCipher(alphabet, "алфавит", "я");
		printTitle("Шифр Playfair");
		Out.print("Исходный алфавит: %s", playfair.getAlphabet());
		Out.print("Лозунг: %s", playfair.getSlogan());
		Out.print("Квадрат:\n%s", playfair.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = playfair.cipher(message));
		Out.print("Результат дешифрования: %s", playfair.decipher(ciphertext));
		sectionDelimiter();
		
		var homophones = new HomophonicCipher(alphabet, 0);
		printTitle("Система омофонов");
		Out.print("Исходный алфавит: %s", homophones.getAlphabet());
		Out.print("Омофоны:\n%s", homophones.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = homophones.cipher(message));
		Out.print("Результат дешифрования: %s", homophones.decipher(ciphertext));
		sectionDelimiter();
		
		var vigenere = new VigenereCipher(alphabet, "автомат");
		printTitle("Шифр Виженера");
		Out.print("Исходный алфавит: %s", vigenere.getAlphabet());
		Out.print("Ключ: %s", vigenere.getKey());
		Out.print("Квадрат:\n%s", vigenere.getCipherAlphabet());
		Out.print("Результат шифрования: %s", ciphertext = vigenere.cipher(message));
		Out.print("Результат дешифрования: %s", vigenere.decipher(ciphertext));
		sectionDelimiter();
	}
	
	static CipherTest cipher(String name, SubstitutionСipher cipher) {
		return new CipherTest(name, cipher);
	}
	
	static record CipherTest(String cipherName, SubstitutionСipher cipher) {}
}
