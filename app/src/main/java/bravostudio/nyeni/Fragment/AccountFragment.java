package bravostudio.nyeni.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import bravostudio.nyeni.Adapter.GridAdapter;
import bravostudio.nyeni.Custom.ArchitectActivity1;
import bravostudio.nyeni.Custom.CircleImageView;
import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Custom.SharedPreferencesHelper;
import bravostudio.nyeni.MainActivity;
import bravostudio.nyeni.R;

/**
 * Created by jouvyap on 7/20/16.
 */
public class AccountFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private View accountFragmentView;
    public GridView gridView;

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
        GridAdapter gridAdapter = new GridAdapter(getContext());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment(NyeniConstant.MENU_TAB.PHOTO_VIEW);
            }
        });

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
}
