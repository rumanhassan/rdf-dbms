/* File Stream.java */

package diskmgr;

import java.io.*;
import global.*;
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
	final int CONFIDX = 3;
	
	// attributes of the Stream
	private rdfDB rdfdatabase;
	private int orderType;
	private String subjectFilter;
	private String predicateFilter;
	private String objectFilter;
	private float confidenceFilter;
	private short[] filtersIncluded = {UNINITIALIZED, UNINITIALIZED, 
			UNINITIALIZED, UNINITIALIZED};
     
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
	   	   IOException
  {
	  this.rdfdatabase = rdfDataBase;
	  this.orderType = orderType;
	  this.subjectFilter = subjectFilter;
	  this.predicateFilter = predicateFilter;
	  this.objectFilter = objectFilter;
	  this.confidenceFilter = confidenceFilter;
	  processFilters(subjectFilter, predicateFilter,objectFilter, confidenceFilter);
  }
  
  /** Retrieve the next triple in the stream
   *
   * @exception InvalidTripleSizeException Invalid triple size
   * @exception IOException I/O errors
   *
   * @param tid Triple ID of the triple
   * @return the Triple object with the specified TID. If no such triple 
   * 		exists, return null.
   */
  public Triple getNext(TID tid) 
    throws InvalidTripleSizeException,
	   IOException
  {
    Triple recptrtriple = null;
    
    // TODO Write the code to perform the following actions:
    // 1. Use rdfDataBase's b-tree structure to get the next Triple from the
    //    triple heap file.    
    // 2. Check that the triple's data matches the filter
    // 3. Return the triple with matching TID
     
    return recptrtriple;
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
    	
    	float zero = 0;
        int i1 = Float.compare(zero,conf);
    	if( i1 == 0) // confidence filter equals 0
    		filtersIncluded[CONFIDX] = NOT_INCLUDED;
    	else filtersIncluded[CONFIDX] = INCLUDED;    	
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