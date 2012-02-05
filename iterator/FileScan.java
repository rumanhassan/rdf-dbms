package iterator;
   

import tripleheap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;


import java.lang.*;
import java.io.*;

/**
 *open a heapfile and according to the condition expression to get
 *output file, call get_next to get all triples
 */
public class FileScan extends  TripleIterator
{
  private AttrType[] _in1;
  private short in1_len;
  private short[] s_sizes; 
  private TripleHeapFile f;
  private TScan scan;
  private Triple     triple1;
  private Triple    Jtriple;
  private int        t1_size;
  private int nOutFlds;
  private CondExpr[]  OutputFilter;
  public FldSpec[] perm_mat;

 

  /**
   *constructor
   *@param file_name tripleheapfile to be opened
   *@param in1[]  array showing what the attributes of the input fields are. 
   *@param s1_sizes[]  shows the length of the string fields.
   *@param len_in1  number of attributes in the input triple
   *@param n_out_flds  number of fields in the out triple
   *@param proj_list  shows what input fields go where in the output triple
   *@param outFilter  select expressions
   *@exception IOException some I/O fault
   *@exception FileScanException exception from this class
   *@exception TripleUtilsException exception from this class
   *@exception InvalidRelation invalid relation 
   */
  public  FileScan (String  file_name,
		    AttrType in1[],                
		    short s1_sizes[], 
		    short     len_in1,              
		    int n_out_flds,
		    FldSpec[] proj_list,
		    CondExpr[]  outFilter        		    
		    )
    throws IOException,
	   FileScanException,
	   TripleUtilsException, 
	   InvalidRelation
    {
      _in1 = in1; 
      in1_len = len_in1;
      s_sizes = s1_sizes;
      
      Jtriple =  new Triple();
      AttrType[] Jtypes = new AttrType[n_out_flds];
      short[]    ts_size;
      ts_size = TripleUtils.setup_op_triple(Jtriple, Jtypes, in1, len_in1, s1_sizes, proj_list, n_out_flds);
      
      OutputFilter = outFilter;
      perm_mat = proj_list;
      nOutFlds = n_out_flds; 
      triple1 =  new Triple();

    /*  try {
	triple1.setHdr(in1_len, _in1, s1_sizes);
      }catch (Exception e){
	throw new FileScanException(e, "setHdr() failed");
      }
    */  t1_size = triple1.size();
      
      try {
	f = new TripleHeapFile(file_name);
	
      }
      catch(Exception e) {
	throw new FileScanException(e, "Create new heapfile failed");
      }
      
      try {
	scan = f.openScan();
      }
      catch(Exception e){
	throw new FileScanException(e, "openScan() failed");
      }
    }
  
  /**
   *@return shows what input fields go where in the output triple
   */
  public FldSpec[] show()
    {
      return perm_mat;
    }
  
  /**
   *@return the result triple
   *@exception JoinsException some join exception
   *@exception IOException I/O errors
   *@exception InvalidTripleSizeException invalid triple size
   *@exception InvalidTypeException triple type not valid
   *@exception PageNotReadException exception from lower layer
   *@exception PredEvalException exception from PredEval class
   *@exception UnknowAttrType attribute type unknown
   *@exception FieldNumberOutOfBoundException array out of bounds
   *@exception WrongPermat exception for wrong FldSpec argument
   */
  public Triple get_next()
    throws JoinsException,
	   IOException,
	   InvalidTripleSizeException,
	   InvalidTypeException,
	   PageNotReadException, 
	   PredEvalException,
	   UnknowAttrType,
	   FieldNumberOutOfBoundException,
	   WrongPermat
    {     
      TID tid = new TID();;
      
      while(true) {
	if((triple1 =  scan.getNext(tid)) == null) {
	  return null;
	}
	
	/*triple1.setHdr(in1_len, _in1, s_sizes);
	*/
	if (PredEval.Eval(OutputFilter, triple1, null, _in1, null) == true){
	  Projection.Project(triple1, _in1,  Jtriple, perm_mat, nOutFlds); 
	  return  Jtriple;
	}        
      }
    }

  /**
   *implement the abstract method close() from super class Iterator
   *to finish cleaning up
   */
  public void close() 
    {
     
      if (!closeFlag) {
	scan.closescan();
	closeFlag = true;
      } 
    }
  
}


