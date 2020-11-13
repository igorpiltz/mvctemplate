package mvctemplate.view;

import java.io.IOException;

import util.parser.UserInterruptException;

public interface View {

	void run() throws IOException;

	void messageToUser(Object actualString);

	void signalQuit();

	String askQuestion(Object question) throws IOException, UserInterruptException;

	boolean askYesnoQuestion(String question) throws IOException, UserInterruptException;

	void printControlScheme();

}
