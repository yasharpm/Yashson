package com.yashoid.yashson.sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yashar on 11/17/2017.
 */

public class Person<T> {

    private String mName;

    private int mAge;

    private ArrayList<T> mGrades;

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public int getAge() {
        return mAge;
    }

    public void setGrades(ArrayList<T> grades) {
        mGrades = grades;
    }

    public ArrayList<T> getGrades() {
        return mGrades;
    }

    @Override
    public String toString() {
        return "name=" + mName + " age=" + mAge + " grades=" + mGrades;
    }

    public static JSONObject getSampleJson() {
        JSONObject person = new JSONObject();

        try {
            person.put("name", "Sample Person");
            person.put("age", 32);

            JSONArray grades = new JSONArray();
            grades.put(12);
            grades.put(20);
            grades.put(19);

            person.put("grades", grades);
        } catch (JSONException e) { }

        return person;
    }

}
