package com.CyberMindHomeAssignment.TopWords.model;

import java.util.Set;

public class Validator {

    // CTOR:
    public Validator(){}

    // Methods:
    public static boolean IsWordValid(String word){
        boolean retVal = true;
        // todo check if word empty
        Set<String> DBWords = new API().GetWords();
        if (DBWords.contains(word)){
            retVal = false;
        }

        return retVal;
    }
}

