package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.AdapterView;
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

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    database db;
    VolleyCallback callback;
    private ListView listView;
    List<CategoryClass> categoryRow;
    ListArrayAdapter adapter;
    private String rowItemString;

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

        listView = (ListView) v.findViewById((R.id.list));

        // instantiating singleton
        Singleton.get(getActivity().getApplicationContext()).Instantiate();
        // instantiate Volley callback
        callback = new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<CategoryClass> result) {
                populateList();

            }
        };

        db = new database(getActivity().getApplication(), callback);
        return v;
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
        getActivity().finish();
        startActivity(intent);
//        intent.putExtra(MainActivity.EXTRA_CATEGORY)

    }
}
