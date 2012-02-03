/* File LHFPage.java */

package labelheap;

import java.io.*;
import java.lang.*;

import global.*;
import diskmgr.*;

/**
 * Define constant values for INVALID_SLOT and EMPTY_SLOT
 */

interface ConstSlot {
	int INVALID_SLOT = -1;
	int EMPTY_SLOT = -1;
}

/**
 * Class Label heap file page. The design assumes that labels are kept compacted
 * when deletions are performed.
 */

public class LHFPage extends Page implements ConstSlot, GlobalConst {

	public static final int SIZE_OF_SLOT = 4;
	public static final int DPFIXED = 4 * 2 + 3 * 4;

	public static final int SLOT_CNT = 0;
	public static final int USED_PTR = 2;
	public static final int FREE_SPACE = 4;
	public static final int TYPE = 6;
	public static final int PREV_PAGE = 8;
	public static final int NEXT_PAGE = 12;
	public static final int CUR_PAGE = 16;

	/*
	 * Warning: These items must all pack tight, (no padding) for the current
	 * implementation to work properly. Be careful when modifying this class.
	 */

	/**
	 * number of slots in use
	 */
	private short slotCnt;

	/**
	 * offset of first used byte by data labels in data[]
	 */
	private short usedPtr;

	/**
	 * number of bytes free in data[]
	 */
	private short freeSpace;

	/**
	 * an arbitrary value used by subclasses as needed
	 */
	private short type;

	/**
	 * backward pointer to data page
	 */
	private PageID prevPage = new PageID();

	/**
	 * forward pointer to data page
	 */
	private PageID nextPage = new PageID();

	/**
	 * page number of this page
	 */
	protected PageID curPage = new PageID();

	/**
	 * Default constructor
	 */

	public LHFPage() {
	}

	/**
	 * Constructor of class LHFPage open a LHFPage and make this LHFpage piont
	 * to the given page
	 * 
	 * @param page
	 *            the given page in Page type
	 */

	public LHFPage(Page page) {
		data = page.getpage();
	}

	/**
	 * Constructor of class LHFPage open a existed Lhfpage
	 * 
	 * @param apage
	 *            a page in buffer pool
	 */

	public void openLHFpage(Page apage) {
		data = apage.getpage();
	}

	/**
	 * Constructor of class LHFPage initialize a new page
	 * 
	 * @param pageNo
	 *            the page number of a new page to be initialized
	 * @param apage
	 *            the Page to be initialized
	 * @see Page
	 * @exception IOException
	 *                I/O errors
	 */

	public void init(PageID pageNo, Page apage) throws IOException {
		data = apage.getpage();

		slotCnt = 0; // no slots in use
		Convert.setShortValue(slotCnt, SLOT_CNT, data);

		curPage.pid = pageNo.pid;
		Convert.setIntValue(curPage.pid, CUR_PAGE, data);

		nextPage.pid = prevPage.pid = INVALID_PAGE;
		Convert.setIntValue(prevPage.pid, PREV_PAGE, data);
		Convert.setIntValue(nextPage.pid, NEXT_PAGE, data);

		usedPtr = (short) MAX_SPACE; // offset in data array (grow backwards)
		Convert.setShortValue(usedPtr, USED_PTR, data);

		freeSpace = (short) (MAX_SPACE - DPFIXED); // amount of space available
		Convert.setShortValue(freeSpace, FREE_SPACE, data);

	}

	/**
	 * @return byte array
	 */

	public byte[] getLHFpageArray() {
		return data;
	}

