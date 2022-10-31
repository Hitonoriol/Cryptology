package cryptology.util.message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * Text abstraction that treats a byte array as its contents and a String
 * instance
 * as a representation of bytes in certain encoding.
 * The string is kept in sync with the byte array, not the other way around.
 */
public class Message {
	private static final HexFormat hexFormat = HexFormat.ofDelimiter(" ");

	private Charset encoding = StandardCharsets.UTF_8;
	private byte[] bytes = {};
	private String stringRepresentation = "";

	public Message() {}

	public Message(Charset encoding) {
		this.encoding = encoding;
	}

	public Message(byte[] bytes) {
		set(bytes);
	}

	public Message(byte[] bytes, Charset encoding) {
		this.encoding = encoding;
		set(bytes);
	}

	public Message(String string) {
		set(string);
	}

	public Message(String string, Charset encoding) {
		this.encoding = encoding;
		set(string);
	}

	public void set(Message rhs) {
		encoding = rhs.encoding;
		bytes = rhs.bytes;
		stringRepresentation = rhs.stringRepresentation;
	}

	public void set(byte[] bytes) {
		this.bytes = bytes;
		bytesChanged();
	}

	public void set(String string) {
		set(string.getBytes(encoding));
	}

	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
		bytesChanged();
	}

	public Message trim() {
		int l = 0;
		while (bytes[l] == 0x0)
			++l;

		int r = bytes.length - 1;
		while (bytes[r] == 0x0)
			--r;

		if (l != 0 || r != bytes.length - 1)
			set(Arrays.copyOfRange(bytes, l, r + 1));
		return this;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int byteCount() {
		return bytes.length;
	}

	public int characterCount() {
		return stringRepresentation.length();
	}

	private void bytesChanged() {
		stringRepresentation = new String(bytes, encoding);
	}

	public String toHexString() {
		return hexFormat.formatHex(bytes);
	}

	public String toBinString() {
		var sb = new StringBuilder();
		for (var b : bytes) {
			sb.append(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1)).append(' ');
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}
}
