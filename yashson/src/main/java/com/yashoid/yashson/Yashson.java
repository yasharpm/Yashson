package com.yashoid.yashson;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.yashoid.yashson.datareader.ObjectDataReader;
import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.jsonreader.JsonReaderDataReader;
import com.yashoid.yashson.fieldprovider.FieldProvider;
import com.yashoid.yashson.fieldprovider.FieldWrapper;
import com.yashoid.yashson.serializer.ArraySerializer;
import com.yashoid.yashson.serializer.BooleanSerializer;
import com.yashoid.yashson.serializer.CollectionSerializer;
import com.yashoid.yashson.serializer.MapSerializer;
import com.yashoid.yashson.serializer.NumberSerializer;
import com.yashoid.yashson.serializer.Serializer;
import com.yashoid.yashson.serializer.StringSerializer;
import com.yashoid.yashson.valueparser.ArrayValueParser;
import com.yashoid.yashson.valueparser.BooleanValueParser;
import com.yashoid.yashson.valueparser.ByteValueParser;
import com.yashoid.yashson.valueparser.CharacterValueParser;
import com.yashoid.yashson.valueparser.DoubleValueParser;
import com.yashoid.yashson.valueparser.FloatValueParser;
import com.yashoid.yashson.valueparser.HashMapValueParser;
import com.yashoid.yashson.valueparser.IntegerValueParser;
import com.yashoid.yashson.valueparser.ArrayListValueParser;
import com.yashoid.yashson.valueparser.LongValueParser;
import com.yashoid.yashson.valueparser.StringValueParser;
import com.yashoid.yashson.valueparser.ValueParser;
import com.yashoid.yashson.valueparser.YashsonValueParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yashar on 11/15/2017.
 *
 * <p>Use <code>Parse</code> methods to deserialize objects.</p>
 *
 * <p>Use <code>toJson</code> methods to serialize objects.</p>
 *
 * <p>Use <code>@Include</code> and <code>@Exclude</code> on class fields to explicitly determine which fields are include in serialization and deserialization.</p>
 *
 * <p>Call <code>setIncludeFieldsByDefault()</code> to specify the default behaviour of field inclusion.</p>
 *
 * <p>Use <code>@ParseName</code> on class fields to explicitly specify the serialization name. Add <code>exactMatch=true</code>
 * if you want to disable the field name matching guess mechanism.</p>
 *
 * <p>If you want to manually deserialize a type use <code>addValueParser()</code>.</p>
 *
 * <p>If you want to manually serialize a type use <code>addSerializer()</code>. The provided class must be the same class of the serializing object.</p>
 *
 * <p>If your class does not have a default constructor or you want to specifically customize deserialization of a class field,
 * add an <i>static final</i> subclass of <code>InstanceCreator</code> to your class.</p>
 *
 * <p>If you don't want a mapping for a JSON field but need the value, implement <code>JsonParsable</code> on your class.</p>
 *
 * <p>If you want a different name for serializing a class field or want to customize the serialization on a class field, implement
 * <code>JsonSerializable</code> on that class.</p>
 */

public class Yashson {

    public static final String TAG = "Yashson";

    private static final HashMap<Class, ValueParser> sValueParsers = new HashMap<>();
    private static final HashMap<Class, Serializer> sSerializers = new HashMap<>();

    static {
        sValueParsers.put(Boolean.TYPE, new BooleanValueParser());
        sValueParsers.put(Boolean.class, new BooleanValueParser());
        sValueParsers.put(Byte.TYPE, new ByteValueParser());
        sValueParsers.put(Byte.class, new ByteValueParser());
        sValueParsers.put(Character.TYPE, new CharacterValueParser());
        sValueParsers.put(Character.class, new CharacterValueParser());
        sValueParsers.put(Double.TYPE, new DoubleValueParser());
        sValueParsers.put(Double.class, new DoubleValueParser());
        sValueParsers.put(Float.TYPE, new FloatValueParser());
        sValueParsers.put(Float.class, new FloatValueParser());
        sValueParsers.put(Integer.TYPE, new IntegerValueParser());
        sValueParsers.put(Integer.class, new IntegerValueParser());
        sValueParsers.put(Long.TYPE, new LongValueParser());
        sValueParsers.put(Long.class, new LongValueParser());
        sValueParsers.put(String.class, new StringValueParser());
        sValueParsers.put(CharSequence.class, new StringValueParser());

        sSerializers.put(Boolean.TYPE, new BooleanSerializer());
        sSerializers.put(Boolean.class, new BooleanSerializer());
        sSerializers.put(Byte.TYPE, new NumberSerializer());
        sSerializers.put(Byte.class, new NumberSerializer());
        sSerializers.put(Character.TYPE, new StringSerializer());
        sSerializers.put(Character.class, new StringSerializer());
        sSerializers.put(Double.TYPE, new NumberSerializer());
        sSerializers.put(Double.class, new NumberSerializer());
        sSerializers.put(Float.TYPE, new NumberSerializer());
        sSerializers.put(Float.class, new NumberSerializer());
        sSerializers.put(Integer.TYPE, new NumberSerializer());
        sSerializers.put(Integer.class, new NumberSerializer());
        sSerializers.put(Long.TYPE, new NumberSerializer());
        sSerializers.put(Long.class, new NumberSerializer());
        sSerializers.put(String.class, new StringSerializer());
        sSerializers.put(CharSequence.class, new StringSerializer());
    }

    private Context mContext;

    private boolean mIncludeFieldsByDefault = true;

    private HashMap<Class, ValueParser> mValueParsers = new HashMap<>();
    private HashMap<Class, Serializer> mSerializers = new HashMap<>();

    private HashMap<Class, FieldProvider> mFieldProviders = new HashMap<>();
    private HashMap<Class, InstanceCreator> mInstanceCreators = new HashMap<>();

    public Yashson(Context context) {
        mContext = context == null ? null : context.getApplicationContext();
    }

    public Yashson() {
        this(null);
    }

    public void addValueParser(Class type, ValueParser valueParser) {
        mValueParsers.put(type, valueParser);
    }

    public void addSerializer(Class type, Serializer serializer) {
        mSerializers.put(type, serializer);
    }

    public void setIncludeFieldsByDefault(boolean includeByDefault) {
        mIncludeFieldsByDefault = includeByDefault;
    }

    public<T> List<T> parseList(JSONArray json, Class<T> clazz, Class... subTypes) throws IOException {
        return parseList(json.toString(), clazz, subTypes);
    }

    public<T> List<T> parseList(String json, Class<T> clazz, Class... subTypes) throws IOException {
        return parseList(new JsonReader(new StringReader(json)), clazz, subTypes);
    }

    public<T> List<T> parseList(JsonReader reader, Class<T> clazz, Class... subTypes) throws IOException {
        return parseList(new JsonReaderDataReader(reader), clazz, subTypes);
    }

    public<T> List<T> parseList(DataReader reader, Class<T> clazz, Class... subTypes) throws IOException {
        ArrayListValueParser valueParser = new ArrayListValueParser(getValueParser(clazz, subTypes));

        return (List<T>) valueParser.parseValue(reader);
    }

    public<T> T parse(JSONObject json, Class<T> clazz, Class... subTypes) throws IOException {
        return parse(json.toString(), clazz, subTypes);
    }

    public<T> T parse(String json, Class<T> clazz, Class... subTypes) throws IOException {
        return parse(new JsonReader(new StringReader(json)), clazz, subTypes);
    }

    public<T> T parse(JsonReader reader, Class<T> clazz, Class... subTypes) throws IOException {
        return parse(new JsonReaderDataReader(reader), clazz, subTypes);
    }

    public<T> T parse(DataReader dataReader, Class<T> clazz, Class... subTypes) throws IOException {
        if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
            return (T) getValueParser(clazz, subTypes).parseValue(dataReader);
        }

        ObjectDataReader objectDataReader = dataReader.asObjectReader();

        FieldProvider fieldProvider = getFieldProvider(clazz);

        T object = makeInstance(clazz);

        while (objectDataReader.hasNextField()) {
            DataReader fieldReader = objectDataReader.nextField();

            String parsedFieldName = fieldReader.getFieldName();

            FieldWrapper fieldWrapper = fieldProvider.getTargetedField(parsedFieldName, subTypes);

            if (fieldWrapper == null) {
                if (JsonParsable.class.isInstance(object)) {
                    ((JsonParsable) object).onUnidentifiedParsedFieldName(parsedFieldName, fieldReader, mContext);
                }

                fieldReader.onReadingFinished();

                continue;
            }

            if (fieldReader.isNull()) {
                fieldReader.onReadingFinished();

                fieldWrapper.setValue(object, null);

                continue;
            }

            ValueParser valueParser = getValueParser(fieldWrapper, clazz, parsedFieldName, fieldWrapper.getName());

            Object value = valueParser.parseValue(fieldReader);

            fieldReader.onReadingFinished();

            fieldWrapper.setValue(object, value);
        }

        return object;
    }

    private FieldProvider getFieldProvider(Class<?> clazz) {
        FieldProvider fieldProvider = mFieldProviders.get(clazz);

        if (fieldProvider == null) {
            fieldProvider = new FieldProvider(clazz, mIncludeFieldsByDefault);

            mFieldProviders.put(clazz, fieldProvider);
        }

        return fieldProvider;
    }

    private<T> T makeInstance(Class<T> clazz) {
        return acquireInstanceCreator(clazz).newInstance(mContext);
    }

    private<T> InstanceCreator<T> acquireInstanceCreator(Class<T> clazz) {
        InstanceCreator<T> creator = mInstanceCreators.get(clazz);

        if (creator == null) {
            creator = makeInstanceCreator(clazz);

            mInstanceCreators.put(clazz, creator);
        }

        return creator;
    }

    private<T> InstanceCreator<T> makeInstanceCreator(Class<T> clazz) {
        Field[] fields = clazz.getFields();

        for (Field field: fields) {
            try {
                Class fieldType = field.getType();

                if (InstanceCreator.class.isAssignableFrom(fieldType)) {
                    Method method = fieldType.getMethod("newInstance", Context.class);

                    if (clazz.isAssignableFrom(method.getReturnType())) {
                        return (InstanceCreator<T>) field.get(null);
                    }
                }
            } catch (IllegalAccessException e) {
                // Won't really happen.
            } catch (NoSuchMethodException e) {
                // No problem.
            } catch (NullPointerException e) {
                throw new RuntimeException("Declared instance of '" + InstanceCreator.class.getSimpleName() + "' on class '" + clazz.getName() + "' must be static.", e);
            }
        }

        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();

            return new DefaultInstanceCreator<>(constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No instances of '" + InstanceCreator.class.getSimpleName() + "' or an empty default constructor found for '" + clazz.getName() + "'.", e);
        } catch (SecurityException e) {
            throw new RuntimeException("Default constructor for '" + clazz.getName() + "' is not accessible. Alternatively define a final static instance of '" + InstanceCreator.class.getSimpleName() + "' in your class.", e);
        }
    }

    private ValueParser getValueParser(FieldWrapper fieldWrapper, Class targetClass, String parsedFieldName, String targetedFieldName) {
        InstanceCreator instanceCreator = acquireInstanceCreator(targetClass);

        if (instanceCreator != null) {
            ValueParser valueParser = instanceCreator.getValueParser(parsedFieldName, targetedFieldName, mContext);

            if (valueParser != null) {
                return valueParser;
            }
        }

        return getValueParser(fieldWrapper);
    }

    private ValueParser getValueParser(FieldWrapper fieldWrapper) {
        return getValueParser(fieldWrapper.getType(), fieldWrapper.getSubTypes());
    }

    public ValueParser getValueParser(Class type, Class... subTypes) {
        ValueParser valueParser = mValueParsers.get(type);

        if (valueParser != null) {
            return valueParser;
        }

        valueParser = sValueParsers.get(type);

        if (valueParser != null) {
            return valueParser;
        }

        if (type.isArray()) {
            Class subType = type.getComponentType();

            return new ArrayValueParser(subType, getValueParser(subType));
        }

        if (Collection.class.isAssignableFrom(type)) {
            Class subType = subTypes[0];

            ValueParser listParser = new ArrayListValueParser(getValueParser(subType));

            return listParser;
        }

        if (Map.class.isAssignableFrom(type)) {
            if (!subTypes[0].isAssignableFrom(String.class)) {
                throw new RuntimeException("Parsing instances of Map requires the key parameter to be defined as String.");
            }

            Class subType = subTypes[1];

            ValueParser mapParser = new HashMapValueParser(getValueParser(subType));

            return mapParser;
        }

        return new YashsonValueParser(this, type, subTypes);
    }





    public String toJson(Object object) {
        StringWriter writer = new StringWriter();

        try {
            toJson(object, writer);
        } catch (IOException e) { }

        return writer.toString();
    }

    public void toJson(Object object, OutputStream output) throws IOException {
        toJson(object, new OutputStreamWriter(output));
    }

    public void toJson(Object object, Writer writer) throws IOException {
        if (object == null) {
            writer.write("null");
            return;
        }

        Serializer serializer = mSerializers.get(object.getClass());

        if (serializer != null) {
            serializer.serialize(object, writer);
            return;
        }

        serializer = sSerializers.get(object.getClass());

        if (serializer != null) {
            serializer.serialize(object, writer);
            return;
        }

        if (object instanceof Collection) {
            new CollectionSerializer(this).serialize(object, writer);
            return;
        }

        if (object instanceof Map) {
            new MapSerializer(this).serialize(object, writer);
            return;
        }

        if (object.getClass().isArray()) {
            new ArraySerializer(this).serialize(object, writer);
            return;
        }

        FieldProvider fieldProvider = getFieldProvider(object.getClass());

        JsonSerializable serializableObject = (object instanceof JsonSerializable) ? (JsonSerializable) object : null;

        List<FieldProvider.ParsedField> fields = fieldProvider.getFields();

        writer.write("{");

        for (int index = 0; index < fields.size(); index++) {
            FieldProvider.ParsedField field = fields.get(index);

            String name = null;
            String fieldName = field.getSerializationName();

            if (serializableObject != null) {
                name = serializableObject.getSerializedNameForField(fieldName);
            }

            if (name == null) {
                name = fieldName;
            }

            writer.write("\"");
            writer.write(name);
            writer.write("\"");

            writer.write(":");

            if (serializableObject == null || !serializableObject.serializeField(name, this, writer, mContext)) {
                try {
                    Field realField = field.getField();

                    boolean wasAccessible = realField.isAccessible();

                    realField.setAccessible(true);

                    toJson(realField.get(object), writer);

                    realField.setAccessible(wasAccessible);
                } catch (IllegalAccessException e) {
                    Log.wtf(TAG, "IllegalAccessException where it must not happen.", e);

                    toJson(null, writer);
                }
            }

            if (index < fields.size() - 1) {
                writer.write(",");
            }
        }

        writer.write("}");
    }

}
