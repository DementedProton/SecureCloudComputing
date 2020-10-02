package pathOramHw;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

/*
Sudharshan Swaminathan UT-s2378809
Vasanth Subramanian UT-s2493055
 */

public class Bucket{
	private static boolean is_init = false;
	private static int max_size_Z = -1;

	private ArrayList<Block> blocks_of_bucket;
	private int current_size_of_bucket_Z;
	
	Bucket()
	{
		if(is_init == false)
		{
			throw new RuntimeException("Please set bucket size before creating a bucket");
		}
		current_size_of_bucket_Z = 0;
		blocks_of_bucket = new ArrayList<Block>(max_size_Z);
		for(int i=0; i<max_size_Z; i++)
		{
			blocks_of_bucket.add(new Block()); // malloc blocks to bucket
		}
	}
	
	// Copy constructor
	Bucket(Bucket other)
	{
		if(other == null)
		{
			throw new RuntimeException("the other bucket is not malloced.");
		}
		blocks_of_bucket = new ArrayList<Block>(max_size_Z);
		for(int i=0; i<other.getBlocks().size(); i++)
		{
			blocks_of_bucket.add(new Block(other.getBlocks().get(i)));
		}
		current_size_of_bucket_Z = other.current_size_of_bucket_Z;

	}


	Block getBlockByKey(int key) // returns the block where the index == key
	{
		for(int i=0; i<blocks_of_bucket.size(); i++)
		{
			if(blocks_of_bucket.get(i).index == key)
			{
				return blocks_of_bucket.get(i);
			}
		}
		return null;
	}


	void addBlock(Block new_blk)
	{
		blocks_of_bucket.set(current_size_of_bucket_Z, new_blk);
		current_size_of_bucket_Z++;
	}


	boolean removeBlock(Block rm_blk)
	{
		for(int i=0; i<blocks_of_bucket.size(); i++)
		{
			if(blocks_of_bucket.get(i).index == rm_blk.index)
			{

				blocks_of_bucket.remove(i);
				blocks_of_bucket.add(new Block()); // replace the one removed with a empty block
				current_size_of_bucket_Z--;
				return true;
			}
		}
		return false;
	}
	
	
	ArrayList<Block> getBlocks()
	{
		return blocks_of_bucket;
	}


	int returnRealSize()
	{
		return current_size_of_bucket_Z;
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