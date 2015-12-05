package ghanshyamguides.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<PhotoItem> photoItemsList;
    private PhotoItemAdapter photoItemAdapter;
    public static final String CLIENT_ID = "b008f314e4004692b103cc1a1f98f4bf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photoItemsList = new ArrayList<PhotoItem>();
        // Create Adapter and bind it to photos list
        photoItemAdapter = new PhotoItemAdapter(this, photoItemsList);

        // get the listview from layout
        ListView lvphotos = (ListView) findViewById(R.id.lvPhotos);

        // Bind adapter to the listview
        lvphotos.setAdapter(photoItemAdapter);

        //https://api.instagram.com/v1/media/popular?client_id=b008f314e4004692b103cc1a1f98f4bf
        //json data->[x]->images->standard resolution->url
        // setup url endpoint
        String popularurl= "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger network request
        client.get(popularurl, new JsonHttpResponseHandler() {
            // response is json object for photos
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                //url, height, username, caption
                JSONArray photosJSON = null;

                try {
                    //Clear the list first
                    photoItemsList.clear();
                    photosJSON = response.getJSONArray("data");

                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                   
                        if (photoJSON.optJSONObject("images") != null) {
                            PhotoItem iPhoto = new PhotoItem();

                            // ALso check for standard_resolution
                            if (photoJSON.getJSONObject("images").optJSONObject("standard_resolution") != null) {
                                iPhoto.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            }

                            if (photoJSON.optJSONObject("user") != null) {
                                iPhoto.imageUserName = photoJSON.getJSONObject("user").getString("username");
                            }

                            if (photoJSON.optJSONObject("caption") != null) {
                                iPhoto.imageCaption = photoJSON.getJSONObject("caption").getString("text");
                            }

                            // add photo to the array
                            photoItemsList.add(iPhoto);
                        }
                    }

                    // Update adapter
                    photoItemAdapter.notifyDataSetChanged();

                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
    }
}
