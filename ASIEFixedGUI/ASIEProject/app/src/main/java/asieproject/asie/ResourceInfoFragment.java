package asieproject.asie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.View.ResourceInfoListAdapter;
import asieproject.asie.View.ResourceListAdapter;


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
    TextView desTtitle;
    ImageView backImage;
    BottomNavigationView bottomNavigationView;

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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.resource_info_fragment, container, false);

        //mListView = (ListView) v.findViewById(R.id.res_info_list);
        phoneTextView = (TextView) v.findViewById(R.id.res_info_phone);
        webTextView = (TextView) v.findViewById(R.id.res_info_website);
        addressTextView = (TextView) v.findViewById(R.id.res_info_address);
        descriptionTextView = (TextView) v.findViewById(R.id.res_info_description);
        nameTextView = (TextView) v.findViewById(R.id.topText);
        desTtitle  = (TextView) v.findViewById(R.id.destitle);

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

        if (!mResource.GetResourceName().isEmpty() ) {
            desTtitle.setText(mResource.GetResourceName());
        }else{
            desTtitle.setText("No information available");
        }
        if (!mResource.GetResourcePhone().isEmpty()) {
            String htmlString="<u>"+mResource.GetResourcePhone()+"</u>";
            phoneTextView.setText(Html.fromHtml(htmlString));
           // phoneTextView.setText("<u>"+mResource.GetResourcePhone()+"</u>");
        }else{
            phoneTextView.setText("No phone number available");
        }
        if (!mResource.GetResourceWebsite().isEmpty()){
            webTextView.setText(mResource.GetResourceWebsite());
        }
        else{
            webTextView.setText("No website link available");
        }
        if(!mResource.GetResourceAddress().isEmpty()) {
            addressTextView.setText(mResource.GetResourceAddress());
        }else{
            addressTextView.setText("No address available");
        }
        if(!mResource.GetResourceDescription().isEmpty()) {
            descriptionTextView.setText(mResource.GetResourceDescription()+"\n\n\n\n");
        }else
        {
            descriptionTextView.setText("No description provided for this link");
        }

        //clicking on the phone for calling
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if( (getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){

                }else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+ mResource.GetResourcePhone().toString().toString()));

                    startActivity(callIntent);
                }
                  
            }
        });

        //clicking on the website
        webTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url=mResource.GetResourceWebsite();
                if(!url.contains("http://")){
                    url= "http://"+url;
                }
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(myIntent);
                } catch(Exception e){

                    Log.d(TAG, "website", e);
                    Toast toast = Toast.makeText(getActivity(),
                            "Item " + (2) + ": " + mResource.GetResourceWebsite(),
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        });

        //clicking on the address and open the google map
        addressTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Uri.encode(mResource.GetResourceAddress()));

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                try {
                    startActivity(mapIntent);
                } catch(Exception e){
                    Log.d(TAG, "Address: ", e);
                    Toast toast = Toast.makeText(getActivity(),
                            "Item " + (3) + ": " + mResource.GetResourceDescription(),
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
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

}
