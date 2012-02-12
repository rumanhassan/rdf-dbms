package iterator;

import global.AttrType;
import global.PageID;
import global.RID;
import global.SystemDefs;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import index.IndexException;

import java.io.IOException;

import labelheap.InvalidTypeException;
import bufmgr.PageNotReadException;
import diskmgr.Page;
import tripleheap.BasicPatternClass;
import tripleheap.InvalidTripleSizeException;
import tripleheap.InvalidTupleSizeException;

public class BP_Triple_Join {
	
	private AttrType      _in1[],  _in2[];
	  private   int        in1_len, in2_len;
	  private   Iterator  outer;
	  private   short t2_str_sizescopy[];
	  private   CondExpr OutputFilter[];
	  private   CondExpr RightFilter[];
	  private   int        n_buf_pgs;        // # of buffer pages available.
	  private   boolean        done,         // Is the join complete
	    get_from_outer;                 // if TRUE, a tuple is got from outer
	  private   BasicPatternClass     outer_BP, inner_BP;
	  private   BasicPatternClass     joined_BP;           // Joined Basic Pattern
	  private   FldSpec   perm_mat[];
	  private   int        nOutFlds;
	  private   Heapfile  hf;
	  private   Scan      inner;
	
	/**
	 * @param amt_of_mem - available pages for the operation
	 * @param num_left_nodes - the number of node IDs in the left basic pattern stream
	 * @param left_itr - the left basic pattern stream
	 * @param BPJoinNodePosition - the position of the join node in the basic pattern
	 * @param JoinOnSubjectorObject - 0: join on subject; 1: join on object
	 * @param RightSubjectFilter - subject filter for the right source
	 * @param RightPredicateFilter - predicate filter for the right source
	 * @param RightObjectFilter - object filter for the right source
	 * @param RightConfidenceFilter - confidence filter for the right source
	 * @param LeftOutNodePositions - positions of the projected nodes from the left source
	 * @param OutputRightSubject - 0/1 project subject node from the right source?
	 * @param OutputRightObject - 0/1 project object node from the right source?
	 */
	public BP_Triple_Join( int amt_of_mem, int num_left_nodes, BPIterator left_itr,
			int BPJoinNodePosition, int JoinOnSubjectorObject, String
			RightSubjectFilter, String RightPredicateFilter, String
			RightObjectFilter, double RightConfidenceFilter, int [] LeftOutNodePositions,
			int OutputRightSubject, int OutputRightObject) {
		//TODO Auto-generated
	} // end constructor
	

	  
	  /**
	   * a flag to indicate whether this iterator has been closed.
	   * it is set to true the first time the <code>close()</code> 
	   * function is called.
	   * multiple calls to the <code>close()</code> function will
	   * not be a problem.
	   */
	  public boolean closeFlag = false; // added by bingjie 5/4/98

