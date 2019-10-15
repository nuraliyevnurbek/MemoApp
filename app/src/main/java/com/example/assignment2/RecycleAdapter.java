package com.example.assignment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignment2.db.Data;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private List<Data> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private OnDataClickListener onDataClickListener;


    public RecycleAdapter(Context context, List<Data> list, OnDataClickListener onDataClickListener) {
        this.data = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.onDataClickListener = onDataClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_for_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleInRecycleList)
        TextView titleInRecycleList;

        @BindView(R.id.imageInRecycleList)
        ImageView imageViewRecycleList;

        @BindView(R.id.dataInRecycleList)
        TextView dateInRecycleList;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(Data data) {
            titleInRecycleList.setText(data.title);
            dateInRecycleList.setText(data.date);
            Glide.with(context)
                    .load(data.url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewRecycleList);
        }

        @OnClick(R.id.linear_layout_data)
        public void onClickData() {
            onDataClickListener.onClickData(data.get(getAdapterPosition()).id);
        }
    }

    public interface OnDataClickListener {
        void onClickData(int id);
    }
}
