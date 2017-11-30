package com.yashoid.yashson.serializer;

import com.yashoid.yashson.Yashson;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;

/**
 * Created by Yashar on 11/29/2017.
 */

public class ArraySerializer implements Serializer {

    private Yashson mYashson;

    public ArraySerializer(Yashson yashson) {
        mYashson = yashson;
    }

    @Override
    public void serialize(Object object, Writer output) throws IOException {
        output.write("[");

        int length = Array.getLength(object);

        for (int index = 0; index < length; index++) {
            mYashson.toJson(Array.get(object, index), output);

            if (index < length - 1) {
                output.write(",");
            }
        }

        output.write("]");
    }

}
