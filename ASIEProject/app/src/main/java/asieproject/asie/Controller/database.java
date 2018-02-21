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
import java.util.Map;
import java.util.Vector;

import asieproject.asie.Model.CategoryClass;
import asieproject.asie.Model.VolleyCallback;
import asieproject.asie.R;
import asieproject.asie.Model.Singleton;

import static android.content.ContentValues.TAG;

/**
 * Created by CACTUS on 1/25/2018.
 */

public class database {//extends AsyncTask<Object, Void, JSONObject> {

    private Context mContext;
    private final String dbUrlCategory = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/categories";
    private final String  dbUrlResource = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/resources";
    private final String dbUrlCategoryToResource = "http://www.ieautism.org:81/mobileappdata/db/Children/expArr/category_to_resource";

    public static final Integer[] mCategoryImages = { R.drawable.medicalrecords,
            R.drawable.healthcare, R.drawable.balance, R.drawable.charity,
            R.drawable.americanfootball, R.drawable.library, R.drawable.suitcase, R.drawable.team };

    private Vector<CategoryClass> mMainCategoryVector;

    private Vector<CategoryClass> mSubCategoryVector;
    private Vector<JSONObject> mJsonObjectVector;

    public database(final Context context, final VolleyCallback callback) {

        mContext = context;

        // instantiate category vector
        mMainCategoryVector = new Vector<CategoryClass>();
        mSubCategoryVector = new Vector<CategoryClass>();
        mJsonObjectVector = new Vector<JSONObject>();

        // initialize a new requestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest //make json object request
                (Request.Method.GET, dbUrlCategory, null, new Response.Listener<JSONArray>() { //anonymous listener
                    @Override
                    public void onResponse(JSONArray response) { //the server response is a JSONArray containing the data
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                mJsonObjectVector.add(response.getJSONObject(i));
                            }
                            parseJson();
                            callback.onSuccess(Singleton.get(mContext).GetCategory());

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
    private void parseJson() {
        try {
            for (int i = 0; i < mJsonObjectVector.size(); ++i) {
                String id =  mJsonObjectVector.elementAt(i).getString("_id").substring(9, 33);
                String name = mJsonObjectVector.elementAt(i).getString("name");
                String parent_id = mJsonObjectVector.elementAt(i).getString("parent_id");

                CategoryClass c;
                if (parent_id.length() == 0) {
                    c = new CategoryClass(id, parent_id, name, mCategoryImages[i]);
                    Singleton.get(mContext).AddCategory(c);
                } else {
                    c = new CategoryClass(id, parent_id, name);
                    Singleton.get(mContext).AddSubCategory(c);
                }
            }

            // add subcategory to its corresponding parent category
            AssignSubCategory(Singleton.get(mContext).GetSubCategory());

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
                    break;
                }
            }
        }
    }
}
