package iterator;

import global.BPOrder;
import global.EID;
import global.LID;
import global.PageID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import labelheap.InvalidLabelSizeException;
import labelheap.LabelHeapFile;
import tripleheap.BasicPatternClass;

public class BPSort /* implements Comparator */{

	/**
	 * @param input_itr
	 * @param sort_order
	 * @param SortNodeIDPos
	 * @param n_pages
	 * @throws Exception
	 * @throws InvalidLabelSizeException
	 * @throws labelheap.InvalidSlotNumberException
	 */
	@SuppressWarnings("unchecked")
	public BPSort(BPIterator input_itr, BPOrder order, int SortNodeIDPos,
			int n_pages) throws labelheap.InvalidSlotNumberException,
			InvalidLabelSizeException, Exception {
		ArrayList<BasicPatternClass> basicPatterns = input_itr.getArrayList();
		java.util.Iterator<BasicPatternClass> bpIterator = basicPatterns
				.listIterator();
		LabelHeapFile entlabelfileObj = new LabelHeapFile("file_2");
		EID eid = new EID();
		LID lid = new LID();
		PageID pageID = new PageID();
		ArrayList<String> b1 = new ArrayList<String>();
		BasicPatternClass bpc_temp = new BasicPatternClass();
		while ((bpIterator.hasNext())) {
			bpc_temp = bpIterator.next();
			eid = bpc_temp.getEIDbyNodePosition(SortNodeIDPos);
			pageID.pid = eid.pageNo.pid;
			lid.pageNo = pageID;
			lid.slotNo = eid.slotNo;
			String label = entlabelfileObj.getLabel(lid);
			b1.add(label);
		}

		bpIterator = basicPatterns.listIterator();
		switch (order.basicPatternOrder)
		{
		case 0:	
			Collections.sort(b1, new sort1());
			break;
		case 1:
			Collections.sort(b1, new sort1());
			Collections.reverse(b1);
			break;
		case 2:
			break;
			
		}
		
		int blength = b1.size();
		System.out.print("Sorted Patterns \n");
		for (int i = 0; i < blength; i++) {
			String compareLabel = b1.get(i);
			while (bpIterator.hasNext()) {
				bpc_temp = bpIterator.next();
				eid = bpc_temp.getEIDbyNodePosition(SortNodeIDPos);
				pageID.pid = eid.pageNo.pid;
				lid.pageNo = pageID;
				lid.slotNo = eid.slotNo;
				String label = entlabelfileObj.getLabel(lid);
				if (compareLabel.equalsIgnoreCase(label)) {
					System.out.print("BP " + i + ": ");
					bpc_temp.print();
				}
			}
			bpIterator = basicPatterns.listIterator();
		}
	}
}

@SuppressWarnings("rawtypes")
class sort1 implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;

		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}
	void printSorted(){
		
	}
}