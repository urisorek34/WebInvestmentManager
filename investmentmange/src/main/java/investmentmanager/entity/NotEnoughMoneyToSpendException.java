package investmentmanager.entity;

public class NotEnoughMoneyToSpendException extends Exception{
	
	public NotEnoughMoneyToSpendException(String msg) {
		super(msg);
	}
}
