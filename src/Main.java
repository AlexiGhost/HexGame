import java.awt.Color;

import Controller.HexController;
import HexaGhost.HexaGhost;
import Model.HexModel;
import View.HexView;
import Model.Cell;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class Main {

	public Main(){
		// Poue les valeur de playerblue et playerred :0 = Joueur humain au clic de souris et 1 = joueur aléatoire
		// Le joueur bleu commence
		int playerblue = 2;
		int playerred = 0;
		int size = 9;
		//Creation du model
		HexModel model = new HexModel(size);
		//Creation de la vue
		HexView view = new HexView(model,"Jeu de Hex");
		//Creation du controller
		HexController controller = new HexController(model,view,playerblue,playerred);
		//Creation de l'IA
		HexaGhost hexaghostRed = null;
		HexaGhost hexaghostBlue = null;
		
		if(playerblue == 2) {
			hexaghostBlue = new HexaGhost(model, true);			
		}
		if(playerred == 2) {
			hexaghostRed = new HexaGhost(model, false);
		}
		
		while(!model.getFinished()){
			System.out.print("");
			if(!model.getVictory()){
				if(playerblue>=1 && model.getPlayer() == Color.BLUE && model.getCurrentGame()){
					Cell c;
					switch(playerblue) {
					case 1:
						c = controller.randomPlay();
						break;
					case 2:
						c  = hexaghostBlue.play();
						break;
					default:
						c = controller.randomPlay();
						break;
					}
					System.out.println("Bleu IA : Cell("+c.getX()+" "+c.getY()+")");
					c.setColor(Color.BLUE);
					model.setPlayer(Color.RED);
					model.researchVictory(0,1);
				}
				else if(playerred>=1 && model.getPlayer() == Color.RED && model.getCurrentGame()){
					Cell c;
					switch(playerred) {
					case 1:
						c = controller.randomPlay();
						break;
					case 2:
						c  = hexaghostRed.play();
						break;
					default:
						c = controller.randomPlay();
						break;
					}
					System.out.println("Rouge IA : Cell("+c.getX()+" "+c.getY()+")");
					c.setColor(Color.RED);
					model.setPlayer(Color.BLUE);
					model.researchVictory(1,0);
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
