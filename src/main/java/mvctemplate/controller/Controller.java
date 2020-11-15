package mvctemplate.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import mvctemplate.dao.DAO;
import mvctemplate.dao.ObjectStreamDAO;
import mvctemplate.model.Model;
import mvctemplate.view.CommandLineView;
import mvctemplate.view.View;
import util.parser.Callable;
import util.parser.Command;
import util.parser.CommandLineParser;
import util.parser.StringToken;
import util.parser.Token;
import util.parser.UnknownCommandException;
import util.parser.UserInterruptException;

public class Controller {
	
	private CommandLineParser parser;
	private Model model;
	private View view;
	private DAO dao;
	
		
	public Controller(DAO dao, View view) {
		
		this.dao = dao;
		this.view = view;
		
		model = dao.getModel();
		model.checkConsistency();
		
		parser = new CommandLineParser();
		
		// Lite variabler för kommandona att använda
		Command command = null;
		
		
				
		// Kommandona börjar här
			
		
		// help
		// ett hjälp-kommando som printar alla kommandon en gång till
		command = new Command();
		command.add(new StringToken("help"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				help(tokens);
			}});
		parser.add(command);
		
						
		// avsluta
		command = new Command();
		command.add(new StringToken("exit"));
		command.add(new StringToken("program"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				quit(tokens);
			}});
		parser.add(command);
	}
	

	protected void help(List<Token> tokens) {
		view.printControlScheme(parser.getCommandList());
	}


	protected void quit(List<Token> tokens) {
		System.exit(0);
	}


	public static void main(String args[]) throws IOException, ClassNotFoundException {
		
		Controller controller = null;
		View view = new CommandLineView();
		
		controller = new Controller(new ObjectStreamDAO(), view);
			
		
		controller.run();
		
				
	}

	private void run() {

		
		view.startMessage();
		view.printControlScheme(parser.getCommandList());
		view.showStatus(model);
		
		
				
		while(true) {
								
			
			String input = null;
			try {
				input = view.askQuestion("What command?");
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			} catch (UserInterruptException e1) {
				e1.printStackTrace();
			}
			
			if (input == null)
				continue;
				
			try {
				processInput(input);
			} catch (UnknownCommandException e) {
				System.out.println("Unknown Command: \"" + e.getMessage() + "\"");
			} catch (UserInterruptException e) {
				System.out.println("User interrupt");
			}
			
			view.showStatus(model);
			
		}
			
		
	}


	public void setView(View view) {
				
		this.view = view;
	}

	public Object getState() {
		
		StringBuffer buf = new StringBuffer();
		buf.append(model.getState());
		
		
		return buf.toString();
		
	}

	public void processInput(String input) throws UnknownCommandException, UserInterruptException {
		
		Command command = parser.parse(input);
		command.getCallable().exec(command.getTokens());
				
		
		try {
			dao.persist();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	

	public Model getModel() {
		return model;
	}


	public void add(Command command) {
		parser.add(command);
	}


	public View getView() {
		return view;
	}


	public DAO getDAO() {
		return dao;
	}
}
