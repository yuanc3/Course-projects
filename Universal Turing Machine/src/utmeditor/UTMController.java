package utmeditor;

/**
 * Defines the methods that a UTMController is required to implement.
 * @author Damian Arellanes
 */
public interface UTMController {
  
  /**
   * Loads a TM from a description file and renders it on the UTM GUI.
   * @param filePath A string representing the absolute path to the TM 
   * description file.
   */
  public void loadTuringMachineFrom(String filePath);
  
  /**
   * Graphically executes the loaded TM on a given input.
   * @param input A string representing the input for the loaded TM.
   */
  public void runUTM(String input);
}
