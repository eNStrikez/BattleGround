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

	/**
	 * @param u
	 * @param s
	 * @param da
	 * @param r
	 * @param di
	 * @param c
	 */
	public Score(String u, int s, String da, int r, String di, String c){
		username = new SimpleStringProperty(u);
		score = new SimpleIntegerProperty(s);
		date = new SimpleStringProperty(da);
		round = new SimpleIntegerProperty(r);
		difficulty = new SimpleStringProperty(di);
		clone = new SimpleStringProperty(c);
	}

	/**
	 * Returns the username
	 * 
	 * @return
	 */
	public String getUsername(){
		return username.get();
	}
	/**
	 * Returns the score
	 * 
	 * @return
	 */
	public int getScore(){
		return score.get();
	}
	/**
	 * Returns the date
	 * 
	 * @return
	 */
	public String getDate(){
		return date.get();
	}
	/**
	 * Returns the round
	 * 
	 * @return
	 */
	public int getRound(){
		return round.get();
	}
	/**
	 * Returns the difficulty
	 * 
	 * @return
	 */
	public String getDifficulty(){
		return difficulty.get();
	}
	/**
	 * Returns the clone
	 * 
	 * @return
	 */
	public String getClone(){
		return clone.get();
	}

	/* (non-Javadoc)
	 * @see game.Sortable#getValue()
	 */
	@Override
	public double getValue() {
		return sortValue;
	}

	/* (non-Javadoc)
	 * @see game.Sortable#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		if(value.equals("score")){
			sortValue = score.getValue();
		} else if (value.equals("round")){
			sortValue = round.getValue();
		}
	}
}