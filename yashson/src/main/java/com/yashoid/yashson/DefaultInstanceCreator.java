package com.yashoid.yashson;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Yashar on 11/15/2017.
 */

public class DefaultInstanceCreator<T> extends InstanceCreator<T> {

    private Constructor<T> mConstructor;

    protected DefaultInstanceCreator(Constructor<T> constructor) {
        mConstructor = constructor;
    }

    @Override
    public T newInstance(Context context) {
        try {
            return mConstructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("An instance of an abstract class can not be created.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Default constructor is unexpectedly inaccessible.", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Creation a new instance of declared default constructor failed.", e);
        }
    }

}
