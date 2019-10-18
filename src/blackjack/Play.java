package blackjack;

import java.util.Random;
import java.util.Scanner;

public class Play {
	public static Scanner input = new Scanner(System.in);
	public static Random rand = new Random();
	/**
	 * Cett fonction me cree un deck de 52 cartes
	 * @return int[] Mon deck;
	 */
	public static int[] createDeck() {
		int[] deck = new int[52]; // Je declare mon tableau
		int[] color = {1,2,3,4,5,6,7,8,9,10,10,10,10}; // Je declare mes valeurs par famille

		int index = 0;
		for(int i =0; i< 4;i++) { // Je boucle sur chaque couleur(coeur/carreau/pic/trefle)
			for(int c :color) { // je boucle sur chaque valeure de ma famille
				deck[index] = c;
				index++;
			}
		}
		return deck; // je retroune mon deck remplie
	}
	
	
	
	/**
	 * cette fonction me permet de piocher uen carte aléatoirement
	 * @param deck c'est un tableau qui repertorie mes cartes 
	 * @return je retourne la valurs de la carte
	 */
	public static int getCardFromDeck(int[] deck) {
		int tmp =0;
		int a = 0;
		do {
			a = rand.nextInt(deck.length); // je genere un nombre aléatoire entre 0 et la taille de mon deck 
		}while(deck[a]== 0); // je regarde si la valeur de ma carte est 0 si c'est le cas je reessaye en bouclan 
		tmp = deck[a]; // je sauvegarde la valeur de ma carte pioché 
		deck[a] = 0; // je remplace la valeur dans le deck par un 0 (signifiant deja pioché)
		return tmp; // je retourne la valeur de ma carte 
	}
	/**
	 * setNextCard permet de rajouter une carte a la main
	 * @param playerScore est un tab qui represente la main de mon joueur 
	 * @param card est la valeur a ajouter a ma main
	 */
	public static void setNextCard(int[] playerScore, int card) {
		for(int i=0;i<playerScore.length; i++) {// je parcours chacune des valeurs de ma main
			if(playerScore[i]==0) {// je compare ma valeur à 0 pour savoir ou la placer dans mon tableau(playerScore)
				playerScore[i]= card;// la case est vide donc je met la carte dans la case
				break;
			}
		}
	}
	/**
	 * Cette fonction retourne la chaine de caractere avec les valeur valides possibles (ex: maxMin> 21 ->nn valide)
	 * @param maxMin Le tableau des score minimum et maximum d'un joueur
	 * @return 
	 */
	public static String displayEndScore(int[] maxMin) {
		if (maxMin[0] == maxMin[1]) {
			return ""+maxMin[0];
		}else {
			if(maxMin[1]>21)
				return ""+maxMin[0];
			else
				return maxMin[0]+ " ou "+ maxMin[1];
		}
	}
	/**
	 * Cette nous permet 
	 * @param playerScore
	 * @param deck
	 * @param dealerScore 
	 */
	public static void playerTurn(int[] playerScore, int[] deck, int[] dealerScore) {
		boolean pioche;
		int tmp =0;
		int[] maxMinDealer = getScore(true, dealerScore);
		do {
			System.out.println("Le score actuel du dealer est " + displayEndScore(maxMinDealer) );

			System.out.println("Veuiller choisir de piocher une carte: 1 = OUI 2 = NON");
			do {
				tmp = input.nextInt();
			}while(!(tmp == 1 || tmp == 2));
			pioche = tmp == 1;
			//Recupere le choix du joueur et le compare a 1 pour savoir si piocher ou non
			if (pioche) 
				setNextCard(playerScore, getCardFromDeck(deck));
			int[] maxMin = getScore(false, playerScore);
			System.out.println("Votre score actuel est de " + displayEndScore(maxMin) );//Piocher
		}while(pioche && playerScore[playerScore.length - 1] == 0 );
	}
	
