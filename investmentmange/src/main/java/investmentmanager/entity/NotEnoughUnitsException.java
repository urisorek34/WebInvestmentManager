package investmentmanager.entity;

public class NotEnoughUnitsException extends Exception{
	public NotEnoughUnitsException(String msg) {
		super(msg);
	}
}
