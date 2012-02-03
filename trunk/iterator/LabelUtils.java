package iterator;


import labelheap.*;
import global.*;
import java.io.*;
import java.lang.*;

/**
 *some useful method when processing Label 
 */
public class LabelUtils
{	
	/**
	 * This function compares a label with another label in respective field, and
	 *  returns:
	 *
	 *    0        if the two are equal,
	 *    1        if the tuple is greater,
	 *   -1        if the tuple is smaller,
	 *
	 * @param l1		one label.
	 * @param l2		another label.
	 * @return			0        if the two are equal,
	 *          		1        if the label is greater,
	 *         		   -1        if the label is smaller,
	 * @throws IOException some I/O fault
	 * @throws LabelUtilsException exception from this class
	 */
public static int compareLabelWithLabel(Label  l1, Label  l2)
    throws IOException,
	   LabelUtilsException
    {
      String l1_s, l2_s;      
      l1_s = l1.getLabel();
      l2_s = l2.getLabel();
	  
	  if(l1_s.compareTo( l2_s)>0)return 1;
	  if (l1_s.compareTo( l2_s)<0)return -1;
	  return 0;	
    }
  
  
  
  /**
   * This function  compares  label1 with another label2 whose
   * field number is same as the label1
   *
   *@param    l1        one label
   *@param    value     the string identifier of another label.
   *@return   0        if the two are equal,
   *          1        if the label is greater,
   *         -1        if the label is smaller,  
   *@exception IOException some I/O fault
   *@exception LabelUtilsException exception from this class   
   */            
  public static int compareLabelWithValue(Label  l1, String  value)
    throws IOException,
	   LabelUtilsException
    {
	  Label tempLabel = new Label();
	  tempLabel = tempLabel.setLabel(value);
      return compareLabelWithLabel(l1, tempLabel);
    }
  
  /**
   *This function Compares two Label in all fields 
   * @param l1 the first label
   * @param l2 the second label
   * @param type[] the field types
   * @param len the field numbers
   * @return  0        if the two are not equal,
   *          1        if the two are equal,
   *@exception UnknowAttrType don't know the attribute type
   *@exception IOException some I/O fault
   *@exception LabelUtilsException exception from this class
   */            
  
  public static boolean Equal(Label l1, Label l2)
    throws IOException,LabelUtilsException
    {
	  if (compareLabelWithLabel(l1, l2) != 0)
		  return false;
      return true;
    }
  
  /**
   *get the string from the specified Label object
   *@param label the Label 
   *@return the content of the field number
   *@exception IOException some I/O fault
   *@exception LabelUtilsException exception from this class
   */
  public static String Value(Label  label)
    throws IOException,
	   LabelUtilsException
    {
      String temp;
      temp = label.getLabel();
      return temp;
    }
  
 
  /**
   *set up a label from another label
   *@param value the label to be set 
   *@param label the given label
   *@exception IOException some I/O fault
   *@exception LabelUtilsException exception from this class
   */  
  public static void SetValue(Label value, Label  label)
    throws IOException,
	   LabelUtilsException
    {
	  	// do the stuff we want to do
	    value.setLabel(label.getLabel());      
	    return;
    }
  
	  /** This method converts an byte array to a String object.
	 * @param byte[] the array of bytes to be converted to a String
	 * @return the string extracted from the byte array
	 */
	public static String convertByteArrayToString(byte[] byteArray) {      
	      String value = new String(byteArray);      
	      return value;
	  }

