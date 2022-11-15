package com.example.ssj.gpswell;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by ssj on 18/03/18.
 */

public class wellSpecAdapter extends RecyclerView.Adapter<wellSpecAdapter.ViewHolder> {

    private final String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.well_specs);
        }
    }

    public wellSpecAdapter(String[] wellspecs) {
        mDataset = wellspecs;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //create a new view.
        View v  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_view, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view(invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(mDataset[position]);
    }

    // Return the size of the dataset,
    public int getItemCount() {
        return mDataset.length;
    }
}
