/*  File TID.java   */
/* SVN test comment - ok to remove */
/* comment by Ritesh */

package global;

/** class TID
 */

public class TID {
  
  /** public PageId pageNo
   */
  public PageID pageNo = new PageID();
  
  /** public int slotNo
   */
  public int slotNo;
  
  /**
   * default constructor of class
   */
  public TID () { }
  
  /**
   *  constructor of class
   */
  public TID (PageID pageno, int slotno)
    {
      pageNo = pageno;
      slotNo = slotno;
    }
  
  /**
   * make a copy of the given tid
   */
  public void copyTid (TID tid)
    {
      pageNo = tid.pageNo;
      slotNo = tid.slotNo;
    }  
  
  /** Compares two TID object, i.e, this to the tid
   * @param tid TID object to be compared to
   * @return true is they are equal
   *         false if not.
   */
  public boolean equals(TID tid) {
    
    if ((this.pageNo.pid==tid.pageNo.pid)
	&&(this.slotNo==tid.slotNo))
      return true;
    else
      return false;
  }
  
  /** Write the tid into a byte array at offset
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
