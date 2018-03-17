package asieproject.asie.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.R;
import asieproject.asie.Model.Singleton;

import static android.content.ContentValues.TAG;

/**
 * Created by CACTUS on 1/25/2018.
 */

public class database {//extends AsyncTask<Object, Void, JSONObject> {

    private final int URL_CODE_CAT = 1;
    private final int URL_CODE_CAT_TO_RES = 2;
    private final int URL_CODE_RES = 3;

    private Context mContext;
//    private final String dbUrlCategory = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/categories";
    private final String  dbUrlResource = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";
    private final String dbUrlCategoryToResource = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";

    public static final Integer[] mCategoryImages = { R.drawable.medicalrecords,
            R.drawable.healthcare, R.drawable.library, R.drawable.americanfootball,
            R.drawable.suitcase, R.drawable.balance, R.drawable.team, R.drawable.charity };

    private Vector<CategoryClass> mMainCategoryVector;

    private Vector<CategoryClass> mSubCategoryVector;
    private Vector<JSONObject> mJsonObjectVector;

    public database(final Context context, final VolleyCallback callback, String dbURL, final int urlCode) {

        mContext = context;

        // instantiate category vector
        mMainCategoryVector = new Vector<CategoryClass>();
        mSubCategoryVector = new Vector<CategoryClass>();
        mJsonObjectVector = new Vector<JSONObject>();

        // initialize a new requestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest //make json object request
                (Request.Method.GET, dbURL, null, new Response.Listener<JSONArray>() { //anonymous listener
                    @Override
                    public void onResponse(JSONArray response) { //the server response is a JSONArray containing the data
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                mJsonObjectVector.add(response.getJSONObject(i));
                            }
                            parseJson(urlCode);

                            if (urlCode == URL_CODE_CAT) {
                                Log.d(TAG, ".................. parsing category");
                                callback.onSuccess(Singleton.get(mContext).GetCategory());
                            } else if (urlCode == URL_CODE_CAT_TO_RES) {
                                callback.onSuccess(Singleton.get(mContext).GetCategoryToResourceMap());
                            } else if (urlCode == URL_CODE_RES) {
                                callback.onSuccessResource(Singleton.get(mContext).GetResource());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //if you got no data back from the server, handle it somehow.

                    }
                })
        {
                @Override
                public Map<String,String> getHeaders(){
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String credentials = "asie"+":"+"fighton";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }
        };

        //Remember to add it to our queue so it will actually start!
        requestQueue.add(jsArrayRequest);
    }

