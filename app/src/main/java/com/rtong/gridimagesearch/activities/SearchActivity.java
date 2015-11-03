package com.rtong.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rtong.gridimagesearch.EndlessScrollListener;
import com.rtong.gridimagesearch.adapters.ImageResultsAdapter;
import com.rtong.gridimagesearch.models.ImageResult;
import com.rtong.gridimagesearch.R;
import com.rtong.gridimagesearch.models.SearchOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private final int REQUEST_CODE = 90;
    private String size = "";
    private String color = "";
    private String type = "";
    private String site = "";
    private String query = "";

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
//        Toast.makeText(SearchActivity.this, "offset="+Integer.toString(offset), Toast.LENGTH_SHORT).show();
      // This method probably sends out a network request and appends new data items to your adapter.
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
        String searchUrl = buildSearchUrl();
        if(offset > 0){
            String start = Integer.toString(offset * 8);
            searchUrl = searchUrl + "&start=" + start;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //create the data source
        imageResults = new ArrayList<ImageResult>();
        // attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //link the adapter to the adapterview(gridview)
        gvResults.setAdapter(aImageResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch image display activity
                // create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // get the image result to display
                ImageResult result = imageResults.get(position);
                // pass image result to display
                i.putExtra("url", result.fullUrl);

                // launch the new activity
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchOptionActivity.class);
            SearchOptions options = new SearchOptions(size, color, type, site);
            intent.putExtra("options", options);

            //start request activity
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            size = data.getStringExtra("size");
            color = data.getStringExtra("color");
            type = data.getStringExtra("type");
            site = data.getStringExtra("site");
        }
    }

    public void onImageSearch(View view) {
        query = etQuery.getText().toString();
//        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = buildSearchUrl();
        Log.d("DEBUG", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*
                {
                    responseData: {
                        results:[
                            {
                                url:
                                title:
                                tbUrl:
                            },
                            {
                            }
                        ]
                }
                 */
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); // clear the existing images from the array in case where its a new search
                    // when you make change to the adapter, it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));// why not =
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String buildSearchUrl() {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        if(size.length() > 0){
            url += "&imgsz="+size;
        }
        if(color.length() > 0){
            url += "&imgcolor="+color;
        }
        if(type.length() > 0){
            url += "&imgtype="+type;
        }
        if(site.length() > 0){
            url += "&as_sitesearch="+site;
        }

        Log.d("Debug-url", url);
        return url;
    }
}
