package com.yashoid.yashson.datareader.jsonreader;

import android.util.JsonReader;

import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.ListDataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class JsonReaderListDataReader implements ListDataReader {

    private JsonReader mReader;

    private DepthManager mDepthManager;

    private boolean mHasStarted = false;

    protected JsonReaderListDataReader(JsonReader reader, DepthManager depthManager) {
        mReader = reader;

        mDepthManager = depthManager;
    }

    private void start() throws IOException {
        mHasStarted = true;

        mReader.beginArray();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (!mHasStarted) {
            start();
        }

        return mReader.hasNext();
    }

    @Override
    public DataReader next() throws IOException {
        if (!mHasStarted) {
            start();
        }

        return new JsonReaderDataReader(null, mReader, mDepthManager);
    }

}
