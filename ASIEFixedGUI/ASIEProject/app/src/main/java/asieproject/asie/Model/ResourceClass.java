package asieproject.asie.Model;

import java.io.Serializable;

/**
 * Created by CACTUS on 2/8/2018.
 */

public class ResourceClass implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static String TAG = ResourceClass.class.getSimpleName();
    private String mResourceId;
    private String mResourceName;
    private String mResourceAddress;
    private String mResourcePhone;
    private String mResourceWeb;
    private String mResourceEmail;
    private String mResourceDescription;
    private String mSubcategoryId;
    private String mMainCategoryId;
    private String mSubcategory;

    public ResourceClass() {
        super();
    }

    public ResourceClass(String id, String name, String address, String phone, String web, String email, String desc) {
        super();
        this.mResourceId = id;
        this.mResourceName = name;
        this.mResourceAddress = address;
        this.mResourcePhone = phone;
        this.mResourceWeb = web;
        this.mResourceEmail = email;
        this.mResourceDescription = desc;
    }

    public void SetSubcategory(String subcat) {
        mSubcategory = subcat;
    }

    public String GetResourceId() { return mResourceId; }
    public String GetResourceName() {
        return mResourceName;
    }
    public String GetResourceAddress() {
        return mResourceAddress;
    }
    public String GetResourcePhone() {return mResourcePhone;}
    public String GetResourceEmail() {
        return mResourceEmail;
    }
    public String GetResourceWebsite() { return mResourceWeb; }
    public String GetResourceDescription() { return mResourceDescription; }
    public String GetSubcategory() { return mSubcategory; }

    public String getSubcategoryId() {return mSubcategoryId;}
    public String getmMainCategoryId() {return mMainCategoryId;}
}
