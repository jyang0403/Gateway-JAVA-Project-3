import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

/**
 * Project 3: Gateway Computing: JAVA
 * -------------------------------------------------------------------
 * The program asks the user to select a file of movie reviews, and then
 * asks the user for certain menu options to find a word's average review score
 * or a full review, where multiple words' scores are combined and it is 
 * determined if the score is overall thumbs up, or thumbs down. The limits
 * for the determination of whether reviews are overall thumbs up/down, delta, 
 * can also be altered. There is also an option to exit the program 
 * immediately. 
 * 
 * @author: Jiashu Yang
 * @JHED: jyang166
 * @date: 02/28/2022
 *
 */

public class Proj3 {
    
   /**
    * Prints the menu for the various functions possible.
    *
    * @param option1 a String displaying the first option (quit)
    * @param option2 a String displaying the second option (avg score)
    * @param option3 a String displaying the third option (avg scores/thumbs)
    * @param option4 a String displaying the fourth option (change delta)
    */ 
   public static void printMenu(String option1, String option2, 
                                 String option3, String option4) {
      System.out.println();
      System.out.println(option1);
      System.out.println(option2);
      System.out.println(option3);
      System.out.println(option4);
      return;
   }
   
   /**
    * Determines if a file exists in the current directory.
    *
    * @param fileName a string input determining the file's name
    * @return true if the file exists
    */
   public static boolean findFile(String fileName) {
      File file = new File(fileName);
      if (file.exists()) {
         return true;
      }
      return false;
   }
   
   /** 
    * Determines if the word exists in each of the file's lines.
    *
    * @param wordLower a lowercase String of the user's word
    * @param lineSearchLower a lowercase String of the current line of the file
    * @return true if the word exists in the line, false if not
    */
   public static boolean isSubstring(String wordLower, 
                                     String lineSearchLower) {
      if (lineSearchLower.indexOf(wordLower) > -1) {
         return true;
      }
      return false;
   }
   
   /**
    * Determines the average score correlated to a user's word and prints
    * the average score with the number of reviews. Also appends the reviews
    * containing the word into a new text file, "reviews2.txt".
    *
    * @param reviews a Scanner file containing all reviews
    * @param word the user's inputted word
    * @param wordLower the user's inputted word in lowercase
    */
   public static void findWord(Scanner reviews, String word, 
                                 String wordLower) throws Exception {
      double avgScore = 0;
      double totalScore = 0;
      int numReviews = 0;
      int reviewScore = 0;
      String fullLine;
      
      File file = new File(wordLower + ".txt");
      PrintWriter pw = new PrintWriter(file);
      while (reviews.hasNext()) {
         reviewScore = reviews.nextInt();
         String lineSearch = reviews.nextLine();
         String lineSearchLower = lineSearch.toLowerCase();
         if (isSubstring(wordLower, lineSearchLower)) {
            totalScore += reviewScore;
            numReviews += 1;
            pw.print(reviewScore + " " + lineSearch);
            pw.println();
         }
      }
      pw.flush();
      pw.close();
      System.out.println(word + " appears in " + numReviews + " reviews");
      if (numReviews > 0) {
         avgScore = (double) totalScore / numReviews;
         System.out.printf("average score for those reviews is %.5f\n", 
                              avgScore);
      }
      reviews = new Scanner(file);
      return;
   }
   
   /**
    * Computes the average review score associated with multiple words
    * the user inputs, and then prints a relative "thumbs up" or 
    * "thumbs down" depending on the delta value, and prints nothing if 
    * the value is neutral. 
    *
    * @param deltaUpper a double representing the upper bounds of the delta
    * @param deltaLower a double representing the lower bounds of the delta
    * @param file a file previously inputted by the user to scan
    * @param reviews a Scanner file containing all reviews
    * @param wordFull a String containing all words inputted by the user
    * @param noCompute a String containing an error message
    */
   public static void findWordFull(double deltaUpper, double deltaLower, 
                                    File file, Scanner reviews, String wordFull,
                                    String noCompute) 
                                    throws Exception {
      double avgScore = 0;
      int reviewScore = 0;
      int numReviewsTotal = 0;
     
      String[] fullArray = wordFull.split("\\s+"); 
      for (String wordTemp : fullArray) {
         double totalScoreWord = 0;
         int numReviewsWord = 0;
         while (reviews.hasNext()) {
            reviewScore = reviews.nextInt();
            String lineSearch = reviews.nextLine();
            String lineSearchLower = lineSearch.toLowerCase();
            if (isSubstring(wordTemp, lineSearchLower)) {
               totalScoreWord += reviewScore;
               numReviewsWord++;
               numReviewsTotal++;
            }
         }
         avgScore = avgScore + (totalScoreWord / numReviewsWord);
         reviews = new Scanner(file);
      }
      if (numReviewsTotal > 0) {
         avgScore = avgScore / fullArray.length;
         System.out.printf("full review score is %.5f\n", avgScore);
         if (avgScore > deltaUpper) {
            System.out.println("thumbs up");
         }
         else if (avgScore < deltaLower) {
            System.out.println("thumbs down");
         }

      } 
      else {
         System.out.println(noCompute);
      }
      return;
   }
   
   public static void main(String[] args) throws Exception {
      
      final String GETFILE = "enter input filename: ";
      final String GETWORD = "enter word to score --> ";
      final String GETREVIEW = "enter review line --> ";
      final String GETDELTA = "enter new delta [0,1] --> ";
      final String NOCOMPUTE = "review score can't be computed";
      final String DOWN = "thumbs down";
      final String UP = "thumbs up";
      final String BYE = "goodbye";
      final String INVALID = "invalid option, try again";
      final String GETCHOICE = "enter choice by number --> ";
      final String OPTION1 = "1. quit program";
      final String OPTION2 = "2. word scores";
      final String OPTION3 = "3. full review";
      final String OPTION4 = "4. cutoff delta";

      Scanner scnr = new Scanner(System.in);
           
      boolean programOn = false;
      
      double delta = 0;
      double deltaUpper = 2;
      double deltaLower = 2;
      
      System.out.print(GETFILE);
      String fileName = scnr.nextLine();
      
      if (findFile(fileName)) {
         programOn = true;
      }
      else {
         System.out.println(INVALID);
      }
      
      while (programOn) {
         File file = new File(fileName);
         Scanner reviews = new Scanner(file);
         printMenu(OPTION1, OPTION2, OPTION3, OPTION4);
         System.out.print(GETCHOICE);
         String menuChoice = scnr.next();
         if ("1".equals(menuChoice)) {
            System.out.println(BYE);
            programOn = false;
         }
         else if ("2".equals(menuChoice)) {
            System.out.print(GETWORD);
            String word = scnr.next();
            String toss2 = scnr.nextLine();
            String wordLower = word.toLowerCase();
            findWord(reviews, word, wordLower);
            continue;
         }
         else if ("3".equals(menuChoice)) {
            System.out.print(GETREVIEW);
            String toss = scnr.nextLine();
            String wordFull = scnr.nextLine();
            String fullLower = wordFull.toLowerCase();
            findWordFull(deltaUpper, deltaLower, file, reviews,
                           fullLower, NOCOMPUTE);
            continue;
         }
         else if ("4".equals(menuChoice)) {
            delta = 0;
            deltaUpper = 2;
            deltaLower = 2;
            System.out.print(GETDELTA);
            delta = scnr.nextDouble();
            deltaUpper += delta;
            deltaLower -= delta;
            continue;
         }
         else {
            System.out.println(INVALID);
         }
         
      }
   }
}