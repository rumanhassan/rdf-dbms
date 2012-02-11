/* File Stream.java */

package diskmgr;

import java.io.*;

import global.*;
import btree.*;
import bufmgr.*;
import tests.BatchInsert;
import tripleheap.*;

/**	
 * A Stream object is created ONLY through the function openStream
 * of an rdfDB. It supports the getNext interface which will
 * simply retrieve the next record in the TripleHeapFile.
 *
 * An object of type Stream will always have pinned one directory page
 * of the TripleHeapFile.
 */
public class Stream implements GlobalConst{
	// some values to avoid magic numbers
	final int UNINITIALIZED = -1; 
	final int NOT_INCLUDED = 0; 
	final int INCLUDED = 1; 
	final int SUBJIDX = 0;
	final int PREDIDX = 1;
	final int OBJIDX = 2;
//	final int CONFIDX = 3;  --- allow to query where confidence >= 0
	
	// attributes of the Stream
	private rdfDB rdfdatabase;
	private int orderType;
	private String subjectFilter;
	private String predicateFilter;
	private String objectFilter;
	private float confidenceFilter;
	private short[] filtersIncluded = {UNINITIALIZED, UNINITIALIZED, 
									   UNINITIALIZED};
	private BTFileScan btScan = null; // for scanning the index of the rdfDB
     
    /** The constructor initializes the Stream's private data members from the
     * given parameters.
     *
	 * @param rdfDataBase The rdfDB used to access heap files/b-trees
	 * @param orderType
	 * @param subjectFilter
	 * @param predicateFilter
	 * @param objectFilter
	 * @param confidenceFilter
	 * @throws InvalidTripleSizeException Invalid triple size
	 * @throws IOException I/O errors
	 */
	public Stream(rdfDB rdfDataBase, int orderType, String subjectFilter, 
		 String predicateFilter, String objectFilter, float confidenceFilter)
    throws InvalidTripleSizeException,
	   	   IOException, 
	   	   UnpinPageException,
	   	   PinPageException,
	   	   ConstructPageException,
	   	   IteratorException,
	   	   KeyNotMatchException
  {
	  this.rdfdatabase = rdfDataBase;
	  this.orderType = orderType;
	  this.subjectFilter = subjectFilter;
	  this.predicateFilter = predicateFilter;
	  this.objectFilter = objectFilter;
	  this.confidenceFilter = confidenceFilter;
	  processFilters(subjectFilter, predicateFilter,objectFilter, confidenceFilter);
	//initiate a scan of the whole index file, assumes that the btree is in sorted order
	  btScan = rdfDataBase.bTreeIndexFile.new_scan(null, null); 
  }
  
	/** Retrieve the next triple in the stream by using the rdfDataBase's 
	 *  b-tree structure to get the next Triple from the triple heap file  
	 * @return The next Triple that matches the filter criteria, null if
	 *         we have reached the end of the heap file
	*/
	public Triple getNext() 
	  {
		Triple aTriple = new Triple();
		Triple fillTriple=new Triple();
	    // Just don't want to initialize these each time we iterate later
		LeafData dummyLeaf = null;
	    DataClass indexData = dummyLeaf; // upcast, hopefully some Java magic will happen soon...
	    KeyDataEntry keyData = null;
	    boolean weHaveAMatch = false;
	    
	    EID subjEntity = null;
	    LID subjLabel = null;
	    String subjStr = null;
	    PID currPred = null;	  
	    LID predLabel = null;	    
	    String predStr = null;
	    EID objEntity = null;	    
	    LID objLabel = null;	    
	    String objStr = null;    
	    
	    try {
			keyData = btScan.get_next();
		} catch (ScanIteratorException e) {
			System.out.println("STREAM: error when attempting to get next btree leaf node");
			e.printStackTrace();
		} // no matches yet, keep moving down the line
	    
	    while (keyData != null) { // iterate here
	    	try {
				keyData = btScan.get_next();
			} catch (ScanIteratorException e) {
				System.out.println("STREAM: error when attempting to get next btree leaf node");
				e.printStackTrace();
			} // no matches yet, keep moving down the line
	    	if(keyData != null) // we have not yet reached the end of the scan
	    	{
	    		indexData = keyData.data;  // when scanning the btree, type DataClass is returned
	            LeafData currTreeNode = (LeafData)indexData; // MAGIC, downcasting is allowed since indexData references a LeafData object
	
	    	    try {    	    	
		    	    // 2. Check that the triple's data matches the filter  
		            TID myTID = currTreeNode.getData();
		      	    TID tripleID = new TID(myTID.pageNo, myTID.slotNo);
		      	    aTriple = rdfdatabase.tripleHeapFile.getTriple(tripleID); 
		      	    
		      	    fillTriple=getTripleFromByteArray(aTriple.getTripleByteArray());
		      	    //---------------------------------------------
		      	    subjEntity = fillTriple.getSubjectId();
		      	    subjLabel = subjEntity.returnLID();    
		      	    subjStr = rdfdatabase.entityLabelHeapFile.getLabel(subjLabel);
		      	    //---------------------------------------------
		      	   // currPred = fillTriple.getPredicateId();	
		      	    currPred = fillTriple.getPredicateId();
		      	    predLabel = currPred.returnLID();	    
		      	    predStr = rdfdatabase.predicateLabelHeapFile.getLabel(predLabel);
		      	    //---------------------------------------------
		      	   // objEntity = fillTriple.getObjectId();
		      	    objEntity = fillTriple.getObjectId();
		      	    objLabel = objEntity.returnLID();	    
		      	    objStr = rdfdatabase.entityLabelHeapFile.getLabel(objLabel);
		      	    this.confidenceFilter = fillTriple.value;
				} catch (InvalidSlotNumberException e) {
					System.out.println("STREAM: error attempting access to triple heap file");
					e.printStackTrace();
				} catch (InvalidTupleSizeException e) {
					System.out.println("STREAM: Invalid Triple size???");
					e.printStackTrace();
				} catch (THFException e) {
					System.out.println("STREAM: error attempting access to triple heap file");
					e.printStackTrace();
				} catch (THFDiskMgrException e) {
					System.out.println("STREAM: error attempting access to triple heap file");
					e.printStackTrace();
				} catch (THFBufMgrException e) {
					System.out.println("STREAM: error attempting access to triple heap file");
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("STREAM: error when attempting to get next btree leaf node");
					e.printStackTrace();
				}    	      	    	    
	
	    	    // Check that the triple's data matches the filter
	    	    weHaveAMatch = tripleDataMatchesFilter(subjStr, objStr, predStr, confidenceFilter);

	    	    if(weHaveAMatch)
	    	    	keyData = null; // to break out of the do-while
	    	}
	   } 
	 
	    return aTriple; // Return the triple with matching TID, null if no more matching triples    
	  } // end getNext method

