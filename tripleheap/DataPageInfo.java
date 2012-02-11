package tripleheap;


/** File DataPageInfo.java */


import global.*;
import java.io.*;

/** DataPageInfo class : the type of records stored on a directory page.
*
* April 9, 1998
*/

class DataPageInfo implements GlobalConst{


  /** HFPage returns int for avail space, so we use int here */
  int    availspace; 
  
  /** for efficient implementation of getRecCnt() */
  int    recct;    
  
  /** obvious: id of this particular data page (a HFPage) */
  PageID pageId = new PageID();   
    
  /** auxiliary fields of DataPageInfo */

  public static final int size = 28;// size of DataPageInfo object in bytes

  private byte [] data;  // a data buffer
  
  private int offset;


/**
 *  We can store roughly pagesize/sizeof(DataPageInfo) records per
 *  directory page; for any given HeapFile insertion, it is likely
 *  that at least one of those referenced data pages will have
 *  enough free space to satisfy the request.
 */


  /** Default constructor
   */
  public DataPageInfo()
  {  
    data = new byte[28]; // size of datapageinfo
    int availspace = 0;
    recct =0;
    pageId.pid = INVALID_PAGE;
    offset = 0;
  }
  
  /** Constructor 
   * @param array  a byte array
   */
  public DataPageInfo(byte[] array)
  {
    data = array;
    offset = 0;
  }

      
   public byte [] returnByteArray()
   {
     return data;
   }
      
      
  /** constructor: translate a triple to a DataPageInfo object
   *  it will make a copy of the data in the triple
   * @param atriple: the input triple
   */
  public DataPageInfo(Triple _atriple)
       throws InvalidTripleSizeException, IOException
  {   
     // need check _atriple size == this.size ?otherwise, throw new exception
    if (_atriple.getLength()!=28){
      throw new InvalidTripleSizeException(null, "TRIPLEHEAPFILE: TRIPLE SIZE ERROR");
    }

  
      data = _atriple.returnTripleByteArray();
      offset = _atriple.getOffset();
      
      availspace = Convert.getIntValue(offset, data);
      recct = Convert.getIntValue(offset+4, data);
      pageId = new PageID();
      pageId.pid = Convert.getIntValue(offset+8, data);
      
   
  }
  
  
  /** convert this class objcet to a triple(like cast a DataPageInfo to Triple)
   *  
   *
   */
  public Triple convertToTriple()
       throws IOException
  {

    // 1) write availspace, recct, pageId into data []
    Convert.setIntValue(availspace, offset, data);
    Convert.setIntValue(recct, offset+4, data);
    Convert.setIntValue(pageId.pid, offset+8, data);


    // 2) creat a Triple object using this array
    Triple atriple = new Triple(data, offset, 28); 
 
    // 3) return triple object
    return atriple;

  }
  
    
  /** write this object's useful fields(availspace, recct, pageId) 
   *  to the data[](may be in buffer pool)
   *  
   */
  public void flushToTriple() throws IOException
  {
     // write availspace, recct, pageId into "data[]"
    Convert.setIntValue(availspace, offset, data);
    Convert.setIntValue(recct, offset+4, data);
    Convert.setIntValue(pageId.pid, offset+8, data);

    // here we assume data[] already points to buffer pool
  
  }
  
}






