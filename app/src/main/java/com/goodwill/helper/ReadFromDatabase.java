package com.goodwill.helper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by lenovo on 1/16/2018.
 */


public class ReadFromDatabase extends AppCompatActivity {


    public static String getLineString(int lineNumber, Activity activity, String filename) {
        BufferedReader line = null;
        int count = 1;
        try {
            line = new BufferedReader(new InputStreamReader(activity.getAssets().open(filename)));
            String read;
            while ((read = line.readLine()) != null) {

                if (count == lineNumber) {
                    return read;
                } else {
                    count++;
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }


    public static Question readFromDatabaseQuotes(int lineNumber, Activity activity, String filename) {

        String read = getLineString(lineNumber, activity, filename);
        if (read != null) {
            Question question = new Question();
            String splited[] = read.split(";");
            question.setDisplay(splited[0].trim());
            question.setOpA(splited[1].trim());
            question.setOpB(splited[2].trim());
            question.setOpC(splited[3].trim());
            question.setOpD(splited[4].trim());
            question.setAnswer(splited[5].trim());
            question.setHint1(splited[6].trim());
            question.setQuestion(splited[7].trim());

            return question;
        }
        return null;
    }

    public static Question readFromDatabasePicture(int lineNumber, Activity activity, String filename) {
        String read = getLineString(lineNumber, activity, filename);
        if (read != null) {
            Question question = new Question();
            String splited[] = read.split(";");
            question.setPicname(splited[0].trim());
            question.setQuestion(splited[1].trim());
            question.setOpA(splited[2].trim());
            question.setOpB(splited[3].trim());
            question.setOpC(splited[4].trim());
            question.setOpD(splited[5].trim());
            question.setAnswer(splited[6].trim());
            question.setHint1(splited[7].trim());
            return question;
        }
        return null;
    }

    public static Question readFromDatabaseEmoji(int lineNumber, Activity activity, String filename) {
        String read = getLineString(lineNumber, activity, filename);
        if (read != null) {
            Question question = new Question();
            String splited[] = read.split(";");
            question.setPicname(splited[0].trim());
            question.setQuestion(splited[2].trim());
            question.setHint1(splited[3].trim());
            question.setAnswer(splited[1].trim());
            return question;
        }
        return null;
    }
}

