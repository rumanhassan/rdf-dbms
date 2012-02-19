package iterator;

import global.AttrType;
import global.Convert;
import global.EID;
import global.PageID;
import global.RID;
import global.SystemDefs;
import global.TID;
import diskmgr.Stream;
import heap.Tuple;
import index.IndexException;

import java.io.IOException;
import java.util.ArrayList;

import labelheap.InvalidTypeException;
import btree.ConstructPageException;
import btree.IteratorException;
import btree.KeyNotMatchException;
import btree.PinPageException;
import btree.UnpinPageException;
import bufmgr.PageNotReadException;
import diskmgr.Page;
import tripleheap.BasicPatternClass;
import tripleheap.InvalidTripleSizeException;
import tripleheap.Triple;

public class BP_Triple_Join {
	
	private AttrType      _in1[],  _in2[];
	  private   int        in1_len, in2_len;
	  private   BPIterator  outer;
	  private   short t2_str_sizescopy[];
	  private   CondExpr OutputFilter[];
	  private   CondExpr RightFilter[];
	  private   int        n_buf_pgs;        // # of buffer pages available.
	  private   boolean        done,         // Is the join complete
	    get_from_outer;                 // if TRUE, a tuple is got from outer
	  private   Triple     outer_Triple;
	  private   BasicPatternClass inner_BP;
	  private   BasicPatternClass     joined_BP;           // Joined Basic Pattern
	  private   FldSpec   perm_mat[];
	  private   int        nOutFlds;
	  private   Stream      tripleStream;
	  private Triple currentTriple;
	  
	  private int leftBPIndex;
	  
	  private boolean joinOnSubj = false;
	  private boolean joinOnObj = false;
	  private boolean outputRightSubj = false;
	  private boolean outputRightObj = false;
	  
	  private int amt_of_mem; // - available pages for theoperation
	  private int num_left_nodes; // - the number of node IDs in the left basic pattern stream
	  public BPIterator left_itr; // - the left basic pattern stream
	  private int BPJoinNodePosition; // - the position of the join node in the basic pattern

	  private ArrayList<Integer> LeftOutNodePositions; //- positions of the projected nodes from the left source
	  
	  
	
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
	 * @throws IOException 
	 * @throws KeyNotMatchException 
	 * @throws IteratorException 
	 * @throws ConstructPageException 
	 * @throws PinPageException 
	 * @throws UnpinPageException 
	 * @throws InvalidTripleSizeException 
	 */
	public BP_Triple_Join( int amt_of_mem, int num_left_nodes, BPIterator left_itr,
			int BPJoinNodePosition, int JoinOnSubjectorObject, String
			RightSubjectFilter, String RightPredicateFilter, String
			RightObjectFilter, float RightConfidenceFilter, ArrayList<Integer> LeftOutNodePositions,
			int OutputRightSubject, int OutputRightObject) throws InvalidTripleSizeException, UnpinPageException, PinPageException, ConstructPageException, IteratorException, KeyNotMatchException, IOException {
		
		  this.amt_of_mem = amt_of_mem; 
		  this.num_left_nodes = num_left_nodes; 
		  this.left_itr = left_itr; 
		  this.BPJoinNodePosition = BPJoinNodePosition; 
		  if(JoinOnSubjectorObject == 0)
			  joinOnSubj = true;
		  else joinOnObj = true;
 
		  this.LeftOutNodePositions = LeftOutNodePositions;
		  if(OutputRightSubject == 1)
			  outputRightSubj = true;
		  if(OutputRightObject == 1)
			  outputRightObj = true;
		  
		  tripleStream = new Stream(SystemDefs.JavabaseDB, 2, RightSubjectFilter, RightPredicateFilter, RightObjectFilter, RightConfidenceFilter);
		  get_from_outer = true;
		  done = false;
		  leftBPIndex = 0;
	} // end constructor
	
	/** Build the initial list of BPs using the left filter criteria
	 * @param subjFilter
	 * @param predFilter
	 * @param objFilter
	 * @param confFilter
	 * @throws IOException 
	 * @throws KeyNotMatchException 
	 * @throws IteratorException 
	 * @throws ConstructPageException 
	 * @throws PinPageException 
	 * @throws UnpinPageException 
	 * @throws InvalidTripleSizeException 
	 */
	public void intializeBPIterator(String subjFilter, String predFilter,
			String objFilter, float confFilter) throws InvalidTripleSizeException, UnpinPageException, PinPageException, ConstructPageException, IteratorException, KeyNotMatchException, IOException{
		left_itr.bpList = new ArrayList<BasicPatternClass>();
		Stream leftStream = new Stream(SystemDefs.JavabaseDB, 2, subjFilter, predFilter, objFilter, confFilter);
		Triple trip = leftStream.getNext();
		while(trip != null){
			
			BasicPatternClass bp = new BasicPatternClass();
			bp.addEntityToBP(trip.getSubjectId());	
			bp.addEntityToBP(trip.getObjectId());
			bp.setConfidence(trip.getConfidence());

			left_itr.bpList.add(bp);
			
			trip = leftStream.getNext();
		} // end while
		
	}
	

	  
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
	  public BasicPatternClass get_next() 
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
		  // If get_from_outer is true, Get a triple from the outer stream, delete
		  // an existing scan on the file, and reopen a new scan on the file.
		  // If a get_next on the outer returns DONE?, then the nested loops
		  //join is done too.
		  
