package tictactoe;

public class GameLogic
{
	public boolean checkWin(Board board, char player) {
		for(int row=0; row<3; row++) 
		{
			boolean win = true;
			for(int col=0; col<3; col++) 
			{
				if(board.getCell(row,col)!= player) 
				{
					win = false;
				}
			}
			if(win) 
			{
				return true;
			}
			
		}
		for(int col=0; col<3; col++) 
		{
			boolean win = true;
			for(int row=0; row<3; row++) 
			{
				if(board.getCell(row,col)!= player) 
				{
					win = false;
				}
			}
			if(win) 
			{
				return true;
			}
			
		}
		if(board.getCell(0,0) == player && board.getCell(1,1) == player && board.getCell(2,2) == player) 
		{
			return true;
		}
		if(board.getCell(0,2) == player && board.getCell(1,1) == player && board.getCell(2,0) == player  ) 
		{
			return true;
		}
		
		return false;
		
	}
	
	public boolean isDraw(Board board)
	{

	    if (checkWin(board, 'X') || checkWin(board, 'O'))
	    {
	        return false; 
	    }
	    for (int row = 0; row < 3; row++)
	    {
	        for (int col = 0; col < 3; col++)
	        {
	            if (board.getCell(row, col) == 'E')
	            {
	                return false; 
	            }
	        }
	    }
	    return true;
	}
	
	public boolean isGameOver(Board board) {
		
			return checkWin(board,'X') || checkWin(board,'O') || isDraw(board);
				
					
	}

	
	
	
	
}






