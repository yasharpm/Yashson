package com.yashoid.yashson.valueparser;

import android.graphics.Color;

import com.yashoid.yashson.datareader.DataReader;

import java.io.IOException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class ColorValueParser extends ValueParser {

    private static ColorValueParser mInstance = null;

    public ColorValueParser getInstance() {
        if (mInstance == null) {
            mInstance = new ColorValueParser();
        }

        return mInstance;
    }

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        String rawColor = reader.readString();

        try {
            return tryReadColorAsInteger(rawColor);
        } catch (Throwable t) { }

        try {
            tryReadHexedColor(rawColor.startsWith("#") ? rawColor.substring(1) : rawColor);
        } catch (Throwable t) { }

        return Color.parseColor(rawColor);
    }

    private int tryReadColorAsInteger(String rawColor) {
        int color = Integer.parseInt(rawColor);

        if (color == 0) {
            return 0;
        }

        return 0xff000000 | color;
    }

    private int tryReadHexedColor(String rawColor) {
        if (rawColor.length() == 3) {
            char r = rawColor.charAt(0);
            char g = rawColor.charAt(1);
            char b = rawColor.charAt(2);

            rawColor = "" + r + r + g + g + b + b;
        }

        return Color.parseColor("#" + rawColor);
    }

}
