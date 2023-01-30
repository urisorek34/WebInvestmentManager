package investmentmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import investmentmanager.dal.InvestmentManagerDao;
import investmentmanager.entity.Entity;
import investmentmanager.entity.InvestmentFund;
import investmentmanager.entity.Investor;
import investmentmanager.entity.Stock;
import jdk.internal.org.objectweb.asm.util.CheckAnnotationAdapter;
import investmentmanager.dal.InvestmentManagerFileDao;

@Component
@PropertySource("classpath:params.properties")
public class InvestmentService implements Manageable {
	// injected from xml
	@Autowired
	private InvestmentManagerDao dao;

	// injected properties from file
	@Value("${Manageable.max.stock.risk}")
	private double maxStockRisk;

	@Value("${Manageable.max.fund.for.investor}")
	private int maxFundForInvestor;

	@Value("${Manageable.min.stock.initial.price}")
	private double minStockInitialPrice;

	public void setDao(InvestmentManagerDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Entity> getAll(int id) throws Exception {
		return dao.getAll(id);
	}

	@Override
	public void save(Entity t) throws Exception {
		// Checked not saved before
		AlreadySavedEntity(t);
		// check if investor above max funds for investor
		checkTooManyFunds(t);
		// check min stock initial price
		
		dao.save(t);
	}

	public void AlreadySavedEntity(Entity t) throws Exception  {
		for (int i = 0; i <= InvestmentManagerFileDao.fund; i++) {
			if (getAll(i) != null) {
				for (Entity e : getAll(i)) {
					if (e.equals(t)) {
						throw new AlreadySavedEntityException(String.format("%s already saved.", t.toString()));
					}
				}

			}
		}
	}
	
	public void checkTooManyFunds(Entity t) throws TooManyFundsForInvestor  {
		// check if investor above max funds for investor
		if (t instanceof Investor) {
			Investor investor = (Investor) t;
			if (investor.getHashMap().size() > maxFundForInvestor) {
				throw new TooManyFundsForInvestor("can't save. The max funds for investor is: " + maxFundForInvestor);					}
			}	
	}
	
	public void checkStockExceptions(Entity t) throws BellowAlowedInitialPriceException, RiskNotValidException {
		if (t instanceof Stock) {
			Stock stock = (Stock) t;
			// check min stock price
			if (stock.getPrice() < minStockInitialPrice) {
				throw new BellowAlowedInitialPriceException(
						"The stock is bellow allowed initial price: " + minStockInitialPrice);
			}
			// check max stock price
			if (stock.getRisk() > maxStockRisk || stock.getRisk() < 0) {
				throw new RiskNotValidException(
						"The risk you entered for this stock is not valid. risk has to be above 0 and below: "
								+ maxStockRisk);
			}
		}
	}
	
	
	@Override
	public void update(Entity t) throws Exception {
		checkTooManyFunds(t);

		dao.update(t);
	}

	@Override
	public void delete(int id, Class c) throws Exception {
		dao.delete(id, c);
	}

	@Override
	public Entity get(int idm, Class c) throws Exception {
		return dao.get(idm, c);
	}

	// when the container is up
	public void whenIsUp() throws Exception {
		System.out.println("Hello fellow user!");
		try {
			Stock.initIdCounter(dao.GetStaticId(0));
			Investor.initIdCounter(dao.GetStaticId(1));
			InvestmentFund.initIdCounter(dao.GetStaticId(2));
			printAllEntities();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// when the container is down
	public void whenIsDown() throws Exception {
		System.out.println("Goodbye! thank you for using our system!");
		printAllEntities();

	}

	// prints all the entities
	private void printAllEntities() throws Exception {
		List<Entity> stocksList = getAll(0);
		List<Entity> investorsList = getAll(1);
		List<Entity> fundsList = getAll(2);
		System.out.println("here is the list of entinties in the system: ");
		for (Object stock : stocksList) {
			System.out.println(((Stock) stock).toString());

		}

		for (Object investor : investorsList) {
			System.out.println(((Investor) investor).toString());

		}

		for (Object fund : fundsList) {
			System.out.println(((InvestmentFund) fund).toString());
		}
	}
}
