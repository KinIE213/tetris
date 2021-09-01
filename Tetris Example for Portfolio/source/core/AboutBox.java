/**
 * Polytechnic West Project example
 */
 
package core;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;

public class AboutBox extends JDialog implements ActionListener {

	//Panel
	private JPanel pnlContainer;
	private JPanel pnlTop;
	private JPanel pnlCentre;
	private JPanel pnlBottom;
	//Labels
	private JLabel lblTitle;
	//Button
	private JButton btnExit;
	//TextArea
	private JTextArea txtCredit;
    
	/**
	 * Constructor
	 *
	 */
	public AboutBox() {
        
		setTitle("About");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		//Panel
		pnlContainer = new JPanel(new BorderLayout());
		pnlTop = new JPanel();
		pnlCentre = new JPanel();
		pnlBottom = new JPanel();
		
		//Labels
		lblTitle = new JLabel("Tetris");
		Font bigFont = new Font("Arial", Font.BOLD, 14);
		lblTitle.setFont(bigFont);

		//TextArea
		txtCredit = new JTextArea(7,15);
		txtCredit.setEditable(false);
		txtCredit.setText("Diploma of Software Development");
		txtCredit.append("\n");
		txtCredit.append("Object Oriented Programming");
		txtCredit.append("\n");
		txtCredit.append("Polytechnic West");
		txtCredit.append("\n");
		txtCredit.append("September, 20XX");
		txtCredit.append("\n\n");
		txtCredit.append("Sample Developer");
		
		//Button
		btnExit = new JButton("OK");
		btnExit.setMnemonic('O');
		//Button Add Action
		btnExit.addActionListener(this);
		
		
		//Adding components into panel
		pnlTop.add(lblTitle);
		pnlCentre.add(txtCredit);
		pnlBottom.add(btnExit);
		
		//Adding panel to container panel
		pnlContainer.add(pnlTop, BorderLayout.NORTH);
		pnlContainer.add(pnlCentre, BorderLayout.CENTER);
		pnlContainer.add(pnlBottom, BorderLayout.SOUTH);
		
		getContentPane().add(pnlContainer);
		pack();
   }

	/**
	 * Shows about box
	 * 
	 */
	public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
	}
}
