package asieproject.asie.View;

import android.widget.ArrayAdapter;

import asieproject.asie.Model.ResourceClass;
import asieproject.asie.R;

        import java.util.List;
        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

/**
 * Created by lero on 1/31/18.
 */

public class SearchResultListAdapter extends ArrayAdapter<ResourceClass> {
    Context context;

    public SearchResultListAdapter(Context context, int resourceId,
                            List<ResourceClass> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ResourceClass rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_result_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.search);
            //holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            //holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.GetResourceName());
        //holder.txtTitle.setText(rowItem.getTitle());
        //holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }


}

