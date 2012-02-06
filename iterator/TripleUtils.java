package iterator;

import tripleheap.*;
import global.*;

import java.io.*;
import java.lang.*;

/**
 * some useful method when processing Triple
 */
public class TripleUtils {

	/**
	 * This function compares a triple with another triple and returns:
	 * 
	 * 0 if the two are equal, 1 if the triple is greater, -1 if the triple is
	 * smaller,
	 * 
	 * @param fldType
	 *            the type of the field being compared.
	 * @param t1
	 *            one triple.
	 * @param t2
	 *            another triple.
	 * @param t1_fld_no
	 *            the field numbers in the triples to be compared.
	 * @param t2_fld_no
	 *            the field numbers in the triples to be compared.
	 * @exception UnknowAttrType
	 *                don't know the attribute type
	 * @exception IOException
	 *                some I/O fault
	 * @exception TripleUtilsException
	 *                exception from this class
	 * @return 0 if the two are equal, 1 if the triple is greater, -1 if the
	 *         triple is smaller,
	 */
	public static int CompareTripleWithTriple(AttrType fldType, Triple t1,int t1fieldNo,
			Triple t2, int t2fieldNo) throws IOException, UnknowAttrType, TripleUtilsException {

		/*if (t1 == t2)
			return 0;
		else if (t1.getSubjectId() == t2.getSubjectId()
				&& t1.getPredicateId() == t2.getPredicateId()
				&& t1.getObjectId() == t2.getObjectId()
				&& t1.getConfidence() == t2.getConfidence()) {
			return 0;
		}
*/
		switch (t1fieldNo) {
		case 1:

			if ((PageID) (t1.getSubjectId().pageNo) == (PageID) ((t2
					.getSubjectId()).pageNo)
					&& t1.getSubjectId().slotNo == (t2.getSubjectId()).slotNo) {
				return 0;
			}
		case 2:
			if ((PageID) (t1.getObjectId().pageNo) == (PageID) ((t2
					.getObjectId()).pageNo)
					&& t1.getObjectId().slotNo == (t2.getObjectId()).slotNo) {
				return 0;
			}
		case 3:
			if ((PageID) (t1.getPredicateId().pageNo) == (PageID) ((t2
					.getPredicateId()).pageNo)
					&& t1.getObjectId().slotNo == (t2.getObjectId()).slotNo) {
				return 0;
			}
		case 4:
			if(t1.getConfidence()==t2.getConfidence())
			{
				return 0;
			}
		default:

			throw new UnknowAttrType(null,
					"Don't know how to handle attrSymbol, attrNull");
		}

	}
	
	public static int CompareTripleWithValue(AttrType fldType, Triple t1,int fieldNo,
			Triple t2) throws IOException, UnknowAttrType, TripleUtilsException {

		if (t1 == t2)
			return 0;
		else if (t1.getSubjectId() == t2.getSubjectId()
				&& t1.getPredicateId() == t2.getPredicateId()
				&& t1.getObjectId() == t2.getObjectId()
				&& t1.getConfidence() == t2.getConfidence()) {
			return 0;
		}

		switch (fieldNo) {
		case 1:

			if ((PageID) (t1.getSubjectId().pageNo) == (PageID) ((t2
					.getSubjectId()).pageNo)) {
				return 0;
			}
		case 2:
			if (t1.getSubjectId().slotNo == (t2.getSubjectId()).slotNo) {
				return 0;
			}
		case 3:
			if ((PageID) (t1.getPredicateId().pageNo) == (PageID) ((t2
					.getPredicateId()).pageNo)) {
				return 0;
			}
		case 4:
			if(t1.getPredicateId().slotNo == (t2.getPredicateId()).slotNo)
			{
				return 0;
			}
		case 5:
			if ((PageID) (t1.getObjectId().pageNo) == (PageID) ((t2
					.getObjectId()).pageNo)) {
				return 0;
			}
		case 6:
			if (t1.getObjectId().slotNo == (t2.getObjectId()).slotNo) {
				return 0;
			}
		case 7:
			if (t1.getConfidence()==t2.getConfidence()) {
				return 0;
			}
		default:

			throw new UnknowAttrType(null,
					"Don't know how to handle attrSymbol, attrNull");
		}

	}


