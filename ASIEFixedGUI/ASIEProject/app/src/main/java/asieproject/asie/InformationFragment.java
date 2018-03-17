package asieproject.asie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by lero on 3/8/18.
 */

public class InformationFragment extends Fragment {

    TextView phone;
    TextView website;
    TextView email;
    TextView donate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "in onCreateView");
        View v = inflater.inflate(R.layout.main_activity_fragment, container, false);


        phone = (TextView)v.findViewById(R.id.info_phone);
        website = (TextView)v.findViewById(R.id.info_website);
        email = (TextView)v.findViewById(R.id.info_email);
        donate = (TextView)v.findViewById(R.id.info_donate);

        //clicking on the phone for calling
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try
                {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText())));

                }
                catch(Exception e)
                {
                    Log.d(TAG, "caller: ", e);
                    Toast toast = Toast.makeText(getActivity(),
                            "Item " + (1) + ": " + phone,
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

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


        return v;
    }


}
