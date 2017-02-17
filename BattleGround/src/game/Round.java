package game;

public class Round {
	int currentRound = 1;
	int difficulty;

	/**
	 * @param difficultyS
	 */
	public Round(String difficultyS){
		if(difficultyS.equals("Youngling")){
			difficulty = 1;
		} else if(difficultyS.equals("Padawan")){
			difficulty = 2;
		} else if(difficultyS.equals("Jedi Knight")){
			difficulty = 4;
		} else if(difficultyS.equals("Jedi Master")){
			difficulty = 8;
		} else if(difficultyS.equals("Sith Lord")){
			difficulty = 16;
		} else if(difficultyS.equals("Emperor")){
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
