package asieproject.asie.Model;

import java.io.Serializable;
import java.util.ArrayList;

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
    }

    public ArrayList<CategoryClass> getSubCategoryList() {
        return mSubCategoryList;
    }
}
