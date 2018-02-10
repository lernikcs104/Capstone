package com.example.lero.asiecapstone401;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lero on 1/31/18.
 */

public class ResourcesActivity extends Activity implements AdapterView.OnItemClickListener{

//    public static final String[] descriptions = new String[] {
//            "Diagnosis/Evaluation Assistance",
//            "Chiropractors", "Dentists/Dental Care", "Doctors for Childrens and Teens with ASD","Health Clinics",
//            "Doctors - Forensic Psychology" , "Doctor - Neurologist" };
//
private int INFO_REQUEST;

    TextView headerText;
    private String t;



    public static final String[] descriptions = new String[] {
            "Animal Assisted Therapy",
            "Authority Integration Therapy (AIT)", "Augmentative and Alternative Communication",
            "Behavioural Service","Behavioral Services - Teens & Adults",
            "Biomedical interventions" , "Cognitive Behavior Therapy", "Counseling Mental Help",
            "Craniosacral Therapy", "Early Intervention (0-3) years old","Educational Assessments and Therapy"
             };



    ListView listView;
    List<Resources> rowItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        Intent intent = getIntent();
        String h = intent.getStringExtra("itemClicked");
        headerText = (TextView)findViewById(R.id.topText);

        headerText.setText(h);

        INFO_REQUEST=1;
        rowItems = new ArrayList<Resources>();
        for (int i = 0; i < descriptions.length; i++) {
            Resources item = new Resources( descriptions[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        ResourceListAdapter adapter = new ResourceListAdapter(this, R.layout.resource_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        t = rowItems.get(position).toString();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + t,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        intent.putExtra("clickedItem", t);
        startActivity(intent);
    }

}
