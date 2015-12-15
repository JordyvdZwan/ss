package ss.week6;

public class ArgumentLengthsDifferException extends WrongArgumentException {
	int lengthi;
	int lengthj;
	
	public ArgumentLengthsDifferException() {
		// TODO Auto-generated constructor stub
	}
	
	public ArgumentLengthsDifferException(int i, int j) {
		lengthi = i;
		lengthj = j;
	}

	public ArgumentLengthsDifferException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ArgumentLengthsDifferException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ArgumentLengthsDifferException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ArgumentLengthsDifferException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return "This is a very bad error heur!" + lengthi + " " + lengthj;
	}
}
