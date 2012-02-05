package tests;

import java.io.*;
import java.lang.*;
import java.util.regex.*;

import javax.print.DocFlavor.STRING;

import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;
import labelheap.LHFException;
import labelheap.LabelHeapFile;
import tripleheap.InvalidTripleSizeException;
import tripleheap.THFBufMgrException;
import tripleheap.THFDiskMgrException;
import tripleheap.THFException;
import tripleheap.Triple;

import diskmgr.Stream;
import diskmgr.rdfDB;
import global.*;

/**
 * @author shodhan
 * 
 */
public class testProject {

	/**
	 * @param args
	 */
	static Pattern p = Pattern.compile("^[A-Za-z0-9]+$");

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		for (;;) {
			System.out.println("[1] Batch Insert");
			System.out.println("[2] Query your Database ");
			System.out.println("[3] Quit");
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
					DBName = reader.readLine();
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
				rdfDB queryDB = null;

				try {
					queryDB = new rdfDB(Integer.parseInt(sortOption));
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

				Stream outStream;
				try {
					outStream = new Stream(queryDB,
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
					outStream = queryDB.openStream(
							Integer.parseInt(sortOption), subjectFilter,
							predicateFilter, objectFilter,
							Float.parseFloat(confidenceFilter));
					TID tid = new TID();
					EID sLid = new EID();
					PID pLid = new PID();
					EID oLid = new EID();
					double confidence;
					Triple triple = new Triple();
					try {
						while(triple != null){
							triple = outStream.getNext(tid);
							sLid= triple.getSubjectId();
							pLid= triple.getPredicateId();
							oLid= triple.getObjectId();
							confidence = triple.getConfidence();
							LabelHeapFile
							
						
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
			}
		}
	}

	static boolean match(String s) {
		return p.matcher(s).matches();
	}
}