	/** This method converts a String to an array of bytes
	 * @param stringToConvert the String to convert into a byte array
	 * @return the array byte[] from the given String
	 */
	public static byte[] convertStringToByteArray(String stringToConvert) 
	throws UnsupportedEncodingException {
	    byte[] theByteArray = stringToConvert.getBytes("UTF-8");    
	    return theByteArray;
	}
  
  
  /**
   *set up the Jlabel's attrtype, string size,field number for using join
   *@param Jlabel  reference to an actual tuple  - no memory has been malloced
   *@param res_attrs  attributes type of result tuple
   *@param in1  array of the attributes of the tuple (ok)
   *@param len_in1  num of attributes of in1
   *@param in2  array of the attributes of the tuple (ok)
   *@param len_in2  num of attributes of in2
   *@param l1_str_sizes shows the length of the string fields in S
   *@param l2_str_sizes shows the length of the string fields in R
   *@param proj_list shows what input fields go where in the output tuple
   *@param nOutFlds number of outer relation fileds
   *@exception IOException some I/O fault
   *@exception TupleUtilsException exception from this class
   */
  /* TODO: uncomment or remove this method
   * public static short[] setup_op_tuple(Label Jlabel, AttrType[] res_attrs,
				       AttrType in1[], int len_in1, AttrType in2[], 
				       int len_in2, short l1_str_sizes[], 
				       short l2_str_sizes[], 
				       FldSpec proj_list[], int nOutFlds)
    throws IOException,
	   LabelUtilsException
    {
      short [] sizesT1 = new short [len_in1];
      short [] sizesT2 = new short [len_in2];
      int i, count = 0;
      
      for (i = 0; i < len_in1; i++)
        if (in1[i].attrType == AttrType.attrString)
	  sizesT1[i] = l1_str_sizes[count++];
      
      for (count = 0, i = 0; i < len_in2; i++)
	if (in2[i].attrType == AttrType.attrString)
	  sizesT2[i] = l2_str_sizes[count++];
      
      int n_strs = 0; 
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer)
	    res_attrs[i] = new AttrType(in1[proj_list[i].offset-1].attrType);
	  else if (proj_list[i].relation.key == RelSpec.innerRel)
	    res_attrs[i] = new AttrType(in2[proj_list[i].offset-1].attrType);
	}
      
      // Now construct the res_str_sizes array.
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset-1].attrType == AttrType.attrString)
            n_strs++;
	  else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset-1].attrType == AttrType.attrString)
            n_strs++;
	}
      
      short[] res_str_sizes = new short [n_strs];
      count         = 0;
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset-1].attrType ==AttrType.attrString)
            res_str_sizes[count++] = sizesT1[proj_list[i].offset-1];
	  else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset-1].attrType ==AttrType.attrString)
            res_str_sizes[count++] = sizesT2[proj_list[i].offset-1];
	}
      try {
	Jlabel.setHdr((short)nOutFlds, res_attrs, res_str_sizes);
      }catch (Exception e){
	throw new LabelUtilsException(e,"setHdr() failed");
      }
      return res_str_sizes;
    }*/
  
 
   /**
   *set up the Jlabel's attrtype, string size,field number for using project
   *@param Jlabel  reference to an actual label  - no memory has been malloced
   *@param res_attrs  attributes type of result label
   *@param in1  array of the attributes of the label (ok)
   *@param len_in1  num of attributes of in1
   *@param l1_str_sizes shows the length of the string fields in S
   *@param proj_list shows what input fields go where in the output label
   *@param nOutFlds number of outer relation fields
   *@exception IOException some I/O fault
   *@exception LabelUtilsException exception from this class
   *@exception InvalidRelation invalid relation 
   */

  /* TODO: uncomment or remove this method
  public static short[] setup_op_label(Label Jlabel, AttrType res_attrs[],
				       AttrType in1[], int len_in1,
				       short l1_str_sizes[], 
				       FldSpec proj_list[], int nOutFlds)
    throws IOException,
	   LabelUtilsException, 
	   InvalidRelation
    {
      short [] sizesT1 = new short [len_in1];
      int i, count = 0;
      
      for (i = 0; i < len_in1; i++)
        if (in1[i].attrType == AttrType.attrString)
	  sizesT1[i] = l1_str_sizes[count++];
      
      int n_strs = 0; 
      for (i = 0; i < nOutFlds; i++)
	  {
	    if (proj_list[i].relation.key == RelSpec.outer) 
              res_attrs[i] = new AttrType(in1[proj_list[i].offset-1].attrType);
	  
	    else throw new InvalidRelation("Invalid relation -innerRel");
	  }
      
      // Now construct the res_str_sizes array.
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer
	      && in1[proj_list[i].offset-1].attrType == AttrType.attrString)
	    n_strs++;
	}
      
      short[] res_str_sizes = new short [n_strs];
      count         = 0;
      for (i = 0; i < nOutFlds; i++) {
	if (proj_list[i].relation.key ==RelSpec.outer
	    && in1[proj_list[i].offset-1].attrType ==AttrType.attrString)
	  res_str_sizes[count++] = sizesT1[proj_list[i].offset-1];
      }
     
      try {
	Jlabel.setHdr((short)nOutFlds, res_attrs, res_str_sizes);
      }catch (Exception e){
	throw new LabelUtilsException(e,"setHdr() failed");
      } 
      return res_str_sizes;
    } */
}




