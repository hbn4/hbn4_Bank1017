package edu.pitt.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;
import edu.pitt.bank.Security;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.SystemColor;

public class AccountDetailsUI {
	
	//declares all the objects on the account frame

	private JFrame frmAccountDetails;
	private Customer accountOwner;
	private JTextArea balanceArea;
	private JTextArea amountArea;
	private JLabel lblAmount;
	private JComboBox<String> cboAccounts;
	private Account a;
	private JTextArea interestRateArea;
	private JTextArea penaltyArea;
	private JButton btnShowTransactions;
	private int timerCnt;
	private Timer time;
	private Security s;
	
	//generates the new Account window for the given customer that the user logged in with
	public AccountDetailsUI(Customer c) {
		accountOwner = c;
		initialize();
		frmAccountDetails.setVisible(true);
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAccountDetails = new JFrame();
		frmAccountDetails.setTitle("Account Deatils");
		frmAccountDetails.setBounds(100, 100, 450, 300);
		frmAccountDetails.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAccountDetails.getContentPane().setLayout(null);
		
		timerCnt = 0;
		//starts a new timer that logs the user out after 60 secounds
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(timerCnt == 60)
				{
					JOptionPane.showMessageDialog(null, "Session Expired");
					DbUtilities db = new MySqlUtilities();
					db = null;
					System.exit(0);
				}
				else
				{
					timerCnt++;
				}
			}
		}, new Date(), 1000);
		
		ArrayList<Account> acctArr = new ArrayList<Account>();
		
		//select query to add the accounts associated with the given owner to the combo box on the account window
		String sql = "SELECT fk_accountID FROM customer_account WHERE fk_customerID = '" + accountOwner.getCustomerID() + "';";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next())
			{
				Account a = new Account(rs.getString("fk_accountID"));
				acctArr.add(a);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//generates exit button and closes the program
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Have a nice day, " + accountOwner.getFirstName());
				DbUtilities db = new MySqlUtilities();
				db = null;
				System.exit(0);
			}
		});
		btnExit.setBounds(321, 216, 89, 23);
		frmAccountDetails.getContentPane().add(btnExit);
		
		JLabel lblYourAccounts = new JLabel("Your Accounts:");
		lblYourAccounts.setBounds(16, 71, 89, 47);
		frmAccountDetails.getContentPane().add(lblYourAccounts);
		
		s = new Security();
		
		JTextArea permArea = new JTextArea();
		permArea.setText(accountOwner.getFirstName()+" "+accountOwner.getLastName() +", "
				+ "welcome to 1017 bank.\n You have the following permissions in this system: " + permission());
		permArea.setEditable(false);
		permArea.setBackground(SystemColor.control);
		permArea.setLineWrap(true);
		permArea.setBounds(16, 11, 408, 47);
		frmAccountDetails.getContentPane().add(permArea);
		
		amountArea = new JTextArea();
		amountArea.setBounds(269, 120, 89, 22);
		frmAccountDetails.getContentPane().add(amountArea);
		
		lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(189, 113, 50, 38);
		frmAccountDetails.getContentPane().add(lblAmount);
		
		balanceArea = new JTextArea();
		balanceArea.setBackground(SystemColor.control);
		balanceArea.setText("Balance:");
		balanceArea.setBounds(16, 129, 120, 22);
		frmAccountDetails.getContentPane().add(balanceArea);
		
		interestRateArea = new JTextArea();
		interestRateArea.setBackground(SystemColor.control);
		interestRateArea.setBounds(16, 164, 161, 22);
		frmAccountDetails.getContentPane().add(interestRateArea);
		
		penaltyArea = new JTextArea();
		penaltyArea.setBackground(SystemColor.control);
		penaltyArea.setBounds(16, 197, 120, 22);
		frmAccountDetails.getContentPane().add(penaltyArea);
		
		cboAccounts = new JComboBox<String>();
		cboAccounts.setBounds(109, 83, 311, 23);
		frmAccountDetails.getContentPane().add(cboAccounts);
		for(int k = 0; k < acctArr.size(); k++)
		{
			cboAccounts.addItem(acctArr.get(k).getAccountID());
		}
		a = new Account(cboAccounts.getSelectedItem().toString());
		balanceArea.setText("Balance: " + (a.getBalance()));
		interestRateArea.setText("Interest Rate: " + (a.getInterestRate()));
		penaltyArea.setText("Penalty: " + (a.getPenalty()));
		cboAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a = new Account(cboAccounts.getSelectedItem().toString());
				balanceArea.setText("Balance: " + (a.getBalance()));
				interestRateArea.setText("Interest Rate: " + (a.getInterestRate()));
				penaltyArea.setText("Penalty: " + (a.getPenalty()));
			}
		});
		
		//generates deposit button and if clicked adds the given amount into the customer's balance, if the account isn't
		//frozen and if the text entered is a valid number
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if(a.getStatus().equals("frozen"))
					{
						JOptionPane.showMessageDialog(null,"Your account is frozen");
					}
					else
					{
						double d = Double.parseDouble(amountArea.getText());
						a.deposit(d);
						balanceArea.setText("Balance: " + (a.getBalance()));
						amountArea.setText("");
					}
				}
				catch(NumberFormatException ex)
				{
					ErrorLogger.log("Invalid Amount: " + amountArea.getText());
					ErrorLogger.log(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Please enter a valid number");
					amountArea.setText("");
				}
				
			}
		});
		btnDeposit.setBounds(187, 165, 89, 23);
		frmAccountDetails.getContentPane().add(btnDeposit);
		
		
		//generates withdraw button and if clicked subtracts the given amount into the customer's balance, if the account isn't
		//frozen and if the text entered is a valid number
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if(a.getStatus().equals("frozen"))
					{
						JOptionPane.showMessageDialog(null, "Your account is frozen");
					}
					else
					{
						double d = Double.parseDouble(amountArea.getText());
						a.withdraw(d);
						balanceArea.setText("Balance: " + (a.getBalance()));
						amountArea.setText("");
					}
				}
				catch(NumberFormatException ex)
				{
					ErrorLogger.log("Invalid Number");
					ErrorLogger.log(ex.getMessage());
					JOptionPane.showMessageDialog(null,"Please enter a valid number");
				}
			}
		});
		btnWithdraw.setBounds(286, 165, 89, 23);
		frmAccountDetails.getContentPane().add(btnWithdraw);
		
		
		//opens up the transaction window for the given account
		btnShowTransactions = new JButton("Show Transactions");
		btnShowTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionUI td = new TransactionUI(a);
			}
		});
		btnShowTransactions.setBounds(146, 216, 161, 23);
		frmAccountDetails.getContentPane().add(btnShowTransactions);
		
		
		//extra button for giggles: it logs out the user and returns them to the Login window and turns off and resets the timer
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				timerCnt = 0;
				time.cancel();
				String[] args = new String[10];
				frmAccountDetails.setVisible(false);
				LoginUI.main(args);
				
			}
		});
		btnLogOut.setBounds(16, 230, 89, 23);
		frmAccountDetails.getContentPane().add(btnLogOut);
		
		
	}
	
	
	//permission method adds the string of group names to the welcoming message.

	private String permission() {
		
		String str = "";
		ArrayList<String> groupName = s.listUserGroups(accountOwner.getCustomerID());
		for(int i = 0; i < groupName.size(); i++)
		{
			str = str + groupName.get(i) + ", ";
		}
		
		
		return str;
	}
}
