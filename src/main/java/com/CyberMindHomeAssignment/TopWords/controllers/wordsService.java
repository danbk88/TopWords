package com.CyberMindHomeAssignment.TopWords.controllers;

import com.CyberMindHomeAssignment.TopWords.model.API;
import com.CyberMindHomeAssignment.TopWords.model.Validator;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Random;


@Controller
public class wordsService {
    // Consts:
    private static final String SUCCESS = "success";
    private static final String ERR = "err";
    private static final String QUERY = "query";
    private static final String TOP = "top";

    //CTOR:
    public wordsService(){
    }

    // Methods:
    @RequestMapping(value = "/insert/{word}", method= {RequestMethod.POST})
    @ResponseBody
    public String AddWordToDB(@PathVariable String word) throws Exception {
        JSONObject res = new JSONObject();
        boolean success = false;
        int err = 0;

        // Debug:
        System.out.println("word: " + word + " recieved.");
        if(Validator.IsWordValid(word)){
            new API().AddWord(word);
            success = true;
        }
        else{
            err = getRandomErrorCode();
        }

        res.put(SUCCESS, success);
        res.put(ERR, err);

        return res.toString();
    }

    @RequestMapping(value = "/match/{word}", method= {RequestMethod.GET})
    @ResponseBody
    public String getTopWords(@PathVariable String word) throws JSONException {
        List<String> topWords =  new API().GetTopWords(word);
        JSONObject res = new JSONObject();

        System.out.println("Top words are : " + topWords);
        res.put(TOP, topWords);
        res.put(QUERY, word);

        return res.toString();
    }

    private int getRandomErrorCode() {
        Random rand = new Random();

        int  number = rand.nextInt(100) + 1;
        return number;
    }
}
