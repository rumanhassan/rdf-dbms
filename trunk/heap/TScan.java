package tripleheap;

/** JAVA */
/**
 * Scan.java-  class Scan
 *
 */

import java.io.*;
import global.*;
import bufmgr.*;
import diskmgr.*;


/**	
 * A Scan object is created ONLY through the function openScan
 * of a TripleHeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the triplelheapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the triplelheapfile.
 */
public class TScan implements GlobalConst{
 
    /**
     * Note that one record in our way-cool TripleHeapFile implementation is
     * specified by six (6) parameters, some of which can be determined
     * from others:
     */

    /** The triplelheapfile we are using. */
    private TripleHeapFile  _thf;

    /** PageId of current directory page (which is itself an THFPage) */
    private PageID dirpageId = new PageID();

    /** pointer to in-core data of dirpageId (page is pinned) */
    private THFPage dirpage = new THFPage();

    /** record ID of the DataPageInfo struct (in the directory page) which
     * describes the data page where our current record lives.
     */
    private TID datapageTid = new TID();

    /** the actual PageId of the data page with the current record */
    private PageID datapageId = new PageID();

    /** in-core copy (pinned) of the same */
    private THFPage datapage = new THFPage();

    /** record ID of the current record (from the current data page) */
    private TID usertid = new TID();

    /** Status of next user status */
    private boolean nextUserStatus;
    
     
    /** The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from thf
     *
     * @exception InvalidTripleSizeException Invalid triple size
     * @exception IOException I/O errors
     *
     * @param thf A TripleHeapFile object
     */
  public TScan(TriplelHeapFile thf) 
    throws InvalidTripleSizeException,
	   IOException
  {
	init(thf);
  }


  
  /** Retrieve the next record in a sequential scan
   *
   * @exception InvalidTripleSizeException Invalid triple size
   * @exception IOException I/O errors
   *
   * @param tid Record ID of the record
   * @return the Triple of the retrieved record.
   */
  public Triple getNext(TID tid) 
    throws InvalidTripleSizeException,
	   IOException
  {
    Triple recptrtriple = null;
    
    if (nextUserStatus != true) {
        nextDataPage();
    }
     
    if (datapage == null)
      return null;
    
    tid.pageNo.pid = usertid.pageNo.pid;    
    tid.slotNo = usertid.slotNo;
         
    try {
    	recptrtriple = datapage.getRecord(tid);
    }
    
    catch (Exception e) {
  //    System.err.println("SCAN: Error in Scan" + e);
      e.printStackTrace();
    }   
    
    usertid = datapage.nextRecord(tid);
    if(usertid == null) nextUserStatus = false;
    else nextUserStatus = true;
     
    return recptrtriple;
  }


    /** Position the scan cursor to the record with the given tid.
     * 
     * @exception InvalidTripleSizeException Invalid triple size
     * @exception IOException I/O errors
     * @param tid Record ID of the given record
     * @return 	true if successful, 
     *			false otherwise.
     */
  public boolean position(TID tid) 
    throws InvalidTripleSizeException,
	   IOException
  { 
    TID    nxttid = new TID();
    boolean bst;

    bst = peekNext(nxttid);

    if (nxttid.equals(tid)==true) 
    	return true;

    // This is kind lame, but otherwise it will take all day.
    PageID pgid = new PageID();
    pgid.pid = tid.pageNo.pid;
 
    if (!datapageId.equals(pgid)) {

      // reset everything and start over from the beginning
      reset();
      
      bst =  firstDataPage();

      if (bst != true)
	return bst;
      
      while (!datapageId.equals(pgid)) {
	bst = nextDataPage();
	if (bst != true)
	  return bst;
      }
    }
    
    // Now we are on the correct page.
    
    try{
    	usertid = datapage.firstRecord();
	}
    catch (Exception e) {
      e.printStackTrace();
    }
	

    if (usertid == null)
      {
    	bst = false;
        return bst;
      }
    
    bst = peekNext(nxttid);
    
    while ((bst == true) && (nxttid != tid))
      bst = mvNext(nxttid);
    
    return bst;
  }


    /** Do all the constructor work
     *
     * @exception InvalidTripleSizeException Invalid triple size
     * @exception IOException I/O errors
     *
     * @param thf A TripleHeapFile object
     */
    private void init(TriplelHeapFile thf) 
      throws InvalidTripleSizeException,
	     IOException
  {
	_thf = thf;

    	firstDataPage();
  }


    /** Closes the Scan object */
    public void closescan()
    {
    	reset();
    }
   

    /** Reset everything and unpin all pages. */
    private void reset()
    { 

    if (datapage != null) {
    
    try{
      unpinPage(datapageId, false);
    }
    catch (Exception e){
      // 	System.err.println("SCAN: Error in Scan" + e);
      e.printStackTrace();
    }  
    }
    datapageId.pid = 0;
    datapage = null;

    if (dirpage != null) {
    
      try{
	unpinPage(dirpageId, false);
      }
      catch (Exception e){
	//     System.err.println("SCAN: Error in Scan: " + e);
	e.printStackTrace();
      }
    }
    dirpage = null;
 
    nextUserStatus = true;

  }
 
 
  /** Move to the first data page in the file. 
   * @exception InvalidTripleSizeException Invalid triple size
   * @exception IOException I/O errors
   * @return true if successful
   *         false otherwise
   */
  private boolean firstDataPage() 
    throws InvalidTripleSizeException,
	   IOException
  {
    DataPageInfo dpinfo;
    Triple        rectriple = null;
    Boolean      bst;

    /** copy data about first directory page */
 
    dirpageId.pid = _thf._firstDirPageId.pid;  
    nextUserStatus = true;

    /** get first directory page and pin it */
    	try {
	   dirpage  = new THFPage();
       	   pinPage(dirpageId, (Page) dirpage, false);	   
       }

    	catch (Exception e) {
    //    System.err.println("SCAN Error, try pinpage: " + e);
	e.printStackTrace();
	}
    
    /** now try to get a pointer to the first datapage */
	 datapageTid = dirpage.firstRecord();
	 
    	if (datapageTid != null) {
    /** there is a datapage record on the first directory page: */
	
	try {
          rectriple = dirpage.getRecord(datapageTid);
	}  
				
	catch (Exception e) {
	//	System.err.println("SCAN: Chain Error in Scan: " + e);
		e.printStackTrace();
	}		
      			    
    	dpinfo = new DataPageInfo(rectriple);
        datapageId.pid = dpinfo.pageId.pid;

    } else {

    /** the first directory page is the only one which can possibly remain
     * empty: therefore try to get the next directory page and
     * check it. The next one has to contain a datapage record, unless
     * the triplelheapfile is empty:
     */
      PageID nextDirPageId = new PageID();
      
      nextDirPageId = dirpage.getNextPage();
      
      if (nextDirPageId.pid != INVALID_PAGE) {
	
	try {
            unpinPage(dirpageId, false);
            dirpage = null;
	    }
	
	catch (Exception e) {
	//	System.err.println("SCAN: Error in 1stdatapage 1 " + e);
		e.printStackTrace();
	}
        	
	try {
	
           dirpage = new THFPage();
	    pinPage(nextDirPageId, (Page )dirpage, false);
	
	    }
	
	catch (Exception e) {
	//  System.err.println("SCAN: Error in 1stdatapage 2 " + e);
	  e.printStackTrace();
	}
	
	/** now try again to read a data record: */
	
	try {
	  datapageTid = dirpage.firstRecord();
	}
        
	catch (Exception e) {
	//  System.err.println("SCAN: Error in 1stdatapg 3 " + e);
	  e.printStackTrace();
	  datapageId.pid = INVALID_PAGE;
	}
       
	if(datapageTid != null) {
          
	  try {
	  
	    rectriple = dirpage.getRecord(datapageTid);
	  }
	  
	  catch (Exception e) {
	//    System.err.println("SCAN: Error getRecord 4: " + e);
	    e.printStackTrace();
	  }
	  
	  if (rectriple.getLength() != DataPageInfo.size)
	    return false;
	  
	  dpinfo = new DataPageInfo(rectriple);
	  datapageId.pid = dpinfo.pageId.pid;
	  
         } else {
	   // triplelheapfile empty
           datapageId.pid = INVALID_PAGE;
         }
       }//end if01
       else {// triplelheapfile empty
	datapageId.pid = INVALID_PAGE;
	}
}	
	
	datapage = null;

	try{
         nextDataPage();
	  }
	  
	catch (Exception e) {
	//  System.err.println("SCAN Error: 1st_next 0: " + e);
	  e.printStackTrace();
	}
	
      return true;
      
      /** ASSERTIONS:
       * - first directory page pinned
       * - this->dirpageId has Id of first directory page
       * - this->dirpage valid
       * - if triplelheapfile empty:
       *    - this->datapage == NULL, this->datapageId==INVALID_PAGE
       * - if triplelheapfile nonempty:
       *    - this->datapage == NULL, this->datapageId, this->datapageTid valid
       *    - first datapage is not yet pinned
       */
    
  }
    

