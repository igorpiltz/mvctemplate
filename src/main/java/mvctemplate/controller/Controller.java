package mvctemplate.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mvctemplate.dao.DAO;
import mvctemplate.dao.ObjectStreamDAO;
import mvctemplate.model.Model;
import mvctemplate.view.CommandLineView;
import mvctemplate.view.View;
import util.parser.Callable;
import util.parser.ChoiceToken;
import util.parser.Command;
import util.parser.CommandLineParser;
import util.parser.IntegerToken;
import util.parser.RestOfLineToken;
import util.parser.StringToken;
import util.parser.Token;
import util.parser.UnknownCommandException;
import util.parser.UserInterruptException;

public class Controller {
	
	private CommandLineParser parser;
	private Model model;
	private View view;
	private DAO dao;
	
		
	public Controller(DAO dao) {
		
		this.dao = dao;
		
		model = dao.getModel();
		model.checkConsistency();
		
		parser = new CommandLineParser();
		
		// Lite variabler för kommandona att använda
		Command command = null;
		ChoiceToken choiceToken = null;
		
				
		// Kommandona börjar här
		
		// run
		// Kommando för att köra en instans utan att behöva skriva exec. 
				
		
		
		
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
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				quit(tokens);
			}});
		parser.add(command);
		
		command = new Command();
		command.add(new StringToken("quit"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				quit(tokens);
			}});
		parser.add(command);

		
	}
	

	protected void help(List<Token> tokens) {
		view.printControlScheme();
	}


	protected void quit(List<Token> tokens) {
		view.signalQuit();
	}


	public static void main(String args[]) throws IOException, ClassNotFoundException {
		
		Controller controller = null;
		
		controller = new Controller(new ObjectStreamDAO());
		
		
		View view = new CommandLineView(controller);
		controller.setView(view);
		
		view.run();
		
				
	}

	void setView(View view) {
				
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

	public List<Command> getControlScheme() {
				
		return parser.getCommandList();
		
	}
	

	public Model getModel() {
		return model;
	}
}
