package com.example.moviesearcher.view.details;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.util.JsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private Activity activity;
    private final List<Person> people;

    PeopleAdapter(Activity activity, List<Person> people) {
        this.activity = activity;
        this.people = people;
    }

    void updatePeopleList(List<Person> list){
        people.clear();
        people.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameView.setText(people.get(position).getName());
        holder.positionView.setText(people.get(position).getPosition());
        if (people.get(position).getProfileImageUrl() != null){
            Thread thread = new Thread(() -> {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(JsonUtil.getInstance().getProfileImageUrl(people.get(position).getProfileImageUrl())).openStream());
                        activity.runOnUiThread(() -> holder.imageView.setImageBitmap(bitmap));
                } catch (IOException e) { e.printStackTrace(); }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }



    @Override
    public int getItemCount() {
        return people.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private TextView positionView;
        private ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.person_name);
            positionView = itemView.findViewById(R.id.person_position);
            imageView = itemView.findViewById(R.id.person_image_view);
        }
    }
}
