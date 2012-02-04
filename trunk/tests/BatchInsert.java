/**
 * 
 */
package tests;

import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * @author shodhan
 * 
 */
public class BatchInsert {

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
		String[] fileArray;
		System.out.println("inserting file at " + args[0] + " in " + args[1]
				+ "...");
		ReadFile readFile = new ReadFile(filePath);
		fileArray = readFile.openFile();
		for (int i = 0; i < fileArray.length; i++) {
			char[] lineString = fileArray[i].toCharArray();
			int lineLength = lineString.length;
			String subject = null;
			String predicate = null;
			String object = null;
			String confidence = null;
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
				while (lineString[charNo] != ' ' && lineString[charNo] != '\t') {
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
			
			System.out.println("subject" + subject);
			System.out.println("predicate" + predicate);
			System.out.println("object" + object);
			System.out.println("confidence" + confidence);
			// System.out.println(fileArray[i]);
		}

	}

	public static void run(String path, String dbName, String sortOption)
			throws IOException {

		String[] inputToMain = { path, dbName, sortOption };
		inputToMain[0] = path;
		main(inputToMain);
	}
}
