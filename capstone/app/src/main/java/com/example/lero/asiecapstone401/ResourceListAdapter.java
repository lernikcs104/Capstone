package com.example.lero.asiecapstone401;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lero on 1/31/18.
 */

public class ResourceListAdapter extends ArrayAdapter<Resources> {

    Context context;

    public ResourceListAdapter(Context context, int resourceId,
                            List<Resources> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class DescHolder {

        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ResourceListAdapter.DescHolder holder = null;
        Resources rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ResourceListAdapter.DescHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);

            convertView.setTag(holder);
        } else
            holder = (ResourceListAdapter.DescHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());


        return convertView;
    }
}
