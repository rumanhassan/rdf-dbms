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

	private int currentIndex;
	public ArrayList<BasicPatternClass> bpList;

	public BPIterator()
	{	
		bpList=new ArrayList<BasicPatternClass>();
		currentIndex = 0;
	}
	
	public void addBP( BasicPatternClass b){
		bpList.add(b);
	}
	
	public BasicPatternClass get_next() {
		if(currentIndex >= bpList.size())
			return null;
		else {
			currentIndex++;
			return bpList.get(currentIndex-1);
		}
	}
	
	public void resetIndex(){
		currentIndex = 0;
	}

	public ArrayList<BasicPatternClass> getArrayList()
	{
		return bpList;
	}
}
