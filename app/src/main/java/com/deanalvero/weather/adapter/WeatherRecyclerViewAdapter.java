package com.deanalvero.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deanalvero.weather.R;
import com.deanalvero.weather.model.OWMDataWeather;

import java.util.List;

/**
 * Created by dean on 08/17/16.
 */
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private List<OWMDataWeather> mItemList;

    public void setItemList(List<OWMDataWeather> itemList){
        this.mItemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        OWMDataWeather owmDataWeather = mItemList.get(position);

        holder.mTextViewMain.setText(owmDataWeather.getMain());
        holder.mTextViewDescription.setText(owmDataWeather.getDescription());
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) return 0;
        return mItemList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewMain, mTextViewDescription;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            mTextViewMain = (TextView) itemView.findViewById(R.id.textView_main);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
        }
    }
}
