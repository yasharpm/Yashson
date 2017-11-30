package com.yashoid.yashson.datareader.jsonreader;

import android.util.JsonReader;

import com.yashoid.yashson.datareader.ObjectDataReader;
import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class JsonReaderObjectDataReader extends ObjectDataReader {

    private JsonReader mReader;

    private DepthManager mDepthManager;

    private boolean mHasStarted = false;

    public JsonReaderObjectDataReader(JsonReader reader) {
        this(reader, new DepthManager(reader));
    }

    protected JsonReaderObjectDataReader(JsonReader reader, DepthManager depthManager) {
        mReader = reader;

        mDepthManager = depthManager;

        mReader.setLenient(true);
    }

    private void start() throws IOException {
        mHasStarted = true;

        mReader.beginObject();
    }

    @Override
    public boolean hasNextField() throws IOException {
        if (!mHasStarted) {
            start();
        }

        return mReader.hasNext();
    }

    @Override
    public DataReader nextField() throws IOException {
        if (!mHasStarted) {
            start();
        }

        String name = mReader.nextName();

        return new JsonReaderDataReader(name, mReader, mDepthManager);
    }

}
