package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;

public class Customer {
	//declares all the properties of the Customer object
	private String customerID;
	private String firstName;
	private String lastName;
	private String ssn;
	private String streetAddress;
	private String city;
	private String state;
	private String zip;
	private String loginName;
	private String pin;
	
	
	//selects the information about the given customer from the given customer table
	public Customer(String customerID){
		String sql = ("SELECT * from customer WHERE customerID = '" + customerID + "';");
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			if(rs.next())
			{
				this.customerID = customerID;
				this.firstName = rs.getString("firstName");
				this.lastName = rs.getString("lastName");
				this.ssn = rs.getString("ssn");
				this.streetAddress = rs.getString("streetAddress");
				this.city = rs.getString("city");
				this.state = rs.getString("state");
				this.zip = rs.getString("zip");
				this.loginName = rs.getString("loginName");
				this.pin = rs.getString("pin");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//Inserts a new customer into the customer table
	public Customer(String lastName, String firstName, String ssn, String loginName, int pin, String streetAddress, String city, String state, int zip){
		this.customerID = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.loginName = loginName;
		int p = pin;
		int z = zip;
		
		String sql = "INSERT INTO customer ";
		sql += "(customerID,firstName,lastName,ssn,streetAddress,city,state,zip,loginName,pin) ";
		sql += " VALUES ";
		sql += "('" + this.customerID + "', ";
		sql += "'" + this.firstName + "', ";
		sql += "'" + this.lastName + "', ";
		sql += "'" + this.ssn + "', ";
		sql += "'" + this.streetAddress + "', ";
		sql += "'" + this.city + "', ";
		sql += "'" + this.state + "', ";
		sql += z + ", ";
		sql += "'" + this.loginName + "', ";
		sql += p + ");";
		
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
		
		
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
}
