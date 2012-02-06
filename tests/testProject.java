package tests;

import java.io.*;
import java.lang.*;
import java.util.regex.*;

import javax.print.DocFlavor.STRING;

import labelheap.InvalidLabelSizeException;
import labelheap.InvalidSlotNumberException;
import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;
import labelheap.LHFException;
import labelheap.Label;
import labelheap.LabelHeapFile;
import tripleheap.InvalidTripleSizeException;
import tripleheap.THFBufMgrException;
import tripleheap.THFDiskMgrException;
import tripleheap.THFException;
import tripleheap.Triple;

import diskmgr.Stream;
import diskmgr.rdfDB;
import global.*;
import btree.*;
/**
 * @author shodhan
 * 
 */
public class testProject {

	/**
	 * @param args
	 */
	static Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
	
	public static void main(String[] args) throws InvalidSlotNumberException, InvalidLabelSizeException, LHFException, LHFDiskMgrException, LHFBufMgrException, Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		for (;;) {
			System.out.println("[1] Batch Insert");
			System.out.println("[2] Query your Database ");
			System.out.println("[3] Quit");
			System.out.println("[4] print btree");
			System.out.println("Select your option: ");
			String selectedOption = null;
			try {
				selectedOption = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (Integer.parseInt(selectedOption)) {
			case 1:
				String filePath = null;
				String DBName = null;
				String indexOption = null;
				System.out.println("enter filepath to insert");
				try {
					filePath = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				File f = new File(filePath);
				System.out
						.println(f
								+ (f.exists() ? " file is found "
										: " file is missing "));
				if (f.exists() == false)
					break;
				System.out.println("enter RDFDBNAME");
				try {
					DBName = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!match(DBName)) {
					System.out.println("Database not found");
					break;
				}
				System.out.println("enter Index option");
				System.out.println("1--Unclustered Index");
				try {
					indexOption = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				int intIndexOption = Integer.parseInt(indexOption);
				if (intIndexOption != 1) {
					System.out.println("invalid index option");
					break;
				}
				if (filePath != null && DBName != null && indexOption != null) {
					BatchInsert.run(filePath, DBName, indexOption);
					
				} else {
					System.out.println("please enter valid details to insert");
				}
				
				break;
			case 2:
				String dBName = null;
				String sortOption = null;
				String subjectFilter = null;
				String predicateFilter = null;
				String objectFilter = null;
				String confidenceFilter = null;
				String numBuf = null;

				System.out.println("You entered : " + selectedOption);
				System.out.println("enter RDFDBNAME");
				try {
					dBName = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!match(dBName)) {
					System.out.println("Database name is invalid");
					break;
				} else {
					RandomAccessFile fp = new RandomAccessFile(dBName, "rw");
					try {
						fp.getFilePointer();
					} catch (IOException e) {
						System.err.print("Database does not exist");
					}
				}
				System.out.println("Enter the Sortoption : ");
				try {
					sortOption = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Enter the SubjectFilter : ");
				try {
					subjectFilter = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Enter the PredicateFilter: ");
				try {
					predicateFilter = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Enter the Objectfilter: ");
				try {
					objectFilter = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Enter the ConfidenceFilter: ");
				try {
					confidenceFilter = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Enter the Number of Buffer pages: ");
				try {
					numBuf = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SystemDefs sysdef = new SystemDefs(dBName, 81930, 1000, "Clock");
					//SystemDefs.JavabaseDB = new rdfDB(Integer.parseInt(sortOption));
				
				Stream outStream;
				try {
					outStream = new Stream(SystemDefs.JavabaseDB,
							Integer.parseInt(sortOption), subjectFilter,
							predicateFilter, objectFilter,
							Float.parseFloat(confidenceFilter));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTripleSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				{
					outStream = SystemDefs.JavabaseDB.openStream(
							Integer.parseInt(sortOption), subjectFilter,
							predicateFilter, objectFilter,
							Float.parseFloat(confidenceFilter));
					TID tid = new TID();
					EID sLid = new EID();
					PID pLid = new PID();
					EID oLid = new EID();
					Triple triple = new Triple();
					try {
						while(triple != null){
							triple = outStream.getNext();
							sLid= triple.getSubjectId();
							pLid= triple.getPredicateId();
							oLid= triple.getObjectId();
							double confidence = triple.getConfidence();
							String subjectLabel=SystemDefs.JavabaseDB.entityLabelHeapFile.getLabel(sLid);
							String predicateLabel=SystemDefs.JavabaseDB.entityLabelHeapFile.getLabel(pLid);
							String objectLabel=SystemDefs.JavabaseDB.entityLabelHeapFile.getLabel(oLid);
							System.out.println("S:"+subjectLabel);
							System.out.println("P:"+predicateLabel);
							System.out.println("O:"+objectLabel);
							System.out.println("C:"+confidence);
							
						}
					} catch (InvalidTripleSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			case 3:
				System.out.println("exiting....");
				System.exit(0);

				System.out
						.println("You entered an invalid option:please enter again");
				break;
			case 4:
				System.out.println("printing BTREE....");
			}
		}
	}

	static boolean match(String s) {
		return p.matcher(s).matches();
	}
}
