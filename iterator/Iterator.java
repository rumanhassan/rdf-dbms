package iterator;
import global.*;
import tripleheap.*;
import diskmgr.*;
import bufmgr.*;
import index.*;
import java.io.*;

import labelheap.InvalidTypeException;

import tripleheap.InvalidTripleSizeException;
import tripleheap.Triple;

/**
 *All the relational operators and access methods are iterators.
 */
public abstract class Iterator implements Flags {
  
  /**
   * a flag to indicate whether this iterator has been closed.
   * it is set to true the first time the <code>close()</code> 
   * function is called.
   * multiple calls to the <code>close()</code> function will
   * not be a problem.
   */
  public boolean closeFlag = false; // added by bingjie 5/4/98

  /**
   *abstract method, every subclass must implement it.
   *@return the result tuple
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
  public abstract Triple get_next() 
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
	   Exception;

  /**
   *@exception IOException I/O errors
   *@exception JoinsException some join exception
   *@exception IndexException exception from Index class
   *@exception SortException exception Sort class
   */
  public abstract void close() 
    throws IOException, 
	   JoinsException, 
	   SortException,
	   IndexException;
  
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
}
