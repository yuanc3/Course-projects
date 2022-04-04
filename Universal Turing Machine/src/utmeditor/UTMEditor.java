package utmeditor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 * @author Damian Arellanes
 */
public class UTMEditor implements ActionListener {
  
  /** The window of the UTM editor. */
  private final JFrame window;
  /** The main panel on which all graphical elements will be rendered. */
  private final JPanel mainPanel;  
  /** The button for loading a TM from a description file. */
  private final JButton loadButton;  
  
  /** The panel for displaying the graphical components that are required for the execution of a TM. */
  private final JPanel executionPanel;
  /** The button for executing a loaded TM. */
  private final JButton runButton;
  /** The text field for introducing an input string. */
  private final JTextField input;
      
  /** The UTM controller. */
  private UTMController utmController;
  /** A flag that indicates whether a TM has been loaded or not. */
  private boolean isMachineLoaded;
  /** A flag that indicates whether a TM is already running or not. */
  private boolean isMachineRunning;
  
  /**
   * Creates the graphical components of the UTM editor.
   */
  public UTMEditor() {
    
    // Creates the chooser for TM description files
    loadButton = new JButton("Load TM from file");
    loadButton.setPreferredSize(new Dimension(200, 20));
    loadButton.addActionListener(this);
    
    // Creates the execution panel (run button and input string)
    executionPanel = new JPanel();
    runButton = new JButton("Run TM on Input");
    input = new JTextField("");
    input.setPreferredSize(new Dimension(120, 20));    
    executionPanel.setLayout(new FlowLayout());
    executionPanel.add(runButton);
    executionPanel.add(input);
    runButton.addActionListener(this);    
    
    // Sets the Main Panel
    mainPanel = new JPanel();
    mainPanel.setLayout(new FlowLayout());
    mainPanel.add(loadButton);
    mainPanel.add(executionPanel);    
    
    // Sets the Window
    window = new JFrame("UTM Editor");
    window.setContentPane(mainPanel);
    window.setSize(300,110);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setVisible(true);
    
    isMachineLoaded = false;
    isMachineRunning = false;
  }

  /**
   * Handles the activation of the load and execute buttons.
   * @param e The event containing all the information about the triggered action.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    
    // Validates whether a UTM controller has been registered
    if(utmController == null) {
      System.err.println("--- No UTMController has been registered ---");
      return;
    }
    
    // Validates whether a TM is under execution
    if(isMachineRunning) {
      System.err.println("A TM is already running. It needs to halt before "
          + "running or loading a new one.");
      return;
    }
        
    // Attempts to graphically run the TM with the given input and rules
    if(e.getSource() == runButton) {
      
      if(isMachineLoaded) runTM();
      else System.err.println("No TM has been loaded.");
    } 
    // Attempts to load a TM from a description file
    else if(e.getSource() == loadButton) {
      isMachineLoaded = true;
      loadTM();
    }        
  }
   
  /**
   * Opens a file chooser for a TM description file.
   */
  private void loadTM() {
    
    JFileChooser fileChooser = new JFileChooser();
    
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      
      utmController.loadTuringMachineFrom(
        fileChooser.getSelectedFile().getAbsolutePath()
      );
    }
  }
  
  /**
   * Runs a TM using the registered controller (each execution is done on a new
   * thread to avoid frozen GUIS).
   */
  private void runTM() {
    
    // Executes a TM on a new thread to avoid frozen GUIs
    new SwingWorker() {
      @Override
      protected Object doInBackground() throws Exception {
        
        isMachineRunning = true;
        utmController.runUTM(input.getText());
        isMachineRunning = false;
        return "done";
      }
    }.execute();
  }
  
  /**
   * Registers a new UTM controller with the UTM editor.
   * @param utmController The UTM controller to be registered.
   */
  public void setUTMController(UTMController utmController) {
    this.utmController = utmController;
  }
}
