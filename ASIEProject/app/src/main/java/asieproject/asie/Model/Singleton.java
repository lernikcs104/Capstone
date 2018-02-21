package asieproject.asie.Model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by CACTUS on 2/11/2018.
 */

public class Singleton {
    private final String TAG = Singleton.class.getSimpleName();
    private ArrayList<CategoryClass> mCategoryList = new ArrayList<CategoryClass>();
    private ArrayList<CategoryClass> mSubCategoryList = new ArrayList<CategoryClass>();
    private static Singleton singleton;
    private Context mContext;

    public Singleton(Context context) {
        mContext = context;
    }

    public static Singleton get(Context c) {
        if (singleton == null) {
            singleton = new Singleton(c);
        }
        return singleton;
    }

    public void Instantiate() {
        Log.d(TAG, "instantiating singleton");
    }

    public void AddCategory(CategoryClass c) {
        mCategoryList.add(c);
    }

    public void AddSubCategory(CategoryClass c) {
        mSubCategoryList.add(c);
    }

    public ArrayList<CategoryClass> GetCategory() {
        return mCategoryList;
    }

    public ArrayList<CategoryClass> GetSubCategory() {
        return mSubCategoryList;
    }

    public void Print() {
        Log.d(TAG, "!!!!!! size mainV" + mCategoryList.size());
        for (int i = 0; i < mCategoryList.size(); ++i) {
            Log.d(TAG, "......... id " + mCategoryList.get(i).getId());
            Log.d(TAG, "......... name " + mCategoryList.get(i).getCategoryName());
            Log.d(TAG, "......... parent_id " + mCategoryList.get(i).getParentId());
        }
    }
}
