package com.yashoid.yashson;

import android.content.Context;

import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.valueparser.ValueParser;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public abstract class InstanceCreator<T> {

    abstract public T newInstance(Context context);

    protected ValueParser getValueParser(String parsedFieldName, String targetFieldName, Context context) {
        return null;
    }

}
