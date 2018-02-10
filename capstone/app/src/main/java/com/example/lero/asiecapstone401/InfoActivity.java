package com.example.lero.asiecapstone401;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lero on 2/8/18.
 */

public class InfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView description;
    private TextView headerText;
    public static final String[] header = new String[] {
            "call",
            "Website",
            "Directions" };

    public static final String[] descriptions = new String[] {
            "(951) 247-5538",
            "No Website Available",
            "24104 Sunnymead Blvd, Suit C, Moreno Valley Children's dentistry"

    };


    public static final Integer[] images = { R.drawable.phone,
            R.drawable.website, R.drawable.directions};


    private String descText = "Non-profit organization that assists and " +
            "provide support to anyone with or connected someone with " +
            "special needs and disabilities at any age";

    ListView listView;
    List<Information> rowItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        String h = intent.getStringExtra("clickedItem");
        headerText = (TextView)findViewById(R.id.topText);

        headerText.setText(h);

        rowItems = new ArrayList<Information>();
        for (int i = 0; i < descriptions.length; i++) {
            Information item = new Information(images[i], header[i], descriptions[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        InfoListAdapter adapter = new InfoListAdapter(this,
                R.layout.info_list_items, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        description = (TextView) findViewById(R.id.desc);
        description.setText(descText);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();


    }
}
