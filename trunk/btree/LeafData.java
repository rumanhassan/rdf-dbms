package btree;
import global.*;

/**  IndexData: It extends the DataClass.
 *   It defines the data "genid" for leaf node in B++ tree.
 */
public class LeafData extends DataClass {
  private TID myGenid;

  public String toString() {
     String s;
     s="[ "+ (new Integer(myGenid.pageNo.pid)).toString() +" "
              + (new Integer(myGenid.slotNo)).toString() + " ]";
     return s;
  }

  /** Class constructor
   *  @param    genid  the data genid
   */
  LeafData(TID genid) {myGenid= new TID(genid.pageNo, genid.slotNo);};  

  /** get a copy of the genid
  *  @return the reference of the copy 
  */
  public TID getData() {return new TID(myGenid.pageNo, myGenid.slotNo);};

  /** set the genid
   */ 
  public void setData(TID genid) { myGenid= new TID(genid.pageNo, genid.slotNo);};
}   

