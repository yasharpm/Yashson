package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class LongValueParser extends ValueParser {

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        return reader.readLong();
    }

}