	public static int[] getScore(boolean dealer,int[] tabScore) {
		int[] minMaxScore= new int[2];
		
		for(int i=0; i< tabScore.length;i++) {
			if(!dealer) {
				if(tabScore[i]== 1) {
					minMaxScore[0] += tabScore[i];
					minMaxScore[1] += 11;
				}
				else {
					minMaxScore[0] += tabScore[i];
					minMaxScore[1] += tabScore[i];
				}	
			}
			else {
				if (i !=0) {
					if(tabScore[i]== 1) {
						minMaxScore[0] += tabScore[i];
						minMaxScore[1] += 11;
					}
					else {
						minMaxScore[0] += tabScore[i];
						minMaxScore[1] += tabScore[i];
					}	
				}
			}
		}
		return minMaxScore;
	}
	
	
	public static void dealerTurn(int[] dealerScore, int[] deck) {
		int[] minMaxScore = getScore(false, dealerScore);
		if(minMaxScore[1] <21 ) {
			if(minMaxScore[1] >= 17) {
				return;
			}else {
				int tmpcard = getCardFromDeck(deck);
				System.out.println("Le dealer a pioché la carte "+tmpcard);
				setNextCard(dealerScore, tmpcard);
				if (minMaxScore[1]+ tmpcard <17) {
					dealerTurn(dealerScore, deck);
				}
			}
		}
		else if(minMaxScore[1] == 21 ){
			return;
		}else {
			if(minMaxScore[0] < 21 ) {
				if(minMaxScore[0] >= 17) {
					return;
				}else {
					int tmpcard = getCardFromDeck(deck);
					System.out.println("Le dealer a pioché la carte "+tmpcard);
					setNextCard(dealerScore, tmpcard);
					if (minMaxScore[0]+ tmpcard <17) {
						dealerTurn(dealerScore, deck);
					}
				}
			}
			else if(minMaxScore[0] == 21 ){
				return;
			}else {
				return;
				
			}
		}
	}
	
	public static void firstTurn(int[] playerScore, int[] dealerScore, int[] deck) {
		setNextCard(playerScore, getCardFromDeck(deck));
		setNextCard(playerScore, getCardFromDeck(deck));
		setNextCard(dealerScore, getCardFromDeck(deck));
		setNextCard(dealerScore, getCardFromDeck(deck));
	}
	
	public static void main(String[] args) {
		int[] playerScore = new int[5];
		int[] dealerScore = new int[5];
		int[] deck = createDeck();
		
		firstTurn(playerScore, dealerScore, deck);
		int[] maxMinPlayer = getScore(false, playerScore);

		System.out.println("Votre score actuel est de " +  displayEndScore(maxMinPlayer));//Piocher
		playerTurn(playerScore, deck, dealerScore);

		int[] maxMinDealer = getScore(false, dealerScore);
		System.out.println("Le score actuel du dealer est " +  displayEndScore(maxMinDealer) +" depuis qu'il a retourné sa carte");
		
		
		dealerTurn(dealerScore, deck);
		maxMinDealer = getScore(false, dealerScore);
		System.out.println("Le score actuel du dealer est " + displayEndScore(maxMinDealer) +" en finnissant sont tour.");

		maxMinPlayer = getScore(false, playerScore);
		maxMinDealer = getScore(false,dealerScore);
		int playerMax = 0;
		int dealerMax = 0;
		if(maxMinPlayer[1]>21) {
			if(maxMinPlayer[0]>21) {
				System.out.println("Le joueur a Perdu ! Son Score est de "+ maxMinPlayer[0]);
			}else {
				playerMax = maxMinPlayer[0];
			}
		}else {
			playerMax = maxMinPlayer[1];
		}
		
		
		
		if(maxMinDealer[1]>21) {
			if(maxMinDealer[0]>21) {
				System.out.println("Le dealer a Perdu ! Son Score est de "+ maxMinDealer[0]);
				return;
			}else {
				dealerMax = maxMinDealer[0];
			}
		}else {
			dealerMax = maxMinDealer[1];
		}
		
		if (playerMax>dealerMax) {
			System.out.println("Le joueur a gagné ! Son Score est de "+ playerMax);
		}else if(dealerMax>playerMax) {
			System.out.println("Le joueur a Perdu ! Son Score est de "+ playerMax);
		}else {
			System.out.println("Il y a egalité mais la banque gagne");
		}
		System.out.print("Joueur:");
		for(int i=0;i< playerScore.length;i++) {
			System.out.print(playerScore[i]+" ");
		}
		System.out.println();
		System.out.print("Dealer:");
		for(int i=0;i< playerScore.length;i++) {
			System.out.print(dealerScore[i]+" ");
		}
	}
}
		
		//Joueur: comparer les score max&&min >21 -> Perdu || max|min < 21 Continu
		//Joueur: comparer les score max&&min >21 -> Perdu || max|min < 21 Continu

		// Compare les score 
		
