package com.yashoid.yashson.datareader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public abstract class DataReader {

    private String mFieldName;

    protected DataReader(String fieldName) {
        mFieldName = fieldName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    abstract public boolean isNull() throws IOException;

    abstract public void onReadingFinished() throws IOException;

    abstract public int readInt() throws IOException;

    abstract public long readLong() throws IOException;

    abstract public float readFloat() throws IOException;

    abstract public double readDouble() throws IOException;

    abstract public String readString() throws IOException;

    abstract public byte readByte() throws IOException;

    abstract public char readChar() throws IOException;

    public abstract boolean readBoolean() throws IOException;

    abstract public ListDataReader asListReader() throws IOException;

    abstract public ObjectDataReader asObjectReader() throws IOException;

}
