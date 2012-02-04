/*  File TID.java   */
/* SVN test comment - ok to remove */

package global;

/** class TID
 */

public class PID extends LID{
  
  /** public PageId pageNo
   */
  public PageID pageNo = new PageID();
  
  /** public int slotNo
   */
  public int slotNo;
  
  /**
   * default constructor of class
   */
  public PID () { }
  
  /**
   *  constructor of class
   */
  public PID (PageID pageno, int slotno)
    {
      pageNo = pageno;
      slotNo = slotno;
    }
  
  /**
   * make a copy of the given pid
   */
  public void copyTid (PID pid)
    {
      pageNo = pid.pageNo;
      slotNo = pid.slotNo;
    }  
  
  /** Compares two TID object, i.e, this to the pid
   * @param pid TID object to be compared to
   * @return true is they are equal
   *         false if not.
   */
  public boolean equals(PID pid) {
    
    if ((this.pageNo.pid==pid.pageNo.pid)
	&&(this.slotNo==pid.slotNo))
      return true;
    else
      return false;
  }
  
  /** Write the pid into a byte array at offset
   * @param ary the specified byte array
   * @param offset the offset of byte array to write 
   * @exception java.io.IOException I/O errors
   */ 
  public void writeToByteArray(byte [] ary, int offset)
    throws java.io.IOException
    {
      Convert.setIntValue ( slotNo, offset, ary);
      Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }
  
}
