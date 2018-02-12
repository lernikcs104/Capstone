package asieproject.asie.Model;

import java.io.Serializable;

/**
 * Created by CACTUS on 2/8/2018.
 */

public class ResourceClass implements Serializable {

    private final static String TAG = ResourceClass.class.getSimpleName();
    private String mResourceId;
    private String mResourceName;
    private String mResourceAddress;
    private String mResourcePhone;
    private String mResourceWeb;
    private String mResourceDescription;

    public ResourceClass() {
        super();
    }

    public ResourceClass(String id, String name, String address, String phone, String web, String desc) {
        super();
        this.mResourceId = id;
        this.mResourceName = name;
        this.mResourceAddress = address;
        this.mResourcePhone = phone;
        this.mResourceWeb = web;
        this.mResourceDescription = desc;
    }

    public void SetResource(String id, String name, String address, String phone, String web, String desc) {
        this.mResourceId = id;
        this.mResourceName = name;
        this.mResourceAddress = address;
        this.mResourcePhone = phone;
        this.mResourceWeb = web;
        this.mResourceDescription = desc;
    }
}
