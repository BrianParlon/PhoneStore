package com.example.phonefinder;

import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context itemContext;
    private List<Upload> itemUploads;
    public ImageAdapter(Context context, List<Upload>uploads){
        itemContext=context;
        itemUploads=uploads;

    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(itemContext).inflate(R.layout.image_item,parent,false);
            return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Upload uploadCurrent = itemUploads.get(position);
            holder.textViewName.setText(uploadCurrent.getName());
            Picasso.with(itemContext)
                    .load(uploadCurrent.getImageUrl())
                    .placeholder(R.drawable.phone)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }

}
