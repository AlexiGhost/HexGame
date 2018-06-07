package HexaGhost;

import java.util.Random;

import Model.Cell;

public class HGAttack implements HGStrategy{
	private HexaGhost hexaghost;
	
	public HGAttack(HexaGhost hexaghost) {
		this.hexaghost = hexaghost;
	}
	
	@Override
	public Cell play() {
		Cell cell = null;
		Random random = new Random();
		//1st hit
		if(hexaghost.getFreeCells().size() == hexaghost.getNbLine()*hexaghost.getNbColumn()) {
			//Choisir une ligne impaire
			int line = 1+random.nextInt(hexaghost.getNbLine());
			if(line%2!=0) {
				line=(line!=hexaghost.getNbLine())?line+1:line-1;
			}
			//Définir la colonne selon la ligne (doit être sur la diagonale)
			int column = hexaghost.getNbLine()-line+1;
			return hexaghost.getModel().grid.getCell(line, column);
		}
		//TODO close bridge if 1 empty space left
		if((cell=hexaghost.closeBridge()) != null) return cell;
		System.out.println("\n\n\nno bridge to close");
		//TODO Check if there is a wining chain
		//Cell with highest value
		int cValue = Integer.MIN_VALUE;
		for(Cell c : hexaghost.getFreeCells()) {
			int value;
			if((value = hexaghost.cellValue(c.getX(), c.getY(), true)) > cValue) {
				cell = c;
				cValue = value;
			}
		}
		return cell;
	}
}