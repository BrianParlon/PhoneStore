package com.example.phonefinder;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private OnItemCLickListener itemCLickListener;

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
        int amount= Integer.parseInt(uploadCurrent.getStock());
        if(amount==0){
            holder.textViewName.setText(uploadCurrent.getName() +" OUT OF STOCK");
            holder.textViewName.setTextColor(Color.RED);
            Picasso.with(itemContext)
                    .load(uploadCurrent.getImageUrl())
                    .placeholder(R.drawable.phone)
                    .fit()
                    .centerInside()
                    .into(holder.imageView);

        }else {
            holder.textViewName.setText(uploadCurrent.getName() + " €" + uploadCurrent.getPrice());
            Picasso.with(itemContext)
                    .load(uploadCurrent.getImageUrl())
                    .placeholder(R.drawable.phone)
                    .fit()
                    .centerInside()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemCLickListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    itemCLickListener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem edit = contextMenu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = contextMenu.add(Menu.NONE,2,2,"Delete");

            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(itemCLickListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch(menuItem.getItemId()){
                        case 1:
                            itemCLickListener.onEditCLick(position);
                            return true;
                            case 2:
                            itemCLickListener.onDeleteCLick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemCLickListener {
        void OnItemClick(int position);

        void onEditCLick(int position);

        void onDeleteCLick(int position);
    }
    public void setOnItemClickListener(ItemsActivity listener){
        itemCLickListener=listener;
    }

}
