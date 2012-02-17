package tripleheap;

import global.AttrType;
import global.Convert;
import global.EID;
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
	   * length of this BasicPatternClass in bytes
	   */
	  private int bp_length;

	  /** 
	   * private field
	   * Number of entities in this BasicPatternClass
	   */
	  private short entityCnt;

	  /** 
	   * private field
	   * Array of offsets of the entities
	   */	 
	  private short [] entityOffset = {0}; 

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
	       entityCnt = fromBasicPatternClass.noOfEntities(); 
	       entityOffset = fromBasicPatternClass.copyFldOffset(); 
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
	       entityCnt = 0;
	  }
	   
	   /** Copy a tuple to the current tuple position
	    *  you must make sure the tuple lengths must be equal
	    * @param fromBasicPatternClassTuple the tuple being copied
	    */
	   public void bpCopy(BasicPatternClass fromBasicPatternClass)
	   {
	       byte [] temparray = fromBasicPatternClass.getBPByteArray();
	       System.arraycopy(temparray, 0, data, bp_offset, bp_length);   
	       entityCnt = fromBasicPatternClass.noOfEntities(); 
	       entityOffset = fromBasicPatternClass.copyFldOffset(); 
	   }

	   /** This is used when you don't want to use the constructor.
	    *  Assumes that aBP contains 1 entity
	    * @param aBP  a byte array which contains the fromBasicPatternClass
	    * @param offset the offset of the fromBasicPatternClass in the byte array
	    * @param length the length of the fromBasicPatternClass
	    */

	   public void bpInit(byte [] aBP, int offset, int length)
	   {
	      data = aBP;
	      bp_offset = offset;
	      bp_length = length;
	      entityCnt = 1;
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
	      return ((short) (entityOffset[entityCnt] - bp_offset));
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

	   public byte [] getBPByteArray() {
	       byte [] tuplecopy = new byte [bp_length];
	       System.arraycopy(data, bp_offset, tuplecopy, 0, bp_length);
	       return tuplecopy;
	   }
	   
	   /** return the data byte array 
	    *  @return  data byte array 		
	    */
	    
	   public byte [] returnBPByteArray() {
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
	    if ( (fldNo > 0) && (fldNo <= entityCnt))
	     {
	      val = Convert.getIntValue(entityOffset[fldNo -1], data);
	      return val;
	     }
	    else
	     throw new FieldNumberOutOfBoundException (null, "BasicPatternClass:TUPLE_FLDNO_OUT_OF_BOUND");
	  }
	  
	  /**
	 * @param nodePos the position of the entity within the Basic Pattern
	 * 				  (starts at 1).
	 * @return the EID of the node at the given position
	 * @throws IOException
	 */
	public EID getEIDbyNodePosition(int nodePos) throws IOException{
		  EID retVal = new EID();
		    if ( (nodePos > 0) && (nodePos <= entityCnt))
		     {		    	
		      retVal.slotNo = Convert.getIntValue(entityOffset[nodePos -1], data);
		      retVal.pageNo.pid = Convert.getIntValue(entityOffset[nodePos -1] + Integer.SIZE , data);
		     }
		    else
		    	System.out.println("BasicPatternClass: Node position out of bounds!");
		  return retVal;
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
	      if ( (fldNo > 0) && (fldNo <= entityCnt))
	       {
	        val = Convert.getFloValue(entityOffset[fldNo -1], data);
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
	    if ( (fldNo > 0) && (fldNo <= entityCnt))      
	     {
	        val = Convert.getStrValue(entityOffset[fldNo -1], data, 
			entityOffset[fldNo] - entityOffset[fldNo -1]); //strlen+2
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
	      if ( (fldNo > 0) && (fldNo <= entityCnt))      
	       {
	        val = Convert.getCharValue(entityOffset[fldNo -1], data);
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
	    if ( (fldNo > 0) && (fldNo <= entityCnt))
	     {
		Convert.setIntValue (val, entityOffset[fldNo -1], data);
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
	   if ( (fldNo > 0) && (fldNo <= entityCnt))
	    {
	     Convert.setFloValue (val, entityOffset[fldNo -1], data);
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
	     if ( (fldNo > 0) && (fldNo <= entityCnt))        
	      {
	         Convert.setStrValue (val, entityOffset[fldNo -1], data);
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
	  
	  entityCnt = numFlds;
	  Convert.setShortValue(numFlds, bp_offset, data);
	  entityOffset = new short[numFlds+1];
	  int pos = bp_offset+2;  // start position for fldOffset[]
	  
	  //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
	  //another 1 for fldCnt
	  entityOffset[0] = (short) ((numFlds +2) * 2 + bp_offset);   
	   
	  Convert.setShortValue(entityOffset[0], pos, data);
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
	  entityOffset[i]  = (short) (entityOffset[i-1] + incr);
	  Convert.setShortValue(entityOffset[i], pos, data);
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

	  entityOffset[numFlds] = (short) (entityOffset[i-1] + incr);
	  Convert.setShortValue(entityOffset[numFlds], pos, data);
	  
	  bp_length = entityOffset[numFlds] - bp_offset;

	  if(bp_length > max_size)
	   throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
	}
	     
	  
	  /**
	   * Returns number of entities in this Basic Pattern
	   *
	   * @return the number of entities in this Basic Pattern
	   *
	   */

	  public short noOfEntities() 
	   {
	     return entityCnt;
	   }

	  /**
	   * Makes a copy of the fldOffset array
	   *
	   * @return a copy of the fldOffset arrray
	   *
	   */

	  public short[] copyFldOffset() 
	   {
	     short[] newFldOffset = new short[entityCnt + 1];
	     for (int i=0; i<=entityCnt; i++) {
	       newFldOffset[i] = entityOffset[i];
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
	  	
	  	/** Add the entity to the Basic Pattern. Will not add duplicate entities.
	  	 * @param eid the EID of an entity to add to the Basic Pattern
	  	 * 			  the entity will be inserted at the end of the byte[] data
	  	 * @throws IOException 
	  	 */
	  	public void addEntityToBP(EID eid) throws IOException{
	  		boolean entityAlreadyInBP = false;
	  		EID itrEid;
	  		for(int i=0 ; i<entityCnt ; i++){
	  			itrEid = getEIDbyNodePosition(i);
	  			if(itrEid.equals(eid)){
	  				entityAlreadyInBP = true;
	  				break;
	  			}	  				
	  		}	  		
	  		
	  		if(!entityAlreadyInBP) // the entity is not in the list, add it!
	  		{
		  		byte[] tempArray = new byte[data.length + eid_size];
		  		System.arraycopy(data, bp_offset, tempArray, 0, bp_length);
		  		Convert.setIntValue(eid.slotNo, data.length, tempArray);
		  		Convert.setIntValue(eid.pageNo.pid, data.length+4, tempArray);
		  		data = tempArray;	 
		  		entityCnt++;
		  		short[] tempOffsetArry = new short[entityOffset.length + 1];
		  		System.arraycopy(entityOffset, 0, tempOffsetArry, 0, entityOffset.length);
		  		short value = (short) (entityOffset[entityOffset.length-1] + 8);
		  		tempOffsetArry[entityOffset.length] = value;
		  		entityOffset = tempOffsetArry;
		  		bp_length += eid_size;
	  		}
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
	  for (i=0; i< entityCnt-1; i++)
	   {
	    switch(type[i].attrType) {

	   case AttrType.attrInteger:
	     val = Convert.getIntValue(entityOffset[i], data);
	     System.out.print(val);
	     break;

	   case AttrType.attrReal:
	     fval = Convert.getFloValue(entityOffset[i], data);
	     System.out.print(fval);
	     break;

	   case AttrType.attrString:
	     sval = Convert.getStrValue(entityOffset[i], data,entityOffset[i+1] - entityOffset[i]);
	     System.out.print(sval);
	     break;
	  
	   case AttrType.attrNull:
	   case AttrType.attrSymbol:
	     break;
	   }
	   System.out.print(", ");
	 } 
	 
	 switch(type[entityCnt-1].attrType) {

	   case AttrType.attrInteger:
	     val = Convert.getIntValue(entityOffset[i], data);
	     System.out.print(val);
	     break;

	   case AttrType.attrReal:
	     fval = Convert.getFloValue(entityOffset[i], data);
	     System.out.print(fval);
	     break;

	   case AttrType.attrString:
	     sval = Convert.getStrValue(entityOffset[i], data,entityOffset[i+1] - entityOffset[i]);
	     System.out.print(sval);
	     break;

	   case AttrType.attrNull:
	   case AttrType.attrSymbol:
	     break;
	   }
	   System.out.println("]");

	 }

	

} // end BasicPatternClass
