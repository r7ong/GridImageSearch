package com.rtong.gridimagesearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    // new ImageResult(..raw item json)
    public ImageResult(JSONObject json){
        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e){

        }

    }

    // ImageResult.fromJSONArray([..., ...]), take an array of json images and return arraylist of iamge results
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for(int i = 0; i < array.length(); i++){
            try{
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e){

            }
        }
        return results;

    }
}
