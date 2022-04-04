package myPackage;

import java.util.ArrayList;
import java.util.HashMap;
import utm.*;

/**
 * Base Turing Machine configuration manager.
 * 
 * @author Chao Yuan
 */
public class BaseTM {
	/** Use HashMap to accelerate searching rules. */
	private HashMap<String, ArrayList<String[]>> map;
	/** The initial head position. */
	protected int initialHeadPosition;
	
	/**
	 * The constructor.
	 */
	public BaseTM() {
		map = new HashMap<String, ArrayList<String[]>>();
	}
	
	/**
	 * Get the rules of tm.
	 * 
	 * @return The rules stored in hashmap.
	 */
	public HashMap<String, ArrayList<String[]>> getRules() {return map;}
	
	/**
	 * Get the initial head position of tm.
	 * 
	 * @return initialHeadPosition.
	 */
	public int getInitialHeadPosition() {return initialHeadPosition;}
	
	/**
	 * Set the input into Universal Turing Machine.
	 * Classical, Left-Reset TM: input; Busy Beaver TM: all 0s(default)
	 * 
	 * @param input  The string input. (e.g. "0011")
	 * @param utm    The Universal Turing Machine need to be loaded the input.
	 */
	public void setInput(String input, UniversalTuringMachine utm) {utm.loadInput(input);}
	
	/**
	 * Get the move direction from string.
	 * 
	 * @param move   The move direction.
	 * @return The Move class.
	 */
	protected Move getHeadMoveDirection(String move) { return move.equals("RIGHT") ? MoveClassical.RIGHT : MoveClassical.LEFT;}
	
	/**
	 * The method to add rules into TM.
	 * 
	 * @param tm    The Turing Machine
	 * @param rules The rules of the TM, which structure is like "q0,0,q1,1,RIGHT"
	 */
	public void addRulesToTM(TuringMachine tm, String[] rules) {
		for (String rule : rules) {
			String[] s = rule.split(",");
			tm.addRule(s[0], s[1].charAt(0), s[2], s[3].charAt(0), getHeadMoveDirection(s[4]));
			// Store rules in HashMap
			ArrayList<String[]> ruleList = map.getOrDefault(s[0], new ArrayList<String[]>());
			ruleList.add(s);
			map.put(s[0], ruleList);
		}
	}
	
	/**
	 * Move head to right or left (default).
	 * 
	 * @param move  The direction that the head to move
	 */
	protected void moveHead(String move, UniversalTuringMachine utm, boolean animation) {
		if(move.equals("LEFT")) utm.moveHead(MoveClassical.LEFT, animation); // move left
		else utm.moveHead(MoveClassical.RIGHT, animation); // move right
	}
}