    // to parse categories and subcategories from JSON obj
    private void parseJson(int requestCode) {

        try {
            for (int i = 0; i < mJsonObjectVector.size(); ++i) {
                if (requestCode == URL_CODE_CAT) {
                    // parsing JSON
                    String id =  mJsonObjectVector.elementAt(i).getString("_id").substring(9, 33);
                    String name = mJsonObjectVector.elementAt(i).getString("name");
                    String parent_id = mJsonObjectVector.elementAt(i).getString("parent_id");

                    // adding main category and sub category to their corresponding lists
                    CategoryClass c;
                    if (parent_id.length() == 0) {
                        c = new CategoryClass(id, parent_id, name, mCategoryImages[i]);
                        Singleton.get(mContext).AddCategory(c);
                    } else {
                        c = new CategoryClass(id, parent_id, name);
                        Singleton.get(mContext).AddSubCategory(c);
                    }
                }
                else if (requestCode == URL_CODE_CAT_TO_RES) {
                    // parse JSON
                    String cat_id =  mJsonObjectVector.elementAt(i).getString("parent_id");
                    String res_id =  mJsonObjectVector.elementAt(i).getString("resource_id");
                    // add resource_id as key and category_id as value to a hashmap
                    Singleton.get(mContext).AddToMap(res_id, cat_id);
                }
                else if (requestCode == URL_CODE_RES) {
                    // parse JSON
                    String id =  mJsonObjectVector.elementAt(i).getString("_id").substring(9, 33);
                    String name = mJsonObjectVector.elementAt(i).getString("name");
                    String address = mJsonObjectVector.elementAt(i).getString("address");
                    String phone = mJsonObjectVector.elementAt(i).getString("phone");
                    String website = mJsonObjectVector.elementAt(i).getString("website");
                    String email = mJsonObjectVector.elementAt(i).getString("email");
                    String description = mJsonObjectVector.elementAt(i).getString("description");

                    // add each resource to a resource list
                    ResourceClass r = new ResourceClass(id, name, address, phone, website, email, description);
                    Singleton.get(mContext).AddResource(r);
                }
            }

            if (requestCode == URL_CODE_CAT) {
                // add subcategory to its corresponding parent category
                AssignSubCategory(Singleton.get(mContext).GetSubCategory());
            } else if( requestCode == URL_CODE_RES) {
                Log.d(TAG, "******************** assign resources");
                // assign each resource to its category
                AssignResource(Singleton.get(mContext).GetResource());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // to assign each subcategory to its parent category
    private void AssignSubCategory(ArrayList<CategoryClass> subCatList) {

        for (int i=0; i<subCatList.size(); ++i) {
            for (int j=0; j<Singleton.get(mContext).GetCategory().size(); ++j) {
                if (subCatList.get(i).getParentId().equals(Singleton.get(mContext).GetCategory().get(j).getId())) {
                    Singleton.get(mContext).GetCategory().get(j).addSubCategory(subCatList.get(i));
                    Singleton.get(mContext).SetSubcatToCategoryMap(subCatList.get(i).getId(), Singleton.get(mContext).GetCategory().get(j).getId());
                    break;
                }
            }
        }
//        Log.d(TAG, "********************** main cat id " +  Singleton.get(mContext).GetSubcategoryToCategoryMap().get("59d6b4dd3fdf2e4bed10fcc1"));
    }

    // to assign resource(s) to their subcatgory
    private void AssignResource(ArrayList<ResourceClass> resourceList) {
        Log.d(TAG, "................ assign resource");
        Log.d(TAG, "$$$$$$$$$$$$$$$$$$ resource size " + resourceList.size());


//        Log.d(TAG, "********************** size of map " +  Singleton.get(mContext).GetCategoryToResourceMap().size());
//        Log.d(TAG, "********************** size of list " +  resourceList.size());

        int count = 0;
        for (int i = 0; i < resourceList.size(); ++i) {
            // get subcategory id for this resource using resource_to_category map
            String subcat_id = Singleton.get(mContext).GetCategoryToResourceMap().get(resourceList.get(i).GetResourceId());
            // get main category id using subcategory_to_category map
            String maincat_id = Singleton.get(mContext).GetSubcategoryToCategoryMap().get(subcat_id);
//            Log.d(TAG, "..................... category id = " + subcat_id );
//            Log.d(TAG, "..................... maincat_id = " + maincat_id );
//
//            Log.d(TAG, "......... index = " + i);
            for (int j = 0; j < Singleton.get(mContext).GetCategory().size(); ++j) {
                String mainCatId = Singleton.get(mContext).GetCategory().get(j).getId();
//                Log.d(TAG, "........ id = " + Singleton.get(mContext).GetCategory().get(j).getId());
                if (maincat_id != null) {

                    // TODO: check which resource is null
                    if (maincat_id.equals(mainCatId)) {
                        String subcat_name = "" ;
                        for (int x = 0; x < Singleton.get(mContext).GetSubCategory().size(); ++ x) {
                            if (subcat_id.equals( Singleton.get(mContext).GetSubCategory().get(x).getId())) {
                                subcat_name = Singleton.get(mContext).GetSubCategory().get(x).getCategoryName();
                                Log.d(TAG, " ....Category " + Singleton.get(mContext).GetSubCategory().get(x).getCategoryName());
                            }
                        }
                        Log.d(TAG, "..res " + resourceList.get(i).GetResourceName() + " .. sc " + subcat_name + ".. c" + Singleton.get(mContext).GetCategory().get(j).getCategoryName());
                        CategoryClass mainCategory = Singleton.get(mContext).GetCategory().get(j);
                        mainCategory.addResourceToSubcategory(subcat_id, resourceList.get(i));
                        ++count;
                        break;
//                        Log.d(TAG, "-------------------------- category name = " + Singleton.get(mContext).GetCategory().get(j).getCategoryName());
                    }
                } else {
//                    Log.d(TAG, "########################################");
//                    Log.d(TAG, "resource name " + resourceList.get(i).GetResourceName());
//                    for (int x = 0; x < Singleton.get(mContext).GetSubCategory().size(); ++ x) {
//                        if (subcat_id.equals( Singleton.get(mContext).GetSubCategory().get(x).getId())) {
//                            Log.d(TAG, " name " + Singleton.get(mContext).GetSubCategory().get(x).getCategoryName());
//                        }
//                    }

                }
            }

//            Log.d(TAG, ".............. resources taht are stored = " + count);
        }

//        for (int i = 0; i < Singleton.get(mContext).GetCategory().size(); ++i) {
//            Log.d(TAG, "............ MAIN CATEGORY = " + Singleton.get(mContext).GetCategory().get(i).getCategoryName());
//            for (int j = 0; j < Singleton.get(mContext).GetCategory().get(i).getSubCategoryList().size(); ++j) {
//                Log.d(TAG, ".............. SUB CATEGORY = " + Singleton.get(mContext).GetCategory().get(i).getSubCategoryList().get(j).getCategoryName());
//                String subcatid = Singleton.get(mContext).GetCategory().get(i).getSubCategoryList().get(j).getId();
//                int resourceSize = Singleton.get(mContext).GetCategory().get(i).GetResourceMap().get(subcatid).size();
//                Log.d(TAG, "............... size of resource = " + resourceSize);
//
//                for (int k = 0; k < resourceSize; ++k ) {
//                    Log.d(TAG, "............... resource " + k + Singleton.get(mContext).GetCategory().get(i).GetResourceMap().get(subcatid).get(k).GetResourceName());
//                }
//            }
//        }
    }
}
