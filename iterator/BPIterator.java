package iterator;
import global.*;
import tripleheap.*;
import diskmgr.*;
import bufmgr.*;
import index.*;
import java.io.*;
import java.util.*;

import java.util.ArrayList;

import tripleheap.BasicPatternClass;

public class BPIterator {

public ArrayList<BasicPatternClass> bpList;

public BPIterator(ArrayList abc)
{

	bpList=abc;
}

public void addBP( BasicPatternClass b)
{bpList.add(b);
}

public BasicPatternClass get_next() {
	// TODO Auto generated stub
	return null;
}




public ArrayList getArrayList()
{
	return bpList;
}
}
