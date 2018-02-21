package asieproject.asie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.Singleton;
import asieproject.asie.View.SubCategoryListAdapter;

import static android.content.ContentValues.TAG;


/**
 * Created by CACTUS on 2/20/2018.
 */

public class SubCategoryFragment extends Fragment {

    public static final String TAG = SubCategoryFragment.class.getSimpleName();
    private ListView mListView;
    List<CategoryClass> subcategoryRow;
    SubCategoryListAdapter adapter;
    private int mListPosition;

    public SubCategoryFragment() {}

    public static SubCategoryFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_CATEGORY, pos);
        SubCategoryFragment f = new SubCategoryFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListPosition = bundle.getInt(MainActivity.EXTRA_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sub_category_fragment, container, false);

        mListView = (ListView)v.findViewById(R.id.sub_category_list);

        populateList();

        return v;
    }

    private void populateList() {
        ArrayList<CategoryClass> subCategoryList = new ArrayList<CategoryClass>();
        // get subcategories based on the category row clicked by the user
        subCategoryList = Singleton.get(getActivity().getApplicationContext()).GetCategory().get(mListPosition).getSubCategoryList();
        subcategoryRow = new ArrayList<CategoryClass>();

//      populate each row
        for (int i=0; i<subCategoryList.size(); ++i) {
            subcategoryRow.add(subCategoryList.get(i));
        }

        // populate the list view with category row
        adapter = new SubCategoryListAdapter(getActivity().getApplicationContext(), R.layout.subcategory_list_item, subcategoryRow);
        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(this);
    }
}