	  /**
	   *@return the result BasicPatternClass
	   *@exception IOException I/O errors
	   *@exception JoinsException some join exception
	   *@exception IndexException exception from super class    
	   *@exception InvalidTupleSizeException invalid tuple size
	   *@exception InvalidTypeException tuple type not valid
	   *@exception PageNotReadException exception from lower layer
	   *@exception TupleUtilsException exception from using tuple utilities
	   *@exception PredEvalException exception from PredEval class
	   *@exception SortException sort exception
	   *@exception LowMemException memory error
	   *@exception UnknowAttrType attribute type unknown
	   *@exception UnknownKeyTypeException key type unknown
	   *@exception Exception other exceptions
	   */
	  public BasicPatternClass getnext() 
	    throws IOException,
		   JoinsException ,
		   IndexException,
		   InvalidTripleSizeException,
		   InvalidTypeException, 
		   PageNotReadException,
		   TupleUtilsException, 
		   PredEvalException,
		   SortException,
		   LowMemException,
		   UnknowAttrType,
		   UnknownKeyTypeException,
		   Exception{
	      // This is a DUMBEST form of a join, not making use of any key information...
	      
	      
	      if (done)
		return null;
	      
	      do
		{
		  // If get_from_outer is true, Get a tuple from the outer, delete
		  // an existing scan on the file, and reopen a new scan on the file.
		  // If a get_next on the outer returns DONE?, then the nested loops
		  //join is done too.
		  
		  if (get_from_outer == true)
		    {
		      get_from_outer = false;
		      if (inner != null)     // If this not the first time,
			{
			  // close scan
			  inner = null;
			}
		    
		      try {
			inner = hf.openScan();
		      }
		      catch(Exception e){
			throw new NestedLoopException(e, "openScan failed");
		      }
		      
		      if ((outer_BP=outer.get_next()) == null) //call to Iterator.get_next
			{
			  done = true;
			  if (inner != null) 
			    {
	                      
			      inner = null;
			    }
			  
			  return null;
			}   
		    }  // ENDS: if (get_from_outer == TRUE)
		 
		  
		  // The next step is to get a tuple from the inner,
		  // while the inner is not completely scanned && there
		  // is no match (with pred),get a tuple from the inner.
		  
		 
		      RID rid = new RID();
		      while ((inner_BP = inner.getNext(rid)) != null)
			{
			  inner_BP.setHdr((short)in2_len, _in2,t2_str_sizescopy);
			  if (PredEval.Eval(RightFilter, inner_BP, null, _in2, null) == true)
			    {
			      if (PredEval.Eval(OutputFilter, outer_BP, inner_BP, _in1, _in2) == true)
				{
				  // Apply a projection on the outer and inner tuples.
				  Projection.Join(outer_BP, _in1, 
						  inner_BP, _in2, 
						  joined_BP, perm_mat, nOutFlds);
				  return joined_BP;
				}
			    }
			}
		      
		      // There has been no match. (otherwise, we would have 
		      //returned from t//he while loop. Hence, inner is 
		      //exhausted, => set get_from_outer = TRUE, go to top of loop
		      
		      get_from_outer = true; // Loop back to top and get next outer tuple.	      
		} while (true);
	   }

	  /**
	   *@exception IOException I/O errors
	   *@exception JoinsException some join exception
	   *@exception IndexException exception from Index class
	   *@exception SortException exception Sort class
	   */
	  public void close() 
	    throws IOException, 
		   JoinsException, 
		   SortException,
		   IndexException{
		  //TODO fill in this method
		  
	  }
	  
	  /**
	   * tries to get n_pages of buffer space
	   *@param n_pages the number of pages
	   *@param PageIds the corresponding PageId for each page
	   *@param bufs the buffer space
	   *@exception IteratorBMException exceptions from bufmgr layer
	   */
	  public void  get_buffer_pages(int n_pages, PageID[] PageIds, byte[][] bufs)
	    throws IteratorBMException
	    {
	      Page pgptr = new Page();        
	      PageID pgid = null;
	      
	      for(int i=0; i < n_pages; i++) {
		pgptr.setpage(bufs[i]);

		pgid = newPage(pgptr,1);
		PageIds[i] = new PageID(pgid.pid);
		
		bufs[i] = pgptr.getpage();
		
	      }
	    }

	  /**
	   *free all the buffer pages we requested earlier.
	   * should be called in the destructor
	   *@param n_pages the number of pages
	   *@param PageIds  the corresponding PageId for each page
	   *@exception IteratorBMException exception from bufmgr class 
	   */
	  public void free_buffer_pages(int n_pages, PageID[] PageIds) 
	    throws IteratorBMException
	    {
	      for (int i=0; i<n_pages; i++) {
		freePage(PageIds[i]);
	      }
	    }

	  private void freePage(PageID pageno)
	    throws IteratorBMException {
	    
	    try {
	      SystemDefs.JavabaseBM.freePage(pageno);
	    }
	    catch (Exception e) {
	      throw new IteratorBMException(e,"Iterator.java: freePage() failed");
	    }
	    
	  } // end of freePage

	  private PageID newPage(Page page, int num)
	    throws IteratorBMException {
	    
	    PageID tmpId = new PageID();
	    
	    try {
	      tmpId = SystemDefs.JavabaseBM.newPage(page,num);
	    }
	    catch (Exception e) {
	      throw new IteratorBMException(e,"Iterator.java: newPage() failed");
	    }

	    return tmpId;

	  } // end of newPage

} // end BP_Triple_Join class
