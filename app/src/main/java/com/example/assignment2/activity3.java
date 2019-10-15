package com.example.assignment2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.assignment2.db.AppDataBase;
import com.example.assignment2.db.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class activity3 extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageResult)
    ImageView imageResult;

    @BindView(R.id.titleResult)
    EditText titleResult;

    @BindView(R.id.massageResult)
    EditText massageResult;

    @BindView(R.id.dateResult)
    EditText dateResult;

    @BindView(R.id.edit)
    Button editButton;

    @BindView(R.id.text_view_toolbar_title)
    TextView titleText;

    @BindView(R.id.nextButton2)
    ImageButton nextButton;

    AppDataBase appDataBase;
    public int id;
    Intent intent;
    int b, editOrSave = 1;

    private Data data;
    private String urlForSave;
    private static int n = 0;

    final String urls[] = {"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRaV_yKUvKU_shlje02xFarRVcPPgHf5YSrY1aJQdP3EkptvshQ",
            "https://fsa.zobj.net/crop.php?r=tlDq_4z4leKwbt49le2f7Rr4GwkBPDKuuUQGlz5CYwvIXPlJ--Xi6lwrysQR30BE4Y8qAN-eAXjNO98LTMveSuzij949muVLyP98qAoO9iwh8snL6sj1qKJj7YpSBVezjenfvqAjH5N9kolRhltENZ2d38H9CHlVFjw635aPY3zgMePvc7NxrfgWpOFEhdJGuROTMSQSAnRikqcN",
            "https://galshir.com/c/wallpapers/galshir-koi-wallpaper.jpg",
            "https://fsb.zobj.net/crop.php?r=0LiQmnPDKuyyucAlpy7PE1utSA3ZafLHtgAkICjZA_CWsbcwmdP0d9QVP7Rtwq0TuUNEAVpikRthhhKA5QpLSUXyva67ZwDxFHwJhcEAsfSdR3x9hd-NaDzxrlGDRfURMOstcXfWKno1PmxRTP6plZHMfAYAzlpQ7JHouFTsJkVFnNJ0hC9Oq6hIOAid1yD2szA7-hU64kBJaKYb"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        nextButton.setVisibility(View.INVISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            titleText.setText("Edit Memos");
        }

        intent = getIntent();
        id = intent.getIntExtra("key", -1);
        appDataBase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name").build();
        (new LoadAsyncTask()).execute();
        titleResult.setEnabled(false);
        dateResult.setEnabled(false);
        massageResult.setEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    class LoadAsyncTask extends AsyncTask<Void, Void, Data> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Data doInBackground(Void... voids) {
            return appDataBase.Dao().getDataById(id);
        }

        @Override
        protected void onPostExecute(Data data) {
            super.onPostExecute(data);
            activity3.this.data = data;
            titleResult.setText(data.title);
            massageResult.setText(data.massage);
            dateResult.setText(data.date);
            urlForSave=data.url.toString();
            b = data.id;
            Glide.with(activity3.this)
                    .load(data.url)
                    .into(imageResult);

        }
    }

    @OnClick(R.id.nextButton2)
    public void setNextImageInImageView() {
        SelectImage();
    }

    public void SelectImage() {
        n++;
        takePhoto(urls[n]);
        urlForSave = urls[n];
        if (n == 3) {
            n = -1;
        }
    }

    public void takePhoto(String url) {

        Glide.with(this)
                .load(url)
                .into(imageResult);

    }

    class SaveChanges extends AsyncTask<Data, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Data... data) {
            appDataBase.Dao().updateTest(data[0].title, data[0].massage, data[0].id,data[0].url);
            return "Success";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            setResult(RESULT_OK,intent);
            finish();
//            Intent intent = new Intent(activity3.this, MainActivity.class);
//            startActivity(intent);
        }
    }

    class Delete extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... integers) {
            appDataBase.Dao().deleteByUserId(integers[0]);
            return "Success";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", 1);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @OnClick(R.id.edit)
    public void editData() {
        if (editOrSave == 1) {
            nextButton.setVisibility(View.VISIBLE);
            titleResult.setEnabled(true);
            massageResult.setEnabled(true);
            editButton.setText("Save");
            editOrSave = 0;
        } else {
            if(titleResult.getText().toString().trim().isEmpty() || massageResult.getText().toString().trim().isEmpty()){
                Toast.makeText(activity3.this,"Fill all required data",Toast.LENGTH_SHORT).show();
            }
            else {
                titleResult.setEnabled(false);
                massageResult.setEnabled(false);
                editButton.setText("Edit");
                editOrSave = 1;

                data.massage = massageResult.getText().toString();
                data.title = titleResult.getText().toString();
                data.date = dateResult.getText().toString();
                data.url=urlForSave;
                (new SaveChanges()).execute(data);
            }

        }

    }

    @OnClick(R.id.delete)
    public void delete() {
        (new Delete()).execute(b);
    }


}
