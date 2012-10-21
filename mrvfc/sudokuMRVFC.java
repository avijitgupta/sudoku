package mrvfc;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

//Pair of coordinates (i,j)
class pair
{
	int x;
	int y;
	public pair(int x, int y)
	{
		this.x  = x;
		this.y  = y;
	}
}

public class sudokuMRVFC {
	static int M, N, K;
	static int[][][] sudokuBoard;
	static pair[] usedPositions;
	static pair[] unusedPositions;
	static int numberUnusedPositions;
	static int unmodifiable = -10;
	static int modifiable = 10;
	static int count = 0;
	public static void displayBoard()
	{
		for(int i = 0 ; i < N ; i ++)
		{
			for(int  j =0 ; j < N ; j ++)
				System.out.print(sudokuBoard[i][j][0] + "\t");
			System.out.println();
		}
		System.out.println();
	}
	
	public static void displayConstraints()
	{
		for(int i = 0 ; i < N ; i ++)
		{
			for(int j = 0 ; j < N ; j ++)
			{
				System.out.print(sudokuBoard[i][j][1]+ " ");
				for(int  k = 2 ; k < N+2; k++ )
				{
					if(sudokuBoard[i][j][k]>0)
					System.out.print(k-1 + ", ");
				}
				System.out.print("\t\t");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	public static boolean hasBeenChecked(pair value, pair[] unusedPositions, int level)
	{
		for(int i = 0 ; i < level ; i ++)
		{
			if((value.x == unusedPositions[i].x) && (value.y == unusedPositions[i].y))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void displayBoardHash(int value)
	{
		for(int i = 0 ; i < N ; i ++)
		{
			for(int  j =0 ; j < N ; j ++)
				System.out.print(sudokuBoard[i][j][value + 1] + "\t");
			System.out.println();
		}
		System.out.println();
	}
	
	public static boolean updateBoard(int value, int row, int col, pair[] unusedPos, int level)
	{
		boolean success = true;
		
		//Update one row
		for(int i = 0; i < N ; i ++)
		{
			if(sudokuBoard[row][i][N+2] !=unmodifiable && i!=col)
			{
				count++;

				if(sudokuBoard[row][i][value+1] > 0)
				{
					sudokuBoard[row][i][1] --;
				}
				sudokuBoard[row][i][value+1] --;
				
				//hasBeenChecked(pair value, pair[] unusedPositions, int level)
				
				if(sudokuBoard[row][i][1]<=0 && !hasBeenChecked(new pair(row, i), unusedPos, level))
					success = false;
			}
		}
		
		//Update column
		for(int i = 0; i < N ; i ++)
		{
			if(sudokuBoard[i][col][N+2] !=unmodifiable && i!=row)
			{
				count++;

				if(sudokuBoard[i][col][value+1] > 0)
				{
					sudokuBoard[i][col][1] --;
				}
				sudokuBoard[i][col][value+1] --;
				
				if(sudokuBoard[i][col][1]<=0 && !hasBeenChecked(new pair(i, col), unusedPos, level))
					success = false;
			}
		}
		
		//update block
		int numBlocksX = (int) (row / M);
		int numBlocksY = (int) (col / K);
		int sX = numBlocksX * M;
		int sY = numBlocksY * K;
	//	System.out.println("For "+ currentIndex.x + " " + currentIndex.y + " origin " + startX + " " + startY);
		for(int i = 0 ; i < M ; i++)
		{
			for(int j = 0 ; j < K ; j ++)
			{
				if(sudokuBoard[sX + i][sY + j][N+2]!=unmodifiable && (sX+i)!=row && (sY+j)!=col)
				{
					count++;

					if(sudokuBoard[sX + i][sY + j][value+1] >0)
					{
						sudokuBoard[sX + i][sY + j][1]--;
					}
					
					sudokuBoard[sX + i][sY + j][value + 1] --;
					if(sudokuBoard[sX + i][sY + j][1]<=0 && !hasBeenChecked(new pair(sX + i, sY + j), unusedPos, level)	)
						success = false;
				}
			}
		}
		
		sudokuBoard[row][col][value + 1]--;
		sudokuBoard[row][col][1]--;
		return success;
	}
	
	
	public static void replenishBoard(int value, int row, int col)
	{
		//Update one row
				for(int i = 0; i < N ; i ++)
				{
					if(sudokuBoard[row][i][N+2] !=unmodifiable && i!=col)
					{
						if(sudokuBoard[row][i][value+1] == 0)
						{
							sudokuBoard[row][i][1] ++;
						}
						
						sudokuBoard[row][i][value+1] ++;
						
					}
				}
				
				//Update column
				for(int i = 0; i < N ; i ++)
				{
					if(sudokuBoard[i][col][N+2] !=unmodifiable && i!=row)
					{
						if(sudokuBoard[i][col][value+1] == 0)
						{
							sudokuBoard[i][col][1] ++;
						}
						
						sudokuBoard[i][col][value+1] ++;
						
					}
				}
				
				//update block
				int numBlocksX = (int) (row / M);
				int numBlocksY = (int) (col / K);
				int sX = numBlocksX * M;
				int sY = numBlocksY * K;
			//	System.out.println("For "+ currentIndex.x + " " + currentIndex.y + " origin " + startX + " " + startY);
				for(int i = 0 ; i < M ; i++)
				{
					for(int j = 0 ; j < K ; j ++)
					{
						if(sudokuBoard[sX + i][sY + j][N+2]!=unmodifiable && (sX+i)!=row && (sY+j)!=col)
						{
							if(sudokuBoard[sX + i][sY + j][value+1] ==0)
							{
								sudokuBoard[sX + i][sY + j][1]++;
							}
							
							sudokuBoard[sX + i][sY + j][value + 1] ++;
							
						}
					}
				}
				
				sudokuBoard[row][col][value + 1]++;
				sudokuBoard[row][col][1]++;
		
	}
	

	//Sorts according to # of remaining values 
	static class KeyComparator implements Comparator<pair>
	 {
	     public int compare(pair c1, pair c2)
	     {
	    	 return sudokuBoard[c1.x][c1.y][1] - sudokuBoard[c2.x][c2.y][1] ;
	     }
	 }
	
	public static pair[] sortUnusedPositions(pair unusedPositions[], int level)
	{
		pair[] sortedArray = new pair[numberUnusedPositions];
		List<pair> priority = new ArrayList<pair>();
		//System.out.print("Original:\n");
		for(int i = level ; i < numberUnusedPositions ; i ++)
		{
			priority.add(unusedPositions[i]);
			//System.out.print(priority.get(i-level).x+", "+priority.get(i-level).y+" ");
		}
		Collections.sort(priority, new KeyComparator());
		for(int i = 0 ; i < level ; i ++)
			sortedArray[i] = unusedPositions[i];
		for(int i = 0 ; i < priority.size(); i++)
		{
			sortedArray[level++] = priority.get(i);
		}
		return sortedArray;
	}
	
	public static void solve(pair unusedPos[], int level)
	{
	
		if(level >=numberUnusedPositions)
		{
			System.out.println("Sudoku:");
			displayBoard();
			return;
		}
		else
		{

			pair unsolvedValue = unusedPos[level];
			int row = unsolvedValue.x;
			int col = unsolvedValue.y;
			for(int i = 1; i <= N ; i ++)
			{							
				//System.out.println("Loop ");
				if(sudokuBoard[row][col][i+1]>0)
				{
				
					sudokuBoard[row][col][0] = i;
					//displayConstraints();
					boolean possible = updateBoard(i, row, col, unusedPos, level);
					if(possible)
					{
						pair[] recArray; //= new pair[numberUnusedPositions];
						recArray = sortUnusedPositions(unusedPos, level+1);
						solve(recArray, level + 1);
					}
					replenishBoard(i, row, col);
				}
			}
		//	System.out.println();
			sudokuBoard[row][col][0] = -1;
			return;
			
		}	
	}
	
	
	
	public static void updateUsedPositions(int usedIndex)
	{
		int row = usedPositions[usedIndex].x;
		int col = usedPositions[usedIndex].y;
		boolean possible = updateBoard(sudokuBoard[row][col][0], row, col, unusedPositions, 0);
		if(!possible)
		{
			System.out.print("No Solution");
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		 	if(args.length!=1)  {
		      System.err.println("usage: sudokuBackTrack <filePath> ");
		      return;
		    }		 
			FileInputStream fstream = new FileInputStream(args[0]);
			DataInputStream in = new DataInputStream(fstream);
			String info = in.readLine();
			StringTokenizer st = new StringTokenizer(info);

			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());
			//sudokuBoard[i][j][0] contains actually assigned values
			//sudokuBoard[i][j][1] contains the number of values that can possibly be assigned
			//sudokuBoard[i][j][2...N+2] hashes to if every index-1 th number can be assigned at that pos
			sudokuBoard = new int[N][N][N+3];
			unusedPositions = new pair[N*N];
			usedPositions = new pair[N*N];
			numberUnusedPositions = 0;
			int numberUsedPositions = 0;
			
			for(int i = 0 ; i < N ; i ++)
			{
				 st = new StringTokenizer(in.readLine());
				 for(int j = 0 ; j < N ; j ++)
				 {
					 String nextToken = st.nextToken(" ,");
					 if(nextToken.compareTo("_")!=0)
					 {
						 sudokuBoard[i][j][0] = Integer.parseInt(nextToken);
						 sudokuBoard[i][j][1] = 0;
						 usedPositions[numberUsedPositions++] = new pair(i,j); 
						 for(int k = 2; k < N+2 ; k ++)
							 sudokuBoard[i][j][k] = 0;
						 sudokuBoard[i][j][N+2] = unmodifiable;
					 }
					 else
					 {
						 unusedPositions[numberUnusedPositions] = new pair(i,j);
						 numberUnusedPositions++;
						 sudokuBoard[i][j][0]=-1;
						 sudokuBoard[i][j][1] = N;
						 for(int k = 2; k < N+2 ; k ++)
							 sudokuBoard[i][j][k] = 1;
						 sudokuBoard[i][j][N+2] = modifiable;
					 }
				 }
			}
			
		//	for(int i = 0 ; i < numberUnusedPositions ; i++)
			//	System.out.println(unusedPositions[i].x + " " + unusedPositions[i].y);
			for(int i = 0 ; i < numberUsedPositions;i++ )
				updateUsedPositions(i);
			solve(unusedPositions, 0);
			System.out.println("Consistency Checks: " + count);

			//System.out.println(M + " "+  N +" " + K);
			//displayBoard();
	}
	
}
