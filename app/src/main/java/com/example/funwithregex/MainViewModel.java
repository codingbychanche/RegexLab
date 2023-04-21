package com.example.funwithregex;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewModel extends ViewModel {

    public MutableLiveData<String> regexText;
    public MutableLiveData<String> getRegexText(){
        if (regexText==null) {
            regexText = new MutableLiveData<>();
            regexText.postValue("\\w");
        }
        return regexText;
    }
    public MutableLiveData<SpannableString> testText;
    public MutableLiveData<SpannableString> getTestText(){
        if (testText==null) {
            testText = new MutableLiveData<>();
            testText.postValue(new SpannableString("Teste mich mit einer Regex :-)"));
        }
        return testText;
    }

    public MutableLiveData<Integer> resultsFound;
    public MutableLiveData<Integer> getResultsFound(){
        if (resultsFound==null) {
            resultsFound = new MutableLiveData<Integer>();
            resultsFound.postValue(0);
        }
        return resultsFound;
    }

    public MutableLiveData<String> errorText;
    public MutableLiveData<String> getErrorText(){
        if(errorText==null){
            errorText=new MutableLiveData<>();
            errorText.setValue("");
        }
        return errorText;
    }

    public void runRegex(){
        try {
            Pattern mPattern;
            mPattern = Pattern.compile(regexText.getValue());

            SpannableString markedText = new SpannableString(testText.getValue());
            Matcher matcher = mPattern.matcher(testText.getValue());
            ArrayList matching = new ArrayList();

            int results=0;
            while (matcher.find()) {
                matching.add(matcher.group(0));
                markedText.setSpan(new BackgroundColorSpan(Color.YELLOW), matcher.start(), matcher.end(), 0);
                results++;
            }
            testText.postValue(markedText);
            resultsFound.postValue(results);
        }catch (Exception e) {
            errorText.postValue(e.toString());
        }
    }
}