		  if (get_from_outer == true)
		    {
		      get_from_outer = false;
		      
		      outer_Triple=tripleStream.getNext(); //call to Stream.getNext     
		      if ( outer_Triple == null ) 
			{
			  done = true;
//			  if (tripleStream != null) 
//			    {
//	                      
//			      tripleStream.closeStream(); 
//			    }
			  
			  return null;
			}   
		    }  // ENDS: if (get_from_outer == TRUE)
		 
		  
		  // The next step is to get a BP from the inner,
		  // while the inner is not completely scanned && there
		  // is no match ,get a BP from the inner.
		    EID joinEid;
		    while ( (inner_BP = left_itr.get_next()) != null)
			{
		    	if(BPJoinNodePosition <= inner_BP.noOfEntities()){ // don't join if the BP has less entities than join position
		    		
		    	
				  joinEid = inner_BP.getEIDbyNodePosition(BPJoinNodePosition); // joining on this guy from the set of BPs
				  
				  if(joinOnSubj){
					  if(joinEid.equals(outer_Triple.subjectId)){
						  // here we join! lets have some fun!
						  BasicPatternClass returnBP = new BasicPatternClass();
						  for(int itr=0 ; itr < LeftOutNodePositions.size() ; itr++){
							  int leftOutIdx = Integer.valueOf(LeftOutNodePositions.get(itr));
							  returnBP.addEntityToBP(inner_BP.getEIDbyNodePosition(leftOutIdx));
						  }
						  if(outputRightSubj)
							  returnBP.addEntityToBP(outer_Triple.subjectId);
						  if(outputRightObj)
							  returnBP.addEntityToBP(outer_Triple.objectId);
						  // take the minimum of the two confidence values
						  if(outer_Triple.value < inner_BP.getConfidence())
							  returnBP.setConfidence(outer_Triple.value);
							  //inner_BP.setConfidence(outer_Triple.value);	
						  else returnBP.setConfidence(inner_BP.getConfidence());
						  return returnBP;
				  	  }
					  
				  } // end if(joinOnSubj)
				  else if(joinOnObj){
					  if(joinEid.equals(outer_Triple.objectId)){						  
						// here we join! lets have some fun!
						  BasicPatternClass returnBP = new BasicPatternClass();
						  for(int itr=0 ; itr < LeftOutNodePositions.size() ; itr++){
							  int leftOutIdx = Integer.valueOf(LeftOutNodePositions.get(itr));
							  returnBP.addEntityToBP(inner_BP.getEIDbyNodePosition(leftOutIdx));
						  }
						  if(outputRightSubj)
							  returnBP.addEntityToBP(outer_Triple.subjectId);
						  if(outputRightObj)
							  returnBP.addEntityToBP(outer_Triple.objectId);
						  // take the minimum of the two confidence values
						  if(outer_Triple.value < inner_BP.getConfidence())
							  returnBP.setConfidence(outer_Triple.value);
//							  inner_BP.setConfidence(outer_Triple.value);
						  else returnBP.setConfidence(inner_BP.getConfidence());
						  return returnBP;
				  	  }
				  } // end if(joinOnObj)
				  
		    	}
			} // end while
		    left_itr.resetIndex(); // allow for scanning the inner set of BPs again
		      
		      // There has been no match. (otherwise, we would have 
		      //returned from the while loop. Hence, inner is 
		      //exhausted, => set get_from_outer = TRUE, go to top of loop		      
		      get_from_outer = true; // Loop back to top and get next outer tuple.	      
		} while (true);
	   } // end getnext() method
	  
	  public BasicPatternClass get_next_DONOTUSE() throws IOException{
		  
		  BasicPatternClass retVal = null;
		  do {		  
			  currentTriple = tripleStream.getNext();
			  boolean joinSuccess = false;
			  if(currentTriple != null){ // IF01  -- have not yet reached end of triple stream	  
			  
				  int itr;
				  EID leftEid = null;
				  BasicPatternClass leftBp = null;
				  // go thru all the BPs, if one can be joined, join it and return it
				  for(itr = leftBPIndex ; itr<left_itr.bpList.size();itr++){
					  leftBp = left_itr.bpList.get(itr);
					  leftEid = leftBp.getEIDbyNodePosition(BPJoinNodePosition); // joining on this guy from the set of BPs
					  
					  if(joinOnSubj){
						  if(leftEid.equals(currentTriple.subjectId)){						  
							  leftBp.addEntityToBP(currentTriple.subjectId);
							  retVal = leftBp;
							  break;
					  	  }
						  
					  }
					  else if(joinOnObj){
						  if(leftEid.equals(currentTriple.objectId)){						  
							  leftBp.addEntityToBP(currentTriple.objectId);
							  retVal = leftBp;
							  break;
					  	  }
					  }
					  
				  } // end for
			  } // END IF01
		  }
		  while(currentTriple != null);
		  
		  return retVal;
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
