package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;

public class Account {
	
	//Declares all variables of the account class/table
	private String accountID;
	private String type;
	private double balance;
	private double interestRate;  
	private double penalty;
	private String status;
	private Date dateOpen;
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private ArrayList<Customer> accountOwners = new ArrayList<Customer>();
	
	
	//Retrieves data from account table and populates Account properties,
	//Also retrieves data from transaction table for said account and adds it to transactionList
	public Account(String accountID){
		String sql = "SELECT * FROM account WHERE accountID = '" + accountID + "'"; 
		DbUtilities db = new MySqlUtilities(); 
		try {
			ResultSet rs = db.getResultSet(sql); 
			while(rs.next()){ //populates Account properties
				this.accountID = accountID;
				this.type = rs.getString("type");
				this.balance = rs.getDouble("balance");
				this.interestRate = rs.getDouble("interestRate");
				this.penalty = rs.getDouble("penalty");
				this.status = rs.getString("status");
				this.dateOpen = new Date();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String transSql = "SELECT * FROM transaction WHERE accountID = '" + accountID + "'"; //Selects all values from the transaction table
		DbUtilities dbTrans = new MySqlUtilities();
		try {
			ResultSet rsTrans = dbTrans.getResultSet(transSql);
			while(rsTrans.next())
			{
				createTransaction(this.accountID); //adds given account's transactions to transactionList
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//creates a new account for a new user and adds them to the account table
	public Account(String accountType, double initialBalance){
		this.accountID = UUID.randomUUID().toString();
		this.type = accountType;
		this.balance = initialBalance;
		this.interestRate = 0;
		this.penalty = 0;
		this.status = "active";
		this.dateOpen = new Date();
		
		String sql = "INSERT INTO account ";
		sql += "(accountID,type,balance,interestRate,penalty,status,dateOpen) ";
		sql += " VALUES ";
		sql += "('" + this.accountID + "', ";
		sql += "'" + this.type + "', ";
		sql += this.balance + ", ";
		sql += this.interestRate + ", ";
		sql += this.penalty + ", ";
		sql += "'" + this.status + "', ";
		sql += "CURDATE());";
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql); //inserts new account into the account table
	}
	
	//withdraws the given amount from the account owners balance
	public void withdraw(double amount){
		this.balance -= amount;
		createTransaction(this.accountID, this.type, amount, this.balance);
		updateDatabaseAccountBalance();
	}
	
	//deposits the given amount from the account owner's balance
	public void deposit(double amount){
		this.balance += amount;
		createTransaction(this.accountID, this.type, amount, this.balance);
		updateDatabaseAccountBalance();
	}
	
	//updates the balance of the owner's account after deposit/withdraw
	private void updateDatabaseAccountBalance(){
		String sql = "UPDATE account SET balance = " + this.balance + " ";
		sql += "WHERE accountID = '" + this.accountID + "';";
		
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
	}
	
	//adds a transaction to the transaction list
	private Transaction createTransaction(String transactionID){
		Transaction t = new Transaction(transactionID);
		transactionList.add(t);
		return t;
	}
	
	//adds a transaction to the transaction list
	private Transaction createTransaction(String accountID, String type, double amount, double balance){
		Transaction t = new Transaction(accountID, type, amount, balance);
		transactionList.add(t);
		return t;
	}
	
	//adds a transaction to the transaction list
	private Transaction createTransaction()
	{
		Transaction t = new Transaction(this.accountID,this.type,0.00,this.balance);
		return t;
	}
	
	//adds a new account to the accountOwners list
	public void addAccountOwner(Customer accountOwner)
	{
		Customer c = accountOwner;
		accountOwners.add(c);
	}
	
	public String getAccountID(){
		return this.accountID;
	}
	
	public double getBalance(){
		return this.balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateOpen() {
		return dateOpen;
	}

	public void setDateOpen(Date dateOpen) {
		this.dateOpen = dateOpen;
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public ArrayList<Customer> getAccountOwners() {
		return accountOwners;
	}

	public void setAccountOwners(ArrayList<Customer> accountOwners) {
		this.accountOwners = accountOwners;
	}
	
}
