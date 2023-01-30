package investmentmanager.service;

import java.util.List;

import investmentmanager.entity.Entity;

public interface Manageable {
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

}
