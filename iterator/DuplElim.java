package iterator;

import tripleheap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import index.*;

import java.lang.*;
import java.io.*;

/**
 *Eleminate the duplicate tuples from the input relation
 */
public class DuplElim extends TripleIterator
{
  private AttrType[] _in;     // memory for array allocated by constructor
  private short       in_len;
  private short[]    str_lens;
  
  private TripleIterator _am;
  private boolean      done;
  
  private AttrType  sortFldType;
  private int       sortFldLen;
  private Triple    Jtriple;
  
  private Triple TempTriple1, TempTriple2;
  
  /**
   *Constructor to set up some information.
   *@param in[]  Array containing field types of R.
   *@param len_in # of columns in R.
   *@param s_sizes[] store the length of string appeared in triple
   *@param am input relation iterator, access method for left input to join,
   *@param amt_of_mem the page numbers required IN PAGES
   *@exception IOException some I/O fault
   *@exception DuplElimException the exception from DuplElim.java
   */
  public DuplElim(
		  AttrType in[],         
		  short      len_in,     
		  short    s_sizes[],
		  TripleIterator am,          
		  int       amt_of_mem,  
		  boolean     inp_sorted
		  )throws IOException ,DuplElimException
    {
      _in = new AttrType[in.length];
      System.arraycopy(in,0,_in,0,in.length);
      in_len = len_in;
     
      Jtriple =  new Triple();
    /*  try {
	Jtriple.setHdr(len_in, _in, s_sizes);
      }catch (Exception e){
	throw new DuplelimException(e, "setHdr() failed");
      }
    */ 
      sortFldType = in[0];
      switch (sortFldType.attrType)
	{
	case AttrType.attrInteger:
	  sortFldLen = 4;
	  break;
	case AttrType.attrReal:
	  sortFldLen = 4;
	  break;
	case AttrType.attrString:
	  sortFldLen = s_sizes[0];
	  break;
	default:
	  //error("Unknown type");
	  return;
	}
      
      _am = am;
      TripleOrder order = new TripleOrder(TripleOrder.Ascending);
      if (!inp_sorted)
	{
	  try {
	    _am = new Sort(in, len_in, s_sizes, am, 1, order,
			   sortFldLen, amt_of_mem);
	  }catch(SortException e){
	    e.printStackTrace();
	    throw new DuplElimException(e, "SortException is caught by DuplElim.java");
	  }
	}

      // Allocate memory for the temporary tuples
      TempTriple1 =  new Triple();
      TempTriple2 = new Triple();
    /*  try{
	TempTriple1.setHdr(in_len, _in, s_sizes);
	TempTriple2.setHdr(in_len, _in, s_sizes);
      }catch (Exception e){
	throw new DuplElimException(e, "setHdr() failed");
      }
    */  done = false;
    }

  /**
   * The triple is returned.
   *@return call this function to get the triple
   *@exception JoinsException some join exception
   *@exception IndexException exception from super class    
   *@exception IOException I/O errors
   *@exception InvalidTripleSizeException invalid triple size
   *@exception InvalidTypeException triple type not valid
   *@exception PageNotReadException exception from lower layer
   *@exception TripleUtilsException exception from using triple utilities
   *@exception PredEvalException exception from PredEval class
   *@exception SortException sort exception
   *@exception LowMemException memory error
   *@exception UnknowAttrType attribute type unknown
   *@exception UnknownKeyTypeException key type unknown
   *@exception Exception other exceptions
   */
  public Triple get_next() 
    throws IOException,
	   JoinsException ,
	   IndexException,
	   InvalidTripleSizeException,
	   InvalidTypeException, 
	   PageNotReadException,
	   TripleUtilsException, 
	   PredEvalException,
	   SortException,
	   LowMemException,
	   UnknowAttrType,
	   UnknownKeyTypeException,
	   Exception
    {
      Triple t;
      
      if (done)
        return null;
      Jtriple.tripleCopy(TempTriple1);
     
      do {
	if ((t = _am.get_next()) == null) {
	  done = true;                    // next call returns DONE;
	  return null;
	} 
	TempTriple2.tripleCopy(t);
      } while (TripleUtils.Equal(TempTriple1, TempTriple2, _in, in_len));
      
      // Now copy the the TempTriple2 (new o/p triple) into TempTriple1.
      TempTriple1.tripleCopy(TempTriple2);
      Jtriple.tripleCopy(TempTriple2);
      return Jtriple ;
    }
 
  /**
   * implement the abstract method close() from super class Iterator
   *to finish cleaning up
   *@exception JoinsException join error from lower layers
   */
  public void close() throws JoinsException
    {
      if (!closeFlag) {
	
	try {
	  _am.close();
	}catch (Exception e) {
	  throw new JoinsException(e, "Driplelim.java: error in closing iterator.");
	}
	closeFlag = true;
      }
    }  
}
