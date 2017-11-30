package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public abstract class ValueParser {

    public Object parseValue(DataReader dataReader) throws IOException {
        if (dataReader.isNull()) {
            return null;
        }

        return onParseNotNullValue(dataReader);
    }

    abstract protected Object onParseNotNullValue(DataReader reader) throws IOException;

}
