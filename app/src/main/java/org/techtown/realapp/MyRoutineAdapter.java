package org.techtown.realapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyRoutineAdapter extends RecyclerView.Adapter<MyRoutineAdapter.ViewHolder> {

    ArrayList<Ex> exercise;
    Context mContext;
    private ArrayList<MyData> mDataset;
    int day;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button delete_btn;
        public Button intensity_btn;
        public TextView ex_name;

        public ViewHolder(View view){
            super(view);
            delete_btn = view.findViewById(R.id.delete_btn);
            intensity_btn = view.findViewById(R.id.intensity_btn);
            ex_name = view.findViewById(R.id.Ex_name_);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteMessage(getAdapterPosition());
                }
            });
        }
    }

    public MyRoutineAdapter(ArrayList<MyData> myDataset, Context context, int day) {
        mDataset = myDataset;
        mContext = context;
        this.day = day;
    }

    @NonNull
    @Override
    public MyRoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myexercise, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRoutineAdapter.ViewHolder holder, int position) {
        holder.ex_name.setText(mDataset.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class MyData{
        public String text;
        public int pos;

        public MyData(String text, int pos){
            this.text = text;
            this.pos = pos;
        }

        public String getText() {
            return text;
        }
        public int getPos(){
            return pos;
        }
    }

    private void showDeleteMessage(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("제거");
        builder.setMessage("제거하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String message = "예 버튼이 눌렸습니다.";
                mDataset.remove(position);
                switch(day){
                    case 1:
                        exercise = ReadExerciseData(Constants.EX_SHP_KEY_day1);
                        exercise.get(mDataset.get(position).getPos()).unchoice();
                        SaveExerciseData(exercise, Constants.EX_SHP_KEY_day1);
                        break;
                    case 2:
                        exercise = ReadExerciseData(Constants.EX_SHP_KEY_day2);
                        exercise.get(mDataset.get(position).getPos()).unchoice();
                        SaveExerciseData(exercise, Constants.EX_SHP_KEY_day2);
                        break;
                    case 3:
                        exercise = ReadExerciseData(Constants.EX_SHP_KEY_day3);
                        exercise.get(mDataset.get(position).getPos()).unchoice();
                        SaveExerciseData(exercise, Constants.EX_SHP_KEY_day3);
                        break;
                    case 4:
                        exercise = ReadExerciseData(Constants.EX_SHP_KEY_day4);
                        exercise.get(mDataset.get(position).getPos()).unchoice();
                        SaveExerciseData(exercise, Constants.EX_SHP_KEY_day4);
                        break;
                }
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String message = "아니오 버튼이 눌렸습니다.";
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Ex> ReadExerciseData(String key) {
        SharedPreferences prefForEx = mContext.getSharedPreferences(key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefForEx.getString(Constants.EX_SHP_DATA_KEY, "");
        Type type = new TypeToken<ArrayList<Ex>>(){}.getType();
        ArrayList<Ex> arrayList = gson.fromJson(json, type);

        return arrayList;
    }

    private void SaveExerciseData(ArrayList<Ex> exercise, String key){
        SharedPreferences prefForEx = mContext.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForEx.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exercise);
        editor.putString(Constants.EX_SHP_DATA_KEY, json);
        editor.commit();
    }

}
