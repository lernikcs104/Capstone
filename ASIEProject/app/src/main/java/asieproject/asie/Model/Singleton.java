package asieproject.asie.Model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by CACTUS on 2/11/2018.
 */

public class Singleton {
    private final String TAG = Singleton.class.getSimpleName();
    private ArrayList<CategoryClass> mCategoryList = new ArrayList<CategoryClass>();
    private ArrayList<CategoryClass> mSubCategoryList = new ArrayList<CategoryClass>();
    private ArrayList<ResourceClass> mResourceList = new ArrayList<ResourceClass>();
    private Map<String, String> mCategoryToResourceMap = new HashMap<String, String>();
    private boolean mIsCategorySet, mIsCategoryToResourceSet, mIsResourceSet = false;


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

    public void AddResource(ResourceClass r) {
        mResourceList.add(r);
    }

    public ArrayList<CategoryClass> GetCategory() {
        return mCategoryList;
    }

    public ArrayList<CategoryClass> GetSubCategory() {return mSubCategoryList;}

    public void AddToMap(String key, String value) {
        // key is Resource ID, value is Categories id
        mCategoryToResourceMap.put(key, value);
    }

    public Map<String, String> GetCategoryToResourceMap() {
        return mCategoryToResourceMap;
    }

    public void SetCategoryFlag() {
        mIsCategorySet = true;
    }

    public void SetCategoryToResourceFlag() {
        mIsCategoryToResourceSet = true;
    }

    public void SetResourceFlag() {
        mIsResourceSet = true;
    }

    public boolean IsCategorySet() {
        return mIsCategorySet;
    }

    public boolean IsResToCatSet() {
        return mIsCategoryToResourceSet;
    }

    public boolean IsResourceSet() {
        return mIsResourceSet;
    }
}
