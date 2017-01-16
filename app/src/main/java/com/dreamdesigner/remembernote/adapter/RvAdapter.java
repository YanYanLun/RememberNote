package com.dreamdesigner.remembernote.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.library.Utils.ViewHelper;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.activity.ContentActivity;
import com.dreamdesigner.remembernote.activity.HomeActivity;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.models.DataModel;
import com.dreamdesigner.remembernote.models.Level;
import com.dreamdesigner.remembernote.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishratkhan on 24/02/16.
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

    public List<DataModel> data = new ArrayList<>();
    Context mContext;

    public RvAdapter(Context con) {
        mContext = con;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RvViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, null));
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        DataModel dataModel = data.get(position);
        holder.tv.setText(dataModel.getName());
        holder.setLevel(dataModel.getLevel());
        if (dataModel.getLevel() == Level.LEVEL_THREE) {
            holder.itemView.setTag(R.id.rv_item_card, dataModel.getNote());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(DataModel item) {
        data.add(item);
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        View itemView;
        View marker;
        CardView cardView;

        public RvViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.rv_item_tv);
            marker = itemView.findViewById(R.id.marker);
            cardView = (CardView) itemView.findViewById(R.id.rv_item_card);
        }

        public void setLevel(int level) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            );
            if (level == Level.LEVEL_ONE) {
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_a));
            } else if (level == Level.LEVEL_TWO) {
                params.setMarginStart(ViewUtils.getLevelOneMargin() - ViewHelper.dip2px(mContext, 25) / 2);
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_b));
            } else if (level == Level.LEVEL_THREE) {
                params.setMarginStart(ViewUtils.getLevelTwoMargin() - ViewHelper.dip2px(mContext, 25) / 2);
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_c));
            }

            itemView.setLayoutParams(params);
        }
    }
}
