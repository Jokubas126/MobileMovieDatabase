package com.example.moviesearcher.ui.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.data.Subcategory;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryAdapter.CategoryViewHolder, CategoryAdapter.SubcategoryViewHolder> {

    private OnSubcategoryClickedListener subcategoryClickedListener;

    CategoryAdapter(List<? extends ExpandableGroup> groups, OnSubcategoryClickedListener subcategoryClickedListener) {
        super(groups);
        this.subcategoryClickedListener = subcategoryClickedListener;
    }

    interface OnSubcategoryClickedListener {
        void onSubcategoryClicked(View view, Subcategory subcategory);
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public SubcategoryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory, parent, false);
        return new SubcategoryViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubcategoryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Subcategory subcategory = (Subcategory) group.getItems().get(childIndex);
        holder.onBind(subcategory);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCategoryTitle(group);
    }

    class CategoryViewHolder extends GroupViewHolder {

        private TextView categoryTitleView;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitleView = itemView.findViewById(R.id.title_view);
        }

        void setCategoryTitle(ExpandableGroup group) {
            categoryTitleView.setText(group.getTitle());
        }
    }

    class SubcategoryViewHolder extends ChildViewHolder {

        private TextView titleView;
        private Subcategory subcategory;

        SubcategoryViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title_view);
            itemView.setOnClickListener(v -> {
                subcategoryClickedListener.onSubcategoryClicked(v, subcategory);
            });
        }

        void onBind(Subcategory subcategory) {
            titleView.setText(subcategory.getName());
            this.subcategory = subcategory;
        }
    }
}
