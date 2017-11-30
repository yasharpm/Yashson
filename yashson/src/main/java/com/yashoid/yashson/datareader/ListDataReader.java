package com.yashoid.yashson.datareader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public interface ListDataReader {

    boolean hasNext() throws IOException;

    DataReader next() throws IOException;

}
