package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * @SensorAdapter
 * @author Henrik Olofsson
 * An adapter class to hold information about every individual fragment inside the recycler view,
 * that is contained within Assignment1Fragment.
 */
public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.Holder>{
    private LayoutInflater inflater;
    private List<String> content;
    private Controller controller;

    public SensorAdapter(Context context){
        this(context, new ArrayList<String>());
    }

    public SensorAdapter(Context context, List<String> content){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content = content;
    }

    public void setContent(List<String> content){
        this.content = content;
        super.notifyDataSetChanged();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.fragment_sensor, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvSensorName.setText(content.get(position));
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvSensorName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvSensorName = (TextView) itemView.findViewById(R.id.tvSensorName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            controller.setIndividualSensorFragment(tvSensorName.getText().toString());
        }

    }
}
