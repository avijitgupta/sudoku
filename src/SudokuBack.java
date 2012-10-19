import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Stack;


public class SudokuBack {

  class Data  {
    int value;
    Stack<Integer> currentDomain;
    Data()  {
      currentDomain = new Stack<Integer>();
      for(int i=1; i<= N; i++) {
        currentDomain.push(i);
      }
    }
  }
  private int N;
  int Nrow;
  int Ncol;

  private Data board[][];
  private final int BLANK = 0;

  SudokuBack(String str)  {
    String []temp = str.split(",");

    N = Integer.parseInt(temp[0]);
    Ncol = Integer.parseInt(temp[1]);
    Nrow = Integer.parseInt(temp[2]);
    board = new Data[N][N];

    int count = 3;

    for(int i=0; i<N; i++)  {
      for(int j=0; j<N; j++)  {
        Data tempdata = new Data();
        if(temp[count].equals(" ")) 
          tempdata.value = BLANK;
        else  
          tempdata.value = Integer.parseInt(temp[count]);

        if(tempdata.value> N)  
          throw new RuntimeException("Invalid board data");
        board[i][j] = tempdata;
        count++;
      }
    }
    displayBoard();
  }

  void displayBoard() {
    for(int i=0; i<N; i++)  {
      for(int j=0; j<N; j++)  
        System.out.print(board[i][j].value+"--");
      System.out.println();
    }
  }

  boolean checkBoxConstraint(int iRow, int iCol)  {
    int start_i = iRow*Ncol;
    int start_j  = iCol*Nrow;
    boolean flag[] = new boolean[N+1];
    for(int i=0; i<= N; i++)
      flag[i] = false;

    for(int i=start_i; i<Ncol+start_i; i++) {
      for(int j=start_j; j<Nrow+start_j; j++) {
        if(flag[board[i][j].value] && board[i][j].value!=BLANK)   
          return false;
        else
          flag[board[i][j].value] = true;
      }
    }
    return true;
  }

  boolean checkConstraint()  {
    // check for each box
    for(int i=0; i<Nrow; i++) { 
      for(int j=0; j<Ncol; j++) {
        boolean ret = checkBoxConstraint(i, j);
        if(ret == false)
          return false;
      }
    }

    boolean flag[] = new boolean[N+1];
    // check for row
    for(int j=0; j<N; j++)  {
      for(int i=0; i<=N; i++)
        flag[i] = false;

      for(int i=0; i<N; i++)  {
        if(flag[board[i][j].value] && board[i][j].value!=BLANK) {
          return false;
        }
        else  
          flag[board[i][j].value] = true;
      }
    }
    // check for column
    for(int i=0; i<N; i++)  {
      for(int j=0; j<= N; j++)
        flag[j] = false;

      for(int j=0; j<N; j++)  {
        if(flag[board[i][j].value] && board[i][j].value!=BLANK) {
          return false;
        }
        else  
          flag[board[i][j].value] = true;
      }
    }
    return true;
  }

  boolean isGameOver()  {
    for(int i=0; i<N; i++)  {
      for(int j=0; j< N; j++)
        if(board[i][j].value == BLANK)
          return false;
    }
    
    return true;
  }

  boolean DFS()  {
    if(isGameOver())
      return true;
    
    for(int i=0; i<N; i++)  {
      for(int j=0; j<N; j++)  {
        if(board[i][j].value == BLANK )  {
          
          for(int index=0; index< board[i][j].currentDomain.size(); index++) {
            board[i][j].value = board[i][j].currentDomain.get(index);
            boolean ret = checkConstraint();
            if(ret == false)  {
              System.out.println("doesn't satisfy"+i+"--"+j);
              board[i][j].value = BLANK;
            }
            else  {
              displayBoard();
              System.out.println("dfs");
              boolean retDFS = DFS();
              if(retDFS == true) 
                return true;
              board[i][j].currentDomain.remove(index);          
            }
          }
        }
      }
    }
    System.out.println("djwkjd");
    return false;
  }

  static String readFile(String path)  {
    String inputdata ="";
    try {
      FileInputStream fstream = new 
          FileInputStream(path);
      DataInputStream in = 
          new DataInputStream(fstream);
      inputdata = in.readLine();
      while (in.available() !=0)  {
        inputdata= inputdata +"," +in.readLine();
      }
      in.close();
    } 
    catch (Exception e) {
      System.err.println("File input error");
    }
    return inputdata;
  }

  public static void main(String a[]) {
    String str = SudokuBack.readFile("/home/solstafir/ass2.txt");
    System.out.println(str);
    SudokuBack o = new SudokuBack(str);
    //System.out.println(o.checkConstraint());
    o.DFS();
    System.out.println(o.isGameOver());
    o.displayBoard();
  }
}
