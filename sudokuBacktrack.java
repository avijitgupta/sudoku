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

public class sudokuBacktrack {
	static int M, N, K;
	static int[][] sudokuBoard;
	static pair[] unusedPositions;
	static int numberUnusedPositions;
	static long count = 0 ;
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
			//System.out.println(row + " " + col);
			//possible values
			for(int i = 1; i <= N ; i ++)
			{
			//	System.out.print(i+ " " +isRowConsistant(row, i) + " " +isColumnConsistant(col, i) + " " + isBlockConsistant(unsolvedValue, i) + " | ");
				if(isRowConsistant(row, i) && isColumnConsistant(col, i) && isBlockConsistant(unsolvedValue, i))
				{
				//	System.out.println("C " +row + " " + col);
					sudokuBoard[row][col] = i;
					solve(unsolvedIndex + 1);
				}
			}
		//	System.out.println();
			sudokuBoard[row][col] = -9;
			return;
			
		}	
	}
	
	public static boolean isRowConsistant(int row, int value)
	{
		for(int col = 0 ; col < N ; col ++)
		{
			count++;
			if(sudokuBoard[row][col] == value)
				return false;
		}
		return true;
	}
	
	public static boolean isColumnConsistant(int col, int value)
	{
		for(int row = 0 ; row < N ; row ++)
		{
			count++;
			if(sudokuBoard[row][col] == value)
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
				count++;
				if(sudokuBoard[startX + i][startY + j] == value)
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
			sudokuBoard = new int[N][N];
			unusedPositions = new pair[N*N];
			numberUnusedPositions = 0;
			
			for(int i = 0 ; i < N ; i ++)
			{
				 st = new StringTokenizer(in.readLine());
				 for(int j = 0 ; j < N ; j ++)
				 {
					 String nextToken = st.nextToken(" ,");
					 if(nextToken.compareTo("_")!=0)
						 sudokuBoard[i][j] = Integer.parseInt(nextToken);
					 else
					 {
						 unusedPositions[numberUnusedPositions] = new pair(i,j);
						 numberUnusedPositions++;
						 sudokuBoard[i][j]=-1;
					 }
				 }
			}
			
		//	for(int i = 0 ; i < numberUnusedPositions ; i++)
			//	System.out.println(unusedPositions[i].x + " " + unusedPositions[i].y);
			solve(0);
			System.out.println("Consistency Checks: " + count);
			//System.out.println(M + " "+  N +" " + K);
			//displayBoard();
	}
	
}
