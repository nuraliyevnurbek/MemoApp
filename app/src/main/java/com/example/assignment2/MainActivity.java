package com.example.assignment2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.assignment2.db.AppDataBase;
import com.example.assignment2.db.Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RecycleAdapter.OnDataClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;

    @BindView(R.id.image2)
    ImageView imagaOnMainPage;

    @BindView(R.id.noMemo)
    TextView noMemo;

    @BindView(R.id.textOnMainPage2)
    TextView textOnMainPage2;

    @BindView(R.id.button1)
    ImageButton button1;
    public AppDataBase appDataBase;

    @BindView(R.id.recycle)
    RecyclerView recyclerView;


    private List<Data> dataList = new ArrayList<>();
    private final int requestCodeCreate = 0;
    private final int requestCodeEdit = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appDataBase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name").build();
        (new LoadAsyncTask()).execute();

    }

    @OnClick(R.id.button1)
    public void nextActivity() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivityForResult(intent, requestCodeCreate);
    }

    @Override
    public void onClickData(int id) {
        Intent intent = new Intent(this, activity3.class);
        intent.putExtra("key", id);
        startActivityForResult(intent, requestCodeEdit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeEdit) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Editted", Toast.LENGTH_LONG).show();
                (new LoadAsyncTask()).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == requestCodeCreate) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Created", Toast.LENGTH_LONG).show();
                (new LoadAsyncTask()).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }
    }


    class LoadAsyncTask extends AsyncTask<Void, Void, List<Data>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Data> doInBackground(Void... voids) {
            return appDataBase.Dao().getAll();
        }

        @Override
        protected void onPostExecute(List<Data> data) {
            super.onPostExecute(data);
            dataList = data;
            if (dataList.isEmpty()) {
                imagaOnMainPage.setVisibility(View.VISIBLE);
                noMemo.setVisibility(View.VISIBLE);
                textOnMainPage2.setVisibility(View.VISIBLE);
            } else {
                imagaOnMainPage.setVisibility(View.INVISIBLE);
                noMemo.setVisibility(View.INVISIBLE);
                textOnMainPage2.setVisibility(View.INVISIBLE);
            }
            RecycleAdapter adapter = new RecycleAdapter(MainActivity.this, data, MainActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);

        }
    }


    class DeleteAll extends AsyncTask<Data, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Data... data) {
            appDataBase.Dao().deleteAllData();
            return "Success";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            if (dataList.isEmpty()) {
                Toast.makeText(MainActivity.this, "Storage is already cleared", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "All data are deleted", Toast.LENGTH_LONG).show();
            }
            (new LoadAsyncTask()).execute();
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mybutton) {
            (new DeleteAll()).execute();
        }
        return super.onOptionsItemSelected(item);
    }


}


