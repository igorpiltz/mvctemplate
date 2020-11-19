package mvctemplate.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Model implements java.io.Serializable {
	
	/**
	 * Serialiserad data är:
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private mvctemplate.model.Game currentGame;
	private List<Game> oldGames = new ArrayList<Game>();
	

	/** Function called by the controller to check consistency when updating the model.
	 * 
	 */
	public void checkConsistency() {
	}
	
	public String getState() {
		return toString();
	}


	public Game getCurrentGame() {
		return currentGame;
	}

	public void startGame(Game game) {
		if (currentGame != null)
			throw new IllegalStateException();
		currentGame = game;
	}
	
	public void endCurrentGame() {
		if (currentGame== null)
			throw new IllegalStateException();
		currentGame.endGame();
		oldGames .add(currentGame);
		currentGame = null;
	}
	

	public List<Game> getOldGames() {
		return oldGames;
	}

	public void setOldGames(List<Game> list) {
		oldGames = list;
	}

}
