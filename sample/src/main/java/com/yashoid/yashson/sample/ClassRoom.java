package com.yashoid.yashson.sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClassRoom {

    public List<Person<Integer>> mPersonList;

    public static JSONObject getSampleJson() {
        JSONObject classroom = new JSONObject();

        JSONArray personList = new JSONArray();

        for (int i = 0; i < 5; i++) {
            personList.put(Person.getSampleJson());
        }

        try {
            classroom.put("person_list", personList);
        } catch (JSONException e) { }

        return classroom;
    }

    @Override
    public String toString() {
        return "personList=" + mPersonList;
    }
}
