package investmentmanager.dal;

public class EntityNotExistInSystemException extends Exception {

	public EntityNotExistInSystemException(String msg) {
		super(msg);
	}
}
