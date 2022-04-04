package myPackage;

import utm.Move;

/**
 * Head movements for a Left-Reset Turing Machine.
 * @author Chao Yuan
 */
public enum MoveLRTM implements Move{
  
  /** The right movement of a LRTM. */
  RIGHT, 
  
  /** The RESET movement of a LRTM. */
  RESET;
}