package investmentmanager.entity;


import java.util.HashMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/*Investor class*/
public class Investor extends Entity {

	private static int idCounter = 0;
	private int investorId;
	private String name;
	private HashMap<InvestmentFund, Integer> investments;
	private double availableMoney;

	public Investor(String name, double money) throws NegativeInitialMoneyException {
		
		this.name = name;
		if(money < 0 )
			throw new NegativeInitialMoneyException("The initial money of investor can't be negative.");
		availableMoney = money;
		investorId = idCounter;
		idCounter++;
		investments = new HashMap<InvestmentFund, Integer>();

	}

	public double investorValue() {
		/* Calculate current value of investor */
		double value = 0;
		for (InvestmentFund fund : investments.keySet()) {
			value += fund.calculateFundPrice() * investments.get(fund);
		}
		return value + availableMoney;
	}

	public void buyUnits(InvestmentFund fund, int units) throws NotEnoughMoneyToSpendException {
		/* Buy Investment Fund units */
		double moneyToSpend = fund.calculateFundPrice() * units;
		if (availableMoney >= moneyToSpend) {
			availableMoney -= moneyToSpend;
			if (investments.containsKey(fund)) {
				investments.put(fund, investments.get(fund) + units);
			} else {
				investments.put(fund, units);
			}
		} else {
			throw new NotEnoughMoneyToSpendException("The investor doesn't has enough money to spend on this buy");
		}
	}

	public void sellUnits(InvestmentFund fund, int units) throws NotEnoughUnitsException, FundNotExistException {
		/* Sell Investment Fund units */
		if (investments.containsKey(fund)) {
			if (units < investments.get(fund)) {
				availableMoney += fund.calculateFundPrice() * units;
				investments.put(fund, investments.get(fund) - units);
			} else if (units == investments.get(fund)) {
				availableMoney += fund.calculateFundPrice() * units;
				investments.remove(fund);
			} else {
				throw new NotEnoughUnitsException(name + " doesn't have " +  units + " to sell.");
			}
		} else {
			throw new FundNotExistException(name + " doesn't have any units in this fund");
		}
	}


	public String getName() {
		return name;
	}

	public void updateFund(InvestmentFund fund) {
		int amount = investments.get(fund);
		if (investments.containsKey(fund)) {
			investments.remove(fund);
			investments.put(fund, amount);
		}
		
	}

	@Override
	public String toString() {
		return "Investor [investorId=" + investorId + ", name=" + name + ", investments=" + investments
				+ ", availableMoney=" + availableMoney + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Investor other = (Investor) obj;
		return investorId == other.investorId;
	}


	

	@Override
	public int getId() {
		return investorId;
	}

	@Override
	public double getValue() {
		return investorValue();
	}
	
	public HashMap<InvestmentFund, Integer> getHashMap(){
		return investments;
	}


	public static void initIdCounter(int counter) {
		idCounter = counter;
	}

}
