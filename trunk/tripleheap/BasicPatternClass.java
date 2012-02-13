package tripleheap;

import global.AttrType;
import global.Convert;
import global.GlobalConst;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import java.io.IOException;

public class BasicPatternClass implements GlobalConst{
	 /** 
	  * Maximum size of any BasicPatternClass
	  */
	  public static final int max_size = MINIBASE_PAGESIZE;
	  
	  public static final int eid_size = 8; // 8 bytes for pageNo, slotNo
	  public static final int conf_size = 4; // 4 bytes for confidence

	 /** 
	   * a byte array to hold data
	   */
	  private byte [] data;
	  // private ArrayList<EID> data;
	  
	  /**
	 * The default confidence of the Basic Pattern
	 */
	private float confidence;

	  /**
	   * start position of this BasicPatternClass in data[]
	   */
	  private int bp_offset;

	  /**
	   * length of this BasicPatternClass
	   */
	  private int bp_length;

	  /** 
	   * private field
	   * Number of fields in this BasicPatternClass
	   */
	  private short fldCnt;

	  /** 
	   * private field
	   * Array of offsets of the fields
	   */	 
	  private short [] fldOffset; 

     /**
      * Class constructor
      * Create a new BasicPatternClass with length = max_size,tuple offset = 0.
      */
	  public  BasicPatternClass()
	  {
	       // Create a new BasicPatternClass
	       data = new byte[max_size];
	       bp_offset = 0;
	       bp_length = max_size;
	  }
	   
	   /** Constructor
	    * @param aBP a byte array which contains the BasicPatternClass
	    * @param offset the start position of the BasicPatternClass in the byte array
	    * @param length the length of the BasicPatternClass
	    */
	   public BasicPatternClass(byte [] aBP, int offset, int length)
	   {
	      data = aBP;
	      bp_offset = offset;
	      bp_length = length;
	    //  fldCnt = getShortValue(offset, data);
	   }
	   
	   /** Constructor(used as tuple copy)
	    * @param fromBasicPatternClass   a byte array which contains the fromBasicPatternClass
	    * 
	    */
	   public BasicPatternClass(BasicPatternClass fromBasicPatternClass)
	   {
	       data = fromBasicPatternClass.getBPByteArray();
	       bp_length = fromBasicPatternClass.getLength();
	       bp_offset = 0;
	       fldCnt = fromBasicPatternClass.noOfFlds(); 
	       fldOffset = fromBasicPatternClass.copyFldOffset(); 
	   }

	   /**  
	    * Class constructor
	    * Create a new BasicPatternClass with length = size,BasicPatternClass offset = 0.
	    */
	 
	  public  BasicPatternClass(int size)
	  {
	       // Create a new tuple
	       data = new byte[size];
	       bp_offset = 0;
	       bp_length = size;     
	  }
	   
	   /** Copy a tuple to the current tuple position
	    *  you must make sure the tuple lengths must be equal
	    * @param fromBasicPatternClassTuple the tuple being copied
	    */
	   public void bpCopy(BasicPatternClass fromBasicPatternClass)
	   {
	       byte [] temparray = fromBasicPatternClass.getBPByteArray();
	       System.arraycopy(temparray, 0, data, bp_offset, bp_length);   
	       fldCnt = fromBasicPatternClass.noOfFlds(); 
	       fldOffset = fromBasicPatternClass.copyFldOffset(); 
	   }

	   /** This is used when you don't want to use the constructor
	    * @param aBP  a byte array which contains the fromBasicPatternClass
	    * @param offset the offset of the fromBasicPatternClass in the byte array
	    * @param length the length of the fromBasicPatternClass
	    */

	   public void bpInit(byte [] aBP, int offset, int length)
	   {
	      data = aBP;
	      bp_offset = offset;
	      bp_length = length;
	   }

	 /**
	  * Set a BasicPatternClass with the given BP length and offset
	  * @param	record	a byte array contains the BasicPatternClass
	  * @param	offset  the offset of the BP ( =0 by default)
	  * @param	length	the length of the BP
	  */
	 public void bpSet(byte [] record, int offset, int length)  
	  {
	      System.arraycopy(record, offset, data, 0, length);
	      bp_offset = 0;
	      bp_length = length;
	  }
	  
	 /** get the length of a BasicPatternClass, call this method if you did not 
	  *  call setHdr () before
	  * @return 	length of this fromBasicPatternClass in bytes
	  */   
	  public int getLength()
	   {
	      return bp_length;
	   }

	/** get the length of a fromBasicPatternClass, call this method if you did 
	  *  call setHdr () before
	  * @return     size of this fromBasicPatternClass in bytes
	  */
	  public short size()
	   {
	      return ((short) (fldOffset[fldCnt] - bp_offset));
	   }
	 
	   /** get the offset of a fromBasicPatternClass
	    *  @return offset of the fromBasicPatternClass in byte array
	    */   
	   public int getOffset()
	   {
	      return bp_offset;
	   }   
	   
