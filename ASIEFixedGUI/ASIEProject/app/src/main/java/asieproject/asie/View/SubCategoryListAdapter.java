package asieproject.asie.View;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asieproject.asie.Model.CategoryClass;
import asieproject.asie.R;

/**
 * Created by CACTUS on 2/20/2018.
 */

public class SubCategoryListAdapter extends ArrayAdapter<CategoryClass>{

    Context context;
    public SubCategoryListAdapter(Context context, int resourceId,
                               List<CategoryClass> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class DescHolder {
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SubCategoryListAdapter.DescHolder holder = null;
        CategoryClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.subcategory_list_item, null);
            holder = new SubCategoryListAdapter.DescHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.subcategory_desc);

            convertView.setTag(holder);
        } else
            holder = (SubCategoryListAdapter.DescHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getCategoryName());

        return convertView;
    }

}
