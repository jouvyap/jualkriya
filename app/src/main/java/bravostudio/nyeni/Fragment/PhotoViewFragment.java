package bravostudio.nyeni.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bravostudio.nyeni.Custom.SquareImageView;
import bravostudio.nyeni.R;

/**
 * Created by jouvyap on 7/20/16.
 */
public class PhotoViewFragment extends Fragment {

    private View photoViewFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photoViewFragment = inflater.inflate(R.layout.fragment_photo_view, container, false);

        SquareImageView squareImageView = (SquareImageView) photoViewFragment.findViewById(R.id
                .image);
        Picasso.with(getActivity())
                .load("http://i.imgur.com/DvpvklR.png")
                .placeholder(R.mipmap.ic_query_builder_black_48dp)
                .error(R.mipmap.ic_error_outline_black_48dp)
                .into(squareImageView);

        RelativeLayout favorite = (RelativeLayout) photoViewFragment.findViewById(R.id
                .favorite_layout);
        final TextView favorite_count = (TextView) photoViewFragment.findViewById(R.id.favorite_count);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(favorite_count.getText().toString());
                count++;
                favorite_count.setText("" + count);
            }
        });

        setHasOptionsMenu(true);
        return photoViewFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.viewer_menu, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("image/*");
        myShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("http://i.imgur.com/DvpvklR.png"));
        myShareActionProvider.setShareIntent(myShareIntent);
    }
}
