package RangerSlider.core;

public class RSException extends Exception {

	// ATRIBUTES
	private static final long serialVersionUID = 1L;
	
	// Informations regarding the error that occured
	private String message;

	
	// SETTER AND GETTER
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// CONSTRUCTORS
	public RSException() {
		super();
	}
	
	public RSException(String s) {
		this();
		this.message = s;
	}
}
