package ui;

import game.Sortable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Score implements Sortable{
	private final StringProperty username;
	private final IntegerProperty score;
	private final StringProperty date;
	private final IntegerProperty round;
	private final StringProperty difficulty;
	private final StringProperty clone;
	private double sortValue;

	public Score(String u, int s, String da, int r, String di, String c){
		username = new SimpleStringProperty(u);
		score = new SimpleIntegerProperty(s);
		date = new SimpleStringProperty(da);
		round = new SimpleIntegerProperty(r);
		difficulty = new SimpleStringProperty(di);
		clone = new SimpleStringProperty(c);
	}

	public String getUsername(){
		return username.get();
	}

	public int getScore(){
		return score.get();
	}

	public String getDate(){
		return date.get();
	}

	public int getRound(){
		return round.get();
	}

	public String getDifficulty(){
		return difficulty.get();
	}

	public String getClone(){
		return clone.get();
	}

	@Override
	public double getValue() {
		return sortValue;
	}

	@Override
	public void setValue(String value) {
		if(value.equals("score")){
			sortValue = score.getValue();
		} else if (value.equals("round")){
			sortValue = round.getValue();
		}
	}
}