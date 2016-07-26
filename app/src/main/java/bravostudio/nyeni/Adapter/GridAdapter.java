package bravostudio.nyeni.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bravostudio.nyeni.Custom.SquareImageView;
import bravostudio.nyeni.R;

/**
 * Created by jouvyap on 7/19/16.
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;
    private List<String> mId;

    public GridAdapter(Context c, List<String> d, List<String> i) {
        mContext = c;
        mData= d;
        mId = i;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mId.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new SquareImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(mContext)
                    .load(mData.get(position))
                    .resize(500, 500)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_query_builder_black_48dp)
                    .error(R.mipmap.ic_error_outline_black_48dp)
                    .into(imageView);
        } else {
            imageView = (SquareImageView) convertView;
        }

        return imageView;
    }
}
