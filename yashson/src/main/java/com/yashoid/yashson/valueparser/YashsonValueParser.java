package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.Yashson;
import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.fieldprovider.ParsedType;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class YashsonValueParser extends ValueParser {

    private Yashson mYashson;

    private Class mType;
    private ParsedType[] mSubTypes;

    public YashsonValueParser(Yashson yashson, Class type, ParsedType... subTypes) {
        mYashson = yashson;

        mType = type;
        mSubTypes = subTypes;
    }

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        return mYashson.parse(reader, mType, mSubTypes);
    }

}
