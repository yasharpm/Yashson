package com.yashoid.yashson.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yashoid.yashson.Yashson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classRoomTest();
        personTest();
    }

    private void classRoomTest() {
        try {
            JSONObject json = ClassRoom.getSampleJson();

            Yashson yashson = new Yashson();

            ClassRoom classRoom = yashson.parse(json, ClassRoom.class);

            Log.d("AAA", classRoom.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void personTest() {
        try {
            Person<Integer> firstPerson = new Person<>();

            firstPerson.setName("Yashar");
            firstPerson.setAge(30);

            ArrayList<Integer> grades = new ArrayList<>();
            Collections.addAll(grades, 12, 20, 18);
            firstPerson.setGrades(grades);

            String serializedPerson = new Yashson().toJson(firstPerson);

            Log.d("AAA", "serialized person = " + serializedPerson);

            JSONObject test = new JSONObject(serializedPerson);

            Person<Integer> person = new Yashson().parse(test, Person.class, Integer.class);

            Log.d("AAA", person.toString());

            JSONArray arrayTest = new JSONArray();

            JSONArray jGrades = new JSONArray();
            jGrades.put(12);
            jGrades.put(20);
            jGrades.put(18);

            arrayTest.put(jGrades);
            arrayTest.put(jGrades);
            arrayTest.put(jGrades);

            List<List> listOfGrades = new Yashson().parseList(arrayTest.toString(), List.class, Integer.class);

            Log.d("AAA", "" + listOfGrades);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            JSONObject test = new JSONObject();
            test.put("name", "Yashar");
            test.put("age", 30);

            Person person = new Yashson().parse(test, Person.class);

            System.out.println(person);
//            Class<B> clazz = B.class;
//
//            Field field = clazz.getField("as");
//            Class listClass = field.getType();
//
//            if (field.getGenericType() instanceof ParameterizedType) {
//                ParameterizedType pt = (ParameterizedType) field.getGenericType();
//
//                System.out.println("HAPPY> " + ((Class) pt.getActualTypeArguments()[0]).getName());
//            }
//
//            Object list = listClass.newInstance();
//
//            B b = clazz.newInstance();
//
//            field.set(b, list);
//
//            ((ArrayList) list).add(3.4f);
//            ((ArrayList) list).add(4);
//
//            for (Float a: b.as) {
//                System.out.println(a);
//            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
