package mvctemplate.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import parallellroutineexecutor.dao.DAO;
import parallellroutineexecutor.dao.ObjectStreamDAO;
import parallellroutineexecutor.model.DoIt;
import parallellroutineexecutor.model.Finish;
import parallellroutineexecutor.model.Instance;
import parallellroutineexecutor.model.Model;
import parallellroutineexecutor.model.Question;
import parallellroutineexecutor.model.Routine;
import parallellroutineexecutor.model.RoutineItem;
import parallellroutineexecutor.model.YesnoQuestion;
import parallellroutineexecutor.view.CommandLineView;
import parallellroutineexecutor.view.View;
import util.parser.Callable;
import util.parser.ChoiceToken;
import util.parser.Command;
import util.parser.CommandLineParser;
import util.parser.NumberToken;
import util.parser.RestOfLineToken;
import util.parser.StringToken;
import util.parser.Token;
import util.parser.UnknownCommandException;
import util.parser.UserInterruptException;

public class Controller {
	
	private CommandLineParser parser;
	private Model model;
	private View view;
	private Instance currentInstance;
	private DAO dao;
	
		
	public Controller(DAO dao) {
		
		this.dao = dao;
		
		model = dao.getModel();
		model.consistencyCheck();
		
		parser = new CommandLineParser();
		
		// Lite variabler för kommandona att använda
		Command command = null;
		ChoiceToken choiceToken = null;
		
				
		// Kommandona börjar här
		
		// run
		// Kommando för att köra en instans utan att behöva skriva exec. 
		command = new Command();
		command.add(new StringToken("run"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				runInstance(tokens);
			}});
		parser.add(command);
		
