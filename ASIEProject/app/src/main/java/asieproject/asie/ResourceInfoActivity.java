package asieproject.asie;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import asieproject.asie.Model.ResourceClass;

/**
 * Created by CACTUS on 3/1/2018.
 */

public class ResourceInfoActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create fragment
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        // get data from intent
        Intent intent = getIntent();
        ResourceClass resource = ((ResourceClass)intent.getSerializableExtra(MainActivity.EXTRA_RESOURCE_DETAIL));

        if (f == null) {
            f = ResourceInfoFragment.newInstance(resource);
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
