package asieproject.asie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.View.ResourceInfoListAdapter;
import asieproject.asie.View.ResourceListAdapter;

/**
 * Created by CACTUS on 3/1/2018.
 */

public class ResourceInfoFragment extends Fragment {

    public static final String TAG = ResourceInfoFragment.class.getSimpleName();
    private ResourceClass mResource;
    private ListView mListView;
    List<ResourceClass> mResourceDetailRow;
    ResourceInfoListAdapter adapter;
    TextView phoneTextView;
    TextView webTextView;
    TextView addressTextView;
    TextView descriptionTextView;
    TextView nameTextView;

    public ResourceInfoFragment() {}

    public static ResourceInfoFragment newInstance(ResourceClass res) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.EXTRA_RESOURCE_DETAIL, res);

        ResourceInfoFragment f = new ResourceInfoFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mResource = ((ResourceClass) bundle.getSerializable(MainActivity.EXTRA_RESOURCE_DETAIL));

        Log.d(TAG, "....................... resource name " + mResource.GetResourceName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resource_info_fragment, container, false);
//        mListView = (ListView) v.findViewById(R.id.res_info_list);
        phoneTextView = (TextView) v.findViewById(R.id.res_info_phone);
        webTextView = (TextView) v.findViewById(R.id.res_info_website);
        addressTextView = (TextView) v.findViewById(R.id.res_info_address);
        descriptionTextView = (TextView) v.findViewById(R.id.res_info_description);
        nameTextView = (TextView) v.findViewById(R.id.topText);

        nameTextView.setText(mResource.GetResourceName());
        phoneTextView.setText(mResource.GetResourcePhone());
        webTextView.setText(mResource.GetResourceWebsite());
        addressTextView.setText(mResource.GetResourceAddress());
        descriptionTextView.setText(mResource.GetResourceDescription());
        

        return v;
    }

}
