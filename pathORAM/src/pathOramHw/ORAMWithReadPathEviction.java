package pathOramHw;

import java.util.ArrayList;

/*
 * Name: TODO
 * NetID: TODO
 */

public class ORAMWithReadPathEviction implements ORAMInterface{

	private UntrustedStorageInterface untrustedStorageInterface;
	private RandForORAMInterface randForORAMInterface;
	private int size_of_bucket_Z;
	private int number_of_data_blocks_N;
	private int[] position_map;
	private int height_of_tree_L;
	private ArrayList<Block> client_stash;


	public ORAMWithReadPathEviction(UntrustedStorageInterface storage, RandForORAMInterface rand_gen, int bucket_size,
									int num_blocks)
	{
		untrustedStorageInterface = storage;
		untrustedStorageInterface.setCapacity(getNumBuckets());

		randForORAMInterface = rand_gen;
		randForORAMInterface.setBound(getNumLeaves());

		size_of_bucket_Z = bucket_size;
		number_of_data_blocks_N = num_blocks;
		position_map = new int[number_of_data_blocks_N];
		height_of_tree_L = (int)(Math.ceil(Math.log(number_of_data_blocks_N)/Math.log(2))); // [log2(N)]
		client_stash = new ArrayList<Block>();

		for(int i=0; i<position_map.length; i++)
		{
			position_map[i] = randForORAMInterface.getRandomLeaf();
		}

		Bucket temporary_bucket = new Bucket();

		for(int i=0; i< getNumBuckets(); i++)
		{
			untrustedStorageInterface.WriteBucket(i, temporary_bucket);
		}
	}


	@Override
	public byte[] access(Operation op, int blockIndex, byte[] newdata)
	{
		int x = position_map[blockIndex];
		position_map[blockIndex] = randForORAMInterface.getRandomLeaf();

		for(int i=0; i<=height_of_tree_L; i++)
		{
			Bucket temporary_bucket = untrustedStorageInterface.ReadBucket(P(x, i));
			for(int j=0; j<temporary_bucket.returnRealSize(); j++)
			{
				client_stash.add(temporary_bucket.getBlocks().get(j));
			}
		}

		byte[] data = null;
		for(int i=0; i<client_stash.size(); i++)
		{
			if(client_stash.get(i).index == blockIndex)
			{
				data = client_stash.get(i).data;
			}
		}
		if(op == Operation.WRITE)
		{
			ArrayList<Block> temporary_stash = client_stash;
			for(int i=0; i<temporary_stash.size(); i++)
			{
				if(temporary_stash.get(i).index == blockIndex)
				{
					temporary_stash.remove(i);
				}
			}
			Block temporary_block = new Block(blockIndex, newdata);
			temporary_stash.add(temporary_block);
			client_stash = temporary_stash;
		}

		ArrayList<Block> temporary_stash;
		for(int i=height_of_tree_L; i>=0; i--)
		{
			temporary_stash = new ArrayList<Block>();
			for(int j=0; j<client_stash.size(); j++)
			{
				Block temporary_block = client_stash.get(j);
				if(P(x, i) == P(position_map[temporary_block.index], i))
				{
					temporary_stash.add(temporary_block);
				}
			}
			int a = (int)(Math.min(size_of_bucket_Z, temporary_stash.size()));
			temporary_stash.removeAll(temporary_stash.subList(a, temporary_stash.size()));
			client_stash.removeAll(temporary_stash);
			Bucket temporary_bucket = new Bucket();
			for(int k=0; k<temporary_stash.size(); k++)
			{
				temporary_bucket.addBlock(temporary_stash.get(k));
			}
			untrustedStorageInterface.WriteBucket(P(x, i), temporary_bucket);
		}

	}


	@Override
	public int P(int leaf, int level)
	{
		int nodes_at_current_level = (int)(Math.pow(2, height_of_tree_L-level));
		int total_nodes_at_current_level = 2*(leaf/nodes_at_current_level) + 1;
		return (nodes_at_current_level*total_nodes_at_current_level) - 1;
	}


	@Override
	public int[] getPositionMap()
	{
		return position_map;
	}


	@Override
	public ArrayList<Block> getStash()
	{
		return client_stash;
	}


	@Override
	public int getStashSize()
	{
		return client_stash.size();
	}

	@Override
	public int getNumLeaves()
	{
		return (int)(Math.pow(2, height_of_tree_L)); // 2^L
	}


	@Override
	public int getNumLevels()
	{
		return height_of_tree_L; // depth of tree - L
	}


	@Override
	public int getNumBlocks()
	{
		return number_of_data_blocks_N;
	}


	@Override
	public int getNumBuckets()
	{
		return (int)(Math.pow(2, height_of_tree_L+1) - 1); // 2^(L+1) - 1
	}


	
}