	/**
	 * Dump contents of a page
	 * 
	 * @exception IOException
	 *                I/O errors
	 */
	public void dumpPage() throws IOException {
		int i, n;
		int length, offset;

		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		nextPage.pid = Convert.getIntValue(NEXT_PAGE, data);
		usedPtr = Convert.getShortValue(USED_PTR, data);
		freeSpace = Convert.getShortValue(FREE_SPACE, data);
		slotCnt = Convert.getShortValue(SLOT_CNT, data);

		System.out.println("dumpPage");
		System.out.println("curPage= " + curPage.pid);
		System.out.println("nextPage= " + nextPage.pid);
		System.out.println("usedPtr= " + usedPtr);
		System.out.println("freeSpace= " + freeSpace);
		System.out.println("slotCnt= " + slotCnt);

		for (i = 0, n = DPFIXED; i < slotCnt; n += SIZE_OF_SLOT, i++) {
			length = Convert.getShortValue(n, data);
			offset = Convert.getShortValue(n + 2, data);
			System.out.println("slotNo " + i + " offset= " + offset);
			System.out.println("slotNo " + i + " length= " + length);
		}

	}

	/**
	 * @return PageId of previous page
	 * @exception IOException
	 *                I/O errors
	 */
	public PageID getPrevPage() throws IOException {
		prevPage.pid = Convert.getIntValue(PREV_PAGE, data);
		return prevPage;
	}

	/**
	 * sets value of prevPage to pageNo
	 * 
	 * @param pageNo
	 *            page number for previous page
	 * @exception IOException
	 *                I/O errors
	 */
	public void setPrevPage(PageID pageNo) throws IOException {
		prevPage.pid = pageNo.pid;
		Convert.setIntValue(prevPage.pid, PREV_PAGE, data);
	}

	/**
	 * @return page number of next page
	 * @exception IOException
	 *                I/O errors
	 */
	public PageID getNextPage() throws IOException {
		nextPage.pid = Convert.getIntValue(NEXT_PAGE, data);
		return nextPage;
	}

	/**
	 * sets value of nextPage to pageNo
	 * 
	 * @param pageNo
	 *            page number for next page
	 * @exception IOException
	 *                I/O errors
	 */
	public void setNextPage(PageID pageNo) throws IOException {
		nextPage.pid = pageNo.pid;
		Convert.setIntValue(nextPage.pid, NEXT_PAGE, data);
	}

	/**
	 * @return page number of current page
	 * @exception IOException
	 *                I/O errors
	 */
	public PageID getCurPage() throws IOException {
		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		return curPage;
	}

	/**
	 * sets value of curPage to pageNo
	 * 
	 * @param pageNo
	 *            page number for current page
	 * @exception IOException
	 *                I/O errors
	 */
	public void setCurPage(PageID pageNo) throws IOException {
		curPage.pid = pageNo.pid;
		Convert.setIntValue(curPage.pid, CUR_PAGE, data);
	}

	/**
	 * @return the type
	 * @exception IOException
	 *                I/O errors
	 */
	public short getType() throws IOException {
		type = Convert.getShortValue(TYPE, data);
		return type;
	}

	/**
	 * sets value of type
	 * 
	 * @param valtype
	 *            an arbitrary value
	 * @exception IOException
	 *                I/O errors
	 */
	public void setType(short valtype) throws IOException {
		type = valtype;
		Convert.setShortValue(type, TYPE, data);
	}

	/**
	 * @return slotCnt used in this page
	 * @exception IOException
	 *                I/O errors
	 */
	public short getSlotCnt() throws IOException {
		slotCnt = Convert.getShortValue(SLOT_CNT, data);
		return slotCnt;
	}

	/**
	 * sets slot contents
	 * 
	 * @param slotno
	 *            the slot number
	 * @param length
	 *            length of label the slot contains
	 * @param offset
	 *            offset of label
	 * @exception IOException
	 *                I/O errors
	 */
	public void setSlot(int slotno, int length, int offset) throws IOException {
		int position = DPFIXED + slotno * SIZE_OF_SLOT;
		Convert.setShortValue((short) length, position, data);
		Convert.setShortValue((short) offset, position + 2, data);
	}

	/**
	 * @param slotno
	 *            slot number
	 * @exception IOException
	 *                I/O errors
	 * @return the length of label the given slot contains
	 */
	public short getSlotLength(int slotno) throws IOException {
		int position = DPFIXED + slotno * SIZE_OF_SLOT;
		short val = Convert.getShortValue(position, data);
		return val;
	}

