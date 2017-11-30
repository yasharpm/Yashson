package com.yashoid.yashson.fieldprovider;

import android.util.Log;

import com.yashoid.yashson.Yashson;
import com.yashoid.yashson.valueparser.DoubleValueParser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Yashar on 11/15/2017.
 */

public class FieldWrapper {

    private Field mField;
    private Class mType;
    private Class[] mSubTypes;

    protected FieldWrapper(Field field, Class type, Class... subTypes) {
        mField = field;

        if (type == null) {
            mType = mField.getType();
        }
        else {
            mType = type;
        }

        mSubTypes = subTypes;
    }

    public String getName() {
        return mField.getName();
    }

    public Class getType() {
        return mType;
    }

    public Class getSubType() {
        return getSubType(0);
    }

    public Class getSubType(int index) {
        if (mSubTypes.length > 0) {
            return mSubTypes[index];
        }

        return (Class) ((ParameterizedType) mField.getGenericType()).getActualTypeArguments()[index];
    }

    public Class[] getSubTypes() {
        if (!(mField.getGenericType() instanceof ParameterizedType)) {
            return new Class[0];
        }

        if (mSubTypes.length > 0) {
            return mSubTypes;
        }

        Type[] subTypes = ((ParameterizedType) mField.getGenericType()).getActualTypeArguments();

        Class[] classes = new Class[subTypes.length];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = (Class) subTypes[i];
        }

        return classes;
    }

    public void setValue(Object instance, Object value) {
        if (value == null) {
            setRawValue(instance, null);
            return;
        }

        if (getType().isInstance(value) || (getType().isPrimitive() && getClassForPrimitiveType(getType()).equals(value.getClass()))) {
            setRawValue(instance, value);
            return;
        }

        if (value instanceof Collection) {
            value = prepareCollectionValue(value);
        }
        else if (value instanceof Map) {
            value = prepareMapValue(value);
        }
        else {
            Log.e(Yashson.TAG, "Unexpected condition while trying to assign value of class '" + value.getClass().getName() + "' to field name '" + mField.getName() + "'.");
        }

        setRawValue(instance, value);
    }

    private void setRawValue(Object instance, Object value) {
        boolean wasAccessible = mField.isAccessible();

        mField.setAccessible(true);

        try {
            mField.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can not assign value to a final variable '" + mField.getName() + "'.");
        }

        mField.setAccessible(wasAccessible);
    }

    private Object prepareCollectionValue(Object value) {
        if (getType().isInterface() || Modifier.isAbstract(getType().getModifiers())) {
            // This means we can not create a new instance of the target type. Hopefully the prepared value will be assignable.
            return value;
        }

        Collection collectionValue = (Collection) value;

        try {
            Collection target = (Collection) getType().newInstance();

            target.addAll(collectionValue);

            return target;
        } catch (InstantiationException e) {
            throw new RuntimeException("An empty constructor was not found. Make sure Collection subclass '" + getType().getName() + "' has a default constructor.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("An accessible empty constructor was not found. Make sure the default constructor of '" + getType().getName() + "' is accessible.", e);
        }
    }

    private Object prepareMapValue(Object value) {
        if (getType().isInterface() || Modifier.isAbstract(getType().getModifiers())) {
            // This means we can not create a new instance of the target type. Hopefully the prepared value will be assignable.
            return value;
        }

        Map mapValue = (Map) value;

        try {
            Map target = (Map) getType().newInstance();

            target.putAll(mapValue);

            return target;
        } catch (InstantiationException e) {
            throw new RuntimeException("An empty constructor was not found. Make sure Collection subclass '" + getType().getName() + "' has a default constructor.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("An accessible empty constructor was not found. Make sure the default constructor of '" + getType().getName() + "' is accessible.", e);
        }
    }

    private static Class getClassForPrimitiveType(Class clazz) {
        if (Boolean.TYPE.equals(clazz)) {
            return Boolean.class;
        }

        if (Byte.TYPE.equals(clazz)) {
            return Byte.class;
        }

        if (Character.TYPE.equals(clazz)) {
            return Character.class;
        }

        if (Double.TYPE.equals(clazz)) {
            return DoubleValueParser.class;
        }

        if (Float.TYPE.equals(clazz)) {
            return Float.class;
        }

        if (Integer.TYPE.equals(clazz)) {
            return Integer.class;
        }

        if (Long.TYPE.equals(clazz)) {
            return Long.class;
        }

        return null;
    }

}
