package myPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utm.*;
import utmeditor.*;

/**
 * A UTM controller.
 * 
 * @author Chao Yuan
 */
public class MyUTMController implements UTMController {
	/** The only Universal Turing Machine that executes TMs. */
	private UniversalTuringMachine utm;
	/** The Turing Machine. */
	private String variant;
	/** The base TM configuration manager. */
	private BaseTM basetm;
	/** If use animation. */
	private boolean animation;
	
	/**
	 * The constructor.
	 */
	public MyUTMController() {
		utm = new UniversalTuringMachine();
		animation = false;
	}
	
	/**
	 * Set the animation.
	 * 
	 * @param animation   The boolean value of animation.
	 */
	public void setAnimation(boolean animation) {this.animation = animation;}
	
	/**
	 * Display the window of UTM.
	 */
	public void displayWindow() { utm.display();}
	
	@Override
	public void loadTuringMachineFrom(String filePath) {
		String line = "";
		String initialState = "", acceptState = "", rejectState = "", rulesLine = "";
		// Parse a TM description file
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));		
			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.charAt(0) == '#') continue;
				String[] arr = line.split("=");
				switch (arr[0]) {
					case "variant":
						variant = arr[1];
						break;
					case "initialState":
						initialState = arr[1];
						break;
					case "acceptState":
						acceptState = arr[1];
						break;
					case "rejectState":
						rejectState = arr[1];
						break;
					case "rules":
						rulesLine = arr[1];
						break;
					default:
						break;
				}
			}
			br.close();
		}
		catch (Exception ex) {
			System.err.println(ex);
		}
		
		if(variant.equals("CLASSICAL"))  basetm= new ClassicalTM();
		else if(variant.equals("LEFT_RESET")) basetm = new LRTM();
		else basetm = new BBTM();
		// Parse rules
		String[] rules = rulesLine.split("<>");
		TuringMachine tm = new TuringMachine(rules.length, initialState, acceptState, rejectState);
		// sort the rules
		Arrays.sort(rules, (a, b) -> {
			return a.compareTo(b);
		});
		basetm.addRulesToTM(tm, rules);
		utm.loadTuringMachine(tm);
	}
	
	@Override
	public void runUTM(String input) {
		basetm.setInput(input, utm);
		int initialHeadPosition = basetm.getInitialHeadPosition();
		for(int i=0;i<initialHeadPosition-1;i++)
			utm.moveHead(MoveClassical.RIGHT, false); // move head to the initial cell 10.
		// Implement the steps for UTM
		String current_state = utm.getTuringMachine().getInitialState();
		String accept_state = utm.getTuringMachine().getAcceptState();
		String reject_state = utm.getTuringMachine().getRejectState();
		Tape tape = utm.getTuringMachine().getTape();
		Head head = utm.getTuringMachine().getHead();
		HashMap<String, ArrayList<String[]>> map = basetm.getRules();
		while (!current_state.equals(accept_state) && !current_state.equals(reject_state)) {
			ArrayList<String[]> ruleList = map.get(current_state);
			for (String[] rule : ruleList) {
				if (rule[1].charAt(0) == tape.get(head.getCurrentCell())) {
					utm.writeOnCurrentCell(rule[3].charAt(0));
					basetm.moveHead(rule[4], utm, animation);
					current_state = rule[2];
					if (animation) utm.updateHeadState(current_state);
					break;
				}
			}
		}
		utm.getTuringMachine().setTape(tape);
		boolean accepted = current_state.equals(accept_state);
		if (animation) utm.displayHaltState(accepted? HaltState.ACCEPTED : HaltState.REJECTED);
		else System.out.println(accepted? "ACCEPTED" : "REJECTED");
	}
	
	/**
	 * Main method to execute.
	 * 
	 * @param args args[0]: desc_absPath; args[1]: input; args[2]: [--animation/--noanimation]; No args: Show UI for configurations.
	 */
	public static void main(String[] args) {
		MyUTMController utmController = new MyUTMController();
		if (args.length == 0) {
			utmController.setAnimation(true);
			utmController.displayWindow();
			UTMEditor utmEditor = new UTMEditor();
			utmEditor.setUTMController(utmController);	
		}else {
			if (args.length != 3) {
				System.out.println("Usage: java -jar practical1-ID.jar \"desc_absPath\" "
						+ "\"input\" [--animation/--noanimation]");
				System.exit(0);
			}		
			if (args[2].equals("--animation")) {
				utmController.setAnimation(true);
				utmController.displayWindow();
			}		
			utmController.loadTuringMachineFrom(args[0]);
			utmController.runUTM(args[1]);
		}
	}
}
