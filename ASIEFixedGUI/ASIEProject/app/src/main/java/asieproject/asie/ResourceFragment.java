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
import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.View.ResourceListAdapter;
import asieproject.asie.View.SubCategoryListAdapter;

import asieproject.asie.Model.ResourceClass;

public class ResourceFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = ResourceFragment.class.getSimpleName();

    private int mListPosition;
    private int mainCategoryIndex;
    private String mSubcategoryId;
    private ListView mListView;
    List<ResourceClass> mResourceRow;
    ResourceListAdapter adapter;
    ResourceClass mCurrentResource;
    TextView headerText;
    String header;
    private ImageView backImage;
    BottomNavigationView bottomNavigationView;


    public ResourceFragment() {
    }

    public static ResourceFragment newInstance(int pos, String subcat_id, int mainCatIndex, String header_text) {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_RESOURCE, pos);
        bundle.putInt(MainActivity.EXTRA_CATEGORY, mainCatIndex);
        bundle.putString(MainActivity.EXTRA_SUBCATEGORY_ID, subcat_id);
        bundle.putString(MainActivity.EXTRA_HEADER, header_text);
        Log.d(TAG, ".......................bundleeee header " + header_text);
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
        header = bundle.getString(MainActivity.EXTRA_HEADER);
        Log.d(TAG, "....................... sub cat id " + mSubcategoryId);
        Log.d(TAG, "....................... main cat id " + mainCategoryIndex);
        Log.d(TAG, "....................... main header " + header);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resource_fragment, container, false);
        mListView = (ListView) v.findViewById(R.id.resource_list);
        headerText = (TextView)v.findViewById(R.id.topText);
        bottomNavigationView = (BottomNavigationView) v.findViewById(R.id.navigation);
        populateList();

        backImage = (ImageView) v.findViewById(R.id.back_icon);

        //on clicking th back arrow it goes to previous page
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backImage.setColorFilter(0x55215894, PorterDuff.Mode.MULTIPLY);
                getActivity().finish();
            }
        });
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
        ArrayList<ResourceClass> resourceList = new ArrayList<ResourceClass>();
        // get subcategories based on the category row clicked by the user
        resourceList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(mSubcategoryId);
//        Log.d(TAG, "!!!!!!!!!!!!!! size " + resourceList.size());
        mResourceRow = new ArrayList<ResourceClass>();
       // headerText.setText(Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mainCategoryIndex).GetResourceMap().get(mSubcategoryId).get(mListPosition).GetResourceName());
        headerText.setText(header);
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
