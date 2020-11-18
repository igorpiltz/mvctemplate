package mvctemplate.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mvctemplate.model.Model;
import util.parser.Command;
import util.parser.StringToken;
import util.parser.Token;
import util.parser.UserInterruptException;

public class SwingView extends javax.swing.JFrame implements View {
	
	private List<JButton> buttonList = new ArrayList<JButton>();
	private List<Command> commandList = new ArrayList<Command>();
	private String returnString;
	    
	/** Creates new form CelsiusConverterGUI */
	public SwingView() {
	    for (int index = 0; index < 4; index++) 
	    	buttonList.add(new JButton("Button " + (index + 1)));
		initComponents();
	}
	
	
	
	
	    
	private void initComponents() {
		JPanel commandLinePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		// Layout är commandline-Panel till V och knappPanelen till H
		Container pane = getContentPane();
		pane.add(commandLinePanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.LINE_END);
		
		// commandLinePanel:s komponenter
		// outputArea och commandField
		commandLinePanel.setLayout(new BorderLayout());
		JTextArea outputArea = new JTextArea("Some Text");
		commandLinePanel.add(outputArea, BorderLayout.CENTER);
		JTextField commandField = new JTextField();
		commandLinePanel.add(commandField, BorderLayout.PAGE_END);
		
		// Buttonpanel
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		for (int index = 0; index < buttonList.size(); index++) {
			buttonPanel.add(buttonList.get(index));
		}
		
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Swing View");
		
		/*
		convertButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				convertButtonActionPerformed(evt);
	            	}
	        	});

		*/

		pack();
   	}

	/*
	private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
		//Parse degrees Celsius as a double and convert to Fahrenheit
		int tempFahr = (int)((Double.parseDouble(tempTextField.getText())) * 1.8 + 32);
    	fahrenheitLabel.setText(tempFahr + " Fahrenheit");
    }//GEN-LAST:event_convertButtonActionPerformed
	    
	*/  
	    

	@Override
	public void messageToUser(Object actualString) {
		JOptionPane.showMessageDialog(this, actualString);

	}

	@Override
	public String askQuestion(Object question) throws IOException, UserInterruptException {
		String answer = JOptionPane.showInputDialog(this, question);
		if (answer == null)
			throw new UserInterruptException();
		
		return answer;
	}

	@Override
	public boolean askYesnoQuestion(String question) throws IOException, UserInterruptException {
		int answer = JOptionPane.showConfirmDialog(this, question);
		if (answer == JOptionPane.YES_OPTION)
			return true;
		if (answer == JOptionPane.NO_OPTION)
			return false;
		if (answer == JOptionPane.CANCEL_OPTION)
			throw new UserInterruptException();
		return false;
	}

	@Override
	public void startMessage() {
		
	}

	@Override
	public void addChoice(Command command) {
		commandList.add(command);
	}

	@Override
	public String showChoices() throws IOException {
		// TODO Auto-generated method stub
		for (int index = 0; index < commandList.size(); index++) {
			Command command = commandList.get(index);
			JButton button = buttonList.get(index);
			
			StringBuffer buttonText = new StringBuffer();
			for (int sindex = 0; sindex < 3; sindex++) {
				if (sindex >= command.getTokens().size())
					break;
							
				
				Token token = command.getTokens().get(sindex);
				if (token.getClass() != StringToken.class)
					break;
				buttonText.append(token.getDefining());
				buttonText.append(" ");
			}
				
			
			button.setText(buttonText.toString().trim());
			button.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					buttonActionPerformed(evt);
            	}});

		}

		synchronized(this) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return returnString;
	}

	protected void buttonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		returnString = evt.getActionCommand();
		synchronized(this) {
			notifyAll();
		}
				
	}





	@Override
	public void showStatus(Model model) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void printControlScheme(List<Command> commandList) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this, "The Control Scheme");

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	setVisible(true);
            }
        });
				
	}

}
