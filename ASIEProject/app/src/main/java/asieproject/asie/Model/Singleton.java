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
    private ArrayList<CategoryClass> mCategoryList;
    private Vector<CategoryClass> mMainCategoryVector = new Vector<CategoryClass>();
    private Vector<CategoryClass> mSubCategoryVector = new Vector<CategoryClass>();
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
        mMainCategoryVector.add(c);
    }

    public void AddSubCategory(CategoryClass c) {
        mSubCategoryVector.add(c);
    }

    public void SetCategory(Vector<CategoryClass> c) {
        mMainCategoryVector = c;
    }

    public Vector<CategoryClass> GetCategory() {
        return mMainCategoryVector;
    }

    public void Print() {
        Log.d(TAG, "!!!!!! size mainV" + mMainCategoryVector.size());
        for (int i = 0; i < mMainCategoryVector.size(); ++i) {
            Log.d(TAG, "......... id " + mMainCategoryVector.elementAt(i).getId());
            Log.d(TAG, "......... name " + mMainCategoryVector.elementAt(i).getCategoryName());
            Log.d(TAG, "......... parent_id " + mMainCategoryVector.elementAt(i).getParentId());
        }
    }
}
