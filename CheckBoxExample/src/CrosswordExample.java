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

    File oldPuzzlesFile = new File("./puzzles.txt");
     Crossword getNewPuzzle() throws FileNotFoundException {
	
        
        File newPuzzleFile = new File("./current_puzzle.txt");
        int index = 0;
        Scanner scanPuzzles = new Scanner(newPuzzleFile);
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
        ArrayList<CluePos> allClues = new ArrayList<>();
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
            
            allClues.add(new CluePos(acrossClues.get(acrossClues.size()-1)));
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
            allClues.add(new CluePos(downClues.get(downClues.size()-1)));
            System.out.println(startX +" "+ startY+" "+  length +" "+ cluesAndAnswers[i+accrossStarts.size()]
            + " "+ answersDown.substring(startIndex, startIndex+length));
        }
        Collections.sort(allClues, new clueComparator());   
        for(CluePos c: allClues){
            for(int i = 0, duplicates = 0, preVal = -1; i < allClues.size(); i++){
                
                if(c.val==allClues.get(i).val){
                    c.clue.number = i-duplicates+1;
                    break;
                }
                if(preVal==allClues.get(i).val)
                    duplicates++;
                preVal = allClues.get(i).val;
            }
        }
	Crossword c;
	String title = "Wildebeest";
	int size = 5;
	
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
        ArrayList<CluePos> allClues = new ArrayList<>();
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
            
            allClues.add(new CluePos(acrossClues.get(acrossClues.size()-1)));
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
            allClues.add(new CluePos(downClues.get(downClues.size()-1)));
            System.out.println(startX +" "+ startY+" "+  length +" "+ cluesAndAnswers[i+accrossStarts.size()]
            + " "+ answersDown.substring(startIndex, startIndex+length));
        }
        Collections.sort(allClues, new clueComparator());   
        for(CluePos c: allClues){
            for(int i = 0, duplicates = 0, preVal = -1; i < allClues.size(); i++){
                
                if(c.val==allClues.get(i).val){
                    c.clue.number = i-duplicates+1;
                    break;
                }
                if(preVal==allClues.get(i).val)
                    duplicates++;
                preVal = allClues.get(i).val;
            }
        }
	Crossword c;
	String title = "Wildebeest";
	int size = 5;
	
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

class clueComparator implements Comparator<CluePos> {

    @Override
    public int compare(CluePos o1, CluePos o2) {
        return o1.val-o2.val;
    }
}

class CluePos {
    int val;
    Clue clue;
    public CluePos(Clue cl){
        clue = cl;
        val = clue.y*5+clue.x;
    }
}