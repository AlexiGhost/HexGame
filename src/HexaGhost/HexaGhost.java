package HexaGhost;

import java.awt.Color;
import java.util.ArrayList;

import Model.*;
/**@author Courieux Alexi
 * @author Fournier Nathan
 * @author Fuchs Alexandre**/
public class HexaGhost {
	private ArrayList<Cell> freeCells = new ArrayList<>();
	private ArrayList<Cell> playerCells = new ArrayList<>();
	private ArrayList<Cell> ennemyCells = new ArrayList<>();
	private HexModel model;
	private HGStrategy strategy;
	
	private int nbLine;
	private int nbColumn;
	Color playerColor;
	Color enemyColor;
	
	public HexaGhost(HexModel model, boolean isFirst) {
		this.model = model;
		nbLine = model.grid.getNbLines()-2;
		nbColumn = model.grid.getNbColumns()-2;
		strategy = isFirst ? new HGAttack(this) : new HGDefense(this);
		playerColor = isFirst ? Color.BLUE:Color.RED;
		enemyColor = !isFirst ? Color.BLUE:Color.RED;
	}
	
	public Cell play() {
		setCells();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strategy.play();
	}
	
	/**Return the value of a specific cell.
	 * <br>The value is define by the surrounding cells.**/
	public int cellValue(int x, int y, boolean isBlue) {
		final int ADJACENT_PLAYER = 2;
		final int ADJACENT_EMPTY = 4;
		final int ADJACENT_ENEMY = -1;
		final int BRIDGE_PLAYER = 15;
		final int DEFAULT_PLAYER = 1;
		
		int value = 0;
		
		//Player cell
		if(y-1>=0) {
			if(model.grid.getCell(x, y-1).getColor() == playerColor) value+= (y-1==0) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x, y-1).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		if(y+1<=nbColumn+1) {
			if(model.grid.getCell(x, y+1).getColor() == playerColor) value+= (y+1==nbColumn+1) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x, y+1).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		if(x+1<=nbLine+1) {
			if(model.grid.getCell(x+1, y).getColor() == playerColor) value+= (x+1==nbLine+1) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x+1, y).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		if(x+1<=nbLine+1 && y-1>=0) {
			if(model.grid.getCell(x+1, y-1).getColor() == playerColor) value+= (x+1==nbLine+1) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x+1, y-1).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		if(x-1>=0) {
			if(model.grid.getCell(x-1, y).getColor() == playerColor) value+= (x-1==0) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x-1, y).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		if(x-1>=0 && y-1>=0) {
			if(model.grid.getCell(x-1, y+1).getColor() == playerColor) value+= (x-1==0 || y-1==0) ? DEFAULT_PLAYER : ADJACENT_PLAYER;
			else if(model.grid.getCell(x-1, y+1).getColor() == enemyColor) value += ADJACENT_ENEMY;
			else value += ADJACENT_EMPTY;
		}
		
		//Player bridge
		//Bas-Droite
		if(x+1<=nbLine+1 && y+1<=nbColumn+1 && !(x+1==nbLine+1 && y+1==nbColumn+1)) {
			if(model.grid.getCell(x+1, y+1).getColor() == playerColor) {
				if((model.grid.getCell(x, y+1).getColor()==Color.WHITE) && (model.grid.getCell(x+1, y).getColor()==Color.WHITE)) value+=BRIDGE_PLAYER;			
			}
		}
		//Haut-Gauche
		if(x-1>=0 && y-1>=0 && !(x-1==0 && y-1==0)) {
			if(model.grid.getCell(x-1, y-1).getColor() == playerColor) {
				if((model.grid.getCell(x, y-1).getColor()==Color.WHITE) && (model.grid.getCell(x-1, y).getColor()==Color.WHITE)) value+=BRIDGE_PLAYER;			
			}
		}
		//Bas
		if(x+2<=nbLine+1 && y-1>=0) {
			if(model.grid.getCell(x+2, y-1).getColor() == playerColor) {
				if((model.grid.getCell(x+1, y).getColor()==Color.WHITE) && (model.grid.getCell(x+1, y-1).getColor()==Color.WHITE)) value+=BRIDGE_PLAYER;			
			}
		}
		//Haut
		if(x-2>=0 && y+1<=nbColumn+1) {
			if(model.grid.getCell(x-2, y+1).getColor() == playerColor) {
				if((model.grid.getCell(x-1, y).getColor()==Color.WHITE) && (model.grid.getCell(x-1, y+1).getColor()==Color.WHITE)) value+=BRIDGE_PLAYER;			
			}
		}
		//Bas-Gauche
		if(x+1<=nbLine+1 && y-2>=0) {
			if(model.grid.getCell(x+1, y-2).getColor() == playerColor) {
				if((model.grid.getCell(x+1, y-1).getColor()==Color.WHITE) && (model.grid.getCell(x, y-1).getColor()==Color.WHITE)) value+=BRIDGE_PLAYER;			
			}
		}
		//Haut-Droite
		if(x-1>=0 && y+2<=nbColumn+1) {
			if(model.grid.getCell(x-1, y+2).getColor() == playerColor) {
				if((model.grid.getCell(x-1, y+1).getColor()==Color.WHITE) && (model.grid.getCell(x, y+1).getColor()==Color.WHITE))value+=BRIDGE_PLAYER;
			}
		}
		return value;
	}
	
	public Cell closeBridge() {
		Cell cell = null;
		for(Cell c : playerCells) {
			int x = c.getX();
			int y = c.getY();
			//Bas-Droite
			if(x+1<=nbLine+1 && y+1<=nbColumn+1 && !(x+1==nbLine+1 && y+1==nbColumn+1)) {
				if(model.grid.getCell(x+1, y+1).getColor() == playerColor) {
					if(model.grid.getCell(x, y+1).getColor()==enemyColor && model.grid.getCell(x+1, y).getColor()==Color.WHITE) {
						return model.grid.getCell(x+1, y);
					}
					else if(model.grid.getCell(x+1, y).getColor()==enemyColor && model.grid.getCell(x, y+1).getColor()==Color.WHITE) {
						return model.grid.getCell(x, y+1);
					}
				}
			}
			//Bas
			else if(x+2<=nbLine+1 && y-1>=0) {
				if(model.grid.getCell(x+2, y-1).getColor() == playerColor) {
					if(model.grid.getCell(x+1, y).getColor()==enemyColor && model.grid.getCell(x+1, y-1).getColor()==Color.WHITE) {
						return model.grid.getCell(x+1, y-1);
					}
					else if(model.grid.getCell(x+1, y-1).getColor()==enemyColor && model.grid.getCell(x+1, y).getColor()==Color.WHITE) {
						return model.grid.getCell(x+1, y);
					}
				}
			}
			//Haut-Droite
			else if(x-1>=0 && y+2<=nbColumn+1) {
				if(model.grid.getCell(x-1, y+2).getColor() == playerColor) {
					if(model.grid.getCell(x-1, y+1).getColor()==enemyColor && model.grid.getCell(x, y+1).getColor()==Color.WHITE) {
						return model.grid.getCell(x, y+1);
					}
					else if(model.grid.getCell(x, y+1).getColor()==enemyColor && model.grid.getCell(x-1, y+1).getColor()==Color.WHITE) {
						return model.grid.getCell(x-1, y+1);
					}
				}
			}
		}
		return cell;
	}
	
//GETTERS/SETTERS
	
	//Refill all the cells lists
	public void setCells() {
		freeCells.clear();
		playerCells.clear();
		ennemyCells.clear();
		for(Cell c : model.grid) {
			if(c.getColor() == Color.WHITE) {
				freeCells.add(c);
			} else if (c.getColor() == playerColor) {
				playerCells.add(c);
			} else {
				ennemyCells.add(c);
			}
		}
	}
	
	public ArrayList<Cell> getFreeCells(){
		return freeCells;
	}
	
	public ArrayList<Cell> getPlayerCells(){
		return playerCells;
	}
	
	public ArrayList<Cell> getEnemyCells(){
		return ennemyCells;
	}
	
	public int getNbLine() {
		return nbLine;
	}
	
	public int getNbColumn() {
		return nbColumn;
	}
	
	public HexModel getModel(){
		return model;
	}
	
}