	/**
	 * @param slotno
	 *            slot number
	 * @exception IOException
	 *                I/O errors
	 * @return the offset of label the given slot contains
	 */
	public short getSlotOffset(int slotno) throws IOException {
		int position = DPFIXED + slotno * SIZE_OF_SLOT;
		short val = Convert.getShortValue(position + 2, data);
		return val;
	}

	/**
	 * inserts a new label onto the page, returns LID of this label
	 * 
	 * @param label
	 *            a label to be inserted
	 * @return LID of label, null if sufficient space does not exist
	 * @exception IOException
	 *                I/O errors in C++ Status insertLabel(char *recPtr, int
	 *                labLen, LID& lid)
	 */
	public LID insertLabel(byte[] label) throws IOException {
		LID lid = new LID();

		int labLen = label.length;
		int spaceNeeded = labLen + SIZE_OF_SLOT;

		// Start by checking if sufficient space exists.
		// This is an upper bound check. May not actually need a slot
		// if we can find an empty one.

		freeSpace = Convert.getShortValue(FREE_SPACE, data);
		if (spaceNeeded > freeSpace) {
			return null;

		} else {

			// look for an empty slot
			slotCnt = Convert.getShortValue(SLOT_CNT, data);
			int i;
			short length;
			for (i = 0; i < slotCnt; i++) {
				length = getSlotLength(i);
				if (length == EMPTY_SLOT)
					break;
			}

			if (i == slotCnt) // use a new slot
			{
				// adjust free space
				freeSpace -= spaceNeeded;
				Convert.setShortValue(freeSpace, FREE_SPACE, data);

				slotCnt++;
				Convert.setShortValue(slotCnt, SLOT_CNT, data);

			} else {
				// reusing an existing slot
				freeSpace -= labLen;
				Convert.setShortValue(freeSpace, FREE_SPACE, data);
			}

			usedPtr = Convert.getShortValue(USED_PTR, data);
			usedPtr -= labLen; // adjust usedPtr
			Convert.setShortValue(usedPtr, USED_PTR, data);

			// insert the slot info onto the data page
			setSlot(i, labLen, usedPtr);

			// insert data onto the data page
			System.arraycopy(label, 0, data, usedPtr, labLen);
			curPage.pid = Convert.getIntValue(CUR_PAGE, data);
			lid.pageNo.pid = curPage.pid;
			lid.slotNo = i;
			return lid;
		}
	}

	/**
	 * delete the label with the specified lid
	 * 
	 * @param lid
	 *            the label ID
	 * @exception InvalidSlotNumberException
	 *                Invalid slot number
	 * @exception IOException
	 *                I/O errors in C++ Status deleteLabel(const LID& lid)
	 */
	public void deleteLabel(LID lid) throws IOException,
			InvalidSlotNumberException {
		int slotNo = lid.slotNo;
		short labLen = getSlotLength(slotNo);
		slotCnt = Convert.getShortValue(SLOT_CNT, data);

		// first check if the label being deleted is actually valid
		if ((slotNo >= 0) && (slotNo < slotCnt) && (labLen > 0)) {
			// The labels always need to be compacted, as they are
			// not necessarily stored on the page in the order that
			// they are listed in the slot index.

			// offset of label being deleted
			int offset = getSlotOffset(slotNo);
			usedPtr = Convert.getShortValue(USED_PTR, data);
			int newSpot = usedPtr + labLen;
			int size = offset - usedPtr;

			// shift bytes to the right
			System.arraycopy(data, usedPtr, data, newSpot, size);

			// now need to adjust offsets of all valid slots that refer
			// to the left of the label being removed. (by the size of the hole)

			int i, n, chkoffset;
			for (i = 0, n = DPFIXED; i < slotCnt; n += SIZE_OF_SLOT, i++) {
				if ((getSlotLength(i) >= 0)) {
					chkoffset = getSlotOffset(i);
					if (chkoffset < offset) {
						chkoffset += labLen;
						Convert.setShortValue((short) chkoffset, n + 2, data);
					}
				}
			}

			// move used Ptr forwar
			usedPtr += labLen;
			Convert.setShortValue(usedPtr, USED_PTR, data);

			// increase freespace by size of hole
			freeSpace = Convert.getShortValue(FREE_SPACE, data);
			freeSpace += labLen;
			Convert.setShortValue(freeSpace, FREE_SPACE, data);

			setSlot(slotNo, EMPTY_SLOT, 0); // mark slot free
		} else {
			throw new InvalidSlotNumberException(null,
					"HEAPFILE: INVALID_SLOTNO");
		}
	}

