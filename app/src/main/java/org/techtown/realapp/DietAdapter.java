package org.techtown.realapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    private ArrayList<MyData> mDataset;
    private ArrayList<Ex> exercise;
    private Context mcontext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CheckBox cbSelect;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);
            cbSelect = (CheckBox) view.findViewById(R.id.check_exercise);

            int pos = getAdapterPosition();
        }
    }

    public DietAdapter(ArrayList<MyData> myDataset, Context mcontext) {
        mDataset = myDataset;
        mcontext = mcontext;
    }

    @Override
    public DietAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercisekind, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position).text);
        holder.cbSelect.setChecked(mDataset.get(position).isSelected());
        holder.cbSelect.setVisibility(View.VISIBLE);
        holder.cbSelect.setOnCheckedChangeListener(null);

        exercise = ReadExerciseData();

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDataset.get(position).setSelected(b);

                if(mDataset.get(position).isSelected){
//                    Upper.upperEx[position].choosed = 1;
                    exercise.get(position).choice();
                }else{
//                    Upper.upperEx[position].choosed = 0;
                    exercise.get(position).unchoice();
                }
            }
        });
    }

    public int getItemCount() {
        return mDataset.size();
    }

    static class MyData{
        public String text;
        public boolean isSelected;

        public MyData(String text, boolean isSelected) {
            this.text = text;
            this.isSelected = isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    private void SaveExerciseData(ArrayList<Ex> exercise){
        SharedPreferences preferences = mcontext.getSharedPreferences(Constants.EX_SHP_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exercise);
        editor.putString(Constants.EX_SHP_DATA_KEY, json);
        editor.commit();
    }

    private ArrayList<Ex> ReadExerciseData() {
        SharedPreferences sharedpref = mcontext.getSharedPreferences(Constants.EX_SHP_KEY, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedpref.getString(Constants.EX_SHP_DATA_KEY, "");
        Type type = new TypeToken<ArrayList<Ex>>(){}.getType();
        ArrayList<Ex> arrayList = gson.fromJson(json, type);

        return arrayList;
    }
}