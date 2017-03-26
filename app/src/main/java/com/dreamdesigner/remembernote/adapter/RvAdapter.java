package com.dreamdesigner.remembernote.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamdesigner.library.Utils.ViewHelper;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.models.Note;
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
        if (dataModel.getLevel() == Level.LEVEL_THREE) {
            if (dataModel.getNote().getType() == 2) {
                holder.tv.setText(dataModel.getNote().getTime() + " " + mContext.getString(R.string.voice_note));
            } else if (dataModel.getNote().getType() == 3) {
                holder.tv.setText(dataModel.getNote().getTime() + " " + mContext.getString(R.string.video_note));
            } else {
                holder.tv.setText(dataModel.getNote().getTime() + " " + dataModel.getNote().getTitle());
            }
        } else {
            holder.tv.setText(dataModel.getName());
        }
        holder.setLevel(dataModel.getLevel(), dataModel.getNote());
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
        RelativeLayout marker;
        CardView cardView;
        ImageView icon_write_comment;

        public RvViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.rv_item_tv);
            marker = (RelativeLayout) itemView.findViewById(R.id.marker);
            cardView = (CardView) itemView.findViewById(R.id.rv_item_card);
            icon_write_comment = (ImageView) itemView.findViewById(R.id.icon_write_comment);
        }

        public void setLevel(int level, Note note) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            );
            if (level == Level.LEVEL_ONE) {
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_a));
                icon_write_comment.setVisibility(View.GONE);
            } else if (level == Level.LEVEL_TWO) {
                params.setMarginStart(ViewUtils.getLevelOneMargin() - ViewHelper.dip2px(mContext, 25) / 2);
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_b));
                icon_write_comment.setVisibility(View.GONE);
            } else if (level == Level.LEVEL_THREE) {
                params.setMarginStart(ViewUtils.getLevelTwoMargin() - ViewHelper.dip2px(mContext, 25) / 2);
                marker.setBackground(ContextCompat.getDrawable(mContext, R.drawable.marker_c));
                icon_write_comment.setVisibility(View.VISIBLE);
                if (note.getType() == 2) {
                    icon_write_comment.setImageDrawable(mContext.getDrawable(R.drawable.v_anim3));
                } else if (note.getType() == 3) {
                } else {
                    icon_write_comment.setImageDrawable(mContext.getDrawable(R.mipmap.icon_write_comment));
                }
            }

            itemView.setLayoutParams(params);
        }
    }
}
