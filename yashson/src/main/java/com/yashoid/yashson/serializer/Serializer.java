package com.yashoid.yashson.serializer;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Yashar on 11/29/2017.
 */

public interface Serializer {

    void serialize(Object object, Writer output) throws IOException;

}
