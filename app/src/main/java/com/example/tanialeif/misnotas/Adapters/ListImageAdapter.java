package com.example.tanialeif.misnotas.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tanialeif.misnotas.Model.Media;
import com.example.tanialeif.misnotas.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ViewHolder> {
    private ArrayList<Media> galleryListt;
    private Context context;

    private View.OnLongClickListener onLongClickListener;
    private View.OnClickListener onClickListener;

    public ListImageAdapter(Context context, ArrayList<Media> galleryListt){
        this.context = context;
        this.galleryListt = galleryListt;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public ListImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_view, viewGroup, false);

        view.setOnLongClickListener(onLongClickListener);
        view.setOnClickListener(onClickListener);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListImageAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imgItem.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(galleryListt.get(i).getIdImage().equals("R.drawable.fondo1.png")){
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path,"a.jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            viewHolder.imgItem.setImageBitmap(bitmap);
        }
        else if(galleryListt.get(i).getIdImage().startsWith("content")){
            viewHolder.imgItem.setImageURI(Uri.parse(galleryListt.get(i).getIdImage()));
        }
        else{
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path,galleryListt.get(i).getIdImage());
            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            viewHolder.imgItem.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount(){
        return galleryListt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;

        public ViewHolder(View view){
            super(view);

            imgItem = view.findViewById(R.id.imgItem);
        }

    }

    public Media getItem(int position){
        return galleryListt.get(position);
    }
}
