package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.View.SubCategoryListAdapter;

import static android.content.ContentValues.TAG;


/**
 * Created by CACTUS on 2/20/2018.
 */

public class SubCategoryFragment extends Fragment implements AdapterView.OnItemClickListener{

    public static final String TAG = SubCategoryFragment.class.getSimpleName();
    private ListView mListView;
    List<CategoryClass> subcategoryRow;
    SubCategoryListAdapter adapter;
    private int mListPosition;
    VolleyCallback callback;
    database db;
    private TextView headerText;
    private ImageView backImage;

    private final String categoryToResourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";
    private final String  resourceURL = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";
    public SubCategoryFragment() {}

    public static SubCategoryFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_CATEGORY, pos);
        SubCategoryFragment f = new SubCategoryFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListPosition = bundle.getInt(MainActivity.EXTRA_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sub_category_fragment, container, false);
        mListView = (ListView)v.findViewById(R.id.sub_category_list);
        headerText = (TextView)v.findViewById(R.id.topText);
        backImage = (ImageView) v.findViewById(R.id.back_icon);

        //on clicking th back arrow it goes to previous page
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backImage.setColorFilter(0x55215894, PorterDuff.Mode.MULTIPLY);
                getActivity().finish();
            }
        });

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

        populateList();

        return v;
    }



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



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        ArrayList<CategoryClass> subcategoryList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();

        Intent intent = new Intent(getActivity().getApplicationContext(), ResourceActivity.class);
        intent.putExtra(MainActivity.EXTRA_RESOURCE, position);
        intent.putExtra(MainActivity.EXTRA_SUBCATEGORY_ID, subcategoryList.get(position).getId());
        intent.putExtra(MainActivity.EXTRA_CATEGORY, mListPosition);
        intent.putExtra(MainActivity.EXTRA_HEADER,subcategoryList.get(position).getCategoryName());
        Log.d(TAG, "....................... main header " + subcategoryList.get(position).getCategoryName());
        getActivity().setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
