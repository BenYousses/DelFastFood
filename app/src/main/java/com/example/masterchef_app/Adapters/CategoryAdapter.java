package com.example.masterchef_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.UserActivities.FoodOfCategoryActvity;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CtaegoryViewHolder> {

    ArrayList<Category> categoriesLiset = new ArrayList<>();


    public CategoryAdapter() {
    }

    public ArrayList<Category> getCategoriesLiset() {
        return categoriesLiset;
    }

    public void setCategoriesLiset(ArrayList<Category> categoriesLiset) {
        this.categoriesLiset = categoriesLiset;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CtaegoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // return ViewHolder
        return new CtaegoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_view_holder,viewGroup,false));
    }



    @Override
    public void onBindViewHolder(@NonNull CtaegoryViewHolder ctaegoryViewHolder, int i) {
        Category category = categoriesLiset.get(i);
if (category != null){
    ctaegoryViewHolder.bind(category.getCategoryName());
    Picasso.get()
            .load(category.getCategoryImage())
            .placeholder(R.drawable.empty_category_icon)
            .into(ctaegoryViewHolder.categoryPic);
}
        ctaegoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FoodOfCategoryActvity.class);
                intent.putExtra("CtegoryClicked",category);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesLiset.size();
    }

    class CtaegoryViewHolder extends RecyclerView.ViewHolder{
ImageView categoryPic ;
TextView categoryTitle ;
View itemView ;
        public CtaegoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryPic = itemView.findViewById(R.id.holder_category_image);
            categoryTitle = itemView.findViewById(R.id.holder_category_title);
          this.itemView = itemView;
        }
        public void bind (String catTitle){
            categoryTitle.setText(catTitle);
        }

    }
}