	/**
	 * @return LID of first label on page, null if page contains no labels.
	 * @exception IOException
	 *                I/O errors in C++ Status firstLabel(LID& firstRid)
	 * 
	 */
	public LID firstLabel() throws IOException {
		LID lid = new LID();
		// find the first non-empty slot

		slotCnt = Convert.getShortValue(SLOT_CNT, data);

		int i;
		short length;
		for (i = 0; i < slotCnt; i++) {
			length = getSlotLength(i);
			if (length != EMPTY_SLOT)
				break;
		}

		if (i == slotCnt)
			return null;

		// found a non-empty slot

		lid.slotNo = i;
		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		lid.pageNo.pid = curPage.pid;

		return lid;
	}

	/**
	 * @return LID of next label on the page, null if no more labels exist on
	 *         the page
	 * @param curRid
	 *            current label ID
	 * @exception IOException
	 *                I/O errors in C++ Status nextLabel (LID curRid, LID&
	 *                nextRid)
	 */
	public LID nextLabel(LID curRid) throws IOException {
		LID lid = new LID();
		slotCnt = Convert.getShortValue(SLOT_CNT, data);

		int i = curRid.slotNo;
		short length;

		// find the next non-empty slot
		for (i++; i < slotCnt; i++) {
			length = getSlotLength(i);
			if (length != EMPTY_SLOT)
				break;
		}

		if (i >= slotCnt)
			return null;

		// found a non-empty slot

		lid.slotNo = i;
		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		lid.pageNo.pid = curPage.pid;

		return lid;
	}

	/**
	 * copies out label with LID lid into label pointer. <br>
	 * Status getlabel(LID lid, char *recPtr, int& labLen)
	 * 
	 * @param lid
	 *            the label ID
	 * @return a label contains the label
	 * @exception InvalidSlotNumberException
	 *                Invalid slot number
	 * @exception IOException
	 *                I/O errors
	 * @see Label
	 */
	public Label getlabel(LID lid) throws IOException,
			InvalidSlotNumberException {
		short labLen;
		short offset;
		byte[] label;
		PageID pageNo = new PageID();
		pageNo.pid = lid.pageNo.pid;
		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		int slotNo = lid.slotNo;

		// length of label being returned
		labLen = getSlotLength(slotNo);
		slotCnt = Convert.getShortValue(SLOT_CNT, data);
		if ((slotNo >= 0) && (slotNo < slotCnt) && (labLen > 0)
				&& (pageNo.pid == curPage.pid)) {
			offset = getSlotOffset(slotNo);
			label = new byte[labLen];
			System.arraycopy(data, offset, label, 0, labLen);
			String labelString;
			labelString = Convert.getStrValue(0, label, labLen);
			Label lableObj = new Label();
			lableObj.setLabel(labelString);
			return lableObj;
		}

		else {
			throw new InvalidSlotNumberException(null,
					"HEAPFILE: INVALID_SLOTNO");
		}

	}

