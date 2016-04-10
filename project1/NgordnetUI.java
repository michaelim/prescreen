/* Starter code for NgordnetUI (part 7 of the project). Rename this file and 
   remove this comment. */

package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author [yournamehere mcjones]
 */
public class NgordnetUI {
    private static final int ENDYR = 2015;
    public static void main(String[] args) {
        NGramMap ngm = new NGramMap("ngordnet/words_that_start_with_q.csv", 
                  "ngordnet/total_counts.csv");
        WordNet wn = new WordNet("ngordnet/synsets11.txt", "ngordnet/hyponyms11.txt");
        Plotter pl = new Plotter();
        int startYear = 0;
        int endYear = ENDYR;
        WordLengthProcessor wlp = new WordLengthProcessor();
        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            try {
                switch (command) {
                    case "quit":
                        return;
                    case "help":
                        In helpFile = new In("ngordnet/help.txt");
                        String helpString = helpFile.readAll();
                        System.out.println(helpString);
                        break;
                    case "range": 
                        int tempStartYear = Integer.parseInt(tokens[0]); 
                        int tempEndYear = Integer.parseInt(tokens[1]);
                        if (tempStartYear > tempEndYear) {
                            System.out.println("startDate can't be greater than endDate!");
                        } else {
                            startYear = tempStartYear;
                            endYear = tempEndYear;
                        }
                        break;
                    case "count":
                        String word = tokens[0];
                        int year = Integer.parseInt(tokens[1]);
                        System.out.println(ngm.countInYear(word, year));
                        break;
                    case "hyponyms":
                        String word2 = tokens[0];
                        System.out.println(wn.hyponyms(word2));
                        break;
                    case "history":
                        if (tokens[0] == null) {
                            throw new RuntimeException();
                        }
                        pl.plotAllWords(ngm, tokens, startYear, endYear);
                        break;
                    case "hypohist":
                        if (tokens[0] == null) {
                            throw new RuntimeException();
                        }
                        pl.plotCategoryWeights(ngm, wn, tokens, startYear, endYear);
                        break;
                    case "wordlength":
                        pl.plotProcessedHistory(ngm, startYear, endYear, wlp);
                        break;
                    case "zipf":
                        pl.plotZipfsLaw(ngm, Integer.parseInt(tokens[0]));
                        break;
                    default:
                        System.out.println("Invalid command.");  
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Invalid parameters!");
            }   
        }
    }
} 
