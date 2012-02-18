package iterator;

import global.BPOrder;
import global.EID;
import global.LID;
import global.PageID;

import java.util.ArrayList;
import java.util.Collections;

import labelheap.InvalidLabelSizeException;
import labelheap.LabelHeapFile;
import tripleheap.BasicPatternClass;

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
		@SuppressWarnings("unchecked")
		ArrayList<BasicPatternClass> al = input_itr.getArrayList();
		java.util.Iterator<BasicPatternClass> bpIterator = al.listIterator();
		LabelHeapFile entlabelfileObj = new LabelHeapFile("file_2");
		EID eid = new EID();
		LID lid = new LID();
		PageID pageID = new PageID();
		ArrayList<String> b1 = new ArrayList<String>();
		while (bpIterator.hasNext()) {
			eid = bpIterator.next().getEIDbyNodePosition(SortNodeIDPos);
			pageID.pid = eid.pageNo.pid;
			lid.pageNo = pageID;
			lid.slotNo = eid.slotNo;
			String label = entlabelfileObj.getLabel(lid);
			b1.add(label);
		}

		Collections.sort(b1);
		int blength;
		blength = b1.size();
		for (int i = 0; i < blength; i++) {
			String compareLabel = b1.get(i);
			while (bpIterator.hasNext()) {
				eid = bpIterator.next().getEIDbyNodePosition(SortNodeIDPos);
				pageID.pid = eid.pageNo.pid;
				lid.pageNo = pageID;
				lid.slotNo = eid.slotNo;
				String label = entlabelfileObj.getLabel(lid);
				if (compareLabel.equalsIgnoreCase(label)) {
					String[] basicPatternStrings = bpIterator.next()
							.convertIdsToStrings();
					for (int j = 0; i < basicPatternStrings.length; j++) {
						System.out.print(basicPatternStrings[j] + "");
					}
					System.out.print(bpIterator.next().getConfidence() + "\n");
				}
			}
		}
	}	
}