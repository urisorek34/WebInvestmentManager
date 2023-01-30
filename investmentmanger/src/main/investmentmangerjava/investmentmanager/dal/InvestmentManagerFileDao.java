package investmentmanager.dal;

import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream.GetField;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

// import the entities
import investmentmanager.entity.Entity;
import investmentmanager.entity.InvestmentFund;
import investmentmanager.entity.Investor;
import investmentmanager.entity.Stock;

@Component
public class InvestmentManagerFileDao implements InvestmentManagerDao {

	private static String STOCK_FILE_NAME = "InvestmentsDataStock";
	private static String INVESTOR_FILE_NAME = "InvestmentsDataInvestor";
	private static String FUNDS_FILE_NAME = "InvestmentsDataFunds";

	public static final int stock = 0;
	public static final int investor = 1;
	public static final int fund = 2;
	
	public static int classId = 0;

	/*
	 * stock = 0; investor = 1; fund = 2;
	 */

	private String GiveFileNameFromId(int id) {
		String file_name = null;
		if (id == 0)
			file_name = STOCK_FILE_NAME;
		else if (id == 1)
			file_name = INVESTOR_FILE_NAME;
		else if (id == 2)
			file_name = FUNDS_FILE_NAME;

		return file_name;
	}
	@Override
	public int GetStaticId(int id) throws Exception {
		ArrayList<Entity> arrayList = readFromFile(id);
		if(arrayList == null) return 0;
		Entity maxEntity = arrayList.get(0);
		for (Entity entity : arrayList) {
			if (entity.getId() > maxEntity.getId()) {
				maxEntity = entity;
			}
		}
		
		return maxEntity.getId() + 1;
	}
	
	
	private int GiveIdFromEntity(Class c) {
		if (Stock.class == c)
			return 0;
		else if (Investor.class == c)
			return 1;
		else if (InvestmentFund.class == c)
			return 2;

		return -1;

	}

	public ArrayList<Entity> readFromFile(int id) throws Exception {
		// this func reads from the file the arraylist
		
		// there are three possible lists to pull from three possible files
		ArrayList<Entity> al = new ArrayList<Entity>();
		
		String file_name = GiveFileNameFromId(id);
		if (file_name != null) {
			File yourFile = new File(file_name);
			yourFile.createNewFile(); // if file already exists will do nothing 
			try {
				FileInputStream fis = new FileInputStream(file_name);
				ObjectInputStream ois = new ObjectInputStream(fis);
				al = (ArrayList<Entity>) ois.readObject();
				ois.close();
				fis.close();

			} catch (IOException ioe) {
				return null;
			} catch (ClassNotFoundException cnfe) {
				System.out.println("Class Not Found");
				return null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		// if succeeded now in al is the arraylist with all of the entities
		return al;
	}

	public void writeToFile(ArrayList<Entity> al) throws Exception {
		if(al.isEmpty()) {
			String file_name = GiveFileNameFromId(classId);
			FileWriter file = new FileWriter(file_name);
			file.write("");
			return;
		}
		// this func writes to the file the arraylist
		Entity e = al.get(0);
		String file_name = GiveFileNameFromId(GiveIdFromEntity(e.getClass()));
		try {
			FileOutputStream fos = new FileOutputStream(file_name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(al);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

	@Override
	public List<Entity> getAll(int id) throws Exception {
		// id = 0 - stock list
		// id = 1 - investor list
		// id = 2 - investmentFund list
		return readFromFile(id);
	}

	@Override
	public void save(Entity t) throws Exception {
		// because there are three different arraylists that needs to be saved as 1
		// each array list will have its own purpose
		// 0 - Stock
		// 1 - Investor
		// 2 - InvestmentFund
		
		// reading the correct al from the files
		ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(GiveIdFromEntity(t.getClass()));
		// adding the new entity to the al
		if (entityList == null) {
			entityList = new ArrayList<>();
		}
		entityList.add(t);
		Collections.sort(entityList);
		// writing to the file the new arrayList
		writeToFile(entityList);

	}

	@Override
	public void update(Entity t) throws Exception {

		// reading the correct al from the files
		ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(GiveIdFromEntity(t.getClass()));

		if (entityList.contains((Object) t)) {
			for (int i = 0; i < entityList.size(); i++) {
				if (entityList.get(i).getId() == t.getId()) {
					entityList.remove(i);
					entityList.add(i, t);
					break;
				}
			}
			// writing to the file the new arrayList
			writeToFile(entityList);
		} else {
			throw new EntityNotExistInSystemException("Entity does not exist --> " + t);
		}

	}

	@Override
	public void delete(int id, Class c) throws Exception {

		ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(GiveIdFromEntity(c));
		
		
		classId = GiveIdFromEntity(entityList.get(0).getClass());
		for (int i = 0; i < entityList.size(); i++) {
			if (entityList.get(i).getId() == id) {
				entityList.remove(i);
				break;
			}
		}
		// writing to the file the new arrayList
		writeToFile(entityList);
	}

	@Override
	public Entity get(int id, Class c) throws Exception {

		ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(GiveIdFromEntity(c));
		if (entityList == null) 		
			throw new EntityNotExistInSystemException("Id not exist --> " + id + " of entity --> " + c.getClass());
		for (int i = 0; i < entityList.size(); i++) {
			if (entityList.get(i).getId() == id) {
				return (Entity) entityList.get(i);

			}
		}
		// if nothing matched
		throw new EntityNotExistInSystemException("Id not exist --> " + id + " of entity --> " + c.getClass());
	}

}