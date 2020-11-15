package mvctemplate.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mvctemplate.model.Model;
import util.parser.Command;
import util.parser.UserInterruptException;

public class CommandLineView implements View {
	
	private BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
	private List<Command> possibleCommands = new ArrayList<Command>();

	
	
	
	@Override
	public void printControlScheme(List<Command> list) {
		for (int index = 0; index < list.size(); index++)
			System.out.println(list.get(index));
	}

	@Override
	public void messageToUser(Object actualString) {
		System.out.println(actualString);
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




	@Override
	public void startMessage() {
	
		System.out.println("CommandLineView");
		
	}




	@Override
	public void addChoice(Command command) {
		possibleCommands.add(command);
		
	}




	@Override
	public void showChoices() {
		// TODO Auto-generated method stub
		for (int index = 0; index < possibleCommands.size(); index++)
			System.out.println(possibleCommands.get(index));
	
	}




	@Override
	public void showStatus(Model model) {
		System.out.println(model);
	}
	
}
