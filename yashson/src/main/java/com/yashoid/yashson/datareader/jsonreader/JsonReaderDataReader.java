package com.yashoid.yashson.datareader.jsonreader;

import android.util.JsonReader;
import android.util.JsonToken;

import com.yashoid.yashson.datareader.ObjectDataReader;
import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.ListDataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class JsonReaderDataReader extends DataReader {

    private boolean mIsRoot;

    private JsonReader mReader;

    private DepthManager mDepthManager;

    private int mDepth;

    private boolean mValueRead = false;

    public JsonReaderDataReader(JsonReader reader) {
        super(null);

        mIsRoot = true;

        mReader = reader;

        mDepthManager = new DepthManager(mReader);

        mDepth = 0;
    }

    protected JsonReaderDataReader(String fieldName, JsonReader reader, DepthManager depthManager) {
        super(fieldName);

        mIsRoot = false;

        mReader = reader;

        mDepthManager = depthManager;

        mDepth = mDepthManager.getDepth();
    }

    @Override
    public boolean isNull() throws IOException {
        return mReader.peek() == JsonToken.NULL;
    }

    @Override
    public void onReadingFinished() throws IOException {
        if (mDepthManager.getDepth() > mDepth) {
            mDepthManager.clearToDepth(mDepth);
        }
        else if (!mValueRead) {
            mReader.skipValue();
        }
    }

    @Override
    public int readInt() throws IOException {
        mValueRead = true;

        return mReader.nextInt();
    }

    @Override
    public long readLong() throws IOException {
        mValueRead = true;

        return mReader.nextLong();
    }

    @Override
    public float readFloat() throws IOException {
        mValueRead = true;

        return (float) mReader.nextDouble();
    }

    @Override
    public double readDouble() throws IOException {
        mValueRead = true;

        return mReader.nextDouble();
    }

    @Override
    public String readString() throws IOException {
        mValueRead = true;

        return mReader.nextString();
    }

    @Override
    public byte readByte() throws IOException {
        mValueRead = true;

        return (byte) mReader.nextInt();
    }

    @Override
    public char readChar() throws IOException {
        mValueRead = true;

        return mReader.nextString().charAt(0);
    }

    @Override
    public boolean readBoolean() throws IOException {
        mValueRead = true;

        return mReader.nextBoolean();
    }

    @Override
    public ListDataReader asListReader() {
        mValueRead = true;

        if (!mIsRoot) {
            mDepthManager.add();
        }

        return new JsonReaderListDataReader(mReader, mDepthManager);
    }

    @Override
    public ObjectDataReader asObjectReader() {
        mValueRead = true;

        if (!mIsRoot) {
            mDepthManager.add();
        }

        return new JsonReaderObjectDataReader(mReader, mDepthManager);
    }

}
