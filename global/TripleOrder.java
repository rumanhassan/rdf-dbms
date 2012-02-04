package global;

/** 
 * Enumeration class for TripleOrder
 * 
 */

public class TripleOrder {

  public static final int Ascending  = 0;
  public static final int Descending = 1;
  public static final int Random     = 2;

  public int tripleOrder;

  /** 
   * TripleOrder Constructor
   * <br>
   * A triple ordering can be defined as 
   * <ul>
   * <li>   TripleOrder tripleOrder = new TripleOrder(TripleOrder.Random);
   * </ul>
   * and subsequently used as
   * <ul>
   * <li>   if (tripleOrder.tripleOrder == TripleOrder.Random) ....
   * </ul>
   *
   * @param _tripleOrder The possible ordering of the triples 
   */

  public TripleOrder (int _tripleOrder) {
    tripleOrder = _tripleOrder;
  }

  public String toString() {
    
    switch (tripleOrder) {
    case Ascending:
      return "Ascending";
    case Descending:
      return "Descending";
    case Random:
      return "Random";
    }
    return ("Unexpected TupleOrder " + tripleOrder);
  }

}
