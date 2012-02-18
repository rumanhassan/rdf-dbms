/**
 * 
 */
package tests;

import diskmgr.*;
import global.*;

import iterator.LabelUtils;

import java.io.*;
import java.lang.*;
import java.util.*;

import btree.AddFileEntryException;
import btree.BT;
import btree.BTFileScan;
import btree.BTreeFile;
import btree.ConstructPageException;
import btree.ConvertException;
import btree.DataClass;
import btree.DeleteRecException;
import btree.GetFileEntryException;
import btree.IndexInsertRecException;
import btree.IndexSearchException;
import btree.InsertException;
import btree.IteratorException;
import btree.KeyClass;
import btree.KeyDataEntry;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import btree.LeafData;
import btree.LeafDeleteException;
import btree.LeafInsertRecException;
import btree.NodeNotMatchException;
import btree.PinPageException;
import btree.ScanIteratorException;
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
	public static BTreeFile globalTree1 = null;
	public static BTreeFile globalTree2 = null;
	public static BTreeFile globalTree3 = null;
	public static BTreeFile globalTree4 = null;
	public static BTreeFile globalTree5 = null;
	public static BTreeFile globalTree6 = null;
	public static BTreeFile labelGlobalTree1 = null;
	public static BTreeFile labelGlobalTree2 = null;
	public static TripleHeapFile globalTripleHeapFile = null;
	public static LabelHeapFile globalEntityHeapFile = null;
	public static LabelHeapFile globalPredicateHeapFile = null;
	public static String[] dbNamelist = null;
	public static int entityCount = 0;
	public static int predicateCount = 0;
	public static int tripleCount = 0;
	public static boolean excase = false;
	public BTreeFile btreeFile;
	public static int keyType;

	public static void main(String[] args) throws IOException,
			NumberFormatException, THFException, THFBufMgrException,
			THFDiskMgrException, LHFException, LHFBufMgrException,
			LHFDiskMgrException, InvalidPageNumberException, FileIOException,
			DiskMgrException, InvalidSlotNumberException,
			InvalidLabelSizeException, SpaceNotAvailableException,
			tripleheap.InvalidSlotNumberException, InvalidTupleSizeException,
			tripleheap.SpaceNotAvailableException, InvalidTripleSizeException,
			GetFileEntryException, ConstructPageException,
			AddFileEntryException, labelheap.InvalidTupleSizeException,
			IteratorException, HashEntryNotFoundException,
			InvalidFrameNumberException, PageUnpinnedException,
			ReplacerException, KeyTooLongException, KeyNotMatchException,
			LeafInsertRecException, IndexInsertRecException,
			UnpinPageException, PinPageException, NodeNotMatchException,
			ConvertException, DeleteRecException, IndexSearchException,
			LeafDeleteException, InsertException, ScanIteratorException {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String DBName = args[1];
		String indexOption = args[2];
		boolean dbExists = false;
		if (dbExists == false) {
			
			SystemDefs sysdef = new SystemDefs(DBName, 819300, 10000, "Clock");
			keyType = AttrType.attrString;
			BTreeFile btreeFile1 = new BTreeFile("btreefile1", keyType, 1000, 1);
			BTreeFile btreeFile2 = new BTreeFile("btreefile2", keyType, 1000, 1);
			BTreeFile btreeFile3 = new BTreeFile("btreefile3", keyType, 1000, 1);
			BTreeFile btreeFile4 = new BTreeFile("btreefile4", keyType, 1000, 1);
			BTreeFile btreeFile5 = new BTreeFile("btreefile5", keyType, 1000, 1);
			BTreeFile btreeFile6 = new BTreeFile("btreefile6", keyType, 1000, 1);
			// creating 2 btrees on entityLF and predicateLF
			BTreeFile labelBtreeFile1 = new BTreeFile("labelbtreefile1",
					keyType, 1000, 1);
			BTreeFile labelBtreeFile2 = new BTreeFile("labelbtreefile2",
					keyType, 1000, 1);
			globalTree1 = btreeFile1;
			globalTree2 = btreeFile2;
			globalTree3 = btreeFile3;
			globalTree4 = btreeFile4;
			globalTree5 = btreeFile5;
			globalTree6 = btreeFile6;
			SystemDefs.JavabaseDB.bTreeIndexFile1=globalTree1;
			SystemDefs.JavabaseDB.bTreeIndexFile2=globalTree2;
			SystemDefs.JavabaseDB.bTreeIndexFile3=globalTree3;
			SystemDefs.JavabaseDB.bTreeIndexFile4=globalTree4;
			SystemDefs.JavabaseDB.bTreeIndexFile5=globalTree5;
			SystemDefs.JavabaseDB.bTreeIndexFile6=globalTree6;
			// global trees for entityLF and predicateLF
			labelGlobalTree1 = labelBtreeFile1;
			labelGlobalTree2 = labelBtreeFile2;
			// rdfDB newDatabase = new rdfDB(Integer.parseInt(indexOption));
			// newDatabase.openDB(DBName);
			// TripleHeapFile tripleFileObj= new TripleHeapFile("file_1");
			LabelHeapFile entlabelfileObj = new LabelHeapFile("file_2");
			LabelHeapFile prelabelFileObj = new LabelHeapFile("file_3");
			TripleHeapFile dummyLabelFileObj = new TripleHeapFile("file_4");
			globalEntityHeapFile = entlabelfileObj;
			globalPredicateHeapFile = prelabelFileObj;
			globalTripleHeapFile = dummyLabelFileObj;
			SystemDefs.JavabaseDB.tripleHeapFile=globalTripleHeapFile;
			SystemDefs.JavabaseDB.entityLabelHeapFile=globalEntityHeapFile;
			SystemDefs.JavabaseDB.predicateLabelHeapFile=globalPredicateHeapFile;
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
					// System.out.println("seperated");
					subject = new String(subjectArray).trim();
					predicate = new String(predicateArray).trim();
					object = new String(objectArray).trim();
					confidence = new String(confidenceArray).trim();
					if (subject != null) {
						entityCount++;
					}
					if (predicate != null) {
						predicateCount++;
					}
					if (object != null) {
						entityCount++;
					}

				}
				byte[] subjectBArray = LabelUtils
						.convertStringToByteArray(subject);// subject.getBytes();//new
															// byte[subject.length()*2];
				// Convert.setStrValue(subject, 0, subjectBArray);

				byte[] objectBArray = LabelUtils
						.convertStringToByteArray(object);// object.getBytes();
															// //new
															// byte[object.length()*2];
				// Convert.setStrValue(object, 0, objectBArray);

				byte[] predicateBArray = LabelUtils
						.convertStringToByteArray(predicate);// predicate.getBytes();//new
																// byte[predicate.length()*2];
				// Convert.setStrValue(predicate, 0, predicateBArray);

				/*
				 * Triple tripleObj = new Triple(); EID eid = new
				 * EID(subjectID.pageNo, subjectID.slotNo);
				 * tripleObj.setSubjectId(eid); eid = new EID(objectID.pageNo,
				 * objectID.slotNo); tripleObj.setObjectId(eid); PID pid = new
				 * PID(predicateID.pageNo, predicateID.slotNo);
				 * tripleObj.setPredicateId(pid);
				 * tripleObj.setConfidence(Float.parseFloat(confidence));
				 */

				/*
				 * System.out.println("subject" + subject);
				 * System.out.println("inserted..LID is" + subjectID);
				 * System.out.println("predicate" + predicate);
				 * System.out.println("inserted..LID is" + predicateID);
				 * System.out.println("object" + object);
				 * System.out.println("inserted..LID is" + objectID);
				 * System.out.println("confidence" + confidence);
				 * System.out.println("inserting..");
				 * System.out.println("inserted..TID is" + tripleID);
				 */
				LID subID;
				LID objID;
				LID predicID;
				boolean insertSubject = false;
				boolean insertPredicate = false;
				boolean insertObject = false;
				Triple tripleObj = new Triple();
				byte[] triplebyte;
				Triple tripObj = new Triple();
				TID tripID = new TID();
				// checking for duplicate subjects
				BTFileScan entityLHF = labelBtreeFile1.new_scan(null, null);
				KeyDataEntry keyData = null;
				KeyClass keyClass = null;
				do {
					keyData = entityLHF.get_next();
					if (keyData == null) {
						break;
					}
					keyClass = keyData.key;
					String treeKey = keyClass.toString();
					if (treeKey.equalsIgnoreCase(subject)) {
						insertSubject = true;
						break;
					}

				} while (keyData != null);
				if (insertSubject != true) {
					subID = entlabelfileObj.insertLabel(subjectBArray);
					KeyClass erealKey;
					String eKey = subject.substring(0, subject.length())
							.toLowerCase();
					erealKey = new StringKey(eKey);
					// inserting into entity btree if not duplicate
					labelBtreeFile1.insert(erealKey, subID.returnTid());
				} else {
					LeafData dummyLeaf = null;
					DataClass indexData = dummyLeaf;
					indexData = keyData.data;
					LeafData currTreeNode = (LeafData) indexData;
					subID = currTreeNode.getData().returnLID();
				}
				// checking for duplicate predicates
				entityLHF = labelBtreeFile2.new_scan(null, null);
				do {
					keyData = entityLHF.get_next();
					if (keyData == null) {
						break;
					}
					keyClass = keyData.key;
					String treeKey = keyClass.toString();
					if (treeKey.equalsIgnoreCase(predicate)) {
						insertPredicate = true;
						break;
					}

				} while (keyData != null);
				if (insertPredicate != true) {
					predicID = prelabelFileObj.insertLabel(predicateBArray);
					KeyClass plrealKey;
					String pKey = predicate.substring(0, predicate.length())
							.toLowerCase();
					plrealKey = new StringKey(pKey);
					// inserting into predicate btree
					labelBtreeFile2.insert(plrealKey, predicID.returnTid());
				} else {
					LeafData dummyLeaf = null;
					DataClass indexData = dummyLeaf;
					indexData = keyData.data;
					LeafData currTreeNode = (LeafData) indexData;
					predicID = currTreeNode.getData().returnLID();
				}
				// checking for duplicate objects
				entityLHF = labelBtreeFile1.new_scan(null, null);
				do {
					keyData = entityLHF.get_next();
					if (keyData == null) {
						break;
					}
					keyClass = keyData.key;
					String treeKey = keyClass.toString();
					if (treeKey.equalsIgnoreCase(object)) {
						insertObject = true;
						break;
					}

				} while (keyData != null);
				if (insertObject != true) {
					objID = entlabelfileObj.insertLabel(objectBArray);
					// inserting into entity btree
					KeyClass olrealKey;
					String oKey = object.substring(0, object.length())
							.toLowerCase();
					olrealKey = new StringKey(oKey);
					labelBtreeFile1.insert(olrealKey, objID.returnTid());
				} else {
					LeafData dummyLeaf = null;
					DataClass indexData = dummyLeaf;
					indexData = keyData.data;
					//
					LeafData currTreeNode = (LeafData) indexData;
					objID = currTreeNode.getData().returnLID();
				}
				EID senID = new EID();
				EID oenID = new EID();
				PID penID = new PID();
				senID.pageNo = new PageID();
				senID.pageNo.pid = subID.pageNo.pid;
				senID.slotNo = subID.slotNo;

				oenID.pageNo = new PageID();
				oenID.pageNo.pid = objID.pageNo.pid;
				oenID.slotNo = objID.slotNo;

				penID.pageNo = new PageID();
				penID.pageNo.pid = predicID.pageNo.pid;
				penID.slotNo = predicID.slotNo;

				tripObj.setSubjectId(senID);
				tripObj.setPredicateId(penID);
				tripObj.setObjectId(oenID);
				tripObj.setConfidence(Float.parseFloat(confidence));

				triplebyte = getTripleByteArray(tripObj);

				TID dummyTriple = new TID();
				// tripID = dummyLabelFileObj.insertLabel(triplebyte);
				// cheking for duplicate triples
				Stream outStream = null;

				/*
				 * outStream = SystemDefs.JavabaseDB.openStream(
				 * Integer.parseInt(sortOption), subjectFilter, predicateFilter,
				 * objectFilter, Float.parseFloat(confidenceFilter));
				 */
				boolean dupTriple = false;
				if (tripleCount != 0) {
					try {
						outStream = new Stream(SystemDefs.JavabaseDB,
								Integer.parseInt(args[2]), subject, predicate,
								object, Float.parseFloat(confidence));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidTripleSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Triple triple = new Triple();

						dupTriple = outStream.checkForDuplicates();
				}
				if (dupTriple == false) {
					dummyTriple = dummyLabelFileObj.insertTriple(triplebyte);
					tripleCount++;
				}
				if (dupTriple == false) {
				System.out.println("inserted..SID is  p: " + subID.pageNo + " "
						+ " S " + subID.slotNo);
				System.out.println("inserted..PID is p: " + predicID.pageNo + " " 
						+ " S " + predicID.slotNo);
				System.out.println("inserted..OID is  p: " + objID.pageNo
						+ " " + " S " + objID.slotNo);
				System.out.println("inserting..");
				
					System.out.println("inserted..TID is p: "
							+ dummyTriple.pageNo + "  " + " S "
							+ dummyTriple.slotNo);
				} else {
					System.out.println("duplicate triple...so not inserted ");
				}
				// createBtree("file_2");
				TID tid = new TID();
				tid.pageNo.pid = dummyTriple.pageNo.pid;
				tid.slotNo = dummyTriple.slotNo;
				Float floatkey = Float.parseFloat(confidence);
				confidenceForKey = confidence.substring(0, 8);
				// System.out.println(confidenceForKey+"    "+confidenceForKey.length());
				if (excase == false && dupTriple==false) {
					TID newtid = new TID(dummyTriple.pageNo, dummyTriple.slotNo);
					String subjectForKey = null;
					String predicateForKey = null;
					String objectForKey = null;
					String compositeKey = null;
					String confidenceKey = null;
					KeyClass crealKey, srealKey, prealKey, orealKey;
					// prealKey=new StringKey(confidenceForKey);
					// srealKey=new StringKey(confidenceForKey);
					// orealKey=new StringKey(confidenceForKey);
					// 1 btree
					compositeKey = subject
							.substring(0, subject.length()/2)
							.concat(predicate.substring(0,
									predicate.length() / 2).concat(
									object.substring(0, object.length() / 2)
											.concat(confidenceForKey)))
							.toLowerCase();
					crealKey = new StringKey(compositeKey);
					btreeFile1.insert(crealKey, newtid);
					/*
					 * System.out.println("key  " + crealKey + "    " +
					 * crealKey.toString().length());
					 */
					// 2 btree
					compositeKey = predicate
							.substring(0, predicate.length() / 2)
							.concat(subject.substring(0, subject.length() / 2)
									.concat(object.substring(0,
											object.length() / 2).concat(
											confidenceForKey))).toLowerCase();
					prealKey = new StringKey(compositeKey);
					btreeFile2.insert(prealKey, newtid);
					/*
					 * System.out.println("key  " + prealKey + "    " +
					 * prealKey.toString().length());
					 */
					// 3 btree
					compositeKey = subject.substring(0, subject.length() / 2)
							.concat(confidenceForKey).toLowerCase();
					srealKey = new StringKey(compositeKey);
					btreeFile3.insert(srealKey, newtid);
					/*
					 * System.out.println("key  " + srealKey + "    " +
					 * srealKey.toString().length());
					 */
					// 4 btree
					compositeKey = predicate
							.substring(0, predicate.length() / 2)
							.concat(confidenceForKey).toLowerCase();
					orealKey = new StringKey(compositeKey);
					btreeFile4.insert(orealKey, newtid);
					/*
					 * System.out.println("key   " + orealKey + "    " +
					 * orealKey.toString().length());
					 */
					// 5 btree
					compositeKey = object.substring(0, object.length() / 2)
							.concat(confidenceForKey).toLowerCase();
					orealKey = new StringKey(compositeKey);
					btreeFile5.insert(orealKey, newtid);
					/*
					 * System.out.println("key  " + orealKey + "    " +
					 * orealKey.toString().length());
					 */
					// 6 btree
					KeyClass realKey;
					realKey = new StringKey(confidenceForKey);
					btreeFile6.insert(realKey, newtid);
					/*
					 * System.out.println("key  " + realKey + "    " +
					 * realKey.toString().length());
					 */
				}
			}
			if (excase == false) {
				switch (Integer.parseInt(args[2])) {
				case 1:
					System.out
							.println("Printing B+ Tree : Indexed on <subject,predicate,object,confidence> in order");
					BT.printAllLeafPages(btreeFile1.getHeaderPage());
					break;
				case 2:
					System.out
							.println("Printing B+ Tree : Indexed on <pricate,subject,object,confidence> in order");
					BT.printAllLeafPages(btreeFile2.getHeaderPage());
					break;
				case 3:
					System.out
							.println("Printing B+ Tree : Indexed on <subject,confidence> in order");
					BT.printAllLeafPages(btreeFile3.getHeaderPage());
					break;
				case 4:
					System.out
							.println("Printing B+ Tree : Indexed on <predicate,confidence> in order");
					BT.printAllLeafPages(btreeFile4.getHeaderPage());
					break;
				case 5:
					System.out
							.println("Printing B+ Tree : Indexed on <object,confidence> in order");
					BT.printAllLeafPages(btreeFile5.getHeaderPage());
					break;
				case 6:
					System.out
							.println("Printing B+ Tree : Indexed on <confidence>");
					BT.printAllLeafPages(btreeFile6.getHeaderPage());
					break;
				}
			}

		}
		SystemDefs.JavabaseDB.closeDB();
		// newDatabase.closeDB();
	}

	public static void run(String path, String dbName, String sortOption,
			boolean excase1) throws IOException, NumberFormatException,
			THFException, THFBufMgrException, THFDiskMgrException,
			LHFException, LHFBufMgrException, LHFDiskMgrException,
			InvalidPageNumberException, FileIOException, DiskMgrException,
			InvalidSlotNumberException, InvalidLabelSizeException,
			SpaceNotAvailableException, tripleheap.InvalidSlotNumberException,
			InvalidTupleSizeException, tripleheap.SpaceNotAvailableException,
			InvalidTripleSizeException, GetFileEntryException,
			ConstructPageException, AddFileEntryException,
			labelheap.InvalidTupleSizeException, IteratorException,
			HashEntryNotFoundException, InvalidFrameNumberException,
			PageUnpinnedException, ReplacerException, KeyTooLongException,
			KeyNotMatchException, LeafInsertRecException,
			IndexInsertRecException, UnpinPageException, PinPageException,
			NodeNotMatchException, ConvertException, DeleteRecException,
			IndexSearchException, LeafDeleteException, InsertException,
			ScanIteratorException {

		String[] inputToMain = { path, dbName, sortOption };
		inputToMain[0] = path;
		excase = excase1;
		main(inputToMain);
	}

	private static byte[] getTripleByteArray(Triple tripObj) {
		byte[] triplecopy = new byte[28];
		try {
			Convert.setIntValue(tripObj.subjectId.slotNo, 0, triplecopy);
			Convert.setIntValue(tripObj.subjectId.pageNo.pid, 4, triplecopy);
			Convert.setIntValue(tripObj.predicateId.slotNo, 8, triplecopy);
			Convert.setIntValue(tripObj.predicateId.pageNo.pid, 12, triplecopy);
			Convert.setIntValue(tripObj.objectId.slotNo, 16, triplecopy);
			Convert.setIntValue(tripObj.objectId.pageNo.pid, 20, triplecopy);
			Convert.setFloValue(tripObj.value, 24, triplecopy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return triplecopy;
		/*
		 * byte [] triplecopy = new byte [triple_length]; System.arraycopy(data,
		 * triple_offset, triplecopy, 0, triple_length); return triplecopy;
		 */
	}

}
