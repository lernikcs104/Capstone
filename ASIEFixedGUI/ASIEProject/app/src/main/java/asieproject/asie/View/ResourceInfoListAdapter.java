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

import asieproject.asie.Model.ResourceClass;
import asieproject.asie.R;

/**
 * Created by CACTUS on 3/1/2018.
 */

public class ResourceInfoListAdapter { //extends ArrayAdapter<ResourceClass> {

    /*
    Context context;
    public static final String [] titleArray = {"Call", "Website", "Description"};
    public static final Integer[] images = { R.drawable.phone,
            R.drawable.website, R.drawable.directions};

    public ResourceInfoListAdapter(Context context, int resourceId,
                           List<
                                   > items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
//    private class ViewHolder {
//        ImageView imageView;
//        TextView txtTitle;
//        TextView txtDesc;
//    }

    /*
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ResourceClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.resource_info_list_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.res_info_content);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.res_info_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.res_info_icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.GetResourceDescription());
        holder.txtTitle.setText(titleArray[position]);
        holder.imageView.setImageResource(images[position]);

        return convertView;
    }
    */
}
