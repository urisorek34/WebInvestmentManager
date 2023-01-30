package investmentmanager.cli;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import investmentmanager.dal.InvestmentManagerFileDao;
import investmentmanager.entity.Entity;
import investmentmanager.entity.InvestmentFund;
import investmentmanager.entity.Investor;
import investmentmanager.entity.Stock;
import investmentmanager.service.Manageable;

public class InvestmentsCLI {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Manageable investmentManager = context.getBean("investmentService",Manageable.class);
		// constants for user codes
		final int SHOW_ENTITYS = 1;
		final int GET_ENTITY = 2;
		final int ADD_ENTITY = 3;
		final int UPDATE_ENTITY = 4;
		final int DELETE_ENTITY = 5;
		final int EXIT = 0;
	
		
		String welcomeMsg = "Hello, Welcome to Aviad's, Yuval's and Uri's investment manager!!!";
		Scanner scanner = new Scanner(System.in);
		int userInput = -1;
		while (userInput != 0)
		{
			// print options
			System.out.println("Enter 1 to show entity list.");
			System.out.println("Enter 2 to show specific entity.");
			System.out.println("Enter 3 to add entity.");
			System.out.println("Enter 4 to update entity.");
			System.out.println("Enter 5 to delete entity.");
			System.out.println("Enter 0 to exit.");
			System.out.println("Enter your choice: ");
			// getting user input
			userInput = scanner.nextInt();
			int code = -1;
			int id = -1;
			Entity e = null;
			int units = 0;
			try {
				switch (userInput) {
				case SHOW_ENTITYS:
					
					// get id and print all entities
					System.out.println("Enter entity:\n0 to stock\n1 to investor\n2 to fund");
					code = scanner.nextInt();
					if (code < 0 || code > 3) {
						System.out.println("Invalid choice");
					}
					// print all entities
					if (investmentManager.getAll(code) != null) {
						for (Entity entity : investmentManager.getAll(code)) {
							System.out.println(entity);
						}
					}
					
					break;
				case GET_ENTITY:
					// returns an entity
					System.out.println("Enter entity:\n0 to stock\n1 to investor\n2 to fund");
					code = scanner.nextInt();
					System.out.println("Enter entity id: ");
					id = scanner.nextInt();
					switch (code) {
					case InvestmentManagerFileDao.stock:
						System.out.println(investmentManager.get(id, Stock.class));
						break;
					case InvestmentManagerFileDao.investor:
						System.out.println(investmentManager.get(id, Investor.class));
						break;
					case InvestmentManagerFileDao.fund:
						System.out.println(investmentManager.get(id, InvestmentFund.class));
						break;
					default:
						System.out.println("Please enter valid input.");
						break;
					}
					break;
				case ADD_ENTITY:
					//adds new entity
					System.out.println("Enter entity:\n0 to stock\n1 to investor\n2 to fund");
					code = scanner.nextInt();
					e = null;
					switch (code) {
					case InvestmentManagerFileDao.stock:
						// stock
						System.out.println("Enter company name: ");
						String companyString = scanner.next();
						System.out.println("Enter initial price: ");
						double initialPrice = scanner.nextDouble();
						System.out.println("Enter risk: ");
						double risk = scanner.nextDouble();
						e = new Stock(companyString, initialPrice, risk);
						break;
					case InvestmentManagerFileDao.investor:
						// investor
						System.out.println("Enter investor name: ");
						String nameString = scanner.next();
						System.out.println("Enter investor money: ");
						double money = scanner.nextDouble();
						e = new Investor(nameString, money);
						break;
					case InvestmentManagerFileDao.fund:
						//fund
						e = new InvestmentFund();
						break;
					default:
						System.out.println("Please enter valid input.");
						break;
					}
					investmentManager.save(e);
					break;
				case UPDATE_ENTITY:
					//update entity
					System.out.println("Enter entity:\n0 to update stock price\n1 to investor buy, sell units or add, remove fund\n2 to fund for add,remove stocks");
					code = scanner.nextInt();
					e = null;

					switch (code) {
					case InvestmentManagerFileDao.stock:
						System.out.println("Enter stock id: ");
						id = scanner.nextInt();
						Stock stock = (Stock) investmentManager.get(id, Stock.class);
						stock.changeStockPrice();
						System.out.println(stock.getPrice());
						
						for(Entity fund : investmentManager.getAll(InvestmentManagerFileDao.fund)) {
							((InvestmentFund)fund).updateStocks(stock);
							investmentManager.update(fund);
							for(Entity investor : investmentManager.getAll(InvestmentManagerFileDao.investor)) {
								((Investor)investor).updateFund(((InvestmentFund)fund));
								investmentManager.update(investor);
							}
						}
						System.out.println("Stock price updated!");
						e = stock;
						break;
					case InvestmentManagerFileDao.investor:
						System.out.println("Enter investor id: ");
						id = scanner.nextInt();
						Investor investor = (Investor)investmentManager.get(id, Investor.class);
						System.out.println("Enter 1 to buy\nEnter 2 to sell\nEnter 3 to remove\nEnter code: ");
						code = scanner.nextInt();
						System.out.println("Enter fund id: ");
						id = scanner.nextInt();
						System.out.println("Enter units: ");
						units = scanner.nextInt();
						switch (code) {
						case 1:
							investor.buyUnits((InvestmentFund)investmentManager.get(id, InvestmentFund.class), units);
							break;
						case 2:
							investor.sellUnits((InvestmentFund)investmentManager.get(id, InvestmentFund.class), units);
							break;
						default:
							System.out.println("Please enter valid input.");
							break;
						}
						e = investor;
						break;
					case InvestmentManagerFileDao.fund:
						System.out.println("Enter fund id: ");
						id = scanner.nextInt();
						InvestmentFund fund = (InvestmentFund)investmentManager.get(id, InvestmentFund.class);
						System.out.println("Enter 1 to add stock.\nEnter 2 to remove stock.");
						code = scanner.nextInt();
						switch (code) {
						case 1:
							// add stock to fund
							System.out.println("Enter stock id: ");
							id = scanner.nextInt();
							Stock stock2 = (Stock)investmentManager.get(id, Stock.class);
							System.out.println("Enter amount of stocks: ");
							int amount = scanner.nextInt();
							fund.addStock(stock2, amount);
							break;
						case 2:
							// remove stock
							System.out.println("Enter stock id: ");
							id = scanner.nextInt();
							Stock stock3 = (Stock)investmentManager.get(id, Stock.class);
							fund.removeStock(stock3);
						default:
							System.out.println("Please enter valid input.");
							break;
						}
						e = fund;
						break;
					default:
						System.out.println("Please enter valid input.");
						break;
					}

					investmentManager.update(e);
					break;
				case DELETE_ENTITY:
					//Deletes a given entity
					System.out.println("Enter entity:\n0 to stock\n1 to investor");
					code = scanner.nextInt();
					e = null;
					switch (code) {
					case InvestmentManagerFileDao.stock:
						//stock
						System.out.println("Enter stock id: ");
						id = scanner.nextInt();
						investmentManager.delete(id, Stock.class);
						break;
					case InvestmentManagerFileDao.investor:
						//investor
						System.out.println("Enter investor id: ");
						id = scanner.nextInt();
						investmentManager.delete(id, Investor.class);
						break;
					
					default:
						System.out.println("Please enter valid input.");
						break;
					}
					break;
				default:
					System.out.println("Enter valid input.");
					break;
				}
			}
			catch (Exception e1) {
				System.out.println("invalid input" + e1.getMessage());
			}
		}
	}

}