	public static int CompareTripleWithTripleValue(Triple t1,
			AttrType t1_fldtype, Triple t2, AttrType t2_fldtype)
			throws IOException, UnknowAttrType, TripleUtilsException {

		switch (t1_fldtype.attrType) {
		case 1:
			switch (t2_fldtype.attrType) {

			case 3:

				if ((PageID) (t1.getSubjectId().pageNo) == (PageID) ((t2
						.getObjectId()).pageNo)
						&& t1.getSubjectId().slotNo == (t2.getObjectId()).slotNo) {
					return 0;
				}
			case 1:

				if ((PageID) (t1.getSubjectId().pageNo) == (PageID) ((t2
						.getSubjectId()).pageNo)
						&& t1.getSubjectId().slotNo == (t2.getSubjectId()).slotNo) {
					return 0;

				}
			case 2:
				return 1;
			}
		case 3:
			switch (t2_fldtype.attrType) {

			case 3:

				if ((PageID) (t1.getObjectId().pageNo) == (PageID) ((t2
						.getSubjectId()).pageNo)
						&& t1.getObjectId().slotNo == (t2.getSubjectId()).slotNo) {
					return 0;
				}
			case 1:

				if ((PageID) (t1.getSubjectId().pageNo) == (PageID) ((t2
						.getSubjectId()).pageNo)
						&& t1.getSubjectId().slotNo == (t2.getSubjectId()).slotNo) {
					return 0;

				}
			case 2:
				return 1;
			
		}

		case 2:
			switch (t2_fldtype.attrType) {

			case 2:

				if ((PageID) (t1.getPredicateId().pageNo) == (PageID) ((t2
						.getPredicateId()).pageNo)
						&& t1.getPredicateId().slotNo == (t2.getPredicateId()).slotNo) {
					return 0;
				}
			case 1:
				return 1;
			case 3:
				return 1;

			}
		
		case 4:
			if(t1.getConfidence()==t2.getConfidence())
			{
				return 0;
			}
			
		return 1;
		}
		return 0;

	}

	
	/**
	 * get the string specified by the field number
	 * 
	 * @param triple
	 *            the triple
	 * @param fidno
	 *            the field number
	 * @return the content of the field number
	 * @exception IOException
	 *                some I/O fault
	 * @exception TripleUtilsException
	 *                exception from this class
	 */
	public static float Value(Triple triple) throws IOException,
			TripleUtilsException {
		float temp;
		temp = (float) triple.getConfidence();
		return temp;
	}

	/**
	 * set up a triple in specified field from a triple
	 * 
	 * @param value
	 *            the triple to be set
	 * @param triple
	 *            the given triple
	 * @param fld_no
	 *            the field number
	 * @param fldType
	 *            the triple attr type
	 * @exception UnknowAttrType
	 *                don't know the attribute type
	 * @exception IOException
	 *                some I/O fault
	 * @exception TripleUtilsException
	 *                exception from this class
	 */
	public static void SetValue(Triple t1, Triple t2, int fld_no,
			AttrType fldType) throws IOException, UnknowAttrType,
			TripleUtilsException {
		
		
		switch (fldType.attrType) {
		case 1:

			t1.setSubjectId(t2.getSubjectId());
			
		case 2:
			t1.setObjectId(t2.getObjectId());
		case 3:
			t1.setPredicateId(t2.getPredicateId());
		case 4:
			t1.setConfidence((float)t2.getConfidence());
		default:

			throw new UnknowAttrType(null,
					"Don't know how to handle attrSymbol, attrNull");
		}

	}

