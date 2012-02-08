package tests;

import java.io.*;

import labelheap.InvalidLabelSizeException;
import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;

import tripleheap.InvalidSlotNumberException;
import tripleheap.InvalidTripleSizeException;
import tripleheap.InvalidTupleSizeException;
import tripleheap.THFBufMgrException;
import tripleheap.THFDiskMgrException;
import diskmgr.*;
public class Report {
/**
 * Prints the statistics of RDF Database
 * @throws IOException 
 * @throws InvalidTripleSizeException 
 * @throws THFBufMgrException 
 * @throws THFDiskMgrException 
 * @throws InvalidTupleSizeException 
 * @throws InvalidSlotNumberException 
 * @throws labelheap.InvalidSlotNumberException 
 * @throws LHFBufMgrException 
 * @throws LHFDiskMgrException 
 * @throws InvalidLabelSizeException 
 */
public void printreport() //throws InvalidSlotNumberException, InvalidTupleSizeException, THFDiskMgrException, THFBufMgrException, InvalidTripleSizeException, IOException, labelheap.InvalidSlotNumberException, InvalidLabelSizeException, LHFDiskMgrException, LHFBufMgrException
{
	//rdfDB r = new rdfDB()
System.out.println(" Various statistics of RDF Database " );
/*try {
	System.out.println(" Total number of Triples is " + r.getTripleCnt());
} catch (InvalidSlotNumberException | InvalidTupleSizeException
		| THFDiskMgrException | THFBufMgrException | InvalidTripleSizeException
		| IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
try {
	System.out.println(" Total number of Entities is " + r.getEntityCnt());
} catch (labelheap.InvalidSlotNumberException | InvalidLabelSizeException
		| LHFDiskMgrException | LHFBufMgrException | IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
try {
	System.out.println(" Total number of Predicates is " + r.getPredicateCnt());
} catch (labelheap.InvalidSlotNumberException | InvalidLabelSizeException
		| LHFDiskMgrException | LHFBufMgrException | IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}*/
System.out.println(" Total number of Disk Pages that were read is " + (new PCounter()).rcounter);
System.out.println(" Total number of Disk Pages that were written is " + (new PCounter()).wcounter);
}
}