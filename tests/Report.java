package tests;

import java.io.*;
import diskmgr.*;
public class Report {
public void printreport()
{System.out.println(" Total number of Triples is " + (new DBDirectoryPage()).TripleCnt);
System.out.println(" Total number of Entities is " + (new DBDirectoryPage()).EntityCnt);
System.out.println(" Total number of Predicates is " + (new DBDirectoryPage()).PredicateCnt);
System.out.println(" Total number of Subjects is " + (new DBDirectoryPage()).SubjectCnt);
System.out.println(" Total number of Objects is " + (new DBDirectoryPage()).ObjectCnt);
System.out.println(" Total number of Disk Pages that were read is " + (new PCounter()).rcounter);
System.out.println(" Total number of Disk Pages that were written is " + (new PCounter()).wcounter);
}
}