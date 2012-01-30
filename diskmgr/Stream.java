package diskmgr;

import java.io.*;
import global.*;
import bufmgr.*;

public class Stream implements GlobalConst{
	
	/**
	 * Constructor
	 * @param rdfdatabase
	 * @param orderType
	 * @param subjectFilter
	 * @param predicateFilter
	 * @param objectFilter
	 * @param confidenceFilter
	 */
	public Stream(rdfDB rdfdatabase, int orderType, String subjectFilter, 
				  String predicateFilter, String objectFilter, 
				  double confidenceFilter){
		
	}
	
	/**
	 * Closes the stream object
	 */
	public void closeStream(){
		
	}
	
	/**
	 * @param tid
	 * @return The next triple in the stream
	 */
	public Triple getNext(TID tid){
		
	}

}
