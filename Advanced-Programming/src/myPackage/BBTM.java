package myPackage;

import utm.*;

/**
 * Busy Beaver Turing Machine configuration manager.
 * 
 * @author Chao Yuan
 */
public class BBTM extends BaseTM {
	
	/**
	 * The constructor.
	 */
	public BBTM() {
		super();
		initialHeadPosition = 10;
	}
	
	@Override
	public void setInput(String input, UniversalTuringMachine utm) {utm.loadInput("00000000000000000000");} // default: all 0s
	
	@Override
	protected Move getHeadMoveDirection(String move) { return move.equals("RIGHT") ? MoveBBTM.RIGHT : MoveBBTM.LEFT;}
	
}
