package tests;

import java.io.*;
import java.lang.*;
import java.util.Scanner;
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
public static boolean excase=false;
public static boolean batchorquery=true;
	public static void main(String[] args) throws InvalidSlotNumberException, InvalidLabelSizeException, LHFException, LHFDiskMgrException, LHFBufMgrException, Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		for (;;) {
			System.out.println("[1] Batch Insert and index using unclusterd Btree");
			System.out.println("[2] Query your Database ");
			System.out.println("[3] Quit");
			System.out.println("[4] report Database statistics");
			System.out.println("[5] Do join Operation by uploading a file");
			//System.out.println("[5] Batch insert on Heap Tree without indexing");
			System.out.println("Select your option: ");
			String selectedOption = null;
			try {
				selectedOption = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//try{
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
				System.out.println("1||2||3||4||5||6||");
				try {
					indexOption = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				int intIndexOption = Integer.parseInt(indexOption);
				if (intIndexOption != 1&&intIndexOption != 2&&intIndexOption != 3&&intIndexOption != 4&&intIndexOption != 5&&intIndexOption != 6) {
					System.out.println("invalid index option");
					break;
				}
				if (filePath != null && DBName != null && indexOption != null) {
					BatchInsert.run(filePath, DBName, indexOption,excase);
					
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
				batchorquery=false;
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
				SystemDefs sysdef = new SystemDefs(dBName, 81930, Integer.parseInt(numBuf), "Clock");
					//SystemDefs.JavabaseDB = new rdfDB(Integer.parseInt(sortOption));
				
				Stream outStream=null;
			
					/*outStream = SystemDefs.JavabaseDB.openStream(
							Integer.parseInt(sortOption), subjectFilter,
							predicateFilter, objectFilter,
							Float.parseFloat(confidenceFilter));*/
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
					TID tid = new TID();
					EID sLid = new EID();
					PID pLid = new PID();
					EID oLid = new EID();
					Triple triple = new Triple();
					
					try {
						while(triple != null) {
							
							triple = outStream.getNext();
							if(triple == null){
								break;
							}
							else{
							sLid= triple.getSubjectId();
							pLid= triple.getPredicateId();
							oLid= triple.getObjectId();
							float confidence = triple.value;
							String subjectLabel=SystemDefs.JavabaseDB.entityLabelHeapFile.getLabel(sLid.returnLID());
							String predicateLabel=SystemDefs.JavabaseDB.predicateLabelHeapFile.getLabel(pLid.returnLID());
							String objectLabel=SystemDefs.JavabaseDB.entityLabelHeapFile.getLabel(oLid.returnLID());
							System.out.print("S:"+subjectLabel);
							System.out.print(" P:"+predicateLabel);
							System.out.print(" O:"+objectLabel);
							System.out.format(" C:");
							System.out.println(confidence);
							//triple = outStream.getNext();
							}
						}
						
						
					} catch (InvalidTripleSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

			case 3:
				System.out.println("exiting....exited");
				System.exit(0);

				break;
			case 4:
				Report rpt = new Report();
				rpt.printreport();
				System.out.println("Database Consists:");
				System.out.println("Entity Count:"+BatchInsert.entityCount);
				System.out.println("predicate Count:"+BatchInsert.predicateCount);
				System.out.println("triple Count:"+BatchInsert.tripleCount);
			break;
			case 5:
				String filePath1 = null;
				String dBName1 = null;
				System.out.println("enter filepath to insert");
				try {
					filePath1 = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				File f1 = new File(filePath1);
				System.out
						.println(f1
								+ (f1.exists() ? " file is found "
										: " file is missing "));
				if (f1.exists() == false)
					break;
				System.out.println("your query is here");
				try{
					Scanner input=new Scanner(f1);
					while(input.hasNext())
					{
						String num =input.nextLine();
						System.out.println(num);
					}
						
				}
				catch(FileNotFoundException e)
				{
					System.err.format("File not Found\n");
				}
				System.out.println("enter RDFDBNAME");
				try {
					dBName1 = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!match(dBName1)) {
					System.out.println("Database name is invalid");
					break;
				} else {
					RandomAccessFile fp = new RandomAccessFile(dBName1, "rw");
					try {
						fp.getFilePointer();
					} catch (IOException e) {
						System.err.print("Database does not exist");
					}
				}
				String [] fileArray1;
				ReadFile readFile1 = new ReadFile(filePath1);
				fileArray1 = readFile1.openFile();
				char[] leftSubjectArray = new char[100];
				char[] rightSubjectArray1 = new char[100];
				char[] rightSubjectArray2 = new char[100];
				char[] leftPredicateArray = new char[100];
				char[] rightPredicateArray1 = new char[100];
				char[] rightPredicateArray2 = new char[100];
				char[] leftObjectArray = new char[100];
				char[] rightObjectArray1 = new char[100];
				char[] rightObjectArray2 = new char[100];
				char[] leftConfidenceArray = new char[100];
				char[] rightConfidenceArray1 = new char[100];
				char[] rightConfidenceArray2 = new char[100];
				int jNPosition1=-1;
				int jNPosition2=-1;
				//if 0-join on subject ,else join on object.
				int jOnObjOrSub1=-1;
				int jOnObjOrSub2=-1;
				int [] leftOutNodes1=new int[10];
				int [] leftOutNodes2=new int[10];
				//if 1 project out from join ,else dont project
				int opRightSubject1=-1;
				int opRightSubject2=-1;
				int opRightObject1=-1;
				int opRightObject2=-1;
				int sortOrder=-1;
				int nodePointer=-1;
				char[] numPages=new char[10];
					char[] lineString1 = fileArray1[1].toCharArray();
					int lineLength1 = lineString1.length;
					for (int charNo = 0; charNo < lineLength1-1; charNo++) {
						do {
							charNo++;
						}while (lineString1[charNo] != '[');
						charNo++;
				//second  line from file
						//get left subject filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							leftSubjectArray[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get left predicate filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							leftPredicateArray[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get left object filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							leftObjectArray[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get left confidence filter
						for(int count=0;lineString1[charNo] != ']';count++) {
							leftConfidenceArray[count] = lineString1[charNo];
							count++;
							charNo++;
						}
					}
						lineString1 = fileArray1[2].toCharArray();
					for (int charNo = 0; charNo < lineLength1-1; charNo++) {
						do {
							charNo++;
						}while (lineString1[charNo] == ' ');
				//third line from file
						//get join node position1
						jNPosition1= Character.digit(lineString1[charNo],10);
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						charNo++;
						//get join on subject or object--first
						jOnObjOrSub1= Character.digit(lineString1[charNo],10);
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						//get right subject filter
						charNo++;
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightSubjectArray1[count]= lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right predicate filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightPredicateArray1[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right object filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightObjectArray1[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right confidence filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightConfidenceArray1[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						do {
							charNo++;
						}while (lineString1[charNo] != '{');
						charNo++;
						//get left out node position 1
						int count=0;
						while(lineString1[charNo] != '}') {
							if (lineString1[charNo]==',')
							{
								charNo++;
								continue;
							}
							leftOutNodes1[count] = Character.digit(lineString1[charNo],10);
							count++;
							charNo++;
						}
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						charNo++;
						//get op right sub/no
						opRightSubject1 = Character.digit(lineString1[charNo],10);
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						//get op right object/no
						charNo++;
						opRightObject1 = Character.digit(lineString1[charNo],10);
					}
						lineString1 = fileArray1[4].toCharArray();
						for (int charNo = 0; charNo < lineLength1-1; charNo++) {
							while (lineString1[charNo] == ' '){
								charNo++;
							}
				//fifth line from file
						//get join node position2
							jNPosition2= Character.digit(lineString1[charNo],10);
							do {
								charNo++;
							}while (lineString1[charNo] != ',');
							charNo++;
						//get join on subject or object--second
							jOnObjOrSub2= Character.digit(lineString1[charNo],10);
							do {
								charNo++;
							}while (lineString1[charNo] != ',');
							charNo++;
						//get right subject filter
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightSubjectArray2[count]= lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right predicate filter--second
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightPredicateArray2[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right object filter--second
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightObjectArray2[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						charNo++;
						//get right confidence filter--second
						for(int count=0;lineString1[charNo] != ',';count++) {
							rightConfidenceArray2[count] = lineString1[charNo];
							count++;
							charNo++;
						}
						do {
							charNo++;
						}while (lineString1[charNo] != '{');
						charNo++;
						//get left out node position 2
						int count=0;
						while(lineString1[charNo] != '}') {
							if (lineString1[charNo]==',')
							{
								charNo++;
								continue;
							}
							leftOutNodes2[count] = Character.digit(lineString1[charNo],10);
							count++;
							charNo++;
						}
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						charNo++;
						//get op right sub/no--second
						opRightSubject2 = Character.digit(lineString1[charNo],10);
						do {
							charNo++;
						}while (lineString1[charNo] != ',');
						charNo++;
						//get op right object/no--second
						opRightObject2 = Character.digit(lineString1[charNo],10);
						charNo++;
						}
						lineString1 = fileArray1[6].toCharArray();
						lineLength1=lineString1.length;
						for (int charNo = 0; charNo < lineLength1-1; charNo++) {
							while (lineString1[charNo] == ' '){
								charNo++;
							}
				//seventh line from file
						//get sort order
							sortOrder= Character.digit(lineString1[charNo],10);
							do {
								charNo++;
							}while (lineString1[charNo] != ',');
							charNo++;
						//get Node Pointer
							nodePointer= Character.digit(lineString1[charNo],10);
							do {
								charNo++;
							}while (lineString1[charNo] != ',');
							charNo++;
						//get no.of pages
							int count=0;
							while(charNo<lineString1.length)
							{
							numPages[count]= lineString1[charNo];
							charNo++;
							count++;
							}
						}
						
						System.out.println(leftSubjectArray);
						System.out.println(leftPredicateArray);
						System.out.println(leftObjectArray);
						System.out.println(leftConfidenceArray);
						System.out.println(jNPosition1);
						System.out.println(jOnObjOrSub1);
						System.out.println(rightSubjectArray1);
						System.out.println(rightPredicateArray1);
						System.out.println(rightObjectArray1);
						System.out.println(rightConfidenceArray1);
						System.out.println(leftOutNodes1);
						System.out.println(opRightSubject1);
						System.out.println(opRightObject1);
						System.out.println(jNPosition2);
						System.out.println(jOnObjOrSub2);
						System.out.println(rightSubjectArray2);
						System.out.println(rightPredicateArray2);
						System.out.println(rightObjectArray2);
						System.out.println(rightConfidenceArray2);
						System.out.println(leftOutNodes2);
						System.out.println(opRightSubject2);
						System.out.println(opRightObject2);
						System.out.println(sortOrder);
						System.out.println(nodePointer);
						System.out.println(numPages);
				/*excase=true;
				String filePath2 = null;
				String DBName2 = null;
				String indexOption2 = null;
				System.out.println("enter filepath to insert");
				try {
					filePath2 = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				File f2 = new File(filePath2);
				System.out
						.println(f2
								+ (f2.exists() ? " file is found "
										: " file is missing "));
				if (f2.exists() == false)
					break; 
				System.out.println("enter RDFDBNAME");
				try {
					DBName2 = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!match(DBName2)) {
					System.out.println("Database not found");
					break;
				}
				System.out.println("enter Index option : 1--unclusterd Tree");
				try {
					indexOption2 = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int intIndexOption2 = Integer.parseInt(indexOption2);
				if (intIndexOption2 != 1) {
					System.out.println("invalid index option");
					break;
				}
				
				if (filePath2 != null && DBName2 != null && indexOption2 != null) {
					BatchInsert.run(filePath2, DBName2, indexOption2,excase);
					
				} else {
					System.out.println("please enter valid details to insert");
				}
				
				break;*/
			}
			//}
			/*catch(Exception E)
			{
				System.err.print("You entered an invalid option:please enter again or try again");
			}*/

		}

	}

	static boolean match(String s) {
		return p.matcher(s).matches();
	}
}
