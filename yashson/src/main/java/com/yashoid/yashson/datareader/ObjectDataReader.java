package com.yashoid.yashson.datareader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public abstract class ObjectDataReader {

    abstract public boolean hasNextField() throws IOException;

    abstract public DataReader nextField() throws IOException;

}
