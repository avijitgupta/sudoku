package mrv;
import java.io.DataInputStream;
import java.io.FileInputStream;
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

public class sudokuFC {
	static int M, N, K;
	static int[][][] sudokuBoard;
	static pair[] usedPositions;
	static pair[] unusedPositions;
	static int numberUnusedPositions;
	static int unmodifiable = -10;
	static int modifiable = 10;
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
	
	public static boolean updateBoard(int value, int row, int col)
	{
		boolean success = true;
		
		//Update one row
		for(int i = 0; i < N ; i ++)
		{
			if(sudokuBoard[row][i][N+2] !=unmodifiable && i!=col)
			{
				if(sudokuBoard[row][i][value+1] > 0)
				{
					sudokuBoard[row][i][1] --;
				}
				sudokuBoard[row][i][value+1] --;
				
				if(sudokuBoard[row][i][1]<=0 && i>col)
					success = false;
			}
		}
		
		//Update column
		for(int i = 0; i < N ; i ++)
		{
			if(sudokuBoard[i][col][N+2] !=unmodifiable && i!=row)
			{
				if(sudokuBoard[i][col][value+1] > 0)
				{
					sudokuBoard[i][col][1] --;
				}
				sudokuBoard[i][col][value+1] --;
				
				if(sudokuBoard[i][col][1]<=0 && i>row)
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
					if(sudokuBoard[sX + i][sY + j][value+1] >0)
					{
						sudokuBoard[sX + i][sY + j][1]--;
					}
					
					sudokuBoard[sX + i][sY + j][value + 1] --;
					if(sudokuBoard[sX + i][sY + j][1]<=0 && (sX+i)>row && (sY+j)>col)
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
	
	public static void solve(int unsolvedIndex)
	{
		if(unsolvedIndex >=numberUnusedPositions)
		{
			System.out.println("Sudoku:");
			displayBoard();
			return;
		}
		else
		{
			pair unsolvedValue = unusedPositions[unsolvedIndex];
			int row = unsolvedValue.x;
			int col = unsolvedValue.y;
			for(int i = 1; i <= N ; i ++)
			{
				if(sudokuBoard[row][col][i+1]>0)
				{
					
					//System.out.println("Disp " + sudokuBoard[row][col][i+1]);
					sudokuBoard[row][col][0] = i;
					boolean possible = updateBoard(i, row, col);
					//displayBoard();
					if(possible)solve(unsolvedIndex + 1);
					replenishBoard(i, row, col);
				}
			}
		//	System.out.println();
			sudokuBoard[row][col][0] = -1;
			return;
			
		}	
	}
	
	public static boolean isRowConsistant(int row, int value)
	{
		for(int col = 0 ; col < N ; col ++)
		{
			if(sudokuBoard[row][col][0] == value)
				return false;
		}
		return true;
	}
	
	public static boolean isColumnConsistant(int col, int value)
	{
		for(int row = 0 ; row < N ; row ++)
		{
			if(sudokuBoard[row][col][0] == value)
				return false;
		}
		return true;
	}
	
	public static boolean isBlockConsistant(pair currentIndex, int value)
	{
		int numBlocksX = (int) (currentIndex.x / M);
		int numBlocksY = (int) (currentIndex.y / K);
		int startX = numBlocksX * M;
		int startY = numBlocksY * K;
	//	System.out.println("For "+ currentIndex.x + " " + currentIndex.y + " origin " + startX + " " + startY);
		for(int i = 0 ; i < M ; i++)
		{
			for(int j = 0 ; j < K ; j ++)
			{
				if(sudokuBoard[startX + i][startY + j] [0] == value)
					return false;
			}
		}
		return true;
	}
	
	public static void updateUsedPositions(int usedIndex)
	{
		int row = usedPositions[usedIndex].x;
		int col = usedPositions[usedIndex].y;
		boolean possible = updateBoard(sudokuBoard[row][col][0], row, col);
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
			solve(0);
			//System.out.println(M + " "+  N +" " + K);
			//displayBoard();
	}
	
}