	/**
	 * set up the Jtriple's attrtype, string size,field number for using join
	 * 
	 * @param Jtriple
	 *            reference to an actual triple - no memory has been malloced
	 * @param res_attrs
	 *            attributes type of result triple
	 * @param in1
	 *            array of the attributes of the triple (ok)
	 * @param len_in1
	 *            num of attributes of in1
	 * @param in2
	 *            array of the attributes of the triple (ok)
	 * @param len_in2
	 *            num of attributes of in2
	 * @param t1_str_sizes
	 *            shows the length of the string fields in S
	 * @param t2_str_sizes
	 *            shows the length of the string fields in R
	 * @param proj_list
	 *            shows what input fields go where in the output triple
	 * @param nOutFlds
	 *            number of outer relation fileds
	 * @exception IOException
	 *                some I/O fault
	 * @exception TripleUtilsException
	 *                exception from this class
	 */
	public static short[] setup_op_triple(Triple Jtriple, AttrType[] res_attrs,
			AttrType in1[], int len_in1, AttrType in2[], int len_in2,
			short t1_str_sizes[], short t2_str_sizes[], FldSpec proj_list[],
			int nOutFlds) throws IOException, TripleUtilsException {
		short[] sizesT1 = new short[len_in1];
		short[] sizesT2 = new short[len_in2];
		int i, count = 0;

		for (i = 0; i < len_in1; i++)
			if (in1[i].attrType == AttrType.attrString)
				sizesT1[i] = t1_str_sizes[count++];

		for (count = 0, i = 0; i < len_in2; i++)
			if (in2[i].attrType == AttrType.attrString)
				sizesT2[i] = t2_str_sizes[count++];

		int n_strs = 0;
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer)
				res_attrs[i] = new AttrType(
						in1[proj_list[i].offset - 1].attrType);
			else if (proj_list[i].relation.key == RelSpec.innerRel)
				res_attrs[i] = new AttrType(
						in2[proj_list[i].offset - 1].attrType);
		}

		// Now construct the res_str_sizes array.
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer
					&& in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
				n_strs++;
			else if (proj_list[i].relation.key == RelSpec.innerRel
					&& in2[proj_list[i].offset - 1].attrType == AttrType.attrString)
				n_strs++;
		}

		short[] res_str_sizes = new short[n_strs];
		count = 0;
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer
					&& in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
				res_str_sizes[count++] = sizesT1[proj_list[i].offset - 1];
			else if (proj_list[i].relation.key == RelSpec.innerRel
					&& in2[proj_list[i].offset - 1].attrType == AttrType.attrString)
				res_str_sizes[count++] = sizesT2[proj_list[i].offset - 1];
		}
		/*try {
			Jtriple.setHdr((short) nOutFlds, res_attrs, res_str_sizes);
		} catch (Exception e) {
			throw new TripleUtilsException(e, "setHdr() failed");
		}*/
		return res_str_sizes;
	}

	/**
	 * set up the Jtriple's attrtype, string size,field number for using project
	 * 
	 * @param Jtriple
	 *            reference to an actual triple - no memory has been malloced
	 * @param res_attrs
	 *            attributes type of result triple
	 * @param in1
	 *            array of the attributes of the triple (ok)
	 * @param len_in1
	 *            num of attributes of in1
	 * @param t1_str_sizes
	 *            shows the length of the string fields in S
	 * @param proj_list
	 *            shows what input fields go where in the output triple
	 * @param nOutFlds
	 *            number of outer relation fileds
	 * @exception IOException
	 *                some I/O fault
	 * @exception TripleUtilsException
	 *                exception from this class
	 * @exception InvalidRelation
	 *                invalid relation
	 */

	public static short[] setup_op_triple(Triple Jtriple, AttrType res_attrs[],
			AttrType in1[], int len_in1, short t1_str_sizes[],
			FldSpec proj_list[], int nOutFlds) throws IOException,
			TripleUtilsException, InvalidRelation {
		short[] sizesT1 = new short[len_in1];
		int i, count = 0;

		for (i = 0; i < len_in1; i++)
			if (in1[i].attrType == AttrType.attrString)
				sizesT1[i] = t1_str_sizes[count++];

		int n_strs = 0;
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer)
				res_attrs[i] = new AttrType(
						in1[proj_list[i].offset - 1].attrType);

			else
				throw new InvalidRelation("Invalid relation -innerRel");
		}

		// Now construct the res_str_sizes array.
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer
					&& in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
				n_strs++;
		}

		short[] res_str_sizes = new short[n_strs];
		count = 0;
		for (i = 0; i < nOutFlds; i++) {
			if (proj_list[i].relation.key == RelSpec.outer
					&& in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
				res_str_sizes[count++] = sizesT1[proj_list[i].offset - 1];
		}

		/*try {
			Jtriple.setHdr((short) nOutFlds, res_attrs, res_str_sizes);
		} catch (Exception e) {
			throw new TripleUtilsException(e, "setHdr() failed");
		}*/
		return res_str_sizes;
	}

	public static boolean Equal(Triple tempTriple1, Triple tempTriple2,
			AttrType[] _in, short in_len) {
		// TODO Auto-generated method stub
		return false;
	}
}
