package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.ListDataReader;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Yashar on 11/15/2017.
 */

public class ArrayValueParser extends ValueParser {

    private Class mComponentType;
    private ValueParser mMemberValueParser;

    public ArrayValueParser(Class componentType, ValueParser memberValueParser) {
        mComponentType = componentType;
        mMemberValueParser = memberValueParser;
    }

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        ArrayList<Object> list = new ArrayList<>();

        ListDataReader listReader = reader.asListReader();

        while (listReader.hasNext()) {
            DataReader innerReader = listReader.next();

            list.add(mMemberValueParser.parseValue(innerReader));

            innerReader.onReadingFinished();
        }

        Object array = Array.newInstance(mComponentType, list.size());

        for (int index = 0; index < list.size(); index++) {
            Array.set(array, index, list.get(index));
        }

        return array;
    }

}
