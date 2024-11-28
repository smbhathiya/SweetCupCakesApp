package com.example.sweetcupcakes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class CupcakeAdapter extends RecyclerView.Adapter<CupcakeAdapter.ViewHolder> {

    private final List<Cupcake> cupcakesList;

    public CupcakeAdapter(List<Cupcake> cupcakesList) {
        this.cupcakesList = cupcakesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cupcake, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cupcake cupcake = cupcakesList.get(position);
        holder.bind(cupcake);
    }

    @Override
    public int getItemCount() {
        return cupcakesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.item_price);
        }

        public void bind(Cupcake cupcake) {
            nameTextView.setText(cupcake.getItemName());
            priceTextView.setText(cupcake.getItemPrice());
            Glide.with(itemView.getContext()).load(cupcake.getImageUrl()).into(imageView);
        }
    }
}
