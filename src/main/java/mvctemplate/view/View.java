package mvctemplate.view;

import java.io.IOException;
import java.util.List;

import mvctemplate.model.Model;
import util.parser.Command;
import util.parser.UserInterruptException;

public interface View {

	void messageToUser(Object actualString);

	String askQuestion(Object question) throws IOException, UserInterruptException;

	boolean askYesnoQuestion(String question) throws IOException, UserInterruptException;

	void startMessage();

	void addChoice(Command command);

	void showChoices();

	void showStatus(Model model);

	void printControlScheme(List<Command> commandList);

}
