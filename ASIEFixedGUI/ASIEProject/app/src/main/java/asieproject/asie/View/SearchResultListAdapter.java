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
import asieproject.asie.Model.ResourceClass;
import asieproject.asie.R;


public class SearchResultListAdapter extends ArrayAdapter<ResourceClass>{

    Context context;
    public SearchResultListAdapter(Context context, int resourceId,
                                   List<ResourceClass> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class DescHolder {
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultListAdapter.DescHolder holder = null;
        ResourceClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_result_item, null);
            holder = new SearchResultListAdapter.DescHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.search);

            convertView.setTag(holder);
        } else
            holder = (SearchResultListAdapter.DescHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.GetResourceName());

        return convertView;
    }

}
