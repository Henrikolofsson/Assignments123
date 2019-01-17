package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Entities.Weather;
import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * @WeatherRVAdapter
 * @author Henrik Olofsson
 * This is an adapter that controls the individual fragments inside the recycle view,
 * that is contained within Assignment2Fragment.
 */
public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.Holder> {
    private LayoutInflater inflater;
    private List<Weather> content;
    private Controller controller;

    public WeatherRVAdapter(Context context, List<Weather> content){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content = content;
    }

    public void setContent(List<Weather> content){
        this.content = content;
        super.notifyDataSetChanged();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewgroup, int pos){
        View view = inflater.inflate(R.layout.fragment_weather_rv, viewgroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int pos){
        switch(content.get(pos).getWeatherType()){
            case "TYPE_AMBIENT_TEMPERATURE":
                holder.ivWeatherType.setImageResource(R.drawable.ic_temperature);
                break;
            case "TYPE_RELATIVE_HUMIDITY":
                holder.ivWeatherType.setImageResource(R.drawable.ic_humidity);
                break;

            case "TYPE_PRESSURE":
                holder.ivWeatherType.setImageResource(R.drawable.ic_pressure);
                break;
        }

        switch(content.get(pos).getWeatherType()){
            case "TYPE_AMBIENT_TEMPERATURE":
                holder.tvWeatherType.setText(R.string.rvTemp);
                break;

            case "TYPE_RELATIVE_HUMIDITY":
                holder.tvWeatherType.setText(R.string.rvHumi);
                break;

            case "TYPE_PRESSURE":
                holder.tvWeatherType.setText(R.string.rvPress);
                break;
        }

        switch(content.get(pos).getWeatherType()){
            case "TYPE_AMBIENT_TEMPERATURE":
                holder.tvWeatherResult.setText(content.get(pos).getWeatherResult() + "°c");
                break;

            case "TYPE_RELATIVE_HUMIDITY":
                holder.tvWeatherResult.setText(content.get(pos).getWeatherResult() + "%");
                break;

            case "TYPE_PRESSURE":
                holder.tvWeatherResult.setText(content.get(pos).getWeatherResult() + " hPa");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvWeatherType;
        private TextView tvWeatherResult;
        private ImageView ivWeatherType;

        public Holder(View itemView){
            super(itemView);
            tvWeatherType = (TextView) itemView.findViewById(R.id.tvWeatherType);
            tvWeatherResult = (TextView) itemView.findViewById(R.id.tvWeatherResult);
            ivWeatherType = (ImageView) itemView.findViewById(R.id.ivWeather);
            ivWeatherType.getLayoutParams().height = 80;
            ivWeatherType.getLayoutParams().width = 80;
        }
    }
}
