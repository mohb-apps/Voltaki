/*
 *  Copyright (c) 2016 mohb apps - All Rights Reserved
 *
 *  Project       : Voltaki
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : ListsSavedState.java
 *  Last modified : 7/11/16 9:15 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.voltaki.lists;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import com.apps.mohb.voltaki.Constants;


// This class manages the lists saved states

public class ListsSavedState {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public ListsSavedState(Context context) {
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
        editor = preferences.edit();
    }

    // save bookmarks list on memory through a json string
    public void setBookmarksState(ArrayList<LocationItem> bookmarks) throws IOException {
        String jsonBookmarks = writeJsonString(bookmarks);
        editor.putString(Constants.BOOKMARKS, jsonBookmarks);
        editor.commit();
    }

    // get bookmarks list from memory through a json string
    // if list was not saved yet creates a new array list
    public ArrayList<LocationItem> getBookmarksState() throws IOException {
        String jsonBookmarks = preferences.getString(Constants.BOOKMARKS, null);
        if (jsonBookmarks == null) {
            return new ArrayList<>();
        } else {
            return readJsonString(jsonBookmarks);
        }
    }

    // save history list on memory through a json string
    public void setHistoryState(ArrayList<LocationItem> history) throws IOException {
        String jsonHistory = writeJsonString(history);
        editor.putString(Constants.HISTORY, jsonHistory);
        editor.commit();
    }

    // get history list from memory through a json string
    // if list was not saved yet creates a new array list
    public ArrayList<LocationItem> getHistoryState() throws IOException {
        String jsonHistory = preferences.getString(Constants.HISTORY, null);
        if (jsonHistory == null) {
            return new ArrayList<>();
        } else {
            return readJsonString(jsonHistory);
        }
    }

    // create a json string of a list of location items
    public String writeJsonString(ArrayList<LocationItem> locationItems) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent("  ");
        writeLocationsArrayList(jsonWriter, locationItems);
        jsonWriter.close();
        return stringWriter.toString();
    }

    // write all locations to json string
    public void writeLocationsArrayList(JsonWriter writer, ArrayList<LocationItem> locationItems) throws IOException {
        writer.beginArray();
        for (LocationItem locationItem : locationItems) {
            writeLocationItem(writer, locationItem);
        }
        writer.endArray();
    }

    // write a single location to json string
    public void writeLocationItem(JsonWriter writer, LocationItem locationItem) throws IOException {
        writer.beginObject();
        writer.name(Constants.JSON_NAME).value(locationItem.getLocationName());
        writer.name(Constants.JSON_ADDRESS).value(locationItem.getLocationAddress());
        writer.name(Constants.JSON_LATITUDE).value(locationItem.getLocationLatitude());
        writer.name(Constants.JSON_LONGITUDE).value(locationItem.getLocationLongitude());
        writer.endObject();
    }

    // read a json string containing a list of location items
    public ArrayList<LocationItem> readJsonString(String jsonString) throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        try {
            return readLocationsArrayList(jsonReader);
        } finally {
            jsonReader.close();
        }
    }

    // read a list of location items from a json string
    public ArrayList<LocationItem> readLocationsArrayList(JsonReader jsonReader) throws IOException {
        ArrayList<LocationItem> locationItems = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            locationItems.add(readLocationItem(jsonReader));
        }
        jsonReader.endArray();
        return locationItems;
    }

    // read a single location item from a json string
    public LocationItem readLocationItem(JsonReader jsonReader) throws IOException {
        String locationName = "";
        String locationAddress = "";
        Double locationLatitude = Constants.DEFAULT_LATITUDE;
        Double locationLongitude = Constants.DEFAULT_LONGITUDE;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case Constants.JSON_NAME:
                    locationName = jsonReader.nextString();
                    break;
                case Constants.JSON_ADDRESS:
                    locationAddress = jsonReader.nextString();
                    break;
                case Constants.JSON_LATITUDE:
                    locationLatitude = jsonReader.nextDouble();
                    break;
                case Constants.JSON_LONGITUDE:
                    locationLongitude = jsonReader.nextDouble();
                    break;
                default:
                    jsonReader.skipValue();
            }

        }
        jsonReader.endObject();
        return new LocationItem(locationName, locationAddress, locationLatitude, locationLongitude);
    }

}
