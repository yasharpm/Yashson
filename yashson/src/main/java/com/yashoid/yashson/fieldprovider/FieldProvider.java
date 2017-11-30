package com.yashoid.yashson.fieldprovider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yashar on 11/15/2017.
 */

public class FieldProvider {

    private Class mClass;

    private boolean mIncludeByDefault = true;

    private ArrayList<ParsedField> mFields = new ArrayList<>();

    public FieldProvider(Class clazz, boolean includeByDefault) {
        mClass = clazz;

        setIncludeByDefault(includeByDefault);

        addFields(clazz);
    }

    public void setIncludeByDefault(boolean includeByDefault) {
        mIncludeByDefault = includeByDefault;
    }

    private void addFields(Class clazz) {
        if (clazz.equals(Object.class)) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {
            int mod = field.getModifiers();

            if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                continue;
            }

            Annotation[] annotations = field.getAnnotations();

            boolean forceInclude = false;
            boolean forceExclude = false;

            String parseName = null;
            boolean exactMatch = false;

            for (Annotation annotation: annotations) {
                if (annotation.annotationType().equals(Include.class)) {
                    forceInclude = true;
                }

                if (annotation.annotationType().equals(ParseName.class)) {
                    parseName = ((ParseName) annotation).name();
                    exactMatch = ((ParseName) annotation).exactMatch();
                }

                if (annotation.annotationType().equals(Exclude.class)) {
                    forceExclude = true;
                    break;
                }
            }

            if (forceExclude || (!mIncludeByDefault && !forceInclude)) {
                continue;
            }

            mFields.add(new ParsedField(field, parseName, exactMatch));
        }

        addFields(clazz.getSuperclass());
    }

    public FieldWrapper getTargetedField(String fieldName, Class... subTypes) {
        ParsedField fieldNamePair = findCandidField(fieldName);

        if (fieldNamePair == null) {
            return null;
        }

        Field field = fieldNamePair.field;

        Type type = field.getGenericType();

        if (!(type instanceof ParameterizedType)) {
            return new FieldWrapper(field, null);
        }

        ParameterizedType fieldType = (ParameterizedType) type;

        Type[] typeArguments = fieldType.getActualTypeArguments();
        Class[] fieldSubTypes = new Class[typeArguments.length];

        TypeVariable[] classTypeVariables = mClass.getTypeParameters();

        for (int i = 0; i < fieldSubTypes.length; i++) {
            Type subType = typeArguments[i];

            if (subType instanceof Class) {
                fieldSubTypes[i] = (Class) subType;

                continue;
            }

            String typeVariableName = ((TypeVariable) subType).getName();
            Class subTypeClass = findIndexOfTypeParameterForName(subTypes, classTypeVariables, typeVariableName);

            if (subTypeClass != null) {
                fieldSubTypes[i] = subTypeClass;

                continue;
            }

            throw new RuntimeException("SubType Class is required for declared generic parameter '" + typeVariableName + "' in class '" + mClass.getName() + "'.");
        }

        return new FieldWrapper(field, null, fieldSubTypes);
    }

    private Class findIndexOfTypeParameterForName(Class[] subTypes, TypeVariable[] typeVariables, String parameterName) {
        if (subTypes.length != typeVariables.length) {
            throw new RuntimeException("Provided sub type classes for class '" + mClass.getName() + "' must be " + typeVariables.length + ".");
        }

        for (int i = 0; i <typeVariables.length; i++) {
            if (typeVariables[i].getName().equals(parameterName)) {
                return subTypes[i];
            }
        }

        return null;
    }

    private ParsedField findCandidField(String parsedName) {
        ParsedField matchedPair = null;
        int bestMatchLevel = ParsedField.NOT_MATCHED;

        for (ParsedField pair: mFields) {
            int matchLevel = pair.compare(parsedName);

            if (matchLevel == ParsedField.EXACT_MATCH) {
                return pair;
            }

            if (matchLevel > bestMatchLevel) {
                bestMatchLevel = matchLevel;
                matchedPair = pair;
            }
        }

        return matchedPair;
    }

    public List<ParsedField> getFields() {
        return mFields;
    }

    public static class ParsedField {

        private static final int NOT_MATCHED = -1;
        private static final int MATCHED = 0;
        private static final int WELL_MATCHED = 1;
        private static final int EXACT_MATCH = 2;

        private Field field;
        private String name = null;
        private boolean exactMatch;

        private ParsedField(Field field, String name, boolean exactMatch) {
            this.field = field;
            this.name = name == null ? null : (name.trim().length() == 0 ? null : name.trim());
            this.exactMatch = exactMatch;
        }

        private int compare(String parsedName) {
            if (name != null && name.equals(parsedName)) {
                return exactMatch ? EXACT_MATCH : WELL_MATCHED;
            }
            else if (field.getName().equals(parsedName)) {
                return exactMatch ? EXACT_MATCH : WELL_MATCHED;
            }
            else if (exactMatch) {
                return NOT_MATCHED;
            }

            String compareName = name != null ? name : field.getName();

            return matches(compareName, parsedName) ? MATCHED : NOT_MATCHED;
        }

        public String getSerializationName() {
            if (name != null) {
                return name;
            }

            if (isCamel(field.getName())) {
                return toLowerCase(removeCamel(true, field.getName()), 0, 1);
            }

            return field.getName();
        }

        public Field getField() {
            return field;
        }

        private static boolean matches(String compareName, String parsedName) {
            String sCompareName = simplify(compareName);
            String sParsedName = simplify(parsedName);

            if (sCompareName.equals(sParsedName)) {
                return true;
            }

            boolean compareNameIsCamel = isCamel(compareName);
            boolean parsedNameIsCamel = isCamel(parsedName);

            return removeCamel(compareNameIsCamel, sCompareName).equals(removeCamel(parsedNameIsCamel, sParsedName));
        }

        private static String removeCamel(boolean isCamel, String s) {
            return isCamel ? s.substring(1) : s;
        }

        private static String simplify(String s) {
            return s.replaceAll("\\W", "").toLowerCase(Locale.US);
        }

        private static boolean isCamel(String s) {
            return s.length() >= 2 && s.substring(0, 1).matches("[a-z_0-9]") && s.substring(1, 2).matches("[A-Z]");
        }

        private static String toLowerCase(String s, int start, int end) {
            return s.substring(0, start) + s.substring(start, end).toLowerCase(Locale.US) + s.substring(end);
        }

    }

}
