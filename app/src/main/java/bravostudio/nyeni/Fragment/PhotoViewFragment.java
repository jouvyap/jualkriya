package bravostudio.nyeni.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Custom.SharedPreferencesHelper;
import bravostudio.nyeni.Custom.SquareImageView;
import bravostudio.nyeni.Interface.NyeniNetworkInterface;
import bravostudio.nyeni.Model.PhotoDataModel;
import bravostudio.nyeni.Model.PhotoViewModel;
import bravostudio.nyeni.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jouvyap on 7/20/16.
 */
public class PhotoViewFragment extends Fragment {

    private View photoViewFragment;
    private String photoId;

    private SquareImageView squareImageView;
    private TextView favoriteCount;
    private TextView photoDesc;
    private TextView photoAuthor;
    private TextView photoMedium;
    private TextView photoDate;
    private TextView photoDimension;


    private Retrofit retrofit;
    private NyeniNetworkInterface nyeniNetworkInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photoViewFragment = inflater.inflate(R.layout.fragment_photo_view, container, false);

        Bundle bundle = getArguments();
        photoId = bundle.getString(NyeniConstant.FRAGMENT_BUNDLE_PHOTO_ID);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        String username = sharedPreferencesHelper.getUsernameLoggedIn();

        squareImageView = (SquareImageView) photoViewFragment.findViewById(R.id.image);
        favoriteCount = (TextView) photoViewFragment.findViewById(R.id.favorite_count);
        photoDesc = (TextView) photoViewFragment.findViewById(R.id.photo_description);
        photoAuthor = (TextView) photoViewFragment.findViewById(R.id.photo_author);
        photoMedium = (TextView) photoViewFragment.findViewById(R.id.photo_medium);
        photoDate = (TextView) photoViewFragment.findViewById(R.id.photo_date);
        photoDimension = (TextView) photoViewFragment.findViewById(R.id.photo_dimension);

        retrofit = new Retrofit.Builder()
                .baseUrl(NyeniConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyeniNetworkInterface = retrofit.create(NyeniNetworkInterface.class);

        callGetPhoto(photoId, username);

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

    private void callGetPhoto(String id, String username){
        Call<PhotoViewModel> call = nyeniNetworkInterface.getPhoto(id, username);
        call.enqueue(new Callback<PhotoViewModel>() {
            @Override
            public void onResponse(Call<PhotoViewModel> call, Response<PhotoViewModel> response) {
                PhotoDataModel data = response.body().getData().get(0);

                String link = data.getLink();
                Picasso.with(getActivity())
                        .load(link)
                        .resize(1000, 1000)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_query_builder_black_48dp)
                        .error(R.mipmap.ic_error_outline_black_48dp)
                        .into(squareImageView);

                updateDataToScreen(data.getJudul(), data.getAuthor(), data.getMedium(), data
                        .getTanggalBuat(), data.getDimensi());
            }

            @Override
            public void onFailure(Call<PhotoViewModel> call, Throwable t) {
                Log.d("JOUVY", "ERROR MAKING REQUEST");
            }
        });
    }

    private void updateDataToScreen(String desc, String author, String medium, String date,
                                    String dimension){
        photoDesc.setText(desc);
        photoAuthor.setText(author);
        photoMedium.setText(medium);
        photoDate.setText(date);
        photoDimension.setText(dimension);
    }
}
