package pathOramHw;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

/*
 * Name: TODO
 * StudentID: TODO
 */

public class Bucket{
	private static boolean is_init = false;
	private static int max_size_Z = -1;
	
	//TODO Add necessary variables
	
	Bucket(){
		if(is_init == false)
		{
			throw new RuntimeException("Please set bucket size before creating a bucket");
		}
		//TODO Must complete this method for submission
	}
	
	// Copy constructor
	Bucket(Bucket other)
	{
		if(other == null)
		{
			throw new RuntimeException("the other bucket is not malloced.");
		}
		//TODO Must complete this method for submission
	}
	
	//Implement and add your own methods.
	Block getBlockByKey(int key){
		// TODO Must complete this method for submission
		return null;
	}
	
	void addBlock(Block new_blk){
		// TODO Must complete this method for submission
	}
	
	boolean removeBlock(Block rm_blk)
	{
		// TODO Must complete this method for submission
		return false;
	}
	
	
	ArrayList<Block> getBlocks(){
		// TODO Must complete this method for submission
		return null;
	}
	
	int returnRealSize(){
		// TODO Must complete this method for submission
		return 0;
	}

	static void resetState()
	{
		is_init = false;
	}

	static void setMaxSize(int maximumSize)
	{
		if(is_init == true)
		{
			throw new RuntimeException("Max Bucket Size was already set");
		}
		max_size_Z = maximumSize;
		is_init = true;
	}

}