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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import asieproject.asie.Model.CategoryClass;
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

    public database(final Context context) {
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
                            Singleton.get(context).SetCategory(mMainCategoryVector);

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

    public Vector<CategoryClass> getSubCategoryVector() {
        return mSubCategoryVector;
    }

    private void parseJson() {
        try {
            for (int i = 0; i < mJsonObjectVector.size(); ++i) {
                String id = mJsonObjectVector.elementAt(i).getString("_id");
                String name = mJsonObjectVector.elementAt(i).getString("name");
                String parent_id = mJsonObjectVector.elementAt(i).getString("parent_id");

                CategoryClass c;
                if (parent_id.length() == 0) {
                    c = new CategoryClass(id, parent_id, name, mCategoryImages[i]);
                    mMainCategoryVector.add(c);
                } else {
                    c = new CategoryClass(id, parent_id, name);
                    mSubCategoryVector.add(c);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
