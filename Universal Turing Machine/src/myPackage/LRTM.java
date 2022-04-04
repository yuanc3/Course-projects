package myPackage;

import utm.*;

/**
 * Left-Reset Turing Machine configuration manager.
 * 
 * @author Chao Yuan
 */
public class LRTM extends BaseTM {
	/**
	 * The constructor.
	 */
	public LRTM() {
		super();
		initialHeadPosition = 0;
	}

	@Override
	protected Move getHeadMoveDirection(String move) { return move.equals("RIGHT") ? MoveLRTM.RIGHT : MoveLRTM.RESET;}
	
	/**
	 * Move head to right or reset.
	 * 
	 * @param move  The way that the head to move
	 */
	@Override
	protected void moveHead(String move, UniversalTuringMachine utm, boolean animation) {
		if(move.equals("RIGHT")) utm.moveHead(MoveClassical.RIGHT, animation); // move left
		else { 
			if (animation) {
				try { 
					Thread.sleep(Config.DELAY); // wait for playing animation
				} 
				catch(InterruptedException ex){ 
					System.err.println(ex);
				}
			}
			utm.getTuringMachine().getHead().reset();
		}
	}
}
