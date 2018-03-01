package asieproject.asie.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asieproject.asie.R;

/**
 * Created by CACTUS on 2/8/2018.
 */

public class CategoryClass implements Serializable {

    private final static String TAG = CategoryClass.class.getSimpleName();
    private String mCategoryId;
    private String mCategoryParentId;
    private String mCategoryName;
    private Integer mCategoryImage;
    private ArrayList<CategoryClass> mSubCategoryList;
    private Map<String, ArrayList<ResourceClass>> mResourceMap;
    private Map<String, String> map;

    public CategoryClass() {
        super();
    }

    public CategoryClass(String id, String parentId, String name, Integer image) {
        super();
        this.mCategoryId = id;
        this.mCategoryName = name;
        this.mCategoryParentId = parentId;
        this.mCategoryImage = image;
        mSubCategoryList = new ArrayList<CategoryClass>();
        mResourceMap = new HashMap<String, ArrayList<ResourceClass>>();
    }

    public CategoryClass(String id, String parentId, String name) {
        super();
        this.mCategoryId = id;
        this.mCategoryName = name;
        this.mCategoryParentId = parentId;
    }

    public String getId() {
        return mCategoryId;
    }

    public String getParentId() {
        return mCategoryParentId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public Integer getCategoryImage() {
        return mCategoryImage;
    }

    public void addSubCategory(CategoryClass c) {

        mSubCategoryList.add(c);
        // allocate map key and value for resources belong to this subcategory
        ArrayList<ResourceClass> res = new ArrayList<ResourceClass>();
        ResourceClass r = new ResourceClass("", "", "", "", "", "", "");
        mResourceMap.put(c.getId(), res);
        mResourceMap.get(c.getId()).add(r);
    }

    public void addResourceToSubcategory(String key, ResourceClass r) {

        mResourceMap.get(key).add(r);
    }

    public Map<String, ArrayList<ResourceClass>> GetResourceMap() { return mResourceMap; }

    public ArrayList<CategoryClass> getSubCategoryList() {
        return mSubCategoryList;
    }
}
