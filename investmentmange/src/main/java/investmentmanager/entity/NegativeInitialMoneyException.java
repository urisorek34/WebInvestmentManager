package investmentmanager.entity;

public class NegativeInitialMoneyException extends Exception{

	public NegativeInitialMoneyException(String msg) {
		super(msg);
	}
}
