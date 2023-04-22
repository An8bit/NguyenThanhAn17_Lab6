package com.example.nguyenthanhan17_lab6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoVH> implements Filterable {
   Context context;
    ArrayList<Info> infos;
    ArrayList<Info> infoFilter;
    Listener listener;
   DBHelper dbHelper;

    public InfoAdapter(ArrayList<Info> infos, Listener listener,Context context) {
        this.infos = infos;
        this.listener = listener;
        this.infoFilter= infos;
        this.context=context;
        dbHelper=new DBHelper(context);
    }

    @NonNull
    @Override
    public InfoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row,parent,false);
        return new InfoVH(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull InfoVH holder, @SuppressLint("RecyclerView") int position) {
        Info info = infoFilter.get(position);
       holder.txName.setText(info.getFname()+" "+info.getLname());
        holder.txPhone.setText(info.getPhone());
        holder.txFax.setText(info.getMail());
        String image = info.getImage();
        try {
            InputStream is =context.getAssets().open(image);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.imgFlag.setImageBitmap(bitmap);
        }catch (IOException e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(info);
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditListener(position,info);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             listener.onDeleteListener(position,info);

            }
        });


    }

    @Override
    public int getItemCount() {
       return infoFilter.size();
    }

    @Override
    public Filter getFilter() {

        return new infoFilter();
    }


    class InfoVH extends RecyclerView.ViewHolder{

        CircleImageView imgFlag;
        TextView txName,txPhone,txFax;
        ImageView ivEdit,ivDelete;
        public InfoVH(@NonNull View itemView){
            super(itemView);
            imgFlag=itemView.findViewById(R.id.imgFlag);
            txName=itemView.findViewById(R.id.txName);
            txPhone=itemView.findViewById(R.id.txPhone);
            txFax=itemView.findViewById(R.id.txFax);
            ivEdit=itemView.findViewById(R.id.imgEdit);
            ivDelete=itemView.findViewById(R.id.imgDelete);

        }
    }
   class infoFilter extends Filter{

       @Override
       protected FilterResults performFiltering(CharSequence charSequence) {
           String charString = charSequence.toString();
           if(charString.isEmpty()){
               infoFilter=infos;
           }else {
              infoFilter=dbHelper.Search(charString);
           }
           FilterResults filterResults = new FilterResults();
           filterResults.values=infoFilter;
           return filterResults;
       }

       @Override
       protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
          infoFilter=(ArrayList<Info>) filterResults.values;
          notifyDataSetChanged();
       }
   }
   public void  addInfo(Info info){
        infoFilter.add(info);
       notifyDataSetChanged();
   }
   public  void editInfo(Info info,int pos){
        infoFilter.set(pos,info);
       notifyDataSetChanged();
   }
    public  void deleteInfo(int pos){
        infoFilter.remove(pos);
        notifyDataSetChanged();
    }
    public void deleteInfo(Info info){
        infoFilter.remove(info);
        notifyDataSetChanged();
    }


    interface Listener {
        void onClickListener(Info info);
        void  onEditListener(int pos,Info info);
        void  onDeleteListener(int pos,Info info);


    }
}

