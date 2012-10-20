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
	static pair[] unusedPositions;
	static int numberUnusedPositions;
	static int unmodifiable = -10;
	public static void displayBoard()
	{
		for(int i = 0 ; i < N ; i ++)
		{
			for(int  j =0 ; j < N ; j ++)
				System.out.print(sudokuBoard[i][j] + "\t");
			System.out.println();
		}
		System.out.println();
	}
	
	public static boolean updateBoard(int value, int row, int col)
	{
		boolean updateFailed = false;
		//Row
		for(int i = 0 ; i < N ; i ++)
		{
			if(sudokuBoard[row][i][1]!=unmodifiable && sudokuBoard[row][i][value + 1] !=-1)
			{
				sudokuBoard[row][i][value + 1] = -1;
				sudokuBoard[row][i][1]--;
				if(sudokuBoard[row][i][1] ==0 && i!=col)
					updateFailed = true;
			}
		}
		//Column
		for(int i = 0 ; i < N ; i ++)
		{
			if(sudokuBoard[i][col][1] !=unmodifiable && sudokuBoard[i][col][value + 1] !=-1)
			{
				sudokuBoard[i][col][value + 1] = -1;
				sudokuBoard[i][col][1]--;
				if(sudokuBoard[i][col][1] ==0 && i!=row)
					updateFailed = true;
			}
		}
		//Block
		int numBlocksX = (int) (row / M);
		int numBlocksY = (int) (col / K);
		int startX = numBlocksX * M;
		int startY = numBlocksY * K;
	
		for(int i = 0 ; i < M ; i++)
		{
			for(int j = 0 ; j < K ; j ++)
			{
				if(sudokuBoard[startX + i][startY + j][1] !=unmodifiable && 
							sudokuBoard[startX + i][startY + j][value + 1] !=-1)
				{
					sudokuBoard[startX + i][startY + j][value + 1] = -1;
					sudokuBoard[startX + i][startY + j][1]--;
					if(sudokuBoard[startX + i][startY + j][1] == 0 && (startX + i)!=row && (startY + j)!=col)
						updateFailed = true;
				}
			}
		}
		
		
		
		return !updateFailed;
	}
	
	
	public static void replenishBoard(int value, int row, int col)
	{
		//Row
		for(int i = 0 ; i < N ; i ++)
		{
			if(sudokuBoard[row][i][1]!=unmodifiable && sudokuBoard[row][i][value + 1] ==-1)
			{
				sudokuBoard[row][i][value + 1] = 1;
				sudokuBoard[row][i][1]++;
			}
		}
		//Column
		for(int i = 0 ; i < N ; i ++)
		{
			if(sudokuBoard[i][col][1] !=unmodifiable && sudokuBoard[i][col][value + 1] ==-1)
			{
				sudokuBoard[i][col][value + 1] = 1;
				sudokuBoard[i][col][1]++;
			}
		}
		//Block
		int numBlocksX = (int) (row / M);
		int numBlocksY = (int) (col / K);
		int startX = numBlocksX * M;
		int startY = numBlocksY * K;
	
		for(int i = 0 ; i < M ; i++)
		{
			for(int j = 0 ; j < K ; j ++)
			{
				if(sudokuBoard[startX + i][startY + j][1] !=unmodifiable && 
							sudokuBoard[startX + i][startY + j][value + 1] ==-1)
				{
					sudokuBoard[startX + i][startY + j][value + 1] = 1;
					sudokuBoard[startX + i][startY + j][1]++;
				}
			}
		}
		
	}
	
	public static void solve(int unsolvedIndex)
	{
		if(unsolvedIndex >=numberUnusedPositions)
		{
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
				if(sudokuBoard[row][col][i+1]!=-1)
				{
					sudokuBoard[row][col][0] = i;
					boolean possible = updateBoard(i, row, col);
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
			sudokuBoard = new int[N][N][N+2];
			unusedPositions = new pair[N*N];
			numberUnusedPositions = 0;
			
			for(int i = 0 ; i < N ; i ++)
			{
				 st = new StringTokenizer(in.readLine());
				 for(int j = 0 ; j < N ; j ++)
				 {
					 String nextToken = st.nextToken(" ,");
					 if(nextToken.compareTo("_")!=0)
					 {
						 sudokuBoard[i][j][0] = Integer.parseInt(nextToken);
						 sudokuBoard[i][j][1] = unmodifiable;
						 for(int k = 2; k < N+2 ; k ++)
							 sudokuBoard[i][j][k] = -1;
					 }
					 else
					 {
						 unusedPositions[numberUnusedPositions] = new pair(i,j);
						 numberUnusedPositions++;
						 sudokuBoard[i][j][0]=-1;
						 sudokuBoard[i][j][1] = N;
						 for(int k = 2; k < N+2 ; k ++)
							 sudokuBoard[i][j][k] = 1;
					 }
				 }
			}
			
		//	for(int i = 0 ; i < numberUnusedPositions ; i++)
			//	System.out.println(unusedPositions[i].x + " " + unusedPositions[i].y);
			solve(0);
			//System.out.println(M + " "+  N +" " + K);
			//displayBoard();
	}
	
}
