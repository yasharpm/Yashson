package com.yashoid.yashson.serializer;

import com.yashoid.yashson.Yashson;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Yashar on 11/29/2017.
 */

public class CollectionSerializer implements Serializer {

    private Yashson mYashson;

    public CollectionSerializer(Yashson yashson) {
        mYashson = yashson;
    }

    @Override
    public void serialize(Object object, Writer output) throws IOException {
        output.write("[");

        Collection collection = (Collection) object;

        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            mYashson.toJson(iterator.next(), output);

            if (iterator.hasNext()) {
                output.write(",");
            }
        }

        output.write("]");
    }

}
