/* File Triple.java */

package tripleheap;

import java.io.*;
import java.lang.*;
import global.*;


public class Triple implements GlobalConst {

	/** 
	  * Maximum size of any label
	  */
	  public static final int max_size = MINIBASE_PAGESIZE;
	
	public static final int LENGTH_OF_TRIPLE = 28;
	public EID subjectId;
	public PID predicateId;
	public EID objectId;
	public float value;


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
	  private int triple_length;

	  /** 
	   * private field
	   * Number of fields in this triple
	   */
	  private short fldCnt;

	  /** 
	   * private field
	   * Array of offsets of the fields
	   */
	 
	  private short [] fldOffset; 

	   /**
	    * Class constructor
	    * Creat a new triple with length = max_size,triple offset = 0.
	    */
	public Triple() {
		// Create a new triple
//		data = new byte[max_size];
//		triple_offset = 0;
//	       triple_offset = max_size;
		data = new byte[LENGTH_OF_TRIPLE];
		triple_offset = 0;
	}

	/** Constructor
	    * @param atriple a byte array which contains the atriple
	    * @param offset the offset of the triple in the byte array
	    * @param length the length of the triple
	    */

	public Triple(byte[] atriple, int offset, int length) {
				
		data = atriple;
		triple_offset = offset;
		triple_length = length;

		// byte[] triplecopy = new byte[28];
		try {
			EID subjID=new EID();
			EID objID =new EID();
			PID predID =new PID();
			subjID.slotNo=Convert.getIntValue(0, data);
			subjID.pageNo.pid=Convert.getIntValue(4, data);
			predID.slotNo=Convert.getIntValue(8, data);
			predID.pageNo.pid=Convert.getIntValue(12, data);
			objID.slotNo=Convert.getIntValue(16, data);
			objID.pageNo.pid=Convert.getIntValue(20, data);
			
			
		this.subjectId=subjID;
		this.predicateId=predID;
		this.objectId=objID;
		this.value=Convert.getFloValue(24, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/** Constructor(used as triple copy)
	    * @param fromTriple   a byte array which contains the triple
	    * 
	    */
	public Triple(Triple fromTriple) {
		
		
		data = fromTriple.getTripleByteArray();
		triple_length = fromTriple.getLength();
		triple_offset = 0;
	       fldCnt = fromTriple.noOfFlds(); 
	       fldOffset = fromTriple.copyFldOffset();
	    this.setSubjectId(fromTriple.getSubjectId());
	    this.setPredicateId(fromTriple.getPredicateId());
	    this.setObjectId(fromTriple.getObjectId());
	    try {
			this.value = Convert.getFloValue(24, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**  
	    * Class constructor
	    * Creat a new triple with length = size,triple offset = 0.
	    */
	 
	  public  Triple(int size)
	  {
	       // Creat a new triple
	       data = new byte[size];
	       triple_offset = 0;
	       triple_length = size;     
	  }
	
	  /** Copy a triple to the current triple position
	    *  you must make sure the triple lengths must be equal
	    * @param fromTriple the triple being copied
	    */

	  public void tripleCopy(Triple fromTriple) {
		  byte [] temparray = fromTriple.getTripleByteArray();
	       System.arraycopy(temparray, 0, data, triple_offset, triple_length);
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

	public float getConfidence() {
		return value;
	}

	public void setConfidence(float value) {
		this.value = value;
	}

	

	/*public void tripleInit(byte[] atriple, int offset) {
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
		value=Convert.getFloValue(6, atriple);
		triple_offset = offset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	*//**
	 * Set a triple with the given triple length and offset
	 * 
	 * @param record
	 *            a byte array contains the triple
	 * @param offset
	 *            the offset of the triple ( =0 by default)
	 *//*
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
		value=Convert.getFloValue(6, atriple);
		triple_offset = offset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	*//**
	 * Copy the triple byte array out
	 * 
	 * @return byte[], a byte array contains the triple the length of byte[] =
	 *         length of the triple
	 *//*

	public byte[] getTripleByteArray() {
		byte[] triplecopy = new byte[LENGTH_OF_TRIPLE];
		try {	
		Convert.setIntValue(subjectId.slotNo, 0, triplecopy);
		Convert.setIntValue(subjectId.pageNo.pid, 1, triplecopy);
		Convert.setIntValue(predicateId.slotNo, 2, triplecopy);
		Convert.setIntValue(predicateId.pageNo.pid, 3, triplecopy);
		Convert.setIntValue(objectId.slotNo, 4, triplecopy);
		Convert.setIntValue(objectId.pageNo.pid, 5, triplecopy);
		Convert.setFloValue(value, 6, triplecopy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return triplecopy;
	}
 
	   /** This is used when you don't want to use the constructor
	    * @param atriple  a byte array which contains the triple
	    * @param offset the offset of the triple in the byte array
	    * @param length the length of the triple
	    */

	   public void tripleInit(byte [] atriple, int offset, int length)
	   {
	      data = atriple;
	      triple_offset = offset;
	      triple_length = length;
	   }

	 /**
	  * Set a triple with the given triple length and offset
	  * @param	record	a byte array contains the triple
	  * @param	offset  the offset of the triple ( =0 by default)
	  * @param	length	the length of the triple
	  */
	 public void tripleSet(byte [] record, int offset, int length)  
	  {
	      System.arraycopy(record, offset, data, 0, length);
	      triple_offset = 0;
	      triple_length = length;
	  }
	  
	 /** get the length of a triple, call this method if you did not 
	  *  call setHdr () before
	  * @return 	length of this triple in bytes
	  */   
	  public int getLength()
	   {
	      return triple_length;
	   }

	/** get the length of a triple, call this method if you did 
	  *  call setHdr () before
	  * @return     size of this triple in bytes
	  */
	  public short size()
	   {
	      return ((short) (fldOffset[fldCnt] - triple_offset));
	   }
	 
	   /** get the offset of a triple
	    *  @return offset of the triple in byte array
	    */   
	   public int getOffset()
	   {
	      return triple_offset;
	   }   
	   
	   /** Copy the triple byte array out
	    *  @return  byte[], a byte array contains the triple
	    *		the length of byte[] = length of the triple
	    */
	    
	   public byte [] getTripleByteArray() 
	   {
		   /*byte[] triplecopy = new byte[LENGTH_OF_TRIPLE];
			try {	
			Convert.setIntValue(subjectId.slotNo, 0, triplecopy);
			Convert.setIntValue(subjectId.pageNo.pid, 1, triplecopy);
			Convert.setIntValue(predicateId.slotNo, 2, triplecopy);
			Convert.setIntValue(predicateId.pageNo.pid, 3, triplecopy);
			Convert.setIntValue(objectId.slotNo, 4, triplecopy);
			Convert.setIntValue(objectId.pageNo.pid, 5, triplecopy);
			Convert.setFloValue(value, 6, triplecopy);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return triplecopy;*/
	       byte [] triplecopy = new byte [triple_length];
	       System.arraycopy(data, triple_offset, triplecopy, 0, triple_length);
	       return triplecopy;
	   }
	   
	   /** return the data byte array 
	    *  @return  data byte array 		
	    */
	    
	   public byte [] returnTripleByteArray()
	   {
	       return data;
	   }
	   
	   /**
	    * Convert this field into integer 
	    * 
	    * @param	fldNo	the field number
	    * @return		the converted integer if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	    */

	  public int getIntFld(int fldNo) 
	  	throws IOException, FieldNumberOutOfBoundException
	  {           
	    int val;
	    if ( (fldNo > 0) && (fldNo <= fldCnt))
	     {
	      val = Convert.getIntValue(fldOffset[fldNo -1], data);
	      return val;
	     }
	    else 
	     throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND");
	  }
	    
	   /**
	    * Convert this field in to float
	    *
	    * @param    fldNo   the field number
	    * @return           the converted float number  if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	    */

	    public float getFloFld(int fldNo) 
	    	throws IOException, FieldNumberOutOfBoundException
	     {
		float val;
	      if ( (fldNo > 0) && (fldNo <= fldCnt))
	       {
	        val = Convert.getFloValue(fldOffset[fldNo -1], data);
	        return val;
	       }
	      else 
	       throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND");
	     }


	   /**
	    * Convert this field into String
	    *
	    * @param    fldNo   the field number
	    * @return           the converted string if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	    */

	   public String getStrFld(int fldNo) 
	   	throws IOException, FieldNumberOutOfBoundException 
	   { 
	         String val;
	    if ( (fldNo > 0) && (fldNo <= fldCnt))      
	     {
	        val = Convert.getStrValue(fldOffset[fldNo -1], data, 
			fldOffset[fldNo] - fldOffset[fldNo -1]); //strlen+2
	        return val;
	     }
	    else 
	     throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND");
	  }
	 
	   /**
	    * Convert this field into a character
	    *
	    * @param    fldNo   the field number
	    * @return           the character if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	    */

	   public char getCharFld(int fldNo) 
	   	throws IOException, FieldNumberOutOfBoundException 
	    {   
	       char val;
	      if ( (fldNo > 0) && (fldNo <= fldCnt))      
	       {
	        val = Convert.getCharValue(fldOffset[fldNo -1], data);
	        return val;
	       }
	      else 
	       throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND");
	 
	    }

	  /**
	   * Set this field to integer value
	   *
	   * @param	fldNo	the field number
	   * @param	val	the integer value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	   */

	  public Triple setIntFld(int fldNo, int val) 
	  	throws IOException, FieldNumberOutOfBoundException
	  { 
	    if ( (fldNo > 0) && (fldNo <= fldCnt))
	     {
		Convert.setIntValue (val, fldOffset[fldNo -1], data);
		return this;
	     }
	    else 
	     throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND"); 
	  }

	  /**
	   * Set this field to float value
	   *
	   * @param     fldNo   the field number
	   * @param     val     the float value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	   */

	  public Triple setFloFld(int fldNo, float val) 
	  	throws IOException, FieldNumberOutOfBoundException
	  { 
	   if ( (fldNo > 0) && (fldNo <= fldCnt))
	    {
	     Convert.setFloValue (val, fldOffset[fldNo -1], data);
	     return this;
	    }
	    else  
	     throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND"); 
	     
	  }

	  /**
	   * Set this field to String value
	   *
	   * @param     fldNo   the field number
	   * @param     val     the string value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException Triple field number out of bound
	   */

	   public Triple setStrFld(int fldNo, String val) 
			throws IOException, FieldNumberOutOfBoundException  
	   {
	     if ( (fldNo > 0) && (fldNo <= fldCnt))        
	      {
	         Convert.setStrValue (val, fldOffset[fldNo -1], data);
	         return this;
	      }
	     else 
	       throw new FieldNumberOutOfBoundException (null, "TRIPLE:TRIPLE_FLDNO_OUT_OF_BOUND");
	    }


	   /**
	    * setHdr will set the header of this triple.   
	    *
	    * @param	numFlds	  number of fields
	    * @param	types[]	  contains the types that will be in this triple
	    * @param	strSizes[]      contains the sizes of the string 
	    *				
	    * @exception IOException I/O errors
	    * @exception InvalidTypeException Invalid tupe type
	    * @exception InvalidTripleSizeException Triple size too big
	    *
	    */

	public void setHdr (short numFlds,  AttrType types[], short strSizes[])
	 throws IOException, InvalidTypeException, InvalidTripleSizeException		
	{
	  if((numFlds +2)*2 > max_size)
	    throw new InvalidTripleSizeException (null, "TRIPLE: TRIPLE_TOOBIG_ERROR");
	  
	  fldCnt = numFlds;
	  Convert.setShortValue(numFlds, triple_offset, data);
	  fldOffset = new short[numFlds+1];
	  int pos = triple_offset+2;  // start position for fldOffset[]
	  
	  //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
	  //another 1 for fldCnt
	  fldOffset[0] = (short) ((numFlds +2) * 2 + triple_offset);   
	   
	  Convert.setShortValue(fldOffset[0], pos, data);
	  pos +=2;
	  short strCount =0;
	  short incr;
	  int i;

	  for (i=1; i<numFlds; i++)
	  {
	    switch(types[i-1].attrType) {
	    
	   case AttrType.attrInteger:
	     incr = 4;
	     break;

	   case AttrType.attrReal:
	     incr =4;
	     break;

	   case AttrType.attrString:
	     incr = (short) (strSizes[strCount] +2);  //strlen in bytes = strlen +2
	     strCount++;
	     break;       
	 
	   default:
	    throw new InvalidTypeException (null, "TRIPLE: TRIPLE_TYPE_ERROR");
	   }
	  fldOffset[i]  = (short) (fldOffset[i-1] + incr);
	  Convert.setShortValue(fldOffset[i], pos, data);
	  pos +=2;
	 
	}
	 switch(types[numFlds -1].attrType) {

	   case AttrType.attrInteger:
	     incr = 4;
	     break;

	   case AttrType.attrReal:
	     incr =4;
	     break;

	   case AttrType.attrString:
	     incr =(short) ( strSizes[strCount] +2);  //strlen in bytes = strlen +2
	     break;

	   default:
	    throw new InvalidTypeException (null, "TRIPLE: TRIPLE_TYPE_ERROR");
	   }

	  fldOffset[numFlds] = (short) (fldOffset[i-1] + incr);
	  Convert.setShortValue(fldOffset[numFlds], pos, data);
	  
	  triple_length = fldOffset[numFlds] - triple_offset;

	  if(triple_length > max_size)
	   throw new InvalidTripleSizeException (null, "TRIPLE: TRIPLE_TOOBIG_ERROR");
	}
	     
	  
	  /**
	   * Returns number of fields in this triple
	   *
	   * @return the number of fields in this triple
	   *
	   */

	  public short noOfFlds() 
	   {
	     return fldCnt;
	   }

	  /**
	   * Makes a copy of the fldOffset array
	   *
	   * @return a copy of the fldOffset arrray
	   *
	   */

	  public short[] copyFldOffset() 
	   {
	     short[] newFldOffset = new short[fldCnt + 1];
	     for (int i=0; i<=fldCnt; i++) {
	       newFldOffset[i] = fldOffset[i];
	     }
	     
	     return newFldOffset;
	   }

	 /**
	  * Print out the triple
	  * @param type  the types in the triple
	  * @Exception IOException I/O exception
	  */
	 public void print(AttrType type[])
	    throws IOException 
	 {
	  int i, val;
	  float fval;
	  String sval;

	  System.out.print("[");
	  for (i=0; i< fldCnt-1; i++)
	   {
	    switch(type[i].attrType) {

	   case AttrType.attrInteger:
	     val = Convert.getIntValue(fldOffset[i], data);
	     System.out.print(val);
	     break;

	   case AttrType.attrReal:
	     fval = Convert.getFloValue(fldOffset[i], data);
	     System.out.print(fval);
	     break;

	   case AttrType.attrString:
	     sval = Convert.getStrValue(fldOffset[i], data,fldOffset[i+1] - fldOffset[i]);
	     System.out.print(sval);
	     break;
	  
	   case AttrType.attrNull:
	   case AttrType.attrSymbol:
	     break;
	   }
	   System.out.print(", ");
	 } 
	 
	 switch(type[fldCnt-1].attrType) {

	   case AttrType.attrInteger:
	     val = Convert.getIntValue(fldOffset[i], data);
	     System.out.print(val);
	     break;

	   case AttrType.attrReal:
	     fval = Convert.getFloValue(fldOffset[i], data);
	     System.out.print(fval);
	     break;

	   case AttrType.attrString:
	     sval = Convert.getStrValue(fldOffset[i], data,fldOffset[i+1] - fldOffset[i]);
	     System.out.print(sval);
	     break;

	   case AttrType.attrNull:
	   case AttrType.attrSymbol:
	     break;
	   }
	   System.out.println("]");

	 }

	  /**
	   * private method
	   * Padding must be used when storing different types.
	   * 
	   * @param	offset
	   * @param type   the type of triple
	   * @return short typle
	   */

	  private short pad(short offset, AttrType type)
	   {
	      return 0;
	   }
	
}
