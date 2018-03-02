package asieproject.asie.View;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asieproject.asie.Model.CategoryClass;
import asieproject.asie.R;

/**
 * Created by CACTUS on 2/11/2018.
 */

public class ListArrayAdapter extends ArrayAdapter<CategoryClass> {
    Context context;

    public ListArrayAdapter(Context context, int resourceId,
                            List<CategoryClass> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CategoryClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getCategoryName());
        //holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getCategoryImage());

        return convertView;
    }
}
