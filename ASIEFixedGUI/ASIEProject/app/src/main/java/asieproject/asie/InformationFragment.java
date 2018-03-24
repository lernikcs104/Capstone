package asieproject.asie;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import asieproject.asie.Model.ResourceClass;

import static android.content.ContentValues.TAG;



public class InformationFragment extends Fragment {

    TextView phone;
    TextView website;
    TextView email;
    TextView donate;
    BottomNavigationView bottomNavigationView;
    private ImageView backImage;

    public InformationFragment() {
        // Required empty public constructor
    }
    public static InformationFragment newInstance() {
        Bundle args = new Bundle();

        InformationFragment f = new InformationFragment();
        f.setArguments(args);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "in Information fragment===========");
        View v = inflater.inflate(R.layout.asie_info_fragment, container, false);


        phone = (TextView)v.findViewById(R.id.info_phone);
        website = (TextView)v.findViewById(R.id.info_website);
        email = (TextView)v.findViewById(R.id.info_email);
        donate = (TextView)v.findViewById(R.id.info_donate);
        backImage = (ImageView) v.findViewById(R.id.back_icon);
        bottomNavigationView = (BottomNavigationView) v.findViewById(R.id.navigation);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backImage.setColorFilter(0x55215894, PorterDuff.Mode.MULTIPLY);
                getActivity().finish();
            }
        });
        //clicking on the phone for calling
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if( (getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){

                }else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+ phone.getText().toString()));

                    startActivity(callIntent);
                }


            }
        });
        //clicking on the website
        website.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url= (String) website.getText();
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
            }
        });


        //clicking on the donate
        email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email_to= (String) email.getText();
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                intent.setData(Uri.parse("mailto:info@ieautism.org")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

                try {
                    startActivity(intent);
                } catch(Exception e){

                    Log.d(TAG, "email", e);
                    Toast toast = Toast.makeText(getActivity(),
                            "Item " + (2) + ": " + email,
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        });

        //clicking on the donate
        donate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url= (String) donate.getText();
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
