package global;

import java.io.*;

/** class PageId
 */
public class PageID{
  
  /** public int pid
   */
  public int pid;
  
  /**
   * Default constructor
   */
  public PageID ()  { }

  /**
   * constructor of class
   * @param	pageno	the page ID
   */
  public PageID (int pageno)
  {
   pid = pageno;
  }

 /**
  * make a copy of the given pageId 
  */
  public void copyPageId (PageID pageno)
    {
      pid = pageno.pid;
    } 
  
  /** Write the pid into a specified bytearray at offset
   * @param ary the specified bytearray
   * @param offset the offset of bytearray to write the pid
   * @exception  java.io.IOException I/O errors
   */
  public void writeToByteArray(byte [] ary, int offset) 
    throws java.io.IOException
    {
      Convert.setIntValue ( pid, offset, ary);
    }
  
  public String toString() {
    return (new Integer(pid)).toString();
  } 
}
