package com.yashoid.yashson.valueparser;

import com.yashoid.yashson.datareader.ObjectDataReader;
import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Yashar on 11/15/2017.
 */

public class HashMapValueParser extends ValueParser {

    private ValueParser mTypeParser;

    public HashMapValueParser(ValueParser typeParser) {
        mTypeParser = typeParser;
    }

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        HashMap<String, Object> map = new HashMap<>();

        ObjectDataReader objectDataReader = reader.asObjectReader();

        while (objectDataReader.hasNextField()) {
            DataReader memberReader = objectDataReader.nextField();

            String key = memberReader.getFieldName();
            Object value = mTypeParser.parseValue(memberReader);

            memberReader.onReadingFinished();

            map.put(key, value);
        }

        return map;
    }

}
