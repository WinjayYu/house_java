
package com.ryel.zaja.utils.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangbin on 2015/3/13.
 */
public  class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();

        if (rawType == String.class) {
            return (TypeAdapter<T>) new StringAdapter();
        }
        if(rawType == Integer.class){
            return (TypeAdapter<T>) new IntegerAdapter();
        }
        if(rawType == Boolean.class){
            return (TypeAdapter<T>) new BooleanAdapter();
        }
        if(rawType== Date.class){
            return (TypeAdapter<T>) new DateAdapter();
        }
        return null;
    }
}

class StringAdapter extends TypeAdapter<String> {
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return reader.nextString();
    }
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.value("");
            return;
        }
        writer.value(value);
    }
}


class IntegerAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        if (value == null) {
            writer.value(0);
            return;
        }
        writer.value(value);
    }

    @Override
    public Integer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return reader.nextInt();
    }
}

class BooleanAdapter    extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException {
        if (value == null) {
            writer.value("");
            return;
        }
        writer.value(value);
    }

    @Override
    public Boolean read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return reader.nextBoolean();
    }
}

class DateAdapter extends TypeAdapter<Date> {


    @Override
    public void write(JsonWriter writer, Date value) throws IOException {
        if (value == null) {
            writer.value("");
            return;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        writer.value(sdf.format(value));
    }

    @Override
    public Date read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return null;
    }
}
