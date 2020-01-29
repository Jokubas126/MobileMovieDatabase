package com.example.moviesearcher.view.details;

import android.app.Activity;
import android.content.Context;
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

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    final Handler handler = new Handler();
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
        if (people.get(position).getProfileImage() != null)
            holder.imageView.setImageBitmap(people.get(position).getProfileImage());
        else {
            holder.imageView.setImageResource(R.drawable.blank_profile_picture);
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
