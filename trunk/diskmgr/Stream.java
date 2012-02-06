/* File Stream.java */

package diskmgr;

import java.io.*;
import global.*;
import btree.*;
import bufmgr.*;
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
	//initiate a scan of the whole index file, assume that the btree is in sorted order
	  btScan = rdfDataBase.bTreeIndexFile.new_scan(null, null); 
  }
  
  /** Retrieve the next triple in the stream
   *
   * @param tid Triple ID of the triple
   * @return the Triple object with the specified TID. If no such triple 
   * 		exists, return null.
 * @throws Exception 
 * @throws THFBufMgrException 
 * @throws THFDiskMgrException 
 * @throws THFException 
 * @throws InvalidTupleSizeException 
 * @throws InvalidSlotNumberException 
   */
  public Triple getNext() 
    throws InvalidSlotNumberException, InvalidTupleSizeException, THFException, THFDiskMgrException, THFBufMgrException, Exception
  {
    Triple recptrtriple = null;
  
    // 1. Use rdfDataBase's b-tree structure to get the next Triple from the
    //    triple heap file.   
    LeafData dummyLeaf = null;
    DataClass indexData = dummyLeaf; // upcast, hopefully some Java magic will happen soon...
    indexData = btScan.get_next().data;  // when scanning the btree, type DataClass is returned
    LeafData newLeaf = (LeafData)indexData; // MAGIC, downcasting is allowed since indexData references a LeafData object
    //LeafData idxRecordData = (LeafData) btScan.get_next().data; //must cast DataClass to LeafData
    if(newLeaf == null) // we have reached the end of the scan
    	return null; 
    else {    
	    GENID genericID = newLeaf.getData();
	    TID tripleID = new TID(genericID.pageNo, genericID.slotNo);
	    recptrtriple = rdfdatabase.tripleHeapFile.getTriple(tripleID);
	    
	 // 2. Check that the triple's data matches the filter
	    boolean weHaveAMatch = false;
	    //---------------------------------------------
	    EID subjEntity = recptrtriple.getSubjectId();	    
	    // maybe later we can just call subjEntity.returnLID();
	    LID subjLabel = new LID(subjEntity.pageNo, subjEntity.slotNo);	    
	    String subjStr = rdfdatabase.entityLabelHeapFile.getLabel(subjLabel);
	    //---------------------------------------------
	    PID currPred = recptrtriple.getPredicateId();	  
	    // maybe later we can just call currPred.returnLID();
	    LID predLabel = new LID(currPred.pageNo, subjEntity.slotNo);	    
	    String predStr = rdfdatabase.predicateLabelHeapFile.getLabel(predLabel);
	    //---------------------------------------------
	    EID objEntity = recptrtriple.getObjectId();	    
	    // maybe later we can just call objEntity.returnLID();
	    LID objLabel = new LID(objEntity.pageNo, objEntity.slotNo);	    
	    String objStr = rdfdatabase.entityLabelHeapFile.getLabel(objLabel);
	    
	    weHaveAMatch = tripleDataMatchesFilter(subjStr, objStr, predStr, confidenceFilter);
	 // 3. Return the triple with matching TID
	    if(weHaveAMatch)
	    	return recptrtriple;
	    else // no matches, keep moving down the line
	    	return getNext();	    
    }
  }

    /** Closes the Stream object */
    public void closeStream()
    {
    	// TODO Write code to perform cleanup
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
    
    public Triple getNextIterative() 
    throws InvalidSlotNumberException, InvalidTupleSizeException, THFException,
    	   THFDiskMgrException, THFBufMgrException, Exception
  {
    Triple aTriple = null;

    LeafData idxRecordData = (LeafData) btScan.get_next().data; //must cast DataClass to LeafData
    while(idxRecordData != null) // while we have not yet reached the end of the BTFileScan
    {
    	idxRecordData = (LeafData) btScan.get_next().data;
    	GENID genericID = idxRecordData.getData();
	    TID tripleID = new TID(genericID.pageNo, genericID.slotNo);
	    aTriple = rdfdatabase.tripleHeapFile.getTriple(tripleID);
    }
    return aTriple;
//    if(idxRecordData == null) // we have reached the end of the scan
//    	return null; 
//    else {    
//	    GENID genericID = idxRecordData.getData();
//	    TID tripleID = new TID(genericID.pageNo, genericID.slotNo);
//	    aTriple = rdfdatabase.tripleHeapFile.getTriple(tripleID);
//	    
//	 // 2. Check that the triple's data matches the filter
//	    boolean weHaveAMatch = false;
//	    //---------------------------------------------
//	    EID subjEntity = aTriple.getSubjectId();	    
//	    // maybe later we can just call subjEntity.returnLID();
//	    LID subjLabel = new LID(subjEntity.pageNo, subjEntity.slotNo);	    
//	    String subjStr = rdfdatabase.entityLabelHeapFile.getLabel(subjLabel);
//	    //---------------------------------------------
//	    PID currPred = aTriple.getPredicateId();	  
//	    // maybe later we can just call currPred.returnLID();
//	    LID predLabel = new LID(currPred.pageNo, subjEntity.slotNo);	    
//	    String predStr = rdfdatabase.predicateLabelHeapFile.getLabel(predLabel);
//	    //---------------------------------------------
//	    EID objEntity = aTriple.getObjectId();	    
//	    // maybe later we can just call objEntity.returnLID();
//	    LID objLabel = new LID(objEntity.pageNo, objEntity.slotNo);	    
//	    String objStr = rdfdatabase.entityLabelHeapFile.getLabel(objLabel);
//	    
//	    weHaveAMatch = tripleDataMatchesFilter(subjStr, objStr, predStr, confidenceFilter);
//	 // 3. Return the triple with matching TID
//	    if(weHaveAMatch)
//	    	return aTriple;
//	    else // no matches, keep moving down the line
//	    	return getNext();	    
//    	}
    
  }
    
  
} // end of class Stream
