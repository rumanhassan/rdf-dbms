package diskmgr;

import tripleheap.BasicPatternClass;
import iterator.BPIterator;

public class BP_Triple_Join {
	
	/**
	 * @param amt_of_mem - available pages for the operation
	 * @param num_left_nodes - the number of node IDs in the left basic pattern stream
	 * @param left_itr - the left basic pattern stream
	 * @param BPJoinNodePosition - the position of the join node in the basic pattern
	 * @param JoinOnSubjectorObject - 0: join on subject; 1: join on object
	 * @param RightSubjectFilter - subject filter for the right source
	 * @param RightPredicateFilter - predicate filter for the right source
	 * @param RightObjectFilter - object filter for the right source
	 * @param RightConfidenceFilter - confidence filter for the right source
	 * @param LeftOutNodePositions - positions of the projected nodes from the left source
	 * @param OutputRightSubject - 0/1 project subject node from the right source?
	 * @param OutputRightObject - 0/1 project object node from the right source?
	 */
	public BP_Triple_Join( int amt_of_mem, int num_left_nodes, BPIterator left_itr,
			int BPJoinNodePosition, int JoinOnSubjectorObject, String
			RightSubjectFilter, String RightPredicateFilter, String
			RightObjectFilter, double RightConfidenceFilter, int [] LeftOutNodePositions,
			int OutputRightSubject, int OutputRightObject) {
		//TODO Auto-generated
	} // end constructor
	
	public BasicPatternClass getNext(){
		
		return null;
	} // end getNext() method
	
	public void close(){
		
	}


} // end BP_Triple_Join class
