package tests;

import java.io.*;
import diskmgr.*;
public class Report {
public void printreport()
{
	rdfDB r = new rdfDB();
System.out.println(" Total number of Triples is " + r.getTripleCnt());
System.out.println(" Total number of Entities is " + r.getEntityCnt());
System.out.println(" Total number of Predicates is " + r.getPredicateCnt());
System.out.println(" Total number of Subjects is " + (new DBDirectoryPage()).SubjectCnt);
System.out.println(" Total number of Objects is " + (new DBDirectoryPage()).ObjectCnt);
System.out.println(" Total number of Disk Pages that were read is " + (new PCounter()).rcounter);
System.out.println(" Total number of Disk Pages that were written is " + (new PCounter()).wcounter);
}
}