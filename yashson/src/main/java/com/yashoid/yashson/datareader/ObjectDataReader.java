package com.yashoid.yashson.datareader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public interface ObjectDataReader {

    boolean hasNextField() throws IOException;

    DataReader nextField() throws IOException;

}
