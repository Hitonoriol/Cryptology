package cryptology;

import static cryptology.util.CipherTest.cipher;

import java.util.Arrays;

import cryptology.des.DESApplication;
import cryptology.io.Out;
import cryptology.rsa.RSAApp;
import cryptology.substitution.CaesarCipher;
import cryptology.substitution.HomophonicCipher;
import cryptology.substitution.PlayfairCipher;
import cryptology.substitution.PolybiusSquareCipher;
import cryptology.substitution.SloganCipher;
import cryptology.substitution.SubstitutionСipher.Alphabet;
import cryptology.substitution.TrithemiusCipher;
import cryptology.substitution.VigenereCipher;

public class Demo {
	public static void RSA() {
		var rsa = new RSAApp();
		rsa.listenForCommands();
	}
	
	public static void DES() {
		var des = new DESApplication();
		des.listenForCommands();
	}

	public static void substitutionCiphers() {
		final String alphabet = Alphabet.Russian.getLettersAndPunctuators();
		Arrays.asList(
				cipher("Шифр Цезаря", new CaesarCipher(alphabet, 9), caesar -> {
					Out.print("Сдвиг: %d", caesar.getShift());
				}),

				cipher("Лозунговый шифр", new SloganCipher(alphabet, "индустрия"), slogan -> {
					Out.print("Лозунг: %s", slogan.getSlogan());
				}),

				cipher("Полибианский квадрат", new PolybiusSquareCipher(alphabet)),

				cipher("Шифр Трисемуса", new TrithemiusCipher(alphabet, "автомобиль"), trithemius -> {
					Out.print("Лозунг: %s", trithemius.getSlogan());
				}),

				cipher("Шифр Playfair", new PlayfairCipher(alphabet, "алфавит", "я"), playfair -> {
					Out.print("Лозунг: %s", playfair.getSlogan());
				}),

				cipher("Система омофонов", new HomophonicCipher(alphabet, 0)),

				cipher("Шифр Виженера", new VigenereCipher(alphabet, "автомат"), vigenere -> {
					Out.print("Ключ: %s", vigenere.getKey());
				})
		).forEach(test -> test.setTestMessage("тестовое секретное сообщение.").run());
	}
}