	   /** Copy the tuple byte array out
	    *  @return  byte[], a byte array contains the tuple
	    *		the length of byte[] = length of the tuple
	    */
	    
	   public byte [] getBPByteArray() 
	   {
	       byte [] tuplecopy = new byte [bp_length];
	       System.arraycopy(data, bp_offset, tuplecopy, 0, bp_length);
	       return tuplecopy;
	   }
	   
	   /** return the data byte array 
	    *  @return  data byte array 		
	    */
	    
	   public byte [] returnBPByteArray()
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
	    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
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
	     throw new FieldNumberOutOfBoundException (null, "BasicPatternClass:TUPLE_FLDNO_OUT_OF_BOUND");
	  }
	    
	   /**
	    * Convert this field in to float
	    *
	    * @param    fldNo   the field number
	    * @return           the converted float number  if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
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
	       throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
	     }


	   /**
	    * Convert this field into String
	    *
	    * @param    fldNo   the field number
	    * @return           the converted string if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
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
	     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
	  }
	 
	   /**
	    * Convert this field into a character
	    *
	    * @param    fldNo   the field number
	    * @return           the character if success
	    *			
	    * @exception   IOException I/O errors
	    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
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
	       throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
	 
	    }

	  /**
	   * Set this field to integer value
	   *
	   * @param	fldNo	the field number
	   * @param	val	the integer value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
	   */

	  public BasicPatternClass setIntFld(int fldNo, int val) 
	  	throws IOException, FieldNumberOutOfBoundException
	  { 
	    if ( (fldNo > 0) && (fldNo <= fldCnt))
	     {
		Convert.setIntValue (val, fldOffset[fldNo -1], data);
		return this;
	     }
	    else 
	     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND"); 
	  }

	  /**
	   * Set this field to float value
	   *
	   * @param     fldNo   the field number
	   * @param     val     the float value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
	   */

	  public BasicPatternClass setFloFld(int fldNo, float val) 
	  	throws IOException, FieldNumberOutOfBoundException
	  { 
	   if ( (fldNo > 0) && (fldNo <= fldCnt))
	    {
	     Convert.setFloValue (val, fldOffset[fldNo -1], data);
	     return this;
	    }
	    else  
	     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND"); 
	     
	  }

	  /**
	   * Set this field to String value
	   *
	   * @param     fldNo   the field number
	   * @param     val     the string value
	   * @exception   IOException I/O errors
	   * @exception   FieldNumberOutOfBoundException BasicPatternClass field number out of bound
	   */

	   public BasicPatternClass setStrFld(int fldNo, String val) 
			throws IOException, FieldNumberOutOfBoundException  
	   {
	     if ( (fldNo > 0) && (fldNo <= fldCnt))        
	      {
	         Convert.setStrValue (val, fldOffset[fldNo -1], data);
	         return this;
	      }
	     else 
	       throw new FieldNumberOutOfBoundException (null, "BasicPatternClass:TUPLE_FLDNO_OUT_OF_BOUND");
	    }


	   /**
	    * setHdr will set the header of this tuple.   
	    *
	    * @param	numFlds	  number of fields
	    * @param	types[]	  contains the types that will be in this tuple
	    * @param	strSizes[]      contains the sizes of the string 
	    *				
	    * @exception IOException I/O errors
	    * @exception InvalidTypeException Invalid tupe type
	    * @exception InvalidTupleSizeException Tuple size too big
	    *
	    */

	public void setHdr (short numFlds,  AttrType types[], short strSizes[])
	 throws IOException, InvalidTypeException, InvalidTupleSizeException		
	{
	  if((numFlds +2)*2 > max_size)
	    throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
	  
	  fldCnt = numFlds;
	  Convert.setShortValue(numFlds, bp_offset, data);
	  fldOffset = new short[numFlds+1];
	  int pos = bp_offset+2;  // start position for fldOffset[]
	  
	  //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
	  //another 1 for fldCnt
	  fldOffset[0] = (short) ((numFlds +2) * 2 + bp_offset);   
	   
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
	    throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
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
	    throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
	   }

	  fldOffset[numFlds] = (short) (fldOffset[i-1] + incr);
	  Convert.setShortValue(fldOffset[numFlds], pos, data);
	  
	  bp_length = fldOffset[numFlds] - bp_offset;

	  if(bp_length > max_size)
	   throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
	}
	     
	  
	  /**
	   * Returns number of fields in this tuple
	   *
	   * @return the number of fields in this tuple
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
	  	 * @param newConfidence the confidence to set
	  	 */
	  	public void setConfidence(float newConfidence) {
	  		confidence = newConfidence;
	  	}
	  
	  	/**
	  	 * @return the confidence
	  	 */
	  	public float getConfidence() {
	  		return confidence;
	  	}

	 /**
	  * Print out the tuple
	  * @param type  the types in the tuple
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

	

} // end BasicPatternClass
