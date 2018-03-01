package asieproject.asie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by CACTUS on 2/28/2018.
 */

public class ResourceActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        // get data from intent
        Intent intent = getIntent();
        int listPos = (int)intent.getIntExtra(MainActivity.EXTRA_RESOURCE, 0);
        int maincat_id = (int)intent.getIntExtra(MainActivity.EXTRA_CATEGORY, 0);
        String subcat_id = (String) intent.getStringExtra(MainActivity.EXTRA_SUBCATEGORY_ID);

        if (f == null) {
            f = ResourceFragment.newInstance(listPos, subcat_id, maincat_id);
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
