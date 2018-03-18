package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import asieproject.asie.Model.ResourceClass;

public class SearchActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        // get data from intent
        Intent intent = getIntent();


        ArrayList<ResourceClass> item  = (ArrayList<ResourceClass>) intent.getSerializableExtra(
                "SEARCH_RESULT");
        String searchedItem = intent.getStringExtra("SEARCHED_STRING");

        if (f == null) {
            f = SearchResultFragment.newInstance( item, searchedItem);
        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

}
