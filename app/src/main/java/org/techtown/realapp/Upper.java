package org.techtown.realapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Upper extends AppCompatActivity {
    ArrayList<Ex> exercise = new ArrayList<Ex>();

    private RecyclerView mRecylcerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UpperAdapter.MyData> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upper);
        mRecylcerView = (RecyclerView) findViewById(R.id.upper_recycler);

        mRecylcerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecylcerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        int requestCode = intent.getExtras().getInt("requestCode");

        myDataset = new ArrayList<>();
        mAdapter = new UpperAdapter(myDataset, this, requestCode);
        mRecylcerView.setAdapter(mAdapter);

        exercise = ReadExerciseData(Constants.EX_SHP_KEY_day1);

        for(int i = Constants.EX_UPPER_START; i<Constants.EX_LOWER_START; i++){
            myDataset.add(new UpperAdapter.MyData(exercise.get(i).getName(), false));
        }

        Button button = findViewById(R.id.select_upper);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    private ArrayList<Ex> ReadExerciseData(String key) {
        SharedPreferences prefForEx = getSharedPreferences(key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefForEx.getString(Constants.EX_SHP_DATA_KEY, "");
        Type type = new TypeToken<ArrayList<Ex>>(){}.getType();
        ArrayList<Ex> arrayList = gson.fromJson(json, type);

        return arrayList;
    }
}