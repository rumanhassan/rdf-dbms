package global;

public class BPOrder { // similar to TupleOrder

	  public static final int Ascending  = 0;
	  public static final int Descending = 1;
	  public static final int Random     = 2;

	  public int basicPatternOrder;

	  /** 
	   * BPOrder Constructor
	   * <br>
	   * A Basic Pattern ordering can be defined as 
	   * <ul>
	   * <li>   BPOrder bpOrder = new BPOrder(BPOrder.Random);
	   * </ul>
	   * and subsequently used as
	   * <ul>
	   * <li>   if (bpOrder.basicPatternOrder == BPOrder.Random) ....
	   * </ul>
	   *
	   * @param _basicPatternOrder The possible ordering of the Basic Patterns 
	   */

	  public BPOrder (int _basicPatternOrder) {
	    basicPatternOrder = _basicPatternOrder;
	  }

	  public String toString() {
	    
	    switch (basicPatternOrder) {
	    case Ascending:
	      return "Ascending";
	    case Descending:
	      return "Descending";
	    case Random:
	      return "Random";
	    }
	    return ("Unexpected BPOrder " + basicPatternOrder);
	  }

}
