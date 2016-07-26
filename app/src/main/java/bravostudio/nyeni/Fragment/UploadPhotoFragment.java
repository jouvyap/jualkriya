package bravostudio.nyeni.Fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import bravostudio.nyeni.Custom.AndroidMultipartEntity;
import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Custom.SquareImageView;
import bravostudio.nyeni.Interface.NyeniNetworkInterface;
import bravostudio.nyeni.Model.FileUpload;
import bravostudio.nyeni.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jouvyap on 7/20/16.
 */
public class UploadPhotoFragment extends Fragment {

    private View uploadPhotoFragment;
    private ProgressBar mProgressBar;
    private String mFileUri;
    private long totalSize = 0;
    private boolean isMonetized = false;

    private Retrofit retrofit;
    private NyeniNetworkInterface nyeniNetworkInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uploadPhotoFragment = inflater.inflate(R.layout.fragment_photo_upload, container, false);

        Bundle bundle = getArguments();
        mFileUri = bundle.getString(NyeniConstant.FRAGMENT_BUNDLE_FILE_URI);

        SquareImageView squareImageView = (SquareImageView) uploadPhotoFragment.findViewById(R.id
                .image);
        Picasso.with(getActivity())
                .load(new File(mFileUri))
                .resize(1000, 1000)
                .centerCrop()
                .placeholder(R.mipmap.ic_query_builder_black_48dp)
                .error(R.mipmap.ic_error_outline_black_48dp)
                .into(squareImageView);

        mProgressBar = (ProgressBar) uploadPhotoFragment.findViewById(R.id.progressBar);

        Button buttonUpload = (Button) uploadPhotoFragment.findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testUploadViaRetrofit();
//                new UploadFileToServer().execute();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(NyeniConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyeniNetworkInterface = retrofit.create(NyeniNetworkInterface.class);

        setHasOptionsMenu(true);
        return uploadPhotoFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.upload_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == R.id.monetization){
            toggleMonetization(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleMonetization(MenuItem item){
        if(!isMonetized){
            showMonetizeAlertDialog(item);
        } else{
            showCancelMonetizeAlertDialog(item);
        }
    }

    private void testUploadViaRetrofit(){
        File image = new File(mFileUri);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), image);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), imageBody);

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "Bravyto Takwa Pangukir");
        RequestBody judul = RequestBody.create(MediaType.parse("text/plain"), "Ketika senja");
        RequestBody author = RequestBody.create(MediaType.parse("text/plain"), "Jouvy");
        RequestBody medium = RequestBody.create(MediaType.parse("text/plain"), "Kanvas");
        RequestBody tanggalBuat = RequestBody.create(MediaType.parse("text/plain"), "1994/10/31");
        RequestBody dimensi = RequestBody.create(MediaType.parse("text/plain"), "100 x 100");
        RequestBody harga = RequestBody.create(MediaType.parse("text/plain"), "0");
        Call<FileUpload> call = nyeniNetworkInterface.updateUser(body, username, judul,
                author, medium, tanggalBuat, dimensi, harga);

        call.enqueue(new Callback<FileUpload>() {
            @Override
            public void onResponse(Call<FileUpload> call, Response<FileUpload> response) {
                FileUpload data = response.body();
                Log.d("JOUVY", data.getMessage());
                Log.d("JOUVY", "" + data.getError());
            }

            @Override
            public void onFailure(Call<FileUpload> call, Throwable t) {
                Log.d("JOUVY", "ERROR MAKING REQUEST");
            }
        });
    }

    private void showMonetizeAlertDialog(final MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("750000");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        builder.setView(input, 50, 0, 50, 0);

        builder.setMessage("Tentukan harga untuk hasil karya seni Anda.")
                .setTitle("Jual karya seni ini")
                .setPositiveButton("Jual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setIcon(R.mipmap.ic_monetization_on_white_24dp);
                        isMonetized = true;
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create().show();
    }

    private void showCancelMonetizeAlertDialog(final MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Batalkan penjualan untuk karya seni Anda.")
                .setTitle("Karya seni tidak dijual")
                .setPositiveButton("Tidak dijual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setIcon(R.mipmap.ic_money_off_white_24dp);
                        isMonetized = false;
                    }
                })
                .setNegativeButton("Tetap jual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create().show();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            mProgressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            mProgressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(NyeniConstant.BASE_URL + "fileUpload.php");

            try {
                AndroidMultipartEntity entity = new AndroidMultipartEntity(
                        new AndroidMultipartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(mFileUri);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
//                entity.addPart("website",
//                        new StringBody("www.androidhive.info"));
//                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Foto berhasil di-upload", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

    }
}
