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

	public static String[] dbNamelist = null;

	/**
	 * String[0]-file path String[1]-DB name String[2]-sort option
	 * 
	 * @param args
	 * @throws LHFDiskMgrException
	 * @throws LHFBufMgrException
	 * @throws LHFException
	 * @throws THFDiskMgrException
	 * @throws THFBufMgrException
	 * @throws THFException
	 * @throws NumberFormatException
	 * @throws DiskMgrException
	 * @throws FileIOException
	 * @throws InvalidPageNumberException
	 * @throws SpaceNotAvailableException
	 * @throws InvalidLabelSizeException
	 * @throws InvalidSlotNumberException
	 * @throws tripleheap.InvalidSlotNumberException
	 * @throws tripleheap.SpaceNotAvailableException
	 * @throws InvalidTripleSizeException
	 * @throws InvalidTupleSizeException
	 */
	public static void main(String[] args) throws IOException,
			NumberFormatException, THFException, THFBufMgrException,
			THFDiskMgrException, LHFException, LHFBufMgrException,
			LHFDiskMgrException, InvalidPageNumberException, FileIOException,
			DiskMgrException, InvalidSlotNumberException,
			InvalidLabelSizeException, SpaceNotAvailableException,
			tripleheap.InvalidSlotNumberException, InvalidTupleSizeException,
			tripleheap.SpaceNotAvailableException, InvalidTripleSizeException {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String DBName = args[1];
		String indexOption = args[2];
		boolean dbExists = false;

		if (dbExists == false) {
			// SystemDefs sysdef = new SystemDefs(DBName, 8193, 100, "Clock");
			rdfDB newDatabase = new rdfDB(Integer.parseInt(indexOption));
			newDatabase.openDB(DBName);

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
				EID subjectID = new EID();
				EID objectID = new EID();
				PID predicateID = new PID();
				TID tripleID = new TID();
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
				byte[] subjectBArray = {};
				Convert.setStrValue(subject, 0, subjectBArray);

				byte[] objectBArray = {};
				Convert.setStrValue(object, 0, objectBArray);

				byte[] predicateBArray = {};
				Convert.setStrValue(predicate, 0, predicateBArray);

				Triple tripleObj = new Triple();
				EID eid = new EID(subjectID.pageNo, subjectID.slotNo);
				tripleObj.setSubjectId(eid);
				eid = new EID(objectID.pageNo, objectID.slotNo);
				tripleObj.setObjectId(eid);
				PID pid = new PID(predicateID.pageNo, predicateID.slotNo);
				tripleObj.setPredicateId(pid);
				tripleObj.setConfidence(Float.parseFloat(confidence));

				System.out.println("subject" + subject);
				System.out.println("inserted..LID is" + subjectID);
				System.out.println("predicate" + predicate);
				System.out.println("inserted..LID is" + predicateID);
				System.out.println("object" + object);
				System.out.println("inserted..LID is" + objectID);
				System.out.println("confidence" + confidence);
				System.out.println("inserting..");
				System.out.println("inserted..TID is" + tripleID);
				LID subID;
				LID objID;
				LID predicID;
				byte[] triplebyte;
				Triple tripObj = new Triple();
				TID tripID = new TID();
				subID = newDatabase.entityLabelHeapFile
						.insertLabel(subjectBArray);
				objID = newDatabase.entityLabelHeapFile
						.insertLabel(objectBArray);
				predicID = newDatabase.predicateLabelHeapFile
						.insertLabel(predicateBArray);
				EID senID = new EID();
				EID oenID = new EID();
				PID penID = new PID();
				senID.pageNo = subID.pageNo;
				senID.slotNo = subID.slotNo;
				oenID.pageNo = objID.pageNo;
				oenID.slotNo = objID.slotNo;
				penID.pageNo = predicID.pageNo;
				penID.slotNo = predicID.slotNo;
				tripObj.setSubjectId(senID);
				tripObj.setPredicateId(penID);
				tripObj.setObjectId(oenID);
				triplebyte = tripObj.getTripleByteArray();
				tripID = newDatabase.insertTriple(triplebyte);
				System.out.println("inserted..SID is " + subID.pageNo + " "
						+ subID.slotNo);
				System.out.println("inserted..PID is " + objID.pageNo + " "
						+ objID.slotNo);
				System.out.println("inserted..OID is " + predicID.pageNo + " "
						+ predicID.slotNo);
				System.out.println("inserting..");
				System.out.println("inserted..TID is" + tripleID);
			}
			newDatabase.closeDB();
		}

	}

	public static void run(String path, String dbName, String sortOption)
			throws IOException, NumberFormatException, THFException,
			THFBufMgrException, THFDiskMgrException, LHFException,
			LHFBufMgrException, LHFDiskMgrException,
			InvalidPageNumberException, FileIOException, DiskMgrException,
			InvalidSlotNumberException, InvalidLabelSizeException,
			SpaceNotAvailableException, tripleheap.InvalidSlotNumberException,
			InvalidTupleSizeException, tripleheap.SpaceNotAvailableException,
			InvalidTripleSizeException {

		String[] inputToMain = { path, dbName, sortOption };
		inputToMain[0] = path;
		main(inputToMain);
	}
}