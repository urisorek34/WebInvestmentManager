package investmentmanager.dal;
import investmentmanager.entity.Entity;
import java.util.List;

import org.springframework.stereotype.Component;

public interface InvestmentManagerDao {
	//al - ArrayList
	
	//returns all of the al's in the file
	public List<Entity> getAll(int id) throws Exception;
	
	//saves an al to the file
	public void save(Entity t) throws Exception;
	
	//updates an item in the al in the file
	public void update(Entity t) throws Exception;
	
	//Deletes an item in an al in the file
	public void delete(int id, Class c) throws Exception;
	
	//returns an item from a certain al in the file
	public Entity get(int idm, Class c) throws Exception;


	public int GetStaticId(int id) throws Exception;
	
}