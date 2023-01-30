package investmentmanager.entity;

import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;

import investmentmanager.dal.InvestmentManagerFileDao;

/*Class for stock*/
public class Stock extends Entity {
	private static int idCounter = 0;
	private double price;
	private int stockId;
	private String company;
	private double risk;
	private static Random rnd = new Random();

	public Stock(String company, double initialPrice, double stockRisk) {
		stockId = idCounter;
		idCounter++;
		this.company = company;
		price = initialPrice;
		risk = stockRisk;
	}

	@Override
	public String toString() {
		return "Stock [price=" + price + ", stockId=" + stockId + ", company=" + company + ", risk=" + risk + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(stockId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		return stockId == other.stockId;
	}

	public void changeStockPrice() {
		/* change the price according to the risk */
		price = price * (1 - risk + rnd.nextDouble() * (2 * risk));
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double d) {
		this.price = d;
	}

	public double getRisk() {
		return risk;
	}

	public String getCompany() {
		return company;
	}

	@Override
	public int getId() {
		return stockId;
	}

	@Override
	public double getValue() {
		return price;
	}

	public static void initIdCounter(int counter) {
		idCounter = counter;

	}

}
