package com.example.funwithregex.ui.RegexSandbox;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.widget.ThemedSpinnerAdapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSandBoxViewModel extends ViewModel {

    //
    // This is an unique identifier for each thread
    // trying to find a solution for a voltage divider.
    public long timestampOfLastCalc, timeStampSolAvailable;

    //
    // Mutable live data
    //
    public MutableLiveData<String> regexText;

    public MutableLiveData<String> getRegexText() {
        if (regexText == null) {
            regexText = new MutableLiveData<>();
            regexText.postValue("\\w");
        }
        return regexText;
    }

    public MutableLiveData<SpannableString> testText;

    public MutableLiveData<SpannableString> getTestText() {
        if (testText == null) {
            testText = new MutableLiveData<>();
            testText.postValue(new SpannableString("Teste mich mit einer Regex :-)"));
        }
        return testText;
    }

    public MutableLiveData<String> result;

    public MutableLiveData<String> getResult() {
        if (result == null) {
            result = new MutableLiveData<>();
        }
        return result;
    }

    public MutableLiveData<Integer> resultsFound;

    public MutableLiveData<Integer> getResultsFound() {
        if (resultsFound == null) {
            resultsFound = new MutableLiveData<Integer>();
            resultsFound.postValue(0);
        }
        return resultsFound;
    }

    public MutableLiveData<String> errorText;

    public MutableLiveData<String> getErrorText() {
        if (errorText == null) {
            errorText = new MutableLiveData<>();
            errorText.setValue("");
        }
        return errorText;
    }

    /**
     * This runs the regex and searches for ocurences of the search pattern
     * inside the search text.
     *
     * @param timestamp
     */
    public void runRegex(long timestamp) {

        // Unique idendifier for the last thread started....
        timestampOfLastCalc = timestamp;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Pattern mPattern;
                    mPattern = Pattern.compile(regexText.getValue());

                    SpannableString markedText = new SpannableString(testText.getValue());
                    Matcher matcher = mPattern.matcher(testText.getValue());
                    ArrayList matching = new ArrayList();

                    if (timestamp == timestampOfLastCalc) {
                        int results = 0;
                        while (matcher.find()) {
                            matching.add(matcher.group(0));
                            markedText.setSpan(new BackgroundColorSpan(Color.YELLOW), matcher.start(), matcher.end(), 0);
                            results++;
                            Thread.sleep(15);

                            // Show what we have found....
                            result.postValue(matching.get(results-1)+"\n");

                            resultsFound.postValue(results);

                        }
                        testText.postValue(markedText);
                    }

                } catch (Exception e) {
                    errorText.postValue(e.toString());
                }
            }
        });
        t.start();
    }
}