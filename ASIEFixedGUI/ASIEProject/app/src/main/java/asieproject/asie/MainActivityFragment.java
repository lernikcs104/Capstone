package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.View.ListArrayAdapter;
import asieproject.asie.Model.VolleyCallback;

import static android.content.ContentValues.TAG;


public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    database db;
    VolleyCallback callback;
    private ListView listView;
    List<CategoryClass> categoryRow;
    ListArrayAdapter adapter;
    private String rowItemString;
    private static final String categoryURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/categories";
    private static final String categoryToResourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";
    private static final String resourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";

    BottomNavigationView bottomNavigationView;
    private EditText searchEditText;


    public MainActivityFragment() {}

    public static MainActivityFragment newInstance() {
        Bundle bundle = new Bundle();
        MainActivityFragment f = new MainActivityFragment();
        f.setArguments(bundle);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Log.d(TAG, "in onCreateView");
        View v = inflater.inflate(R.layout.main_activity_fragment, container, false);

        listView = (ListView) v.findViewById((R.id.list));
        searchEditText = (EditText) v.findViewById(R.id.myEditText);
        //searchEditText.addTextChangedListener(searchTextWatcher);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) v.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // instantiating singleton
        Singleton.get(getActivity().getApplicationContext()).Instantiate();

        // instantiate Volley callback
        callback = new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<CategoryClass> result) {
                populateList();
            }

            @Override
            public void onSuccess(Map<String, String> result) { }

            @Override
            public void onSuccessResource(ArrayList<ResourceClass> result) {}
        };

        if (! Singleton.get(getActivity().getApplicationContext()).IsCategorySet() &&
                ! Singleton.get(getActivity().getApplicationContext()).IsResourceSet()){

            // retrieve categories from mongoDB
            db = new database(getActivity().getApplication(), callback, categoryURL, 1);
            Singleton.get(getActivity().getApplicationContext()).SetCategoryFlag();
            Log.d(TAG, "************************************** categories loaded");

            db = new database(getActivity().getApplication(), callback, categoryToResourceURL, 2);
            Singleton.get(getActivity().getApplicationContext()).SetCategoryToResourceFlag();

            // retrieve resources from mongoDB
            db = new database(getActivity().getApplication(), callback, resourceURL, 3);
            Singleton.get(getActivity().getApplicationContext()).SetResourceFlag();
            Log.d(TAG, "............. resources loaded");
        }

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchStr;
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    ////
                    Log.d("TEST RESPONSE", "Enter was pressed");

                    searchStr = searchEditText.getText().toString();
                    Log.d(TAG, "................. search word " + searchStr);

                    ArrayList<ResourceClass> searchResult = SearchResource(searchStr);
                    Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("SEARCH_RESULT", searchResult);
                    intent.putExtra("SEARCH_RESULT",(Serializable)searchResult);
                    //intent.putExtras(bundle);
                    startActivity(intent);

                    Log.d(TAG, "###################################");
                    for (int i = 0; i < searchResult.size(); ++i) {
                        Log.d(TAG, "------------------------------ name: " + searchResult.get(i).GetResourceName());
                    }
                    Log.d(TAG, "###################################");
                }
                //
                    return true;

            }
        });

        return v;
    }
/*
    private final TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            String searchStr;
//            if (count > 0) {
//                searchStr = searchEditText.getText().toString();
//                Log.d(TAG, "................. search word " + searchStr);
//
//                ArrayList<ResourceClass> searchResult = SearchResource(searchStr);
//                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("SEARCH_RESULT", searchResult);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//                Log.d(TAG, "###################################");
//                for (int i=0; i<searchResult.size(); ++i) {
//                    Log.d(TAG,"------------------------------ name: " + searchResult.get(i).GetResourceName());
//                }
//                Log.d(TAG, "###################################");
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String searchStr;

            if (s.charAt(s.length() - 1) == '\n') {
                Log.d("TEST RESPONSE", "Enter was pressed");

                searchStr = searchEditText.getText().toString();
                Log.d(TAG, "................. search word " + searchStr);

                ArrayList<ResourceClass> searchResult = SearchResource(searchStr);
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SEARCH_RESULT", searchResult);
                intent.putExtras(bundle);
                startActivity(intent);

                Log.d(TAG, "###################################");
                for (int i = 0; i < searchResult.size(); ++i) {
                    Log.d(TAG, "------------------------------ name: " + searchResult.get(i).GetResourceName());
                }
                Log.d(TAG, "###################################");
            }
        }

    };
    */

    private ArrayList<ResourceClass> SearchResource(String searchKeyword) {
        ArrayList<ResourceClass> result = new ArrayList<ResourceClass>();
        ArrayList<ResourceClass> resourceList = Singleton.get(getActivity().getApplicationContext()).GetResource();
        for (int i=0; i<resourceList.size(); ++i) {
            if (searchKeyword.toLowerCase().equals("doctor")) {
                searchKeyword = "dr";
            }
            if (resourceList.get(i).GetResourceName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                result.add(resourceList.get(i));
            }
        }
        return result;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.calendar:
                    String url= "www.ieautism.org/events/";
                    if(!url.contains("http://")){
                        url= "http://"+url;
                    }
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    try {
                        startActivity(myIntent);
                    } catch(Exception e){

                        Log.d(TAG, "website", e);
                        Toast toast = Toast.makeText(getActivity(),
                                "Item " + (2) + ": " + url,
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                    return true;
                case R.id.home:

                    return true;
                case R.id.info:
                    Intent intent = new Intent(getActivity().getApplicationContext(), InformationActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    // this function will be called when the user is back from the next activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "request code " + requestCode);
    }

    public void populateList() {
        ArrayList<CategoryClass> mainCategory = new ArrayList<CategoryClass>();
        mainCategory = Singleton.get(getActivity().getApplicationContext()).GetCategory();
        categoryRow = new ArrayList<CategoryClass>();

//      populate each row
        for (int i=0; i<mainCategory.size(); ++i) {
            categoryRow.add(mainCategory.get(i));
        }

        // populate the list view with category row
        adapter = new ListArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item, categoryRow);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        rowItemString = categoryRow.get(position).toString();

        Intent intent = new Intent(getActivity().getApplicationContext(), SubCategoryActivity.class);
        intent.putExtra(MainActivity.EXTRA_CATEGORY, position);
        getActivity().setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }
}
