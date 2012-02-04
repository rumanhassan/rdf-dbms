/**
 * 
 */
package tests;

import diskmgr.*;
import global.*;

import java.io.*;
import java.lang.*;
import java.util.*;

import labelheap.InvalidLabelSizeException;
import labelheap.InvalidSlotNumberException;
import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;
import labelheap.LHFException;
import labelheap.SpaceNotAvailableException;
import tripleheap.InvalidTripleSizeException;
import tripleheap.InvalidTupleSizeException;
import tripleheap.THFBufMgrException;
import tripleheap.THFDiskMgrException;
import tripleheap.THFException;
import tripleheap.Triple;

/**
 * @author shodhan
 * 
 */
public class BatchInsert {

	  public  static  String [] dbNamelist= null;
	/**
	 * String[0]-file path String[1]-DB name String[2]-sort option
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String DBName = args[1];
		String indexOption = args[2];
		boolean dbExists = false;
		if(dbNamelist!=null)
		{
		for (int i = 0; i < dbNamelist.length; i++) {
			if (DBName == dbNamelist[i])
				dbExists = true;
		}
		}
		if (dbExists == false) {
			rdfDB rd=null;
			try {
				rd = new rdfDB(Integer.parseInt(indexOption));
				SystemDefs sysdef = new SystemDefs( DBName, 8193,  100, "Clock" );
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (THFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (THFBufMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (THFDiskMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LHFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LHFBufMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LHFDiskMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*try {
				rd.openDB(DBName);
			} catch (InvalidPageNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DiskMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			String[] fileArray;
			System.out.println("inserting file at " + args[0] + " in "
					+ args[1] + "...");
			ReadFile readFile = new ReadFile(filePath);
			fileArray = readFile.openFile();
			for (int i = 0; i < fileArray.length; i++) {
				char[] lineString = fileArray[i].toCharArray();
				int lineLength = lineString.length;
				String subject = null;
				String predicate = null;
				String object = null;
				String confidence = null;
				EID subjectID=new EID();
				EID objectID=new EID();
				PID predicateID=new PID();
				TID tripleID=new TID();
				for (int charNo = 0; charNo < lineLength; charNo++) {
					char[] subjectArray = new char[100];
					char[] predicateArray = new char[100];
					char[] objectArray = new char[100];
					char[] confidenceArray = new char[100];
					int count = 0;
					// reading subject
					while (lineString[charNo] == ':') {
						charNo++;
					}
					while (lineString[charNo] != ':') {
						subjectArray[count] = lineString[charNo];
						count++;
						charNo++;
					}
					count = 0;
					charNo++;
					while (lineString[charNo] != ':') {
						predicateArray[count] = lineString[charNo];
						count++;
						charNo++;
					}
					charNo++;
					// reading object
					count = 0;
					while (lineString[charNo] != ' '
							&& lineString[charNo] != '\t') {
						objectArray[count] = lineString[charNo];
						count++;
						charNo++;
					}
					while (lineString[charNo] == (' ')
							|| lineString[charNo] == '\t') {
						charNo++;
					}
					// reading confidence
					count = 0;
					while (lineLength - 1 != charNo) {
						confidenceArray[count] = lineString[charNo];
						count++;
						charNo++;
					}
					System.out.println("seperated");
					subject = new String(subjectArray).trim();
					predicate = new String(predicateArray).trim();
					object = new String(objectArray).trim();
					confidence = new String(confidenceArray).trim();
				}
				byte [] subjectBArray={};
				Convert.setStrValue(subject, 0, subjectBArray);
				try {
					subjectID=rd.insertEntity(subjectBArray);
				} catch (InvalidSlotNumberException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidLabelSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SpaceNotAvailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFBufMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFDiskMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte [] objectBArray={};
				Convert.setStrValue(object, 0, objectBArray);
				try {
					objectID=rd.insertEntity(subjectBArray);
				} catch (InvalidSlotNumberException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidLabelSizeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SpaceNotAvailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LHFException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LHFBufMgrException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LHFDiskMgrException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				byte [] predicateBArray={};
				Convert.setStrValue(predicate, 0, predicateBArray);
				try {
					predicateID=rd.insertPredicate(predicateBArray);
				} catch (InvalidSlotNumberException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidLabelSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SpaceNotAvailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFBufMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LHFDiskMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Triple tripleObj=new Triple();
				EID eid=new EID(subjectID.pageNo,subjectID.slotNo);
				tripleObj.setSubjectId(eid);
				eid=new EID(objectID.pageNo,objectID.slotNo);
				tripleObj.setObjectId(eid);
				PID pid=new PID(predicateID.pageNo,predicateID.slotNo);
				tripleObj.setPredicateId(pid);
				tripleObj.setConfidence(Float.parseFloat(confidence));
				try {
					tripleID=rd.insertTriple(tripleObj.getTripleByteArray());
				} catch (tripleheap.InvalidSlotNumberException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTupleSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (tripleheap.SpaceNotAvailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (THFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (THFBufMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (THFDiskMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTripleSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("subject" + subject);
				System.out.println("inserted..LID is"+subjectID);
				System.out.println("predicate" + predicate);
				System.out.println("inserted..LID is"+predicateID);
				System.out.println("object" + object);
				System.out.println("inserted..LID is"+objectID);
				System.out.println("confidence" + confidence);
				System.out.println("inserting..");
				System.out.println("inserted..TID is"+tripleID);
			}
		}

	}

	public static void run(String path, String dbName, String sortOption)
			throws IOException {

		String[] inputToMain = { path, dbName, sortOption };
		inputToMain[0] = path;
		main(inputToMain);
	}
}
