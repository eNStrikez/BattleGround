package game;

public class Round {
	int currentRound = 1;
	int difficulty;

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

	public void increaseRound(){
		currentRound++;
	}

	public int calculateDroids(){
		int droids;

		droids = difficulty*currentRound + 10;
		return droids;
	}
}
