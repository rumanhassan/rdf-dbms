/* File Triple.java */
package tripleheap;

import java.io.*;
import java.lang.*;
import global.*;

public class Triple implements GlobalConst {

	private int triple_length;
	private EID subjectId;
	private PID predicateId;
	private EID objectId;
	private double value;

	/**
	 * Maximum size of any triple
	 */
	public static final int max_size = MINIBASE_PAGESIZE;

	/**
	 * a byte array to hold data
	 */
	private byte[] data;
	/**
	 * start position of this triple in data[]
	 */
	private int triple_offset;

	/**
	 * length of this triple
	 */
	/**
	 * Class constructor Create a new triple with length = max_size,triple
	 * offset = 0.
	 */

	public Triple() {
		// Create a new triple
		triple_offset = 0;
		triple_length = max_size;
	}

	/**
	 * Constructor
	 * 
	 * @param a
	 *            triple a byte array which contains the triple
	 * @param offset
	 *            the offset of the triple in the byte array
	 * @param length
	 *            is the length of the triple
	 */

	public Triple(byte[] atriple, int offset) {
		subjectId = new EID();
		predicateId = new PID();
		objectId = new EID();
		try {
		subjectId.slotNo = Convert.getIntValue(0, atriple);
		subjectId.pageNo.pid = Convert.getIntValue(1, atriple);
		predicateId.slotNo = Convert.getIntValue(2, atriple);
		predicateId.pageNo.pid = Convert.getIntValue(3, atriple);
		objectId.slotNo = Convert.getIntValue(4, atriple);
		objectId.pageNo.pid = Convert.getIntValue(5, atriple);
		triple_offset = offset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Constructor(used as triple copy)
	 * 
	 * @param fromTriple
	 *            a byte array which contains the triple
	 * 
	 */
	public Triple(Triple fromTriple) {
		this.triple_length = fromTriple.triple_length;
		this.subjectId = fromTriple.subjectId;
		this.predicateId = fromTriple.predicateId;
		this.objectId = fromTriple.objectId;
		this.value = fromTriple.value;
	}

	public EID getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(EID subjectId) {
		this.subjectId = subjectId;
	}

	public PID getPredicateId() {
		return predicateId;
	}

	public void setPredicateId(PID predicateId) {
		this.predicateId = predicateId;
	}

	public EID getObjectId() {
		return objectId;
	}

	public void setObjectId(EID objectId) {
		this.objectId = objectId;
	}

	public double getConfidence() {
		return value;
	}

	public void setConfidence(double value) {
		this.value = value;
	}

	public void tripleCopy(Triple fromTriple) {
		this.triple_length = fromTriple.triple_length;
		this.subjectId = fromTriple.subjectId;
		this.predicateId = fromTriple.predicateId;
		this.objectId = fromTriple.objectId;
		this.value = fromTriple.value;
	}

	public void tripleInit(byte[] atriple, int offset) {
		subjectId = new EID();
		predicateId = new PID();
		objectId = new EID();
		try {
		subjectId.slotNo = Convert.getIntValue(0, atriple);	
		subjectId.pageNo.pid = Convert.getIntValue(1, atriple);
		predicateId.slotNo = Convert.getIntValue(2, atriple);
		predicateId.pageNo.pid = Convert.getIntValue(3, atriple);
		objectId.slotNo = Convert.getIntValue(4, atriple);
		objectId.pageNo.pid = Convert.getIntValue(5, atriple);
		triple_offset = offset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set a triple with the given triple length and offset
	 * 
	 * @param record
	 *            a byte array contains the triple
	 * @param offset
	 *            the offset of the triple ( =0 by default)
	 */
	public void tripleSet(byte[] atriple, int offset) {
		subjectId = new EID();
		predicateId = new PID();
		objectId = new EID();
		try {
		subjectId.slotNo = Convert.getIntValue(0, atriple);
		subjectId.pageNo.pid = Convert.getIntValue(1, atriple);
		predicateId.slotNo = Convert.getIntValue(2, atriple);
		predicateId.pageNo.pid = Convert.getIntValue(3, atriple);
		objectId.slotNo = Convert.getIntValue(4, atriple);
		objectId.pageNo.pid = Convert.getIntValue(5, atriple);
		triple_offset = offset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Copy the triple byte array out
	 * 
	 * @return byte[], a byte array contains the triple the length of byte[] =
	 *         length of the triple
	 */

	public byte[] getTripleByteArray() {
		byte[] triplecopy = new byte[triple_length];
		try {
		Convert.setIntValue(subjectId.slotNo, 0, triplecopy);
		Convert.setIntValue(subjectId.pageNo.pid, 1, triplecopy);
		Convert.setIntValue(predicateId.slotNo, 2, triplecopy);
		Convert.setIntValue(predicateId.pageNo.pid, 3, triplecopy);
		Convert.setIntValue(objectId.slotNo, 4, triplecopy);
		Convert.setIntValue(objectId.pageNo.pid, 5, triplecopy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return triplecopy;
	}

	/**
	 * get the length of a triple
	 * 
	 * @return length of this triple in bytes
	 */
	public int size() {
		return triple_length;
	}

	public void print() throws IOException {
		System.out.print("< S:" + subjectId.slotNo + ",P:"
				+ subjectId.pageNo.pid + " >");
		System.out.print("< S:" + predicateId.slotNo + ",P:"
				+ predicateId.pageNo.pid + " >");
		System.out.print("< S:" + objectId.slotNo + ",P:" + objectId.pageNo.pid
				+ " >");
	}

}
