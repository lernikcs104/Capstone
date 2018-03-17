package asieproject.asie.Model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by CACTUS on 2/11/2018.
 */

public interface VolleyCallback {
    void onSuccess(ArrayList<CategoryClass> result);
    void onSuccess(Map<String, String> result);
    void onSuccessResource(ArrayList<ResourceClass> result);
}