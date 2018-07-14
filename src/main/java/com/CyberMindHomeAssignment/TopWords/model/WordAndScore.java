package com.CyberMindHomeAssignment.TopWords.model;

public class WordAndScore {
    private String word;
    private int wrodScore;


    //CTOR:
    public WordAndScore(){
    }

    public WordAndScore(String Word, int score){
        this.word = Word;
        this.wrodScore = score;
    }

    //GETTERS/SETTERS:
    public String getWord(){
        return word;
    }

    public int getWordScore(){
        return wrodScore;
    }

    public void setWord(String val){
        word = val;
    }

    public void setWordScore(int val){
        wrodScore = val;
    }
}