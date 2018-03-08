import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;

import javax.swing.*;
class CrosswordExample {

    File oldPuzzlesFile = new File("C:\\Users\\onur\\Desktop\\is guc\\CheckBoxExample\\puzzles.txt");
     Crossword getNewPuzzle(){

	Crossword c;
	String title = "Wildebeest";
	int size = 5;
	ArrayList<Clue> acrossClues = new ArrayList<Clue>();
	ArrayList<Clue> downClues = new ArrayList<Clue>();
	
        acrossClues.add( new Clue( 1, 0, 0, "Food truck order", "taco") );
        acrossClues.add( new Clue( 5, 0, 1, "Snowman in \"Frozen\"", "olaf") );
        acrossClues.add( new Clue( 6, 0, 2, "In a way, that's true", "sorta") );
        acrossClues.add( new Clue( 8, 0, 3, "Target of a proposed Trump tariff", "steel") );
        acrossClues.add( new Clue( 9, 2, 4, "Genetic material", "dna") );
        
        downClues.add( new Clue( 1, 0, 0, "Beginning of a tennis serve", "toss") );
        downClues.add( new Clue( 2, 1, 0, "Tons", "alot") );
        downClues.add( new Clue( 3, 2, 0, "Gave a hoot", "cared") );
        downClues.add( new Clue( 4, 3, 0, "Much of a time", "often") );
        downClues.add( new Clue( 7, 4, 2, "Dave Brubeck's \"Blue Rondo ___Turk\"", "ala") );
        //downClues.add( new Clue( 7, 0, 1, "Boast", "brag") );
        //downClues.add( new Clue( 11, 3, 4, "Enemy", "foe") );
        //downClues.add( new Clue( 13, 7, 4, "Doze", "nap") );
        //downClues.add( new Clue( 14, 0, 6, "Water vapour", "steam") );
        //downClues.add( new Clue( 15, 2, 6, "Consumed", "eaten") );
        //downClues.add( new Clue( 16, 4, 6, "Loud, resonant sound", "clang") );
        //downClues.add( new Clue( 18, 8, 6, "Yellowish, citrus fruit", "lemon") );
        //downClues.add( new Clue( 19, 10, 6 , "Mongrel dog", "mutt") );
        //downClues.add( new Clue( 20, 6, 7, "Shut with force", "slam") );

	c = new Crossword(title,size,acrossClues,downClues);

	return c;
    }
     Crossword getOldPuzzle(int index) throws FileNotFoundException{
         
        Scanner scanPuzzles = new Scanner(oldPuzzlesFile);
        for(int i = 1; i < index; i++)
            scanPuzzles.nextLine();
        String[] puzzle = scanPuzzles.nextLine().split("}");
        String[] blackTiles = puzzle[0].substring(3).split(",");
        int[] blackInts = new int[blackTiles.length]; 
        for (int i = 0; i < blackInts.length; i++) {
            blackInts[i] = Integer.parseInt(blackTiles[i]);
            System.out.print (blackInts[i] + ", ");
        }
        String[] cluesAndAnswers = puzzle[1].split(",,");
        String answersAccross = cluesAndAnswers[cluesAndAnswers.length-1];
        for(int i = 0; i < blackInts.length; i++)
            answersAccross = answersAccross.substring(0,blackInts[i]) + ' ' + answersAccross.substring(blackInts[i]);
        String answersDown = "";
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                answersDown+=answersAccross.charAt(j*5+i);
        
        System.out.println(answersAccross + "\n" + answersDown);
        ArrayList<Point> accrossStarts = new ArrayList<>();
        ArrayList<Point> downStarts = new ArrayList<>();
        
        for(int i = 0; i < 5; i++)
            for(int j = 0, lastNull = -1; j < 5; j++){
                if(answersAccross.charAt(i*5+j)==' '||j==4){
                    if(j-lastNull>2)
                        accrossStarts.add(new Point(lastNull+1,i));
                    lastNull = j;
                }
            }
        
        for(int i = 0; i < 5; i++)
            for(int j = 0, lastNull = -1; j < 5; j++){
                if(answersDown.charAt(i*5+j)==' '||j==4){
                    if(j-lastNull>2)
                        downStarts.add(new Point(i, lastNull+1));
                    lastNull = j;
                }
            }
        
	ArrayList<Clue> acrossClues = new ArrayList<>();
	ArrayList<Clue> downClues = new ArrayList<>();
        
        for(int i = 0; i < accrossStarts.size(); i++)
        //if there are no black tiles following the answer
        {
            int startX = accrossStarts.get(i).x;
            int startY = accrossStarts.get(i).y;
            int startIndex = startY*5+startX;
            int length;
            
            if((answersAccross.substring(startIndex).indexOf(' ')+startIndex)/5 != startY
                    || answersAccross.substring(startIndex).indexOf(' ')==-1)
                length = 5-startX;
            else
                length = answersAccross.substring(startIndex).indexOf(' ');
            acrossClues.add( new Clue( i, startX, startY, cluesAndAnswers[i], 
                    answersAccross.substring(startIndex, startIndex+length)));
            System.out.println(startX +" "+ startY+" "+  length +" "+ cluesAndAnswers[i]
            + " "+  answersAccross.substring(startIndex, startIndex+length));
        }
        
        Collections.sort(downStarts, new CustomComparator()); 
        for(int i = 0; i < downStarts.size(); i++)
        //if there are no black tiles following the answer
        {
            int startX = downStarts.get(i).x;
            int startY = downStarts.get(i).y;
            int startIndex = startY+5*startX;
            int length;
            if((answersDown.substring(startIndex).indexOf(' ')+startIndex)/5 != startX
                    || answersDown.substring(startIndex).indexOf(' ')==-1)
                length = 5-startY;
            else
                length = answersDown.substring(startIndex).indexOf(' ');
            downClues.add( new Clue( i+accrossStarts.size(), startX, startY, cluesAndAnswers[i+accrossStarts.size()], 
                    answersDown.substring(startIndex, startIndex+length)));
            System.out.println(startX +" "+ startY+" "+  length +" "+ cluesAndAnswers[i+accrossStarts.size()]
            + " "+ answersDown.substring(startIndex, startIndex+length));
        }
               
	Crossword c;
	String title = "Wildebeest";
	int size = 5;
	
        
        /*acrossClues.add( new Clue( 5, 0, 1, "Snowman in \"Frozen\"", "olaf") );
        acrossClues.add( new Clue( 6, 0, 2, "In a way, that's true", "sorta") );
        acrossClues.add( new Clue( 8, 0, 3, "Target of a proposed Trump tariff", "steel") );
        acrossClues.add( new Clue( 9, 2, 4, "Genetic material", "dna") );
        
        downClues.add( new Clue( 1, 0, 0, "Beginning of a tennis serve", "toss") );
        downClues.add( new Clue( 2, 1, 0, "Tons", "alot") );
        downClues.add( new Clue( 3, 2, 0, "Gave a hoot", "cared") );
        downClues.add( new Clue( 4, 3, 0, "Much of a time", "often") );
        downClues.add( new Clue( 7, 4, 2, "Dave Brubeck's \"Blue Rondo ___Turk\"", "ala") );*/
	c = new Crossword(title,size,acrossClues,downClues);
	return c;
    } 
     
     
    /* public static void main(String[] args)
     {
    	 CrosswordExample a = new CrosswordExample();
    	 JFrame test = new JFrame();
    	 a.getPuzzle();
    	 test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 test.setSize(300, 300);
    	 Container p = test.getContentPane();
    	 GridLayout grid = new GridLayout(11, 11, 2, 2);
    	 p.setLayout(grid);
    	 JPanel cell = new JPanel();
    	 cell.setLayout(new BorderLayout());
    	 cell.add(new JLabel("1"), BorderLayout.NORTH);
    	 cell.add(new JLabel("A"), BorderLayout.CENTER);
    	 for(int i = 0; i < 11; i++)
    		 for(int j = 0; j < 11; j++)
    			 p.add(new CellPanel());
    	 test.setVisible(true);
     }*/

    Crossword getPuzzle(int lastSelectedPuzzleIndex) throws FileNotFoundException {
        if(lastSelectedPuzzleIndex==0)
            return  getNewPuzzle();
        else return getOldPuzzle(lastSelectedPuzzleIndex);
    }

}

class CustomComparator implements Comparator<Point> {

    @Override
    public int compare(Point o1, Point o2) {
        return o1.y-o2.y;
    }
}