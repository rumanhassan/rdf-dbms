/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree;
import global.*;

/** KeyDataEntry: define (key, data) pair.
 */
public class KeyDataEntry {
   /** key in the (key, data)
    */  
   public KeyClass key;
   /** data in the (key, data)
    */
   public DataClass data;
   
  /** Class constructor
   */
  public KeyDataEntry( Integer key, PageID pageNo) {
     this.key = new IntegerKey(key); 
     this.data = new IndexData(pageNo);
  }; 



  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key, PageID pageNo) {

     data = new IndexData(pageNo); 
     if ( key instanceof IntegerKey ) 
        this.key= new IntegerKey(((IntegerKey)key).getKey());
     else if ( key instanceof StringKey ) 
        this.key= new StringKey(((StringKey)key).getKey());    
  };


  /** Class constructor.
   */
  public KeyDataEntry( String key, PageID pageNo) {
     this.key = new StringKey(key); 
     this.data = new IndexData(pageNo);
  };

  /** Class constructor.
   */
  public KeyDataEntry( Integer key, TID genid) {
     this.key = new IntegerKey(key); 
     this.data = new LeafData(genid);
  };

  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key, TID genid){
     data = new LeafData(genid); 
     if ( key instanceof IntegerKey ) 
        this.key= new IntegerKey(((IntegerKey)key).getKey());
     else if ( key instanceof StringKey ) 
        this.key= new StringKey(((StringKey)key).getKey());    
  };


  /** Class constructor.
   */
  public KeyDataEntry( String key, TID genid) {
     this.key = new StringKey(key); 
     this.data = new LeafData(genid);
  }; 

  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key,  DataClass data) {
     if ( key instanceof IntegerKey ) 
        this.key= new IntegerKey(((IntegerKey)key).getKey());
     else if ( key instanceof StringKey ) 
        this.key= new StringKey(((StringKey)key).getKey()); 

     if ( data instanceof IndexData ) 
        this.data= new IndexData(((IndexData)data).getData());
     else if ( data instanceof LeafData ) 
        this.data= new LeafData(((LeafData)data).getData()); 
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(KeyDataEntry entry) {
      boolean st1,st2;

      if ( key instanceof IntegerKey )
         st1= ((IntegerKey)key).getKey().equals
                  (((IntegerKey)entry.key).getKey());
      else 
         st1= ((StringKey)key).getKey().equals
                  (((StringKey)entry.key).getKey());

      if( data instanceof IndexData )
         st2= ( (IndexData)data).getData().pid==
              ((IndexData)entry.data).getData().pid ;
      else
         st2= ((TID)((LeafData)data).getData()).equals
                (((TID)((LeafData)entry.data).getData()));

  
      return (st1&&st2);
  }     
}


