package mvctemplate.view;

import java.io.IOException;
import java.util.List;

import mvctemplate.model.Model;
import util.parser.Command;

public class NullView implements View {


	private String stringReceived;
	

	

	@Override
	public void messageToUser(Object actualString) {
		stringReceived = actualString.toString();
	}

	public String getStringReceived() {
		return stringReceived;
	}
		
	

	@Override
	public String askQuestion(Object question) throws IOException {
			
		return null;
	}

	@Override
	public boolean askYesnoQuestion(String question) throws IOException {
	
		return false;
	}

	

	@Override
	public void startMessage() {
				
	}

	@Override
	public void addChoice(Command command) {
		
	}

	@Override
	public String showChoices() {
		return null;
		
	}

	@Override
	public void showStatus(Model model) {
				
	}

	@Override
	public void printControlScheme(List<Command> commandList) {
		
		
	}

	@Override
	public void initialize() {
		
		
	}

}
