package btree;
import global.*;
 
/**  IndexData: It extends the DataClass.
 *   It defines the data "pageNo" for index node in B++ tree.
 */
public class IndexData extends DataClass {
  private PageID pageId;

  public String toString() {
     return (new Integer(pageId.pid)).toString();
  }

  /** Class constructor
   *  @param     pageNo  the page number
   */
  IndexData(PageID  pageNo) { pageId = new PageID(pageNo.pid);};  

  /** Class constructor
   *  @param     pageNo  the page number
   */
  IndexData(int  pageNo) { pageId = new PageID(pageNo);};  


  /** get a copy of the pageNo
  *  @return the reference of the copy 
  */
  protected PageID getData() {return new PageID(pageId.pid); };

  /** set the pageNo 
   */ 
  protected void setData(PageID pageNo) {pageId= new PageID(pageNo.pid);};
}   