  /** Move to the next data page in the file and 
   * retrieve the next data page. 
   *
   * @return 		true if successful
   *			false if unsuccessful
   */
  private boolean nextDataPage() 
    throws InvalidTripleSizeException,
	   IOException
  {
    DataPageInfo dpinfo;
    
    boolean nextDataPageStatus;
    PageID nextDirPageId = new PageID();
    Triple rectriple = null;

  // ASSERTIONS:
  // - this->dirpageId has Id of current directory page
  // - this->dirpage is valid and pinned
  // (1) if triplelheapfile empty:
  //    - this->datapage==NULL; this->datapageId == INVALID_PAGE
  // (2) if overall first record in triplelheapfile:
  //    - this->datapage==NULL, but this->datapageId valid
  //    - this->datapageTid valid
  //    - current data page unpinned !!!
  // (3) if somewhere in triplelheapfile
  //    - this->datapageId, this->datapage, this->datapageTid valid
  //    - current data page pinned
  // (4)- if the scan had already been done,
  //        dirpage = NULL;  datapageId = INVALID_PAGE
    
    if ((dirpage == null) && (datapageId.pid == INVALID_PAGE))
        return false;

    if (datapage == null) {
      if (datapageId.pid == INVALID_PAGE) {
	// triplelheapfile is empty to begin with
	
	try{
	  unpinPage(dirpageId, false);
	  dirpage = null;
	}
	catch (Exception e){
	//  System.err.println("Scan: Chain Error: " + e);
	  e.printStackTrace();
	}
	
      } else {
	
	// pin first data page
	try {
	  datapage  = new THFPage();
	  pinPage(datapageId, (Page) datapage, false);
	}
	catch (Exception e){
	  e.printStackTrace();
	}
	
	try {
	  usertid = datapage.firstRecord();
	}
	catch (Exception e) {
	  e.printStackTrace();
	}
	
	return true;
        }
    }
  
  // ASSERTIONS:
  // - this->datapage, this->datapageId, this->datapageTid valid
  // - current datapage pinned

    // unpin the current datapage
    try{
      unpinPage(datapageId, false /* no dirty */);
        datapage = null;
    }
    catch (Exception e){
      
    }
          
    // read next datapagerecord from current directory page
    // dirpage is set to NULL at the end of scan. Hence
    
    if (dirpage == null) {
      return false;
    }
    
    datapageTid = dirpage.nextRecord(datapageTid);
    
    if (datapageTid == null) {
      nextDataPageStatus = false;
      // we have read all datapage records on the current directory page
      
      // get next directory page
      nextDirPageId = dirpage.getNextPage();
  
      // unpin the current directory page
      try {
	unpinPage(dirpageId, false /* not dirty */);
	dirpage = null;
	
	datapageId.pid = INVALID_PAGE;
      }
      
      catch (Exception e) {
	
      }
		    
      if (nextDirPageId.pid == INVALID_PAGE)
	return false;
      else {
	// ASSERTION:
	// - nextDirPageId has correct id of the page which is to get
	
	dirpageId = nextDirPageId;
	
 	try { 
	  dirpage  = new THFPage();
	  pinPage(dirpageId, (Page)dirpage, false);
	}
	
	catch (Exception e){
	  
	}
	
	if (dirpage == null)
	  return false;
	
    	try {
	  datapageTid = dirpage.firstRecord();
	  nextDataPageStatus = true;
	}
	catch (Exception e){
	  nextDataPageStatus = false;
	  return false;
	} 
      }
    }
    
    // ASSERTION:
    // - this->dirpageId, this->dirpage valid
    // - this->dirpage pinned
    // - the new datapage to be read is on dirpage
    // - this->datapageTid has the Lid of the next datapage to be read
    // - this->datapage, this->datapageId invalid
  
    // data page is not yet loaded: read its record from the directory page
   	try {
	  rectriple = dirpage.getRecord(datapageTid);
	}
	
	catch (Exception e) {
	  System.err.println("TripleHeapFile: Error in Scan" + e);
	}
	
	if (rectriple.getLength() != DataPageInfo.size)
	  return false;
                        
	dpinfo = new DataPageInfo(rectriple);
	datapageId.pid = dpinfo.pageId.pid;
	
 	try {
	  datapage = new THFPage();
	  pinPage(dpinfo.pageId, (Page) datapage, false);
	}
	
	catch (Exception e) {
	  System.err.println("TripleHeapFile: Error in Scan" + e);
	}
	
     
     // - directory page is pinned
     // - datapage is pinned
     // - this->dirpageId, this->dirpage correct
     // - this->datapageId, this->datapage, this->datapageTid correct

     usertid = datapage.firstRecord();
     
     if(usertid == null)
     {
       nextUserStatus = false;
       return false;
     }
  
     return true;
  }