    /** Closes the Stream object, performs necessary cleanup 
     * @throws IOException 
     * @throws HashEntryNotFoundException 
     * @throws PageUnpinnedException 
     * @throws ReplacerException 
     * @throws InvalidFrameNumberException */
    public void closeStream() 
    throws 	InvalidFrameNumberException, ReplacerException, 
    		PageUnpinnedException, HashEntryNotFoundException, IOException
    {
    	btScan.DestroyBTreeFileScan();
    }
    
    /** Sets the filtersIncluded array to keep track of which filters are used
     * @param subj subject filter
     * @param pred predicate filter
     * @param obj object filter
     * @param conf confidence filter
     */
    private void processFilters(String subj, String pred, String obj, 
    		float conf){
    	// subject filter null or 0
    	if( (subj == null) || subj.equalsIgnoreCase("0"))
    		filtersIncluded[SUBJIDX] = NOT_INCLUDED;
    	else filtersIncluded[SUBJIDX] = INCLUDED;
    	// predicate filter null or 0
    	if( (pred == null) || pred.equalsIgnoreCase("0"))
    		filtersIncluded[PREDIDX] = NOT_INCLUDED;
    	else filtersIncluded[PREDIDX] = INCLUDED;
    	// object filter null or 0
    	if( (obj == null) || obj.equalsIgnoreCase("0"))
    		filtersIncluded[OBJIDX] = NOT_INCLUDED;
    	else filtersIncluded[OBJIDX] = INCLUDED;
// ---------------- We do want to be able to query for confidence >= 0.    	
//    	float zero = 0;
//        int i1 = Float.compare(zero,conf);
//    	if( i1 == 0) // confidence filter equals 0
//    		filtersIncluded[CONFIDX] = NOT_INCLUDED;
//    	else filtersIncluded[CONFIDX] = INCLUDED;    	
    }
    private static Triple getTripleFromByteArray(byte [] tripleAray) 
	   {
		  // byte[] triplecopy = new byte[28];
		 Triple atriple=new Triple();
			try {
				EID seid=new EID();
				EID oeid =new EID();
				PID prid =new PID();
				seid.slotNo=Convert.getIntValue(0, tripleAray);
				seid.pageNo.pid=Convert.getIntValue(4, tripleAray);
				prid.slotNo=Convert.getIntValue(8, tripleAray);
				prid.pageNo.pid=Convert.getIntValue(12, tripleAray);
				oeid.slotNo=Convert.getIntValue(16, tripleAray);
				oeid.pageNo.pid=Convert.getIntValue(20, tripleAray);
				
				
			atriple.subjectId=seid;
			atriple.predicateId=prid;
			atriple.objectId=oeid;
			atriple.value=Convert.getFloValue(24, tripleAray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return atriple;
	       /*byte [] triplecopy = new byte [triple_length];
	       System.arraycopy(data, triple_offset, triplecopy, 0, triple_length);
	       return triplecopy;*/
	   }
    
    /** Checks if a triple's data matches the filter criteria
     * @param subject
     * @param predicate
     * @param object
     * @param confidence
     * @return true if there is a match, false otherwise
     */
    public boolean tripleDataMatchesFilter(String subject, String predicate,
    		String object, float confidence){
    	boolean matches = false;
    	
    	// subject filter matches or is not considered
    	if( filtersIncluded[SUBJIDX] == NOT_INCLUDED ||
    		(filtersIncluded[SUBJIDX] == INCLUDED && subject.equalsIgnoreCase(subjectFilter) ) ){
    		// predicate filter matches or is not considered
    		if( filtersIncluded[PREDIDX] == NOT_INCLUDED ||
    	    	(filtersIncluded[PREDIDX] == INCLUDED && predicate.equalsIgnoreCase(predicateFilter) ) ){
    			// object filter matches or is not considered
    			if( filtersIncluded[OBJIDX] == NOT_INCLUDED ||
    	    	    (filtersIncluded[OBJIDX] == INCLUDED && object.equalsIgnoreCase(objectFilter) ) ){
    				// confidence is greater than or equal to the filter
    				if( Float.compare(confidenceFilter,confidence) >= 0 ){
    					// if you got to this point, all criteria are met
    					matches = true;
    				}
    			}    			
    		}
    	}
    	
    	return matches;    	
    }
  
} // end of class Stream
