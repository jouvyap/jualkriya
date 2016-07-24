package bravostudio.nyeni.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import bravostudio.nyeni.Custom.AndroidMultipartEntity;
import bravostudio.nyeni.Custom.NyeniConstant;
import bravostudio.nyeni.Custom.SharedPreferencesHelper;
import bravostudio.nyeni.Custom.SquareImageView;
import bravostudio.nyeni.R;

/**
 * Created by jouvyap on 7/20/16.
 */
public class UploadPhotoFragment extends Fragment {

    private View uploadPhotoFragment;
    private ProgressBar mProgressBar;
    private String mFileUri;
    private long totalSize = 0;
    private boolean isMonetized = false;
    private EditText judul, author, medium, tanggal, dimensi, input;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uploadPhotoFragment = inflater.inflate(R.layout.fragment_photo_upload, container, false);

        judul = (EditText) uploadPhotoFragment.findViewById(R.id.photo_description);
        author = (EditText) uploadPhotoFragment.findViewById(R.id.photo_author);
        medium = (EditText) uploadPhotoFragment.findViewById(R.id.photo_medium);
        tanggal = (EditText) uploadPhotoFragment.findViewById(R.id.photo_yearr);
        dimensi = (EditText) uploadPhotoFragment.findViewById(R.id.photo_dimension);
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
                new UploadFileToServer().execute();
            }
        });

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

    private void showMonetizeAlertDialog(final MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        input = new EditText(getActivity());
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
            HttpPost httppost = new HttpPost(NyeniConstant.BASE_URL);

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
                SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
                entity.addPart("username", new StringBody(sharedPreferencesHelper.getUsernameLoggedIn()));
                entity.addPart("judul", new StringBody(judul.getText().toString()));
                entity.addPart("author", new StringBody(author.getText().toString()));
                entity.addPart("medium", new StringBody(medium.getText().toString()));
                entity.addPart("tanggal_buat", new StringBody(tanggal.getText().toString()));
                entity.addPart("dimensi", new StringBody(dimensi.getText().toString()));
                if (isMonetized) {
                    entity.addPart("harga", new StringBody(input.getText().toString()));
                } else {
                    entity.addPart("harga", new StringBody("Tidak dijual"));
                }

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
