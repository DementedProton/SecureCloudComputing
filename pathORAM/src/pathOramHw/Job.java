/*
 *  You can use this class to test your Oram (for correctness, not security).
 *  
 *  You can experiment modifying this class, but we will not take it into account (we will test your ORAM implementations on this as well as other Jobs)
 *  
 */

package pathOramHw;

import java.util.*;

import pathOramHw.ORAMInterface.Operation;

public class Job {

	public static void main(String[] args) {
		int bucket_size = 4;
		int num_blocks = (int) Math.pow(2, 20);
		ArrayList<Integer> data_collection = new ArrayList<Integer>();

		PrintWriter writer = new PrintWriter("simulation2.txt", "UTF-8");

		//Set the Bucket size for all the buckets.
		Bucket.setMaxSize(bucket_size);
				
		//Initialize new UntrustedStorage
		UntrustedStorageInterface storage = new ServerStorageForHW();

		//Initialize a randomness generator
		RandForORAMInterface rand_gen = new RandomForORAMHW();
		
		//Initialize a new Oram
		ORAMInterface oram = new ORAMWithReadPathEviction(storage, rand_gen, bucket_size, num_blocks);

		//Initialize a buffer value
		byte[] write_bbuf = new byte[128];
		for(int i = 0; i < 128; i++)
		{
			write_bbuf[i] = (byte) 0xa;
		}

		// 3 million warm ups
		for(int i = 0; i < 3000000; i++)
		{
			oram.access(Operation.WRITE, i % num_blocks, write_bbuf);
		}

		// testing accessess by doing writes  - 100 million
		for(int i = 0; i < 100000000; i++)
		{
			oram.access(Operation.WRITE, i % num_blocks, write_bbuf);
			int size_of_data = data_collection.size();
			int stash_size = oram.getStashSize();
			if (size_of_data <= stash_size)
			{
				for (int j=size_of_data; j<stash_size; j++)
				{
					data_collection.add(0);
				}
				data_collection.add(1);
			}
			else
			{
				data_collection.set(stash_size, data_collection.get(stash_size)+1);
			}

		}

		int size_of_data = data_collection.size();
		writer.println(-1 + "," + 100000000);
		for (int i=0; i<size_of_data; i++)
		{
			int counter = 0;
			for (int j=i+1; j<size_of_data; j++)
			{
				counter += data_collection.get(j);
			}
			writer.println(i + "," + counter);
		}


	}
}
