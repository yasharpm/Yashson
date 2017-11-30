package com.yashoid.yashson.datareader.jsonreader;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class DepthManager {

    private JsonReader mReader;

    private int mDepth = 0;

    protected DepthManager(JsonReader reader) {
        mReader = reader;
    }

    protected int getDepth() {
        return mDepth;
    }

    protected void add() {
        mDepth++;
    }

    protected void clearToDepth(int depth) throws IOException {
        while (mDepth > depth) {
            backOneDepth();
        }
    }

    private void backOneDepth() throws IOException{
        mDepth--;

        JsonToken token = mReader.peek();

        while (token != JsonToken.END_ARRAY && token != JsonToken.END_OBJECT) {
            readToken(token);

            token = mReader.peek();
        }

        readToken(token);
    }

    private void readToken(JsonToken token) throws IOException {
        switch (token) {
            case BEGIN_ARRAY:
                mReader.beginArray();
                return;
            case END_ARRAY:
                mReader.endArray();
                return;
            case BEGIN_OBJECT:
                mReader.beginObject();
                return;
            case END_OBJECT:
                mReader.endObject();
                return;
            case NAME:
                mReader.nextName();
                return;
            case STRING:
                mReader.nextString();
                return;
            case NUMBER:
                mReader.nextDouble();
                return;
            case BOOLEAN:
                mReader.nextBoolean();
                return;
            case NULL:
                mReader.nextNull();
                return;
        }
    }

}
