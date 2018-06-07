package HexaGhost;

import java.util.concurrent.ThreadLocalRandom;

import Model.Cell;

public class HGDefense implements HGStrategy{
	private HexaGhost hexaghost;
	
	public HGDefense(HexaGhost hexaghost) {
		this.hexaghost = hexaghost;
	}
	
	@Override
	public Cell play() {
		Cell cell = null;
		int cValue = Integer.MIN_VALUE;
		for(Cell c : hexaghost.getFreeCells()) {
			int value;
			if((value = hexaghost.cellValue(c.getX(), c.getY(), false)) > cValue) {
				cell = c;
				cValue = value;
			}
		}
		return cell;
	}

}
