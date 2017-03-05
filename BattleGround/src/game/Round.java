package game;

public class Round {
	int currentRound = 1;
	int difficulty;
	String difficultyS;

	/**
	 * @param dS
	 */
	public Round(String dS){
		difficultyS = dS;
		if(dS.equals("Youngling")){
			difficulty = 1;
		} else if(dS.equals("Padawan")){
			difficulty = 2;
		} else if(dS.equals("Jedi Knight")){
			difficulty = 4;
		} else if(dS.equals("Jedi Master")){
			difficulty = 8;
		} else if(dS.equals("Sith Lord")){
			difficulty = 16;
		} else if(dS.equals("Emperor")){
			difficulty = 32;
		}
	}

	/**
	 * Increment the current round
	 */
	public void increaseRound(){
		currentRound++;
	}

	/**
	 * Returns the current round
	 *
	 * @return
	 */
	public int getRound(){
		return currentRound;
	}

	/**
	 * Returns the difficulty
	 *
	 * @return
	 */
	public int getDifficulty(){
		return difficulty;
	}

	/**
	 * Returns the difficulty name
	 *
	 * @return
	 */
	public String getDifficultyS(){
		return difficultyS;
	}

	/**
	 * Returns the number of droids to spawn in the current round
	 *
	 * @return
	 */
	public int calculateDroids(){
		int droids;
		droids = difficulty*currentRound + 10;
		return droids;
	}
}