		// Add finish-kommandot
		// Token som avslutar instansen och nollställer currentInstance.
		// Flyttar även instansen till arkivet. 
		command = new Command();
		command.add(new StringToken("add"));
		command.add(new StringToken("finish"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addFinish(tokens);
			}});
		parser.add(command);

		
		// delete 1
		// Tar bort item 1 från currentInstance
		command = new Command();
		command.add(new StringToken("delete"));
		command.add(new NumberToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				deleteItem(tokens);
			}});
		parser.add(command);

		
		
		// add 2 to 1
		// Lägg till 2:an som nextItem till 1:an, fungerar inte för yesno
		command = new Command();
		command.add(new StringToken("add"));
		command.add(new NumberToken());
		command.add(new StringToken("to"));
		command.add(new NumberToken());
		choiceToken = new ChoiceToken();
		choiceToken.add("true");
		choiceToken.add("false");
		command.add(choiceToken);
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addConnectYesno(tokens);
			}});
		parser.add(command);
		
		
		// add 2 to 1
		// Lägg till 2:an som nextItem till 1:an, fungerar inte för yesno
		command = new Command();
		command.add(new StringToken("add"));
		command.add(new NumberToken());
		command.add(new StringToken("to"));
		command.add(new NumberToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addConnect(tokens);
			}});
		parser.add(command);
		
		
		// help
		// ett hjälp-kommando som printar alla kommandon en gång till
		command = new Command();
		command.add(new StringToken("help"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				help(tokens);
			}});
		parser.add(command);
		
		
		
		// add after yesno [true/false] [itemNumber] [question/yesno] [RestOfLine]
		// Kommando att sätta de olika nextItem efter en YesnoQuestion, en för Yes och en för No. 
		command = new Command();
		command.add(new StringToken("add"));
		command.add(new StringToken("after"));
		command.add(new StringToken("yesno"));
		choiceToken = new ChoiceToken();
		choiceToken.add("true");
		choiceToken.add("false");
		command.add(choiceToken);
		command.add(new NumberToken());
		choiceToken = new ChoiceToken(); 
		choiceToken.add("question");
		choiceToken.add("yesno");
		choiceToken.add("doit");
		command.add(choiceToken);
		command.add(new RestOfLineToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addAfterYesnoQuestion(tokens);
			}});
		parser.add(command);
		
		
		
		// Add after [Number] [question/yesno] [RestofLine]
		// Kommando att lägga till en item efter en annan
		command = new Command();
		command.add(new StringToken("add"));
		command.add(new StringToken("after"));
		command.add(new NumberToken());
		choiceToken = new ChoiceToken();
		choiceToken.add("question");
		choiceToken.add("yesno");
		choiceToken.add("doit");
		command.add(choiceToken);
		command.add(new RestOfLineToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addAfterQuestion(tokens);
			}});
		parser.add(command);
		
		
		
		// exec
		// execute the current item
		command = new Command();
		command.add(new StringToken("exec"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) throws UserInterruptException {
				executeCurrentItem(tokens);
			}});
		parser.add(command);		
		
		
		// jump [number]
		// kommando för att hoppa till en viss RoutineItem i en Instance
		command = new Command();
		command.add(new StringToken("jump"));
		command.add(new NumberToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				jumpItem(tokens);
			}});
		parser.add(command);		
		
		
		
		
		
		// print routine [number]
		command = new Command();
		command.add(new StringToken("print"));
		command.add(new StringToken("routine"));
		command.add(new NumberToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				printRoutine(tokens);
			}});
		parser.add(command);
		
		// Add question-kommandot
		command = new Command();
		command.add(new StringToken("add"));
		choiceToken = new ChoiceToken();
		choiceToken.add("question");
		choiceToken.add("yesno");
		choiceToken.add("doit");
		command.add(choiceToken);
		command.add(new RestOfLineToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				addQuestion(tokens);
			}});
		parser.add(command);
		
		
		
		
		// Ny rutin-kommandot
		command = new Command();
		command.add(new StringToken("new"));
		command.add(new StringToken("routine"));
		command.add(new RestOfLineToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				newRoutine(tokens);
			}});
		parser.add(command);
		
		
		// starta rutin-kommandot
		command = new Command();
		command.add(new StringToken("start"));
		command.add(new StringToken("routine"));
		command.add(new NumberToken());
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				startRoutine(tokens);
			}});
		parser.add(command);

		
		// Nästa instans av en rutin som exekverar, oklart ännu vilken. 
		// vi tar väl i nummerordning bara till att börja med. 
		command = new Command();
		command.add(new StringToken("next"));
		command.add(new StringToken("instance"));
		command.addCallable(new Callable() {
			public void exec(List<Token> tokens) {
				nextInstance(tokens);
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
	
	protected void runInstance(List<Token> tokens) {
		
		do {
			
			try {
				executeCurrentItem(null);
			} catch (UserInterruptException e) {
				break;
			}
			view.messageToUser(getState());		
			
			try {
				dao.persist();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} while(true);
				
	}

	protected void addFinish(List<Token> tokens) {
				
		if (currentInstance == null) {
			view.messageToUser("Set the current instance first");
			return;
		}
		
				
		currentInstance.getRoutine().add(new Finish());
				
		
	}

	protected void deleteItem(List<Token> tokens) {
		// delete 1
		if (currentInstance == null) {
			view.messageToUser("Set current instance first");
			return;
		}
		
		int itemIndex = ((NumberToken)tokens.get(1)).getNumber() - 1;
		
		RoutineItem item = currentInstance.getRoutine().getRoutineItems().get(itemIndex);
		
		List<RoutineItem> parentList = item.getParentList();
		for (int index = 0; index < parentList.size(); index++) {
			parentList.get(index).deleteChild(item);
		}
		
		
		currentInstance.getRoutine().getRoutineItems().remove(itemIndex);
	}

	protected void addConnectYesno(List<Token> tokens) {
		// add 2 to 1 true
		
		if (currentInstance == null) {
			view.messageToUser("Set current instance first");
			return;
		}
		
		int tokenToAddIndex = ((NumberToken)tokens.get(1)).getNumber() - 1;
		int tokenReceivingIndex = ((NumberToken)tokens.get(3)).getNumber() - 1;
		
		YesnoQuestion receivingItem = (YesnoQuestion)currentInstance.getRoutine().getRoutineItems().get(tokenReceivingIndex);
		
		if (!receivingItem.getClass().equals(YesnoQuestion.class)) {
			view.messageToUser("Only YesnoQuestion can be connected by this command");
			return;
		}
		
		RoutineItem toAddItem = currentInstance.getRoutine().getRoutineItems().get(tokenToAddIndex);
		
		if (tokens.get(4).getParsedString().equals("true"))
			receivingItem.setNextItem(true, toAddItem);
		else receivingItem.setNextItem(false, toAddItem);
		
	}

	protected void addConnect(List<Token> tokens) {
		// add 2 to 1
		
		if (currentInstance == null) {
			view.messageToUser("Set current instance first");
			return;
		}
		
		int tokenToAddIndex = ((NumberToken)tokens.get(1)).getNumber() - 1;
		int tokenReceivingIndex = ((NumberToken)tokens.get(3)).getNumber() - 1;
		
		RoutineItem receivingItem = currentInstance.getRoutine().getRoutineItems().get(tokenReceivingIndex);
		
		if (receivingItem.getClass().equals(YesnoQuestion.class)) {
			view.messageToUser("YesnoQuestion can not be connected by this command");
			return;
		}
		
		RoutineItem toAddItem = currentInstance.getRoutine().getRoutineItems().get(tokenToAddIndex);
		receivingItem.setNextItem(toAddItem);
				
	}

	protected void help(List<Token> tokens) {
		view.printControlScheme();
	}

	protected void addAfterYesnoQuestion(List<Token> tokens) {
		// add after yesno [true/false] [itemNumber] [question/yesno] [RestOfLine]
		if (currentInstance == null) {
			view.messageToUser("Set current instance first");
			return;
		}
			
		Routine routine = currentInstance.getRoutine();
		
		int itemIndex = ((NumberToken)tokens.get(4)).getNumber() - 1;
		
		YesnoQuestion yesno = (YesnoQuestion)routine.getRoutineItems().get(itemIndex);
		
		RoutineItem newItem = null; 
		if (tokens.get(5).getParsedString().equals("question"))
			newItem = new Question(tokens.get(6).getParsedString());
		if (tokens.get(5).getParsedString().equals("yesno"))
			newItem = new YesnoQuestion(tokens.get(6).getParsedString());
		if (tokens.get(5).getParsedString().equals("doit"))
			newItem = new DoIt(tokens.get(6).getParsedString());
		
		if (newItem == null)
			throw new AssertionError("Forgot to implement a question");
		
		
		if (tokens.get(3).getParsedString().equals("true"))
			yesno.setNextItem(true, newItem);
		if (tokens.get(3).getParsedString().equals("false"))
			yesno.setNextItem(false, newItem);
		
		routine.add(newItem);
		
	}

	protected void addAfterQuestion(List<Token> tokens) {
		// Add after [Number] [question/yesno] [RestofLine]
		Routine routine = currentInstance.getRoutine();
		RoutineItem item = routine.getRoutineItems().get(((NumberToken)tokens.get(2)).getNumber() - 1);
		
		RoutineItem newItem = null; 
		if (tokens.get(3).getParsedString().equals("question"))
			newItem = new Question(tokens.get(4).getParsedString());
		if (tokens.get(3).getParsedString().equals("yesno"))
			newItem = new YesnoQuestion(tokens.get(4).getParsedString());
		if (tokens.get(3).getParsedString().equals("doit"))
			newItem = new DoIt(tokens.get(4).getParsedString());
		
		if (newItem == null)
			throw new AssertionError("Forgot to implement a question");
		
		item.setNextItem(newItem);
		routine.add(newItem);
	}

	
	protected void executeCurrentItem(List<Token> tokens) throws UserInterruptException {
		
		RoutineItem item = currentInstance.getCurrentItem();
		
		if (item.getClass().equals(Question.class)) {
			Question question = (Question)item;
			String answer;
			try {
				answer = view.askQuestion(question.getQuestion());
			} catch (IOException e) {
				throw new RuntimeException(e);				
			}
			
			currentInstance.setVariable(question.getQuestion(), answer);
			currentInstance.setCurrentItem(item.getNextItem());
			return;
		}
		
		if (item.getClass().equals(YesnoQuestion.class)) {
			YesnoQuestion question = (YesnoQuestion)item;
			boolean answer;
			try {
				answer = view.askYesnoQuestion(question.getQuestion());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			question.setAnswer(answer);
			currentInstance.setCurrentItem(item.getNextItem());
			return;
		}
		
		if (item.getClass().equals(DoIt.class)) {
			DoIt doit = (DoIt)item;
			boolean answer;
			try {
				answer = view.askYesnoQuestion("Har du gjort \"" + doit.getToDo() + "\" än?");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			if (answer)
				currentInstance.setVariable(doit.getToDo(), "Gjord!");
			currentInstance.setCurrentItem(item.getNextItem());
			return;
		}
		
		if (item.getClass().equals(Finish.class)) {
			currentInstance.setVariable("Done", format.format(new Date()));
			
			model.markInstanceFinished(currentInstance);
			
			currentInstance = null;
			return;
		}
		
		
		// Om vi glömmer att implementera någon underklass till RoutineItem. 
		throw new AssertionError();
		
	}

	protected void jumpItem(List<Token> tokens) {
		int itemIndex = ((NumberToken)tokens.get(1)).getNumber() - 1;
		if (currentInstance != null)
			currentInstance.setCurrentItem(currentInstance.getRoutine().getRoutineItems().get(itemIndex));
		else view.messageToUser("Set the current instance");
		
	}

	protected void printRoutine(List<Token> tokens) {
		int routineIndex = ((NumberToken)tokens.get(2)).getNumber() - 1;
		List<RoutineItem> items = model.getRoutines().get(routineIndex).getRoutineItems();
		StringBuffer buf = new StringBuffer();
		buf.append("Routine \"" + model.getRoutines().get(routineIndex) + "\"");
		buf.append("\n");
		
		for (int index = 0; index < items.size(); index++) {
			buf.append((index + 1) + ". ");
			buf.append(items.get(index));
			buf.append("\n");
		}
		
		view.messageToUser(buf.toString());
				
	}

	protected void addQuestion(List<Token> tokens) {
		String question = tokens.get(2).getParsedString();
		if (currentInstance == null) {
			view.messageToUser("Set the current instance first");
			return;
		}
		
		RoutineItem item = null;
		
		if (tokens.get(1).getParsedString().equals("question"))
			item = new Question(question);
		if (tokens.get(1).getParsedString().equals("yesno"))
			item = new YesnoQuestion(question);
		if (tokens.get(1).getParsedString().equals("doit"))
			item = new DoIt(question);
		
		if (item == null)
			throw new AssertionError("forgot to implement something");
		
		currentInstance.getRoutine().add(item);	
		 
	}

	protected void quit(List<Token> tokens) {
		view.signalQuit();
	}

	protected void nextInstance(List<Token> tokens) {
		if (currentInstance != null) {
			int index = model.getInstances().indexOf(currentInstance);
			index++;
			if (index == model.getInstances().size())
				index = 0;
			currentInstance = model.getInstances().get(index);
		
		} else {
			if (model.getInstances().size() > 0)
				currentInstance = model.getInstances().get(0);
		}		
	}

	protected void startRoutine(List<Token> tokens) {
				
		int routineNumber = ((NumberToken)tokens.get(2)).getNumber() - 1; 
				
		if (routineNumber >= model.getRoutines().size()) {
			view.messageToUser("Wrong routine number");
			return;
		}
		
		Instance instance = new Instance(model.getRoutines().get(routineNumber));
		model.add(instance);
		currentInstance = instance;
		
		
		
	}

	protected void newRoutine(List<Token> tokens) {
		
		model.addRoutine(new Routine(tokens.get(2).getParsedString()));
		
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
		
		buf.append("**** Current Instance ****\n");
		buf.append(currentInstance);
		buf.append("\n");
		
		// Skriv ut variablerna för currentInstance
		if (currentInstance != null) {
			Map<String, String> variables = currentInstance.getVariables();
			Iterator<String> it = variables.keySet().iterator(); 

			for (; it.hasNext();) {
				String question = it.next();
				String answer = variables.get(question);
				buf.append("\"" + question + "\" : \"" + answer + "\"\n");
			}
			
			buf.append("**** Current Item ****\n");
			if ((currentInstance == null) || (currentInstance.getCurrentItem() == null))
				buf.append("null");
			else buf.append(currentInstance.getCurrentItem());
			buf.append("\n");
		}
		
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
