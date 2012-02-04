package tests;

import java.io.*;
import java.lang.*;
import java.util.regex.*;

import javax.print.DocFlavor.STRING;

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
				System.out.println(f
						+ (f.exists() ? " file is found " : " file is missing "));
				if(f.exists()==false)
					break ;
				System.out.println("enter RDFDBNAME");
				try {
					DBName = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!match(DBName))
				{
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
				if (intIndexOption!=1){
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
				System.out.println("You entered : " + selectedOption);
				break;
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
