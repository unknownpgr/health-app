package org.techtown.realapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class StartEx extends AppCompatActivity {
    TextView time;
    TextView tip;
    TextView textView_numOfEx;
    TextView textView_numOfSet;
    TextView textView_nameOfEx;
    Button btnStart;
    Button btnStop;
    Button btnNextSet;

    ArrayList<Ex> todayEx;

    TimerTask timerTask;
    Timer timer = new Timer();
    MyTimer myTimer;

    int numOfSet;
    int todayEx_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_ex);

        myTimer = new MyTimer(60000, 1000);

        time = findViewById(R.id.time);
        tip = findViewById(R.id.tip);
        textView_nameOfEx = findViewById(R.id.textView_nameOfEx);
        textView_numOfEx = findViewById(R.id.textView_numOfEx);
        textView_numOfSet = findViewById(R.id.textView_numOfSet);

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnNextSet = findViewById(R.id.btn_nextSet);
        Button btn_prev = findViewById(R.id.btn_prev);
        Button btn_next = findViewById(R.id.btn_next);

        todayEx = ReadExerciseData(Constants.EX_SHP_KEY_todayEx);
        todayEx_num = 0;

        textView_numOfSet.setText("1세트 "+"(총" +todayEx.get(todayEx_num).getSet() + "세트)");
        textView_numOfEx.setText(todayEx.get(todayEx_num).getNumber_of_times() + "회");
        textView_nameOfEx.setText(todayEx.get(todayEx_num).getName() + "");

        numOfSet = 0;

        btnNextSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfSet++;
                if(numOfSet != todayEx.get(todayEx_num).getSet()) {
                    time.setText("0 초");
                    textView_numOfSet.setText(numOfSet + 1 + "세트 " + "(총" + todayEx.get(todayEx_num).getSet() + "세트)");
                }

                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                btnNextSet.setVisibility(View.GONE);

                if(numOfSet == todayEx.get(todayEx_num).getSet()){
                    Toast.makeText(getApplicationContext(), "마지막 세트입니다.", Toast.LENGTH_LONG).show();
                    numOfSet--;
                    btnStart.setVisibility(View.GONE);
                    btnStop.setVisibility(View.GONE);
                    btnNextSet.setVisibility(View.VISIBLE);
                }
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimerTask();
                time.setText("0 초");
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                todayEx_num++;
                numOfSet = 0;

                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                btnNextSet.setVisibility(View.GONE);

                if(todayEx_num == todayEx.size()-1){
                    Toast.makeText(getApplicationContext(), "다음 운동이 없습니다.", Toast.LENGTH_SHORT).show();
                    todayEx_num = todayEx.size() - 2;
                } else {
                    textView_numOfSet.setText(numOfSet + 1 + "세트 " + "(총" + todayEx.get(todayEx_num).getSet() + "세트)");
                    textView_numOfEx.setText("세트당" + todayEx.get(todayEx_num).getNumber_of_times() + "회");
                    textView_nameOfEx.setText(todayEx.get(todayEx_num).getName() + "");
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimerTask();
                time.setText("0 초");
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                numOfSet = 0;
                todayEx_num--;

                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                btnNextSet.setVisibility(View.GONE);

                if(todayEx_num < 0){
                    Toast.makeText(getApplicationContext(), "이전 운동이 없습니다.", Toast.LENGTH_SHORT).show();
                    todayEx_num = 0;
                } else {
                    textView_numOfSet.setText(numOfSet + 1 + "세트 " + "(총" + todayEx.get(todayEx_num).getSet() + "세트)");
                    textView_numOfEx.setText("세트당" + todayEx.get(todayEx_num).getNumber_of_times() + "회");
                    textView_nameOfEx.setText(todayEx.get(todayEx_num).getName() + "");
                }
            }
        });
    }

    public class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFutere, long countDownInterval){
            super(millisInFutere, countDownInterval);
        }
        @Override
        public void onTick(long l) {
            time.setText(l/1000 + "초");
        }

        @Override
        public void onFinish() {
            time.setText("0초");
        }
    }

    protected void onDestroy()
    {
        timer.cancel();
        super.onDestroy();
    }

    public void clickHandler(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_start:
                startTimerTask();
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                btnNextSet.setVisibility(View.GONE);
                break;
            case R.id.btn_stop :
                stopTimerTask();
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnNextSet.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startTimerTask()
    {
        stopTimerTask();

        timerTask = new TimerTask()
        {
            int count = 0;

            @Override
            public void run()
            {
                count++;
                time.post(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(count + " 초");
                    }
                });
            }
        };
        timer.schedule(timerTask,0 ,1000);
    }

    private void stopTimerTask()
    {
        if(timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }
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
