package com.example.group44.newscollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private int layoutId;
    private ArrayList<T> data;

    private OnItemClickListener onItemClickListener; //???

    public MyRecyclerViewAdapter(Context _context, int _layoutId, ArrayList<T> _data) {
        context = _context;
        layoutId = _layoutId;
        data = _data;
    }

    public abstract void convert(MyViewHolder holder, T t);
    //声明抽象方法convert

    public interface OnItemClickListener{
        void onClick(int position);
        //void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        this.onItemClickListener = _onItemClickListener;
    }

    public void addAll(ArrayList<T> a) {
        //data.addAll(a);
        data = a;
    }

    public void addItem(T a) {
        data.add(a);
    }

    //获取项
    public Object getItem(int i) {
        return data == null ? null : data.get(i);
    }
    //删除项
    public void deleteItem(int i) {
        data.remove(i);
    }

    //清空
    public void clearItem() {
        data.clear();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = MyViewHolder.get(context, parent, layoutId);
        return holder;
    }
    //重写onCreateViewHolder

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        convert(holder, data.get(position));

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    onItemClickListener.onLongClick(holder.getAdapterPosition());
//                    return true;
//                }
//            });
        }
    }
    //重写onBindViewHolder

}