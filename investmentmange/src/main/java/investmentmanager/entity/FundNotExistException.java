package investmentmanager.entity;

public class FundNotExistException extends Exception{
	public FundNotExistException(String msg) {
		super(msg);
	}
}
