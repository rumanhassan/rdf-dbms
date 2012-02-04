/*  File GENID.java   */
/* SVN test comment - ok to remove */
/* comment by Ritesh */

package global;

/** class GENID
 */

public class GENID extends java.lang.Object{
  
  /** public PageId pageNo
   */
  public PageID pageNo = new PageID();
  
  /** public int slotNo
   */
  public int slotNo;
  
  /**
   * default constructor of class
   */
  public GENID () { }
  
  /**
   *  constructor of class
   */
  public GENID (PageID pageno, int slotno)
    {
      pageNo = pageno;
      slotNo = slotno;
    }
  
  /**
   * make a copy of the given genid
   */
  public void copyGenid (GENID genid)
    {
      pageNo = genid.pageNo;
      slotNo = genid.slotNo;
    }  
  
  /** Compares two GENID object, i.e, this to the genid
   * @param genid GENID object to be compared to
   * @return true is they are equal
   *         false if not.
   */
  public boolean equals(GENID genid) {
    
    if ((this.pageNo.pid==genid.pageNo.pid)
	&&(this.slotNo==genid.slotNo))
      return true;
    else
      return false;
  }
  
  /** Write the genid into a byte array at offset
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