  private boolean peekNext(TID tid) {
    
    tid.pageNo.pid = usertid.pageNo.pid;
    tid.slotNo = usertid.slotNo;
    return true;
    
  }


  /** Move to the next record in a sequential scan.
   * Also returns the TID of the (new) current record.
   */
  private boolean mvNext(TID tid) 
    throws InvalidTripleSizeException,
	   IOException
  {
    TID nextrid;
    boolean status;

    if (datapage == null)
        return false;

    	nextrid = datapage.nextRecord(tid);
	
	if( nextrid != null ){
	  usertid.pageNo.pid = nextrid.pageNo.pid;
	  usertid.slotNo = nextrid.slotNo;
	  return true;
	} else {
	  
	  status = nextDataPage();

	  if (status==true){
	    tid.pageNo.pid = usertid.pageNo.pid;
	    tid.slotNo = usertid.slotNo;
	  }
	
	}
	return true;
  }

    /**
   * short cut to access the pinPage function in bufmgr package.
   * @see bufmgr.pinPage
   */
  private void pinPage(PageID pageno, Page page, boolean emptyPage)
    throws HFBufMgrException {

    try {
      SystemDefs.JavabaseBM.pinPage(pageno, page, emptyPage);
    }
    catch (Exception e) {
      throw new HFBufMgrException(e,"Scan.java: pinPage() failed");
    }

  } // end of pinPage

  /**
   * short cut to access the unpinPage function in bufmgr package.
   * @see bufmgr.unpinPage
   */
  private void unpinPage(PageID pageno, boolean dirty)
    throws HFBufMgrException {

    try {
      SystemDefs.JavabaseBM.unpinPage(pageno, dirty);
    }
    catch (Exception e) {
      throw new HFBufMgrException(e,"Scan.java: unpinPage() failed");
    }

  } // end of unpinPage


}
