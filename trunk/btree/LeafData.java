package btree;
import global.*;

/**  IndexData: It extends the DataClass.
 *   It defines the data "genid" for leaf node in B++ tree.
 */
public class LeafData extends DataClass {
  private GENID myGenid;

  public String toString() {
     String s;
     s="[ "+ (new Integer(myGenid.pageNo.pid)).toString() +" "
              + (new Integer(myGenid.slotNo)).toString() + " ]";
     return s;
  }

  /** Class constructor
   *  @param    genid  the data genid
   */
  LeafData(GENID genid) {myGenid= new GENID(genid.pageNo, genid.slotNo);};  

  /** get a copy of the genid
  *  @return the reference of the copy 
  */
  public GENID getData() {return new GENID(myGenid.pageNo, myGenid.slotNo);};

  /** set the genid
   */ 
  public void setData(GENID genid) { myGenid= new GENID(genid.pageNo, genid.slotNo);};
}   

