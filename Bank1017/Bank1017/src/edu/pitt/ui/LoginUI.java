package edu.pitt.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.pitt.bank.Customer;
import edu.pitt.bank.Security;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginUI {

	//declares Login window objects
	
	private JFrame frmLogin;
	private JTextField txtLogin;
	JLabel lblLoginName;
	JLabel lblPassword;
	private JTextField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Bank 1017 Login");
		frmLogin.setBounds(100, 100, 450, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		lblLoginName = new JLabel("Login Name");
		lblLoginName.setBounds(26, 27, 74, 20);
		frmLogin.getContentPane().add(lblLoginName);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(26, 74, 74, 14);
		frmLogin.getContentPane().add(lblPassword);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(124, 27, 178, 20);
		frmLogin.getContentPane().add(txtLogin);
		txtLogin.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(124, 71, 178, 20);
		frmLogin.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String loginName = txtLogin.getText();
					int pin = Integer.parseInt(txtPassword.getText());
					Security s = new Security();
					Customer c = s.validateLogin(loginName, pin); //determines if username/password entered is a part of the bank system and if so logs you in
					if(c != null)
					{
						AccountDetailsUI ad = new AccountDetailsUI(c);
						frmLogin.setVisible(false);
					}
					else //if invalid login
					{
						ErrorLogger.log("Invalid username/pin was entered: L(" + txtLogin.getText() + "), P(" + txtPassword.getText() + ")");
						JOptionPane.showMessageDialog(null, "Invalid login/pin was enetered. Please try again.");
					}
				}
				catch(NullPointerException e2) //if connect limit was reached or if you can't connect to the database for some reason
				{
					ErrorLogger.log("Unable to Connect to Database");
					ErrorLogger.log(e2.getMessage());
					JOptionPane.showMessageDialog(null, "Unable to Connect to Database");
				}
				catch(Exception ex) //if invalid login
				{
					ErrorLogger.log("Invalid username/pin was entered");
					ErrorLogger.log(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Invalid pin was enetered");
				}
			}
		});
		btnLogin.setBounds(124, 162, 89, 23);
		frmLogin.getContentPane().add(btnLogin);
		
		
		//generates exit button and closes the program
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Have a nice day");
				DbUtilities db = new MySqlUtilities();
				db = null;
				System.exit(0);
			}
		});
		btnExit.setBounds(223, 162, 89, 23);
		frmLogin.getContentPane().add(btnExit);
		
		
	}
}
