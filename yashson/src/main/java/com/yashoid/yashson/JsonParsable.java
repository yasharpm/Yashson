package com.yashoid.yashson;

import android.content.Context;

import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/28/2017.
 */

public interface JsonParsable {

    void onUnidentifiedParsedFieldName(String parsedFieldName, DataReader reader, Context context) throws IOException;

}
