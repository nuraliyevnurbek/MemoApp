package com.example.assignment2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataActivity extends AppCompatActivity {

    @BindView(R.id.text_view_toolbar_title)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //From UI
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.date)
    EditText editTextDate;
    @BindView(R.id.title)
    EditText editTextTitle;
    @BindView(R.id.massage)
    EditText editTextMassage;
    @BindView(R.id.nextButton)
    ImageButton buttonNext;
    //For Calendar
    DatePickerDialog.OnDateSetListener setDateListener;
    final String urls[] = {"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRaV_yKUvKU_shlje02xFarRVcPPgHf5YSrY1aJQdP3EkptvshQ",
            "https://fsa.zobj.net/crop.php?r=tlDq_4z4leKwbt49le2f7Rr4GwkBPDKuuUQGlz5CYwvIXPlJ--Xi6lwrysQR30BE4Y8qAN-eAXjNO98LTMveSuzij949muVLyP98qAoO9iwh8snL6sj1qKJj7YpSBVezjenfvqAjH5N9kolRhltENZ2d38H9CHlVFjw635aPY3zgMePvc7NxrfgWpOFEhdJGuROTMSQSAnRikqcN",
            "https://galshir.com/c/wallpapers/galshir-koi-wallpaper.jpg",
            "https://fsb.zobj.net/crop.php?r=0LiQmnPDKuyyucAlpy7PE1utSA3ZafLHtgAkICjZA_CWsbcwmdP0d9QVP7Rtwq0TuUNEAVpikRthhhKA5QpLSUXyva67ZwDxFHwJhcEAsfSdR3x9hd-NaDzxrlGDRfURMOstcXfWKno1PmxRTP6plZHMfAYAzlpQ7JHouFTsJkVFnNJ0hC9Oq6hIOAid1yD2szA7-hU64kBJaKYb"
    };
    //for image selection
    private static int n = 0;
    private String urlForSave="0" ;
    private String dateForSave;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    //for DataBase
    public AppDataBase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            titleText.setText("New Memo");
        }


        //This code for DatePicker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name").build();

        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(DataActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setDateListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setDateListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = dayOfMonth + "/" + month1 + "/" + year1;
            editTextDate.setText(date);
            dateForSave = date;
        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.nextButton)
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
                .into(imageView);

    }

    public String getUrl(String url) {
        return url;
    }

    public String getDate(String date) {
        return date;
    }


    @OnClick(R.id.save)
    public void Save() {
        if (editTextMassage.getText().toString().trim().isEmpty() || editTextDate.getText().toString().trim().isEmpty() || editTextTitle.getText().toString().trim().isEmpty() || urlForSave.equals("0")) {
            Toast.makeText(DataActivity.this, "Fill all required data", Toast.LENGTH_SHORT).show();
        } else {
            Data data = new Data();
            data.massage = editTextMassage.getText().toString();
            data.title = editTextTitle.getText().toString();
            data.url = urlForSave;
            data.date = dateForSave;
            Log.i("ss", data.date);
            Log.i("sss", data.url);
            Log.i("ss", data.title);
            Log.i("sss", data.massage);
            (new Save()).execute(data);
        }
    }

    class Save extends AsyncTask<Data, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Data... data) {
            appDatabase.Dao().insertAll(data[0]);
            return "Success";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Intent intent = new Intent(DataActivity.this, MainActivity.class);
            setResult(Activity.RESULT_OK, intent);
            finish();

            editTextDate.setText("");
            editTextMassage.setText("");
            editTextTitle.setText("");
            urlForSave = urls[0];
            takePhoto(urls[0]);
        }
    }


   /* //Function for Camera
    public void takePicture(View view) {
        Intent intentTakeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentTakeImage.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intentTakeImage, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    //End of Camera code
    */
}
