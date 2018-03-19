package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.View.SearchResultListAdapter;


public class SearchResultFragment extends Fragment implements AdapterView.OnItemClickListener{


    public static final String TAG = SearchResultFragment.class.getSimpleName();
    private ListView mListView;
    List<ResourceClass> resourceRow;
    SearchResultListAdapter adapter;
    private int mListPosition;
    ArrayList<ResourceClass> searchResultList;
    VolleyCallback callback;
    database db;
    private TextView headerText;
    private ImageView backImage;
    BottomNavigationView bottomNavigationView;
    private String searchedItem;
    ResourceClass mCurrentResource;

    private final String categoryToResourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";
    private final String  resourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";
    public SearchResultFragment() {}

    public static SearchResultFragment newInstance( ArrayList<ResourceClass> searchResult, String searchedItem) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(MainActivity.SEARCH_RESULT,  searchResult);
        bundle.putString("SEARCHED_STRING", searchedItem);
        SearchResultFragment f = new SearchResultFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        searchResultList = (ArrayList<ResourceClass>) bundle.getSerializable(MainActivity.SEARCH_RESULT);
        searchedItem = bundle.getString("SEARCHED_STRING");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result_fragment, container, false);
        mListView = (ListView)v.findViewById(R.id.search_result);
        headerText = (TextView)v.findViewById(R.id.topText);
        backImage = (ImageView) v.findViewById(R.id.back_icon);
        bottomNavigationView = (BottomNavigationView) v.findViewById(R.id.navigation);
        //on clicking th back arrow it goes to previous page
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backImage.setColorFilter(0x55215894, PorterDuff.Mode.MULTIPLY);
                getActivity().finish();
            }
        });

        populateList();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return v;
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
                     Intent intent= new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    return true;
                case R.id.info:
                     intent = new Intent(getActivity().getApplicationContext(), InformationActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    private void populateList() {
        ArrayList<ResourceClass> subCategoryList = searchResultList;

        resourceRow = new ArrayList<ResourceClass>();
        headerText.setText(searchedItem);
        // populate each row
        for (int i=0; i<searchResultList.size(); ++i) {
            resourceRow.add(searchResultList.get(i));
        }

        // populate the list view
        adapter = new SearchResultListAdapter(getActivity().getApplicationContext(), R.layout.search_result_item, resourceRow);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        String resourceid = searchResultList.get(position).GetResourceId();
        for (int i = 0; i < searchResultList.size(); ++i) {
            if (searchResultList.get(i).GetResourceId().equals(resourceid)) {
                mCurrentResource = searchResultList.get(i);
                break;
            }
        }

        Intent intent = new Intent(getActivity().getApplicationContext(), ResourceInfoActivity.class);
        intent.putExtra(MainActivity.EXTRA_RESOURCE_DETAIL, (ResourceClass) mCurrentResource);
        getActivity().setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
