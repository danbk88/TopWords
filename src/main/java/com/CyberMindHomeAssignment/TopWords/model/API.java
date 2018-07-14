package com.CyberMindHomeAssignment.TopWords.model;


import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.lang.Math.abs;


public class API {

    // Consts:
    private static final int NUM_OF_TOP_WORDS = 3;

    // MEMBERS:
    private static Map<String, WordAndScore> m_DB;

    // CTOR:
    public API(){
    }

    // Methods:
    private void initDB() {
        m_DB = new HashMap<>();
    }

    public void AddWord(String word) {
        int wordAsciiValue = calcWordAsciiValue(word);
        m_DB.put(word, new WordAndScore(word, wordAsciiValue));
    }

    private int calcWordAsciiValue(String word) {
        //  Calc word's score based on its chars ascii values, all char but letters are ignored - not included in the calc.
        byte[] bytes = null;
        int asciiSum =0;

        try {
            bytes = word.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        for(byte currByte : bytes){
            if(isCharALetter(currByte)){
                asciiSum += currByte;
            }
        }
        // Debug:
        System.out.println("word score: " + asciiSum);

        return asciiSum;
    }

    private boolean isCharALetter(byte currByte) {
        return Character.isLetter((char)currByte);
    }

    public List<String> GetTopWords(String word) {
        // return top 3 words from DB - closest words (word score wise) stored in the db
        int wordScore = calcWordAsciiValue(word);

        return findTopWords(wordScore);
    }

    private List<String> findTopWords(int wordScore) {
        List<WordAndScore> topWordStrArray;

        if(m_DB == null){
            //DB is empty, return null
            return null;
        }
        else{
            if(m_DB.size() <= NUM_OF_TOP_WORDS) {
                // DB has 3 words or less, return them all:
                topWordStrArray = new ArrayList<>();
                for (Map.Entry<String, WordAndScore> currentry : m_DB.entrySet()) {
                    topWordStrArray.add(currentry.getValue());
                }
            }
            else{
                // Get top 3 words:
                topWordStrArray = calcTopWords(wordScore);
            }
        }

        return getStrList(topWordStrArray);
    }

    private List<String> getStrList(List<WordAndScore> topWordStrArray) {
        List<String> retVal = new ArrayList<>();

        for(WordAndScore curr : topWordStrArray){
            retVal.add(curr.getWord());
        }

        return retVal;
    }

    private List<WordAndScore> calcTopWords(int wordScore) {
        List<WordAndScore> topWords = new ArrayList<>();
        WordAndScore thirdWordInTop = null;

        for (Map.Entry<String, WordAndScore> currentry : m_DB.entrySet()) {
            // insert first 3 entries to top words, after that, compare each entry to third word diff and update top word accordingly
            if (topWords.size() < NUM_OF_TOP_WORDS) {
                thirdWordInTop = onFirst3Words(topWords,currentry, wordScore, thirdWordInTop);
            }
            else {
                //top has 3 strings, compare words to thirdTopWord:
                int currWordDiff = calcWordDiff(wordScore, currentry.getValue().getWordScore());
                if (isWordDiffIsSmallerThenCurrThird(currWordDiff, calcWordDiff(wordScore,thirdWordInTop.getWordScore()))) {
                    //remove third top word:
                    removeFromTopWords(topWords, thirdWordInTop);
                    thirdWordInTop = handleSingleEntry(topWords, wordScore, currentry, currWordDiff);
                    //add curr word to top words:
                    topWords.add(currentry.getValue());
                }
            }
        }

        return topWords;
    }

    private void removeFromTopWords(List<WordAndScore> topWords, WordAndScore thirdWordInTop) {
        int index = -1;
        for (int i=0; i< topWords.size(); i++){
            if(topWords.get(i).getWord().equals(thirdWordInTop.getWord())){
                index = i;
            }
        }
        topWords.remove(index);
    }

    private WordAndScore handleSingleEntry(List<WordAndScore> topWords, int wordScore, Map.Entry<String, WordAndScore> currentry, int currWordDiff) {
        // update top words list - add new and remove old:
        WordAndScore retVal = new WordAndScore();

        if (currWordDiff > calcWordDiff(wordScore ,topWords.get(0).getWordScore())) {
            // currWordDiff > top[0] diff:
            if (currWordDiff > calcWordDiff(wordScore,topWords.get(1).getWordScore())) {
                // top[0], top[1] < currWord => currWord is third:
                retVal.setWord(currentry.getKey());
                retVal.setWordScore(currentry.getValue().getWordScore());
            } else {
                // top[0] < currWord < top[1] => top[1] is third:
                retVal.setWord(topWords.get(1).getWord());
                retVal.setWordScore(topWords.get(1).getWordScore());
            }
        } else {
            // currWordDiff < top[0] diff:
            if (calcWordDiff(wordScore ,topWords.get(0).getWordScore() ) < calcWordDiff(wordScore ,topWords.get(1).getWordScore())) {
                // currWord < top[0] < top[1] => top[1] is third:
                retVal.setWord(topWords.get(1).getWord());
                retVal.setWordScore(topWords.get(1).getWordScore());
            } else {
                // currWord < top[1] < top[0] => top[0] is third:
                retVal.setWord(topWords.get(0).getWord());
                retVal.setWordScore(topWords.get(0).getWordScore());
            }
        }
        return retVal;
    }

    private WordAndScore onFirst3Words(List<WordAndScore> topWords, Map.Entry<String, WordAndScore> currentry, int wordScore, WordAndScore thirdWordInTop) {
        // add word to topWords:
        if (topWords.size() == 0) {
            // first word:
            topWords.add(currentry.getValue());
            //set third top word:
            return new WordAndScore(currentry.getKey(), currentry.getValue().getWordScore());
        }
        else{
            topWords.add(currentry.getValue());
            int currEntryWordDiff = calcWordDiff(wordScore, currentry.getValue().getWordScore());
            int thirdWordInTopWordDiff = calcWordDiff(wordScore, thirdWordInTop.getWordScore());
            if (isWordDiffIsBiggerThenCurrThird(currEntryWordDiff, thirdWordInTopWordDiff)){
                // update thirdTopWord:
                thirdWordInTop.setWord(currentry.getKey());
                thirdWordInTop.setWordScore(currentry.getValue().getWordScore());
            }
            else{
                // just for order:
                thirdWordInTop.setWord(thirdWordInTop.getWord());
                thirdWordInTop.setWordScore(thirdWordInTop.getWordScore());
            }
        }
        return thirdWordInTop;
    }

    private int calcWordDiff(int wordScore, Integer value) {
        return  abs(wordScore - value);
    }

    private boolean isWordDiffIsBiggerThenCurrThird(int worddiff, int thirdTopWordDifference) {
        return worddiff > thirdTopWordDifference;
    }

    private boolean isWordDiffIsSmallerThenCurrThird(int worddiff, int thirdTopWordDifference) {
        return worddiff < thirdTopWordDifference;
    }

    //GETTERS:
    public Set<String> GetWords(){
        if(m_DB == null){
            initDB();
        }
        return m_DB.keySet();
    }
}
