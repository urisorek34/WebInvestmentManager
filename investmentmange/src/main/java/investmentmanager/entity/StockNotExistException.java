package investmentmanager.entity;

public class StockNotExistException extends Exception {
	public StockNotExistException(String msg) {
		super(msg);
	}
}
