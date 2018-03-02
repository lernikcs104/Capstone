package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import asieproject.asie.Controller.database;
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.View.ResourceListAdapter;
import asieproject.asie.View.SubCategoryListAdapter;

import asieproject.asie.Model.ResourceClass;
/**
 * Created by CACTUS on 2/28/2018.
 */

public class ResourceFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = ResourceFragment.class.getSimpleName();

    private int mListPosition;
    private int mainCategoryIndex;
    private String mSubcategoryId;
    private ListView mListView;
    List<ResourceClass> mResourceRow;
    ResourceListAdapter adapter;
    ResourceClass mCurrentResource;

    public ResourceFragment() {
    }

    public static ResourceFragment newInstance(int pos, String subcat_id, int mainCatIndex) {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_RESOURCE, pos);
        bundle.putInt(MainActivity.EXTRA_CATEGORY, mainCatIndex);
        bundle.putString(MainActivity.EXTRA_SUBCATEGORY_ID, subcat_id);
        ResourceFragment f = new ResourceFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListPosition = bundle.getInt(MainActivity.EXTRA_RESOURCE);
        mSubcategoryId = bundle.getString(MainActivity.EXTRA_SUBCATEGORY_ID);
        mainCategoryIndex = bundle.getInt(MainActivity.EXTRA_CATEGORY);
        Log.d(TAG, "....................... sub cat id " + mSubcategoryId);
        Log.d(TAG, "....................... main cat id " + mainCategoryIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resource_fragment, container, false);
        mListView = (ListView) v.findViewById(R.id.resource_list);

        populateList();

        return v;
    }

    private void populateList() {
        ArrayList<ResourceClass> resourceList = new ArrayList<ResourceClass>();
        // get subcategories based on the category row clicked by the user
        resourceList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(mSubcategoryId);
//        Log.d(TAG, "!!!!!!!!!!!!!! size " + resourceList.size());
        mResourceRow = new ArrayList<ResourceClass>();

//     populate each row
        for (int i=1; i<resourceList.size(); ++i) {
            mResourceRow.add(resourceList.get(i));
        }

//        // populate the list view with category row
        adapter = new ResourceListAdapter (getActivity().getApplicationContext(), R.layout.resource_list_item, mResourceRow);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mCurrentResource = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(mSubcategoryId).get(position + 1);
//        Log.d(TAG, "...................... NAME NAME NAME " + mCurrentResource.GetResourceName());

        Intent intent = new Intent(getActivity().getApplicationContext(), ResourceInfoActivity.class);
        intent.putExtra(MainActivity.EXTRA_RESOURCE_DETAIL, (ResourceClass) mCurrentResource);
        getActivity().setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
