package asieproject.asie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.View.ListArrayAdapter;
import asieproject.asie.Model.VolleyCallback;

import static android.content.ContentValues.TAG;

/**
 * Created by CACTUS on 1/25/2018.
 */

public class MainActivityFragment extends Fragment {

    database db;
    private ListView listView;
    List<CategoryClass> categoryRow;
    ListArrayAdapter adapter;
    private Vector<JSONObject> mJsonObjectVector = new Vector<JSONObject>();
    private Vector<CategoryClass> category = new Vector<CategoryClass>();

    public static final Integer[] mCategoryImages = { R.drawable.medicalrecords,
            R.drawable.healthcare, R.drawable.balance, R.drawable.charity,
            R.drawable.americanfootball, R.drawable.library, R.drawable.suitcase, R.drawable.team };
    private final String dbUrlCategory = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/categories";

    public MainActivityFragment() {}

    public static MainActivityFragment newInstance() {
        Bundle bundle = new Bundle();
        MainActivityFragment f = new MainActivityFragment();
        f.setArguments(bundle);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "in onCreateView");
        View v = inflater.inflate(R.layout.main_activity_fragment, container, false);

        // instantiating singleton
        Singleton.get(getActivity().getApplicationContext()).Instantiate();

        //creating toolbar to place logo
//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        listView = (ListView) v.findViewById((R.id.list));

//        db = new database(getActivity().getApplication());
        ConnectDB(new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<CategoryClass> result) {
                Log.d(TAG, "***************************** result size" + result.size());
                populateList();
            }
        });
        Singleton.get(getActivity().getApplicationContext()).Print();

        return v;
    }

    public void ConnectDB(final VolleyCallback callback) {
        // initialize a new requestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest //make json object request
                (Request.Method.GET, dbUrlCategory, null, new Response.Listener<JSONArray>() { //anonymous listener
                    @Override
                    public void onResponse(JSONArray response) { //the server response is a JSONArray containing the data
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                mJsonObjectVector.add(response.getJSONObject(i));
                            }
                            parseJson();
                            callback.onSuccess(Singleton.get(getActivity().getApplicationContext()).GetCategory());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //if you got no data back from the server, handle it somehow.

                    }
                })
        {
            @Override
            public Map<String,String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                String credentials = "asie"+":"+"fighton";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        //Remember to add it to our queue so it will actually start!
        requestQueue.add(jsArrayRequest);
        requestQueue.start();

    }

    private void parseJson() {
        try {
            for (int i = 0; i < mJsonObjectVector.size(); ++i) {
                String id = mJsonObjectVector.elementAt(i).getString("_id");
                String name = mJsonObjectVector.elementAt(i).getString("name");
                String parent_id = mJsonObjectVector.elementAt(i).getString("parent_id");

                CategoryClass c;
                if (parent_id.length() == 0) {
                    c = new CategoryClass(id, parent_id, name, mCategoryImages[i]);
                    Singleton.get(getActivity().getApplicationContext()).AddCategory(c);
                } else {
                    c = new CategoryClass(id, parent_id, name);
                    Singleton.get(getActivity().getApplicationContext()).AddSubCategory(c);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateList() {
        ArrayList<CategoryClass> mainCategory = new ArrayList<CategoryClass>();
        mainCategory = Singleton.get(getActivity().getApplicationContext()).GetCategory();
        categoryRow = new ArrayList<CategoryClass>();

//         populate each row
        for (int i=0; i<mainCategory.size(); ++i) {
            categoryRow.add(mainCategory.get(i));
        }

        // populate the list view with category row
        adapter = new ListArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item, categoryRow);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener();
    }
}
