/**
 * 
 */
package tests;

import diskmgr.*;
import global.*;

import java.io.*;
import java.lang.*;
import java.util.*;

import btree.AddFileEntryException;
import btree.BT;
import btree.BTreeFile;
import btree.ConstructPageException;
import btree.ConvertException;
import btree.DeleteRecException;
import btree.GetFileEntryException;
import btree.IndexInsertRecException;
import btree.IndexSearchException;
import btree.InsertException;
import btree.IteratorException;
import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import btree.LeafDeleteException;
import btree.LeafInsertRecException;
import btree.NodeNotMatchException;
import btree.PinPageException;
import btree.StringKey;
import btree.UnpinPageException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;

import labelheap.InvalidLabelSizeException;
import labelheap.InvalidSlotNumberException;
import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;
import labelheap.LHFException;
import labelheap.LabelHeapFile;
import labelheap.SpaceNotAvailableException;
import tripleheap.*;


/**
 * @author shodhan
 * 
 * 
 */


public class BatchInsert {
	public static BTreeFile globalTree=null;

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
	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws THFException
	 * @throws THFBufMgrException
	 * @throws THFDiskMgrException
	 * @throws LHFException
	 * @throws LHFBufMgrException
	 * @throws LHFDiskMgrException
	 * @throws InvalidPageNumberException
	 * @throws FileIOException
	 * @throws DiskMgrException
	 * @throws InvalidSlotNumberException
	 * @throws InvalidLabelSizeException
	 * @throws SpaceNotAvailableException
	 * @throws tripleheap.InvalidSlotNumberException
	 * @throws InvalidTupleSizeException
	 * @throws tripleheap.SpaceNotAvailableException
	 * @throws InvalidTripleSizeException
	 */
	public static int entityCount=0;
	public static int predicateCount=0;
	public static int tripleCount=0;
	public static boolean excase=false;
	public  BTreeFile btreeFile;
	public static int keyType;
	public static void main(String[] args) throws IOException,
			NumberFormatException, THFException, THFBufMgrException,
			THFDiskMgrException, LHFException, LHFBufMgrException,
			LHFDiskMgrException, InvalidPageNumberException, FileIOException,
			DiskMgrException, InvalidSlotNumberException,
			InvalidLabelSizeException, SpaceNotAvailableException,
			tripleheap.InvalidSlotNumberException, InvalidTupleSizeException,
			tripleheap.SpaceNotAvailableException, InvalidTripleSizeException, GetFileEntryException, ConstructPageException, AddFileEntryException, labelheap.InvalidTupleSizeException, IteratorException, HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException, KeyTooLongException, KeyNotMatchException, LeafInsertRecException, IndexInsertRecException, UnpinPageException, PinPageException, NodeNotMatchException, ConvertException, DeleteRecException, IndexSearchException, LeafDeleteException, InsertException {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String DBName = args[1];
		String indexOption = args[2];
		boolean dbExists = false;
		if (dbExists == false) {
			SystemDefs sysdef = new SystemDefs(DBName, 819300, 10000, "Clock");
			//rdfDB newDatabase = new rdfDB(Integer.parseInt(indexOption));
			//newDatabase.openDB(DBName);
			TripleHeapFile tripleFileObj= new TripleHeapFile("file_1");
			LabelHeapFile entlabelfileObj= new LabelHeapFile("file_2");
			LabelHeapFile prelabelFileObj= new LabelHeapFile("file_3");
			LabelHeapFile dummyLabelFileObj= new LabelHeapFile("file_4");
			String[] fileArray;
			System.out.println("inserting file at " + args[0] + " in "
					+ args[1] + "...");
			ReadFile readFile = new ReadFile(filePath);
			fileArray = readFile.openFile();
			keyType=AttrType.attrString;
			BTreeFile btreeFile=new BTreeFile(DBName, keyType, 1000, 1);
			globalTree=btreeFile;
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
				String confidenceForKey;
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
					//System.out.println("seperated");
					subject = new String(subjectArray).trim();
					predicate = new String(predicateArray).trim();
					object = new String(objectArray).trim();
					confidence = new String(confidenceArray).trim();
					if(subject!=null)
					{
						entityCount++;
					}
					if(predicate!=null)
					{
						predicateCount++;
					}
					if(object!=null)
					{
						entityCount++;
					}
				tripleCount++;
					
				}
				byte [] subjectBArray = new byte[150];
				Convert.setStrValue(subject, 0, subjectBArray);

				byte[] objectBArray = new byte[150];
				Convert.setStrValue(object, 0, objectBArray);

				byte[] predicateBArray = new byte[150];
				Convert.setStrValue(predicate, 0, predicateBArray);

				/*Triple tripleObj = new Triple();
				EID eid = new EID(subjectID.pageNo, subjectID.slotNo);
				tripleObj.setSubjectId(eid);
				eid = new EID(objectID.pageNo, objectID.slotNo);
				tripleObj.setObjectId(eid);
				PID pid = new PID(predicateID.pageNo, predicateID.slotNo);
				tripleObj.setPredicateId(pid);
				tripleObj.setConfidence(Float.parseFloat(confidence));*/

				/*System.out.println("subject" + subject);
				System.out.println("inserted..LID is" + subjectID);
				System.out.println("predicate" + predicate);
				System.out.println("inserted..LID is" + predicateID);
				System.out.println("object" + object);
				System.out.println("inserted..LID is" + objectID);
				System.out.println("confidence" + confidence);
				System.out.println("inserting..");
				System.out.println("inserted..TID is" + tripleID);*/
				LID subID;
				LID objID;
				LID predicID;
				Triple tripleObj = new Triple();
				byte[] triplebyte;
				Triple tripObj = new Triple();
				TID tripID = new TID();
				subID = entlabelfileObj
						.insertLabel(subjectBArray);
				objID = entlabelfileObj
						.insertLabel(objectBArray);
				predicID = prelabelFileObj
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
				tripleObj.setConfidence(Float.parseFloat(confidence));
				triplebyte = tripObj.getTripleByteArray();
				LID dummyTriple=new LID();
				//tripID = dummyLabelFileObj.insertLabel(triplebyte);
				dummyTriple = dummyLabelFileObj.insertLabel(triplebyte);
				System.out.println("inserted..SID is  p: " + subID.pageNo + " "
						+ " S "+subID.slotNo);
				System.out.println("inserted..PID is p: " + objID.pageNo + " "
						+ " S "+objID.slotNo);
				System.out.println("inserted..OID is  p: " + predicID.pageNo + " "
						+ " S "+predicID.slotNo);
				System.out.println("inserting..");
				System.out.println("inserted..TID is p: " + dummyTriple.pageNo+"  "+" S "+dummyTriple.slotNo);
				//createBtree("file_2");
				TID tid=new TID();
				tid.pageNo.pid=dummyTriple.pageNo.pid;
				tid.slotNo=dummyTriple.slotNo;
				Float floatkey=Float.parseFloat(confidence);
				confidenceForKey=confidence.substring(0,8);
				//System.out.println(confidenceForKey+"    "+confidenceForKey.length());
				if(excase==false)
				{
				TID newtid=new TID(dummyTriple.pageNo,dummyTriple.slotNo);
				String subjectForKey=null ;
				String predicateForKey=null ;
				String objectForKey=null;
				String compositeKey =null;
				String confidenceKey=null;
				KeyClass crealKey,srealKey,prealKey,orealKey;
				//prealKey=new StringKey(confidenceForKey);
				//srealKey=new StringKey(confidenceForKey);
				//orealKey=new StringKey(confidenceForKey);
					switch(Integer.parseInt(args[2])){
					case 1:
						compositeKey = subject.substring(0,subject.length()).concat(predicate.substring(0,predicate.length()/2).concat( object.substring(0,object.length()/2).concat(confidenceForKey))).toLowerCase();
						crealKey=new StringKey(compositeKey);
					    btreeFile.insert(crealKey, newtid);
					    System.out.println("key  "+crealKey+"    "+crealKey.toString().length());
					    break;
					case 2:
						//System.out.println(subject.substring(0,8)+predicate);
						compositeKey = predicate.substring(0, predicate.length()/2).concat(subject.substring(0,subject.length()/2).concat(object.substring(0,object.length()/2).concat(confidenceForKey))).toLowerCase();
						prealKey=new StringKey(compositeKey);
				        btreeFile.insert(prealKey, newtid);
				        System.out.println("key  "+prealKey+"    "+prealKey.toString().length());
				        break;
				    case 3:
				    	compositeKey = subject.substring(0,subject.length()/2).concat(confidenceForKey).toLowerCase();
				    	srealKey=new StringKey(compositeKey);
					    btreeFile.insert(srealKey, newtid);
					    System.out.println("key  "+srealKey+"    "+srealKey.toString().length());
					    break;
					case 4:
						compositeKey = predicate.substring(0,predicate.length()/2).concat(confidenceForKey).toLowerCase();
						orealKey=new StringKey(compositeKey);
				        btreeFile.insert(orealKey, newtid);
				        System.out.println("key   "+orealKey+"    "+orealKey.toString().length());
				        break;
				    case 5:
				    	compositeKey =  object.substring(0,object.length()/2).concat(confidenceForKey).toLowerCase();
				    	orealKey=new StringKey(compositeKey);
				        btreeFile.insert(orealKey, newtid);
				        System.out.println("key  "+orealKey+"    "+orealKey.toString().length());
				        break;
				    case 6:
				    	KeyClass realKey;
						realKey=new StringKey(confidenceForKey);
						btreeFile.insert(realKey, newtid);
						System.out.println("key  "+realKey+"    "+realKey.toString().length());
						break;
					}
				}
				if (excase==false)
				{
				switch(Integer.parseInt(args[2])){
				case 1:
					System.out.println("Printing B+ Tree : Indexed on <subject,predicate,object,confidence> in order");
				    break;
				case 2:
					System.out.println("Printing B+ Tree : Indexed on <pricate,subject,object,confidence> in order");
			        break;
			    case 3:
			    	System.out.println("Printing B+ Tree : Indexed on <subject,confidence> in order");
				    break;
				case 4:
					System.out.println("Printing B+ Tree : Indexed on <predicate,confidence> in order");
			        break;
			    case 5:
			    	System.out.println("Printing B+ Tree : Indexed on <object,confidence> in order");
			        break;
			    case 6:
			    	System.out.println("Printing B+ Tree : Indexed on <confidence>");
					break;
				}
				BT.printAllLeafPages(btreeFile.getHeaderPage());
				}
			}
			
					
			}
		SystemDefs.JavabaseDB.closeDB();
			//newDatabase.closeDB();
		}


	public static void run(String path, String dbName, String sortOption ,boolean excase1 )
			throws IOException, NumberFormatException, THFException,
			THFBufMgrException, THFDiskMgrException, LHFException,
			LHFBufMgrException, LHFDiskMgrException,
			InvalidPageNumberException, FileIOException, DiskMgrException,
			InvalidSlotNumberException, InvalidLabelSizeException,
			SpaceNotAvailableException, tripleheap.InvalidSlotNumberException,
			InvalidTupleSizeException, tripleheap.SpaceNotAvailableException,
			InvalidTripleSizeException, GetFileEntryException, ConstructPageException, AddFileEntryException, labelheap.InvalidTupleSizeException, IteratorException, HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException, KeyTooLongException, KeyNotMatchException, LeafInsertRecException, IndexInsertRecException, UnpinPageException, PinPageException, NodeNotMatchException, ConvertException, DeleteRecException, IndexSearchException, LeafDeleteException, InsertException {

		String[] inputToMain = { path, dbName, sortOption };
		inputToMain[0] = path;
		excase=excase1;
		main(inputToMain);
	}

	
}
