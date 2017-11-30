package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.ListDataReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yashar on 11/15/2017.
 */

public class ArrayListValueParser extends ValueParser {

    private ValueParser mMemberValueParser;

    public ArrayListValueParser(ValueParser memberValueParser) {
        mMemberValueParser = memberValueParser;
    }

    @Override
    protected ArrayList<Object> onParseNotNullValue(DataReader reader) throws IOException {
        ArrayList<Object> list = new ArrayList<>();

        ListDataReader listReader = reader.asListReader();

        while (listReader.hasNext()) {
            DataReader innerReader = listReader.next();

            list.add(mMemberValueParser.parseValue(innerReader));

            innerReader.onReadingFinished();
        }

        return list;
    }

}
