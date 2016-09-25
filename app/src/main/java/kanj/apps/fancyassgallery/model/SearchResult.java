package kanj.apps.fancyassgallery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by voldemort on 25/9/16.
 */

public class SearchResult {
    public String Result;
    public String Error;
    public List<Item> Search;

    private class Item {
        public String Poster;
    }

    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        for (Item i: Search) {
            urls.add(i.Poster);
        }
        return urls;
    }
}
