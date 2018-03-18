package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.ParcelableSparseArray;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.View.SearchResultListAdapter;

import static android.content.ContentValues.TAG;


public class SearchResultFragment extends Fragment implements AdapterView.OnItemClickListener{

    public static final String TAG = SearchResultFragment.class.getSimpleName();
    private ListView mListView;
    List<ResourceClass> subcategoryRow;
    SearchResultListAdapter adapter;
    private int mListPosition;
    ArrayList<ResourceClass> searchResultList;
    VolleyCallback callback;
    database db;
    private TextView headerText;
    private ImageView backImage;
    BottomNavigationView bottomNavigationView;

    private final String categoryToResourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";
    private final String  resourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";
    public SearchResultFragment() {}

    public static SearchResultFragment newInstance( ArrayList<ResourceClass> searchResult) {
        Bundle bundle = new Bundle();
        //bundle.putInt(MainActivity.EXTRA_CATEGORY, pos);
        bundle.putSerializable(MainActivity.SEARCH_RESULT,  searchResult);
        SearchResultFragment f = new SearchResultFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        //mListPosition = bundle.getInt(MainActivity.EXTRA_CATEGORY);
        searchResultList = (ArrayList<ResourceClass>) bundle.getSerializable(MainActivity.SEARCH_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sub_category_fragment, container, false);
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

        /**
        // success call to category_to_resource db data
        callback = new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<CategoryClass> result) {}

            @Override
            public void onSuccess(Map<String, String> result) {

                if (! Singleton.get(getActivity().getApplicationContext()).IsResourceSet()) {
                    db = new database(getActivity().getApplication(), callback, resourceURL, 3);
                    Singleton.get(getActivity().getApplicationContext()).SetResourceFlag();

                }
            }

            @Override
            public void onSuccessResource(ArrayList<ResourceClass> result) {}
        };


        if (! Singleton.get(getActivity().getApplicationContext()).IsResToCatSet()) {
            db = new database(getActivity().getApplication(), callback, categoryToResourceURL, 2);
            Singleton.get(getActivity().getApplicationContext()).SetCategoryToResourceFlag();
        }
*/
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
                    //selectedFragment = MainActivityFragment.newInstance();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    startActivity(intent);
//                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, selectedFragment);
//                    transaction.commit();
                    return true;
                case R.id.info:
                     intent = new Intent(getActivity().getApplicationContext(), InformationActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

/*
    private void populateList() {
        ArrayList<CategoryClass> subCategoryList = new ArrayList<CategoryClass>();
        // get subcategories based on the category row clicked by the user
        subCategoryList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();
        subcategoryRow = new ArrayList<CategoryClass>();
        headerText.setText( Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getCategoryName());

//      populate each row
        for (int i=0; i<subCategoryList.size(); ++i) {
            subcategoryRow.add(subCategoryList.get(i));
        }

        // populate the list view with category row
        adapter = new SubCategoryListAdapter(getActivity().getApplicationContext(), R.layout.subcategory_list_item, subcategoryRow);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }
   */

    private void populateList() {
        ArrayList<ResourceClass> subCategoryList = searchResultList;
        // get subcategories based on the category row clicked by the user
        //subCategoryList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();
        //resourceList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(mSubcategoryId);

        subcategoryRow = new ArrayList<ResourceClass>();
        // headerText.setText( Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getCategoryName());
        // populate each row
        for (int i=0; i<subCategoryList.size(); ++i) {
            subcategoryRow.add(subCategoryList.get(i));
        }

        // populate the list view with category row
        adapter = new SearchResultListAdapter(getActivity().getApplicationContext(), R.layout.search_result_item, subcategoryRow);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//        ArrayList<CategoryClass> subcategoryList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();
//
//        Intent intent = new Intent(getActivity().getApplicationContext(), ResourceActivity.class);
//        intent.putExtra(MainActivity.EXTRA_RESOURCE, position);
//        intent.putExtra(MainActivity.EXTRA_SUBCATEGORY_ID, subcategoryList.get(position).getId());
//        intent.putExtra(MainActivity.EXTRA_CATEGORY, mListPosition);
//        intent.putExtra(MainActivity.EXTRA_HEADER,subcategoryList.get(position).getCategoryName());
//        Log.d(TAG, "....................... main header " + subcategoryList.get(position).getCategoryName());
//        getActivity().setResult(Activity.RESULT_OK, intent);
//        startActivity(intent);
    }

}
