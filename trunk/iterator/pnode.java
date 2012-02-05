package iterator; 

import global.*;
import bufmgr.*;
import diskmgr.*;
import tripleheap.*;

/**
 * A structure describing a triple.
 * include a run number and the triple
 */
public class pnode {
  /** which run does this triple belong */
  public int     run_num;

  /** the triple reference */
  public Triple   triple;

  /**
   * class constructor, sets <code>run_num</code> to 0 and <code>triple</code>
   * to null.
   */
  public pnode() 
  {
    run_num = 0;  // this may need to be changed
    triple = null; 
  }
  
  /**
   * class constructor, sets <code>run_num</code> and <code>triple</code>.
   * @param runNum the run number
   * @param t      the triple
   */
  public pnode(int runNum, Triple t) 
  {
    run_num = runNum;
    triple = t;
  }
  
}

