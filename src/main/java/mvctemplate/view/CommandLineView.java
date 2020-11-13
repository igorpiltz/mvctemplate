package mvctemplate.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import mvctemplate.controller.Controller;
import util.parser.Command;
import util.parser.UnknownCommandException;
import util.parser.UserInterruptException;

public class CommandLineView implements View {
	
		
	private Controller controller;
	private boolean timeToQuit = false;
	private BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));

	public CommandLineView(Controller controller) {
		this.controller = controller;		
	}

	public void run() throws IOException {
		
		// Test kod
		/*
		 * try { controller.processInput("new routine ettan");
		 * controller.processInput("new routine tvaan");
		 * controller.processInput("start routine 1");
		 * controller.processInput("add question Smurfar smurfar?");
		 * controller.processInput("print routine 1");
		 * 
		 * } catch (UnknownCommand e) { System.out.println("Unknown Command: \"" +
		 * e.getMessage() + "\"");
		 * 
		 * }
		 */		
		// Slut på testkod
		
		
		
		System.out.println("ParallellRoutineExecutor");
		printControlScheme();
		System.out.println(controller.getState().toString());
		
		
				
		while(true) {
								
			
			String input = buf.readLine();
			
			try {
				controller.processInput(input);
			} catch (UnknownCommandException e) {
				System.out.println("Unknown Command: \"" + e.getMessage() + "\"");
			} catch (UserInterruptException e) {
				System.out.println("User interrupt");
			}
						
			
			if (timeToQuit)
				break;
			
			System.out.println(controller.getState().toString());
			
		}
		
		
	}
	
	
	@Override
	public void printControlScheme() {
		
		List<Command> list = controller.getControlScheme();
		for (int index = 0; index < list.size(); index++)
			System.out.println(list.get(index));
		
		
	}

	@Override
	public void messageToUser(Object actualString) {
		System.out.println(actualString);
	}

	@Override
	public void signalQuit() {
		timeToQuit = true;
	}

	@Override
	public String askQuestion(Object question) throws IOException, UserInterruptException {
		System.out.println(question);
		String answer = buf.readLine();
		
		if (answer == null)
			throw new IOException("EOF");
		
		if (answer.equalsIgnoreCase("abort"))
			throw new UserInterruptException();
				
		return answer;
	}

	@Override
	public boolean askYesnoQuestion(String question) throws IOException, UserInterruptException {
		do {
			System.out.println(question);
			String answer = buf.readLine();
			
			if (answer == null)
				throw new IOException("EOF");
			
			if (answer.equalsIgnoreCase("abort"))
				throw new UserInterruptException();
							
			if (answer.equalsIgnoreCase("yes"))
				return true;
			if (answer.equalsIgnoreCase("y"))
				return true;
			if (answer.equalsIgnoreCase("ja"))
				return true;
			if (answer.equalsIgnoreCase("j"))
				return true;
			if (answer.equalsIgnoreCase("No"))
				return false;
			if (answer.equalsIgnoreCase("N"))
				return false;
			if (answer.equalsIgnoreCase("nej"))
				return false;
		} while(true);
		
	}
	
}
