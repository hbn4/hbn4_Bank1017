package edu.pitt.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.pitt.bank.Account;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransactionUI {
	
	//declares transaction window objects

	private JFrame frmTransactionUI;
	private JScrollPane transactionPane;
	private JTable tblTransactions;
	private Account act;
	private JButton btnClose;
	
	//opens the new transaction window for the given account

	public TransactionUI(Account a) {
		act = a;
		initialize();
		frmTransactionUI.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTransactionUI = new JFrame();
		frmTransactionUI.setTitle("Account Transactions");
		frmTransactionUI.setBounds(100, 100, 450, 300);
		frmTransactionUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		transactionPane = new JScrollPane();
		frmTransactionUI.getContentPane().add(transactionPane, BorderLayout.CENTER);
		DbUtilities db = new MySqlUtilities();
		String[] cols = {"Type","Date/Time","Amount"};
		String sql = "SELECT type,transactionDate,amount FROM transaction WHERE accountID = '" + act.getAccountID() + "';";
		
		try { //adds to the transaction window the type/date/and amount of each transaction for logged in account
			DefaultTableModel transactionList = db.getDataTable(sql,cols);
			tblTransactions = new JTable(transactionList);
			tblTransactions.setShowGrid(true);
			tblTransactions.setGridColor(Color.black);
			transactionPane.setViewportView(tblTransactions);
			
			btnClose = new JButton("Exit");
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmTransactionUI.setVisible(false);
				}
			});
			transactionPane.setRowHeaderView(btnClose);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
