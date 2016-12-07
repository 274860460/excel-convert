package quiet.util;

public class PiaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PiaException(){
		
	}
	
	public PiaException(String message){
		super(message);
	}
}
