import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    //Method for sorting the TreeMap based on values
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
       Comparator<K> valueComparator =  new Comparator<K>() {
          public int compare(K k1, K k2) {
             int compare = map.get(k2).compareTo(map.get(k1));
             if (compare == 0) 
                return 1;
             else 
                return compare;
          }
       };
 
       Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
       sortedByValues.putAll(map);
       return sortedByValues;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        Map<String, Integer> wordCounts = new TreeMap();

        Integer[] onlyTheseLines = getIndexes();

        ArrayList<String> titles = new ArrayList<String>();

        InputStream fstream = new FileInputStream(inputFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null) {
           titles.add(strLine);
        }
        br.close();

       
        ArrayList<String> subsettedTitles = new ArrayList<String>();
        String[] titlesArray = titles.toArray(new String[titles.size()]);
        for(int iTitle = 0; iTitle < onlyTheseLines.length; iTitle++)
        {
           Integer currentIndex = onlyTheseLines[iTitle];
           String currentTitle = titlesArray[currentIndex];
           subsettedTitles.add(currentTitle);
        } 


        for(String title : subsettedTitles)
        {
           StringTokenizer tokenizer = new StringTokenizer(title, delimiters);
           while(tokenizer.hasMoreTokens())
           {
              String word = tokenizer.nextToken();
              //word =  word.trim();        //remove white space
              word = word.toLowerCase();  //convert to lower case
              if(Arrays.asList(stopWordsArray).contains(word))
              {
                 //skip to next word
                 continue;
              }
              else
              {
                 if(wordCounts.containsKey(word))  //we have seen this word before
                 {
                    int currentCount = wordCounts.get(word);
                    wordCounts.put(word, currentCount + 1);   //increment word count
                 }
                 else
                 {
                    wordCounts.put(word, 1);  //initialise word count
                 }
              }  
           }
        }

        //sort tree map by value (already lexicographically sorted)
        Map sortedWordCounts = sortByValues(wordCounts);

        //put top 20 words into ret string array
        int iWord = 0;
        for (Object key : sortedWordCounts.keySet()) {
           ret[iWord] = key.toString();
           iWord = iWord + 1;
           if(iWord >= 20)
              break;  
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
