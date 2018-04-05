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
import android.widget.Button;
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
import java.lang.reflect.Array;
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
    private Button donateBtn;
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
        donateBtn = (Button) v.findViewById(R.id.donation);

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

                    Log.d("TEST RESPONSE", "Enter was pressed");

                    searchStr = searchEditText.getText().toString();
                    Log.d(TAG, "................. search word " + searchStr);

                    ArrayList<ResourceClass> searchResult = SearchResource(searchStr);
                    Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);

                    intent.putExtra("SEARCH_RESULT",(Serializable)searchResult);
                    intent.putExtra("SEARCHED_STRING", searchStr);

                    startActivity(intent);
                }
                    return true;

            }
        });

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url= "www.ieautism.org/make-a-donation";
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
            }
        });

        return v;
    }

    private ArrayList<ResourceClass> SearchResource(String searchKeyword) {
        ArrayList<ResourceClass> result = new ArrayList<ResourceClass>();
        ArrayList<ResourceClass> resourceList = Singleton.get(getActivity().getApplicationContext()).GetResource();

        // ################### Latest Addition ##################################
        if (searchKeyword.toLowerCase().equals("doctor")) {
            searchKeyword = "dr";
        }
        // search by name
        for (int i=0; i<resourceList.size(); ++i) {
            if (resourceList.get(i).GetResourceName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                result.add(resourceList.get(i));
            }
        }

        // search by subcategory
        if (isSubcategoryName(searchKeyword) && ! searchKeyword.equals("dr")) {
            CategoryClass subcat = GetSubcat (searchKeyword);
            if (subcat != null) {
                int mainCategoryIndex = GetMainCatIndex(subcat.getParentId());

                result = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(subcat.getId());
                result.remove(0);
            }
        }
        // ######################################################################

        return result;
    }

    boolean isSubcategoryName(String searchKeyword) {
        ArrayList<CategoryClass> subcat = Singleton.get(getActivity().getApplicationContext()).GetSubCategory();
        for (int i=0; i<subcat.size(); ++i) {
            if (subcat.get(i).getCategoryName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // return sub category index
    private CategoryClass GetSubcat (String searchKeyword) {
        ArrayList<CategoryClass> subcat = Singleton.get(getActivity().getApplicationContext()).GetSubCategory();
        for (int i=0; i<subcat.size(); ++i) {
            if (subcat.get(i).getCategoryName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                return subcat.get(i);
            }
        }
        return null;
    }

    // return main category index
    private int GetMainCatIndex(String maincatid) {
        ArrayList<CategoryClass> mainCat = Singleton.get(getActivity().getApplicationContext()).GetCategory();
        for (int i=0; i<mainCat.size(); ++i) {
            if (maincatid.equals(mainCat.get(i).getId())) {
                return i;
            }
        }
        return 0;
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
