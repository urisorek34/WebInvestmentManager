package investmentmanager.entity;

import java.io.ObjectOutputStream.PutField;
import java.util.HashMap;
import java.util.Objects;

/*Investment fund class*/
public class InvestmentFund extends Entity{

	private static int idCounter = 0;
	private int id;
	private HashMap<Stock, Integer> stocks; // Stock, percentage

	public InvestmentFund() {
		stocks = new HashMap<>();
		id = idCounter;
		idCounter++;
	}

	public void addStock(Stock stock, int amount) {
		stocks.put(stock, amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvestmentFund other = (InvestmentFund) obj;
		return id == other.id;
	}

	// function removes stock from the investment fund
	public void removeStock(Stock stock) throws StockNotExistException {
		if(stocks.containsKey(stock)) {
			stocks.remove(stock);
		}
		else {
			throw new StockNotExistException("this stock dosen't exist in this fund");
		}
	}
	
	private double calculatePercentage(Stock stock) {
		/* Calculate the percentage of each stock from the fund stocks */
		if (!stocks.containsKey(stock)) {
			return 0;
		}
		int sum = 0;
		for (Integer stockAmount : stocks.values()) {
			sum += stockAmount;
		}
		return (double) sum / stocks.get(stock);
	}


	public double calculateFundPrice() {
		/* Calculate unit fund price */
		double price = 0;
		for (Stock stock : stocks.keySet()) {
			price += stock.getPrice() * calculatePercentage(stock);
		}
		return price;
	}

	public int getId() {
		return id;
	}

	public void updateStocks(Stock stock) {
		int amount = stocks.get(stock);
		if (stocks.containsKey(stock)) {
			stock.setPrice(stock.getPrice());
			stocks.put(stock, amount);
		} 
	}
	@Override
	public String toString() {
		String str = "stocks = ";
		for (Stock stock : stocks.keySet()) {
			str += stock.toString() + " --> " + stocks.get(stock) + ", ";
		}
		return "InvestmentFund [id=" + id + ", " + str + "]";
	}


	@Override
	public double getValue() {
		return calculateFundPrice();
	}

	public static void initIdCounter(int counter) {
		idCounter = counter;
		
	}

}