	/**
	 * returns a label in a byte array[pageSize] with given LID lid. <br>
	 * in C++ Status returnLabel(LID lid, char*& recPtr, int& labLen)
	 * 
	 * @param lid
	 *            the label ID
	 * @return a label with its length and offset in the byte array
	 * @exception InvalidSlotNumberException
	 *                Invalid slot number
	 * @exception IOException
	 *                I/O errors
	 * @see Label
	 */
	public Label returnLabel(LID lid) throws IOException,
			InvalidSlotNumberException {
		short labLen;
		short offset;
		byte[] label;
		PageID pageNo = new PageID();
		pageNo.pid = lid.pageNo.pid;
		curPage.pid = Convert.getIntValue(CUR_PAGE, data);
		int slotNo = lid.slotNo;

		// length of label being returned
		labLen = getSlotLength(slotNo);
		slotCnt = Convert.getShortValue(SLOT_CNT, data);
		if ((slotNo >= 0) && (slotNo < slotCnt) && (labLen > 0)
				&& (pageNo.pid == curPage.pid)) {
			offset = getSlotOffset(slotNo);
			label = new byte[labLen];
			System.arraycopy(data, offset, label, 0, labLen);
			String labelString;
			labelString = Convert.getStrValue(0, label, labLen);
			Label lableObj = new Label();
			lableObj.setLabel(labelString);
			return lableObj;
		}

		else {
			throw new InvalidSlotNumberException(null,
					"HEAPFILE: INVALID_SLOTNO");
		}

	}

	/**
	 * returns the amount of available space on the page.
	 * 
	 * @return the amount of available space on the page
	 * @exception IOException
	 *                I/O errors
	 */
	public int available_space() throws IOException {
		freeSpace = Convert.getShortValue(FREE_SPACE, data);
		return (freeSpace - SIZE_OF_SLOT);
	}

	/**
	 * Determining if the page is empty
	 * 
	 * @return true if the HFPage is has no labels in it, false otherwise
	 * @exception IOException
	 *                I/O errors
	 */
	public boolean empty() throws IOException {
		int i;
		short length;
		// look for an empty slot
		slotCnt = Convert.getShortValue(SLOT_CNT, data);

		for (i = 0; i < slotCnt; i++) {
			length = getSlotLength(i);
			if (length != EMPTY_SLOT)
				return false;
		}

		return true;
	}

	/**
	 * Compacts the slot directory on an HFPage. WARNING -- this will probably
	 * lead to a change in the LIDs of labels on the page. You CAN'T DO THIS on
	 * most kinds of pages.
	 * 
	 * @exception IOException
	 *                I/O errors
	 */
	protected void compact_slot_dir() throws IOException {
		int current_scan_posn = 0; // current scan position
		int first_free_slot = -1; // An invalid position.
		boolean move = false; // Move a label? -- initially false
		short length;
		short offset;

		slotCnt = Convert.getShortValue(SLOT_CNT, data);
		freeSpace = Convert.getShortValue(FREE_SPACE, data);

		while (current_scan_posn < slotCnt) {
			length = getSlotLength(current_scan_posn);

			if ((length == EMPTY_SLOT) && (move == false)) {
				move = true;
				first_free_slot = current_scan_posn;
			} else if ((length != EMPTY_SLOT) && (move == true)) {
				offset = getSlotOffset(current_scan_posn);

				// slot[first_free_slot].length =
				// slot[current_scan_posn].length;
				// slot[first_free_slot].offset =
				// slot[current_scan_posn].offset;
				setSlot(first_free_slot, length, offset);

				// Mark the current_scan_posn as empty
				// slot[current_scan_posn].length = EMPTY_SLOT;
				setSlot(current_scan_posn, EMPTY_SLOT, 0);

				// Now make the first_free_slot point to the next free slot.
				first_free_slot++;

				// slot[current_scan_posn].length == EMPTY_SLOT !!
				while (getSlotLength(first_free_slot) != EMPTY_SLOT) {
					first_free_slot++;
				}
			}

			current_scan_posn++;
		}

		if (move == true) {
			// Adjust amount of free space on page and slotCnt
			freeSpace += SIZE_OF_SLOT * (slotCnt - first_free_slot);
			slotCnt = (short) first_free_slot;
			Convert.setShortValue(freeSpace, FREE_SPACE, data);
			Convert.setShortValue(slotCnt, SLOT_CNT, data);
		}
	}

}
