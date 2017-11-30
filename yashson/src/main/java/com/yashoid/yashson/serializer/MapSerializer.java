package com.yashoid.yashson.serializer;

import com.yashoid.yashson.Yashson;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Yashar on 11/29/2017.
 */

public class MapSerializer implements Serializer {

    private Yashson mYashson;

    public MapSerializer(Yashson yashson) {
        mYashson = yashson;
    }

    @Override
    public void serialize(Object object, Writer output) throws IOException {
        output.write("{");

        Map map = (Map) object;

        Set<Map.Entry> entries = map.entrySet();
        Iterator<Map.Entry> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();

            Object key = entry.getKey();
            Object value = entry.getValue();

            if (key == null) {
                continue;
            }

            output.write("\"");
            output.write(key.toString());
            output.write("\"");

            output.write(":");

            mYashson.toJson(value, output);

            if (iterator.hasNext()) {
                output.write(",");
            }
        }

        output.write("}");
    }

}
