package cryptology.util;

public class Bigram {
	private String first;
	private String second;

	public Bigram(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public String first() {
		return first;
	}
	
	public void set(String first, String second) {
		setFirst(first);
		setSecond(second);
	}
	
	public void setFirst(String value) {
		first = value;
	}
	
	public String second() {
		return second;
	}
	
	public void setSecond(String value) {
		second = value;
	}
	
	public boolean isSame() {
		return first.equals(second);
	}
	
	public String join() {
		return first + second;
	}
	
	@Override
	public String toString() {
		return join();
	}
}
