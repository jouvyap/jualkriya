package bravostudio.nyeni.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bravostudio.nyeni.Adapter.GridAdapter;
import bravostudio.nyeni.Custom.ArchitectActivity1;
import bravostudio.nyeni.Custom.CircleImageView;
import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Custom.SharedPreferencesHelper;
import bravostudio.nyeni.Interface.NyeniNetworkInterface;
import bravostudio.nyeni.MainActivity;
import bravostudio.nyeni.Model.UserFeed;
import bravostudio.nyeni.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jouvyap on 7/20/16.
 */
public class AccountFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private View accountFragmentView;
    public GridView gridView;
    private GridAdapter gridAdapter;

    private Retrofit retrofit;
    private NyeniNetworkInterface nyeniNetworkInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        accountFragmentView =  inflater.inflate(R.layout.fragment_account, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        CircleImageView profileImage = (CircleImageView) accountFragmentView.findViewById(R.id
                .image_profile);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), ArchitectActivity1.class);
                startActivity(intent);
            }
        });

        Picasso.with(getActivity())
                .load(sharedPreferencesHelper.getUserImageURL())
                .placeholder(R.mipmap.ic_query_builder_black_48dp)
                .error(R.mipmap.ic_error_outline_black_48dp)
                .into(profileImage);

        TextView nameTextView = (TextView) accountFragmentView.findViewById(R.id.name_profile);
        nameTextView.setText(sharedPreferencesHelper.getUsernameLoggedIn());

        gridView = (GridView) accountFragmentView.findViewById(R.id.grid_view);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("JOUVY", "" + id);

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment(NyeniConstant.MENU_TAB.PHOTO_VIEW, "" + id);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(NyeniConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyeniNetworkInterface = retrofit.create(NyeniNetworkInterface.class);

        callGetUserFeed();

        setHasOptionsMenu(true);
        return accountFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == R.id.account_setting){
            //TODO: Setting Activity here
        } else if(i == R.id.account_logout){
            //TODO: Logout here
        }
        return super.onOptionsItemSelected(item);
    }

    private void callGetUserFeed(){
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        Call<UserFeed> call = nyeniNetworkInterface.getUserFeed(sharedPreferencesHelper.getUsernameLoggedIn());
        call.enqueue(new Callback<UserFeed>() {
            @Override
            public void onResponse(Call<UserFeed> call, Response<UserFeed> response) {
                List<String> data = response.body().getLink();
                List<String> index = response.body().getId();
                gridAdapter = new GridAdapter(getContext(), data, index);
                gridAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onFailure(Call<UserFeed> call, Throwable t) {
                Log.d("JOUVY", "ERROR MAKING REQUEST");
            }
        });
    }
}
