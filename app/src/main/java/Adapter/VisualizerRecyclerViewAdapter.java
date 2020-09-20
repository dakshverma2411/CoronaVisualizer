package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import Model.VisualizerItems;
import daksh.coronavisualizer.R;
import daksh.coronavisualizer.VisualizerActivity;

class GetColors{
    public static int getColor(String s){

        switch (s)
        {
            case "Total Cases": return Color.GRAY;
            case "Active Cases": return Color.YELLOW;
            case "Recovered Cases": return Color.GREEN;
            case "Deaths": return Color.RED;
            case "Daily Cases": return Color.MAGENTA;
        }

        return Color.WHITE;
    }

}

public class VisualizerRecyclerViewAdapter extends RecyclerView.Adapter<VisualizerRecyclerViewAdapter.VisualizerViewHolder> {

    Context context;
    ArrayList<VisualizerItems> entries;

    public VisualizerRecyclerViewAdapter(Context context, ArrayList<VisualizerItems> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public VisualizerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visulaizer_row,parent,false);
        return new VisualizerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisualizerViewHolder holder, int position) {
        VisualizerItems item=entries.get(position);
        holder.graphName.setText(item.getStr());
        holder.graphName.setTextColor(Color.WHITE);
        ArrayList<Entry> yData=new ArrayList<>();
        for(int i=0;i<item.getPastData().size();i++)
        {
            yData.add(new Entry(i,item.getPastData().get(i)));
        }
        ArrayList<ILineDataSet> xData=new ArrayList<>();

        LineDataSet lineDataSet =new LineDataSet(yData,"this is the label");
        lineDataSet.setLineWidth(4f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColors(GetColors.getColor(item.getStr()));

        xData.add(lineDataSet);

        LineData data = new LineData(xData);

        holder.lineChart.getXAxis().setDrawGridLines(false);
        holder.lineChart.getAxisLeft().setDrawGridLines(false);
        holder.lineChart.getAxisRight().setDrawGridLines(false);
        holder.lineChart.getAxisRight().setEnabled(false);

        holder.lineChart.getAxisLeft().setEnabled(false);
        holder.lineChart.getXAxis().setEnabled(false);
        holder.lineChart.getLegend().setEnabled(false);
        holder.lineChart.setTouchEnabled(false);
        holder.lineChart.setData(data);
        holder.lineChart.invalidate();

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class VisualizerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LineChart lineChart;
        TextView graphName;
        public VisualizerViewHolder(@NonNull View itemView) {
            super(itemView);
            graphName=(TextView) itemView.findViewById(R.id.graph_name);
            lineChart=(LineChart) itemView.findViewById(R.id.graph_in_card);
            graphName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            Intent a = new Intent(context, VisualizerActivity.class);
//            view.getContext().startActivity(new Intent(view.getContext(), VisualizerActivity.class));
            Toast.makeText(context,graphName.getText().toString()+" Clicked", Toast.LENGTH_SHORT).show();

        }
    }
}
