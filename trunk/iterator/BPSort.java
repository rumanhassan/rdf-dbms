package iterator;

import index.IndexException;

import java.io.IOException;
import java.util.*;
import java.util.Collections;
import labelheap.InvalidLabelSizeException;
import labelheap.InvalidTypeException;
import labelheap.LHFBufMgrException;
import labelheap.LHFDiskMgrException;
import labelheap.LHFException;
import labelheap.LabelHeapFile;
import btree.DataClass;
import btree.KeyDataEntry;
import btree.LeafData;
import btree.ScanIteratorException;
import bufmgr.PageNotReadException;
import tripleheap.BasicPatternClass;
import tripleheap.InvalidSlotNumberException;
import tripleheap.InvalidTripleSizeException;
import tripleheap.InvalidTupleSizeException;
import tripleheap.THFBufMgrException;
import tripleheap.THFDiskMgrException;
import tripleheap.THFException;
import tripleheap.Triple;
import global.BPOrder;
import global.EID;
import global.LID;
import global.PID;
import global.TID;

public class BPSort {

	/**
	 * @param input_itr
	 * @param sort_order
	 * @param SortNodeIDPos
	 * @param n_pages
	 * @throws Exception
	 * @throws InvalidLabelSizeException
	 * @throws labelheap.InvalidSlotNumberException
	 */
	public BPSort(BPIterator input_itr, BPOrder returnEiD, int SortNodeIDPos,
			int n_pages) throws labelheap.InvalidSlotNumberException,
			InvalidLabelSizeException, Exception {
		int arraylength;
		ArrayList BPArray = new ArrayList();
		BPArray.add(input_itr);
		arraylength = BPArray.size();
		BasicPatternClass basicPattern = new BasicPatternClass();

		ArrayList<BasicPatternClass> al = input_itr.getArrayList();
		java.util.Iterator<BasicPatternClass> bpIterator = al.listIterator();
		LabelHeapFile entlabelfileObj = new LabelHeapFile("file_2");
		LID lid = new LID();
		ArrayList<String> b1 = new ArrayList();
		while (bpIterator.hasNext()) {
			lid = bpIterator.next().getLIDbyNodePosition(SortNodeIDPos);
			String label = entlabelfileObj.getLabel(lid);
			b1.add(label);
		}
		 Collections.sort(b1);
		 int blength;
		 blength = b1.size();
		 for(int i = 0 ; i< blength; i++){
		 	 b1.get(i);
		 	 
		 }
	}
   
	/*
	 * private void getEidFromIterator(ListIterator<BasicPatternClass>
	 * bpIterator) throws labelheap.InvalidSlotNumberException,
	 * InvalidLabelSizeException, LHFException, LHFDiskMgrException,
	 * LHFBufMgrException, Exception { while(bpIterator.hasNext()){ int offset =
	 * 0; LabelHeapFile entlabelfileObj = new LabelHeapFile("file_2"); LID lid =
	 * bpIterator.next().returnLid(); entlabelfileObj.getLabel(lid);
	 * 
	 * //bpIterator.next().returnEid(bpIterator,offset);
	 * 
	 * }
	 */

}