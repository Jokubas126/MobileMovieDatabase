package com.example.moviesearcher.view.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.databinding.ItemPersonBinding;
import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.util.ConverterUtil;
import com.example.moviesearcher.model.util.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private final List<Person> people = new ArrayList<>();

    public void updatePeopleList(List<Person> list){
        people.clear();
        people.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPersonBinding view = DataBindingUtil.inflate(inflater, R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setPerson(people.get(position));
    }



    @Override
    public int getItemCount() {
        return people.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemPersonBinding itemView;

        ViewHolder(@NonNull ItemPersonBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
