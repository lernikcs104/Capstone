package asieproject.asie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ResourceFragment extends Fragment {

    public static final String TAG = ResourceFragment.class.getSimpleName();

    private int mListPosition;
    private ListView mListView;
    List<ResourceClass> mResourceRow;
    ResourceListAdapter adapter;
    VolleyCallback callback;
    database db;

    public ResourceFragment() {
    }

    public static ResourceFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_RESOURCE, pos);
        ResourceFragment f = new ResourceFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListPosition = bundle.getInt(MainActivity.EXTRA_RESOURCE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resource_fragment, container, false);
        mListView = (ListView) v.findViewById(R.id.resource_list);

//        populateList();

        return v;
    }

    private void populateList() {
        ArrayList<ResourceClass> resourceList = new ArrayList<ResourceClass>();
        // get subcategories based on the category row clicked by the user
//        resourceList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();
        mResourceRow = new ArrayList<ResourceClass>();

//      populate each row
        for (int i=0; i<resourceList.size(); ++i) {
            mResourceRow.add(resourceList.get(i));
        }

        // populate the list view with category row
        adapter = new ResourceListAdapter (getActivity().getApplicationContext(), R.layout.resource_list_item, mResourceRow);
        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(this);
    }
}
