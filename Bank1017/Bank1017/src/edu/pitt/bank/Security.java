package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;

public class Security 
{
	private String userID;
	
	//Class that tests whether a given login name and pin are part of the system and if so returns the associated customer
	public Customer validateLogin(String loginName, int pin)
	{
		String sql = ("SELECT * from customer WHERE loginName = '" + loginName + "' and pin = '" + pin + "';");
		Customer cust = null;
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			if(rs.next())
			{
				cust = new Customer(rs.getString("customerID"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cust;
	}
	
	//takes the given customerID (userID) and returns a list of all the groups a given user is a part of (User,Customer,Teller,or Manager)
	public ArrayList<String> listUserGroups(String userID)
	{
		ArrayList<String> group = new ArrayList<String>();
		String userSql = ("SELECT * FROM user_permissions WHERE groupOrUserID = '" + userID + "';");
		DbUtilities userDb = new MySqlUtilities();
		try {
			ResultSet userRs = userDb.getResultSet(userSql);
			while(userRs.next())
			{
				group.add(userRs.getString("groupID")); //adds the groupID of the customer given to group Array
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String groupSql = null;
		ArrayList<String> list = new ArrayList<String>();
		DbUtilities groupDb = new MySqlUtilities();
		
		for(int i = 0; i < group.size(); i++)
		{
			groupSql = ("SELECT * FROM groups WHERE groupID = '" + group.get(i) + "';");
			try {
				ResultSet groupRs = groupDb.getResultSet(groupSql);
				while(groupRs.next())
				{
					list.add(groupRs.getString("groupName")); //adds the type of each groupID from group Array to list Array
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	
	
	
	
	
	
	
	
}