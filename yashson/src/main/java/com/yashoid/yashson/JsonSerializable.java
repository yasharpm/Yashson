package com.yashoid.yashson;

import android.content.Context;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Yashar on 11/29/2017.
 */

public interface JsonSerializable {

    String getSerializedNameForField(String fieldName);

    boolean serializeField(String fieldName, Yashson yashson, Writer writer, Context context) throws IOException;

}
