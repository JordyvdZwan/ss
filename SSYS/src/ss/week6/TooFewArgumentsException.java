package ss.week6;

public class TooFewArgumentsException extends WrongArgumentException {

	public TooFewArgumentsException() {
		// TODO Auto-generated constructor stub
	}

	public TooFewArgumentsException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public TooFewArgumentsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public TooFewArgumentsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TooFewArgumentsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() {
		return "This is a very bad error heur!";
	}
}