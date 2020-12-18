package com.example.gamecollectorscatalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class GamesRecyclerViewAdapter extends RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder> {

    private List<Integer> mIdData;
    private List<String> mNameData;
    private List<String> mImageData;
    private LayoutInflater mInflater;

    GamesRecyclerViewAdapter(Context context) { this.mInflater = LayoutInflater.from(context); }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_game_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemIdView.setText(mIdData.get(position).toString());
        holder.listItemNameView.setText(mNameData.get(position));
        if (mImageData != null) {
            File imgFile = new File(mImageData.get(position));
            if (imgFile.exists())
                holder.listItemImageView.setImageURI(Uri.fromFile(imgFile));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (mIdData != null)
            return mIdData.size();
        return 0;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView listItemIdView;
        TextView listItemNameView;
        ImageView listItemImageView;

        ViewHolder(View itemView) {
            super(itemView);
            listItemIdView = itemView.findViewById(R.id.game_id);
            listItemNameView = itemView.findViewById(R.id.listItemName);
            listItemImageView = itemView.findViewById(R.id.defaultImage);
        }

    }

    // convenience method for getting data at click position
    String getItemName(int position) {
        return mNameData.get(position);
    }
    String getItemImage(int position) {
        return mImageData.get(position);
    }
    int getId(int position) { return mIdData.get(position); }

    List<String> getAllNames() { return mNameData; }
    List<String> getAllImages() { return mImageData; }
    List<Integer> getAllIds() { return mIdData; }

    public void setData(List<String> nameData, List<String> imageData, List<Integer> idData) {
        mNameData = nameData;
        mImageData = imageData;
        mIdData = idData;
        notifyDataSetChanged();
    }
}
