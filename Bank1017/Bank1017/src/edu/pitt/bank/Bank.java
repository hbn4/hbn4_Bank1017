package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

public class Bank {
	
	public ArrayList<Account> accountList = new ArrayList<Account>(); //list of accounts in the bank
	public ArrayList<Customer> customerList = new ArrayList<Customer>(); //list of customers in the bank

	//generates a new bank object, loading in all accounts and customers
	public Bank()
	{
		loadAccounts();
		setAccountOwners();
	}

	//loads the accounts from the account table into accountList
	public void loadAccounts()
	{
		String sql = "SELECT accountID FROM account";
		DbUtilities db = new MySqlUtilities();
		try
		{
			ResultSet rs = db.getResultSet(sql);
			while(rs.next())
			{
				Account a = new Account(rs.getString("accountID"));
				accountList.add(a);
			}
		}
		catch (Exception e)
		{
			ErrorLogger.log("Account cannot be created");
			ErrorLogger.log(e.getMessage());
		}
	}

	//looks through the accountList for the given account and if found returns it
	public Account findAccount(String accountID)
	{
		for(int i = 0; i < accountList.size(); i++)
		{
			if(accountList.get(i).getAccountID().equals(accountID));
			{
				return accountList.get(i);
			}
		}
		return null;
	}

	//looks through the customerList for the given customer and if found returns it
	public Customer findCustomer(String customerID)
	{
		for(int i = 0; i < customerList.size(); i++)
		{
			if(customerList.get(i).getCustomerID().equals(customerID));
			{
				return customerList.get(i);
			}
		}
		return null;
	}

	//sets all of the customers as new account owners and addes them to the customerList
	public void setAccountOwners()
	{
		String sql = ("SELECT * FROM account join customer_account ON accountID = fk_accountID JOIN customer ON fk_customerID = customerID;");
		DbUtilities db = new MySqlUtilities();
		try
		{
			ResultSet rs = db.getResultSet(sql);
			while(rs.next())
			{
				Customer c = new Customer(rs.getString("customerID"));
				customerList.add(c);
				Account a = new Account(rs.getString("accountID"));
				a.addAccountOwner(c);
			}
		}
		catch(Exception ex)
		{
			ErrorLogger.log("Acct/Customer cannot be added");
			ErrorLogger.log(ex.getMessage());
		}
	}

}