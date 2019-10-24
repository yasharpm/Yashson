package com.yashoid.yashson.fieldprovider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ParsedType {

    private Class mClass;
    private ParsedType[] mSubTypes;

    public ParsedType(Type type, Class enclosingClass, ParsedType... subTypes) {
        if (!(type instanceof ParameterizedType)) {
            mClass = (Class) type;
            mSubTypes = null;
            return;
        }

        ParameterizedType fieldType = (ParameterizedType) type;

        Type[] typeArguments = fieldType.getActualTypeArguments();
        ParsedType[] fieldSubTypes = new ParsedType[typeArguments.length];

        TypeVariable[] classTypeVariables = enclosingClass.getTypeParameters();

        for (int i = 0; i < fieldSubTypes.length; i++) {
            Type subType = typeArguments[i];

            if (subType instanceof TypeVariable) {
                String typeVariableName = ((TypeVariable) subType).getName();
                ParsedType subTypeClass = findTypeOfTypeParameterForName(subTypes, classTypeVariables, typeVariableName);

                if (subTypeClass != null) {
                    fieldSubTypes[i] = subTypeClass;
                    continue;
                }

                throw new RuntimeException("SubType Class is required for declared generic parameter '" + typeVariableName + "' in class '" + mClass.getName() + "'.");
            }
            else {
                fieldSubTypes[i] = new ParsedType(subType, (Class) fieldType.getRawType());
            }
        }

        mClass = (Class) fieldType.getRawType();
        mSubTypes = fieldSubTypes;
    }

    private ParsedType findTypeOfTypeParameterForName(ParsedType[] subTypes, TypeVariable[] typeVariables, String parameterName) {
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

    public Class getType() {
        return mClass;
    }

    public ParsedType[] getSubTypes() {
        return mSubTypes;
    }

}
