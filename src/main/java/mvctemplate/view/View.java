package mvctemplate.view;

import java.io.IOException;
import java.util.List;

import mvctemplate.model.Model;
import util.parser.Command;
import util.parser.UserInterruptException;

/**
 * An abstract View of the data. There are two basic models for interacting with the view. 
 * The first model is messageToUser followed by askQuestion. This is inspired by the commandline
 * way of interacting with a program. You output the state with messageToUser and asks the user
 * to input a command with askQuestion. 
 *   
 * The Second model addChoice - showChoices.
 * The second model restricts the complete freedom of the commandline by introducing choices.
 * When the program is in a certain state only certain commands will make sense. addChoice will
 * add those to the View:s internal list. ShowChoices will show those choices in the internal list 
 * to the user and ask the user to make a choice between them. The View will return a command in form
 * of a string for the Controller to interpret.  	
 * 
 * @author igor
 *
 */


public interface View {

	/**
	 * Outputs a message to the user. 
	 * 
	 * @param actualString
	 */
	void messageToUser(Object actualString);

	/**
	 * Outputs a message to the user and asks him for input in response.
	 * 
	 * @param question
	 */
	String askQuestion(Object question) throws IOException, UserInterruptException;

	
	
	boolean askYesnoQuestion(String question) throws IOException, UserInterruptException;

	/**
	 * Inform the user that the program has started.
	 * 
	 *   Must not be implemented. 
	 */
	void startMessage();

	void addChoice(Command command);

	String showChoices() throws IOException;

	void showStatus(Model model);

	
	void printControlScheme(List<Command> commandList);

	/**
	 * Things that must happen after constructor execution goes here. 
	 */
	void initialize();

}
