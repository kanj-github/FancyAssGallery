package kanj.apps.fancyassgallery.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kanj.apps.fancyassgallery.R;
import kanj.apps.fancyassgallery.model.SearchResult;
import kanj.apps.fancyassgallery.network.VolleyInstance;

public class SearchMovies extends Fragment {
    private static final String URL = "http://www.omdbapi.com/?r=json&s=";
    private EditText etSearch;
    private RecyclerView listView;
    private FancyAssListAdapter mAdapter;

    public SearchMovies() {
        // Required empty public constructor
    }

    public static SearchMovies newInstance(String param1, String param2) {
        SearchMovies fragment = new SearchMovies();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_movies, container, false);
        etSearch = (EditText) v.findViewById(R.id.et_search);
        v.findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
            }
        });
        listView = (RecyclerView) v.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        return v;
    }

    private void doSearch() {
        String text = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(text) && text.length() >= 2) {
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.GET,
                    URL + text,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                SearchResult result = new Gson().fromJson(response.toString(), SearchResult.class);
                                displayList(result);
                            } catch (JsonSyntaxException jsone) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            VolleyInstance.getInstance(getContext()).addToRequestQueue(req);
        } else {
            // Ask user to type properly
        }
    }

    private void displayList(SearchResult result) {
        if (result.Search == null || result.Search.size() == 0) {
            if (result.Error != null && getContext() != null) {
                Toast.makeText(getContext(), result.Error, Toast.LENGTH_SHORT).show();
            }
            listView.setAdapter(null);
        } else if (getContext() != null) {
            ArrayList<String> urls = result.getUrls();
            mAdapter = new FancyAssListAdapter(getContext(), urls);
            listView.setAdapter(mAdapter);
        }
    }
}
