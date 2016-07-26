package bravostudio.nyeni.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import bravostudio.nyeni.Adapter.GridAdapter;
import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Interface.NyeniNetworkInterface;
import bravostudio.nyeni.MainActivity;
import bravostudio.nyeni.Model.Feed;
import bravostudio.nyeni.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jouvyap on 7/20/16.
 */
public class HomeFragment extends Fragment {

    private View homeFragmentView;
    private SwipeRefreshLayout swipeRefresh;
    public GridView gridView;
    private GridAdapter gridAdapter;

    private Retrofit retrofit;
    private NyeniNetworkInterface nyeniNetworkInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragmentView =  inflater.inflate(R.layout.fragment_home, container, false);

        gridView = (GridView) homeFragmentView.findViewById(R.id.grid_view);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("JOUVY", "" + id);

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment(NyeniConstant.MENU_TAB.PHOTO_VIEW);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) homeFragmentView.findViewById(R.id.swiperefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callGetFeed();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(NyeniConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyeniNetworkInterface = retrofit.create(NyeniNetworkInterface.class);

        callGetFeed();

        setHasOptionsMenu(true);
        return homeFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == R.id.action_search){
            //TODO: Setting Activity here
        }
        return super.onOptionsItemSelected(item);
    }

    private void callGetFeed(){
        Call<Feed> call = nyeniNetworkInterface.getFeed();
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<String> data = response.body().getLink();
                List<String> index = response.body().getId();
                gridAdapter = new GridAdapter(getContext(), data, index);
                gridAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridAdapter);

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.d("JOUVY", "ERROR MAKING REQUEST");
                swipeRefresh.setRefreshing(false);
            }
        });

    }

}
