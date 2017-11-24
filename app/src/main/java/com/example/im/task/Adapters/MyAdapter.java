package com.example.im.task.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.im.task.Model.RideHistory;
import com.example.im.task.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Im on 21-11-2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    //this context we will use to inflate the layout
    Context context;

    //we are storing all the rides in a list
    private List<RideHistory> rideHistory;

    //getting the context and ride list with constructor
    public MyAdapter(Context context, List<RideHistory> rideHistory) {
        this.context = context;
        this.rideHistory = rideHistory;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //getting the ride of the specified position
        RideHistory ride = rideHistory.get(position);
        String time = ride.getTime();
        String[] separated = time.split("T");
        String day = separated[0];
        //Converting Date to Desired format.
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time = separated[1];
        String[] hr = time.split(":");
        String min = hr[1];
        String[] sec = min.split(":");
        min = sec[0];
        day = outputFormat.format(date);
        //binding the data with the viewholder views
        holder.name.setText("Driver :" + ride.getDriver_name());
        holder.bookingId.setText("Order Id:" + ride.getBooking_id());
        holder.date.setText("Date : " + day + "\n\nTime : " + hr[0] + ":" + min);
        Glide.with(context.getApplicationContext()).load(ride.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

    }

    //Function to get the size of Array List rideHistory.
    @Override
    public int getItemCount() {
        return rideHistory.size();
    }

    //MyHolder class describes an item View and space with the recyclerView(Finds item within cardView Layout).
    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView name, date, bookingId;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            bookingId = (TextView) itemView.findViewById(R.id.bookingId);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }
}
