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

ArrayList al = new ArrayList();

public BPIterator(ArrayList abc)
{

al=abc;
}

public void addBP( BasicPatternClass b)
{al.add(b);
}


public ArrayList getArrayList()
{
	return al;
}
}
