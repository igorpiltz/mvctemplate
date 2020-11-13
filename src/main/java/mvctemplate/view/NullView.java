package mvctemplate.view;

import java.io.IOException;

import parallellroutineexecutor.controller.Controller;

public class NullView implements View {

	private Controller controller;
	private String stringReceived;

	public NullView(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() throws IOException {
		// TODO Auto-generated method stub
		throw new AssertionError();

	}

	@Override
	public void messageToUser(Object actualString) {
		// TODO Auto-generated method stub
		stringReceived = actualString.toString();
	}

	public String getStringReceived() {
		return stringReceived;
	}
	
	
	@Override
	public void signalQuit() {
		// TODO Auto-generated method stub
		throw new AssertionError();

	}

	@Override
	public String askQuestion(Object question) throws IOException {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public boolean askYesnoQuestion(String question) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printControlScheme() {
		// TODO Auto-generated method stub
		throw new AssertionError();

	}

}
