package daksh.coronavisualizer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Country name");

        pieChart = findViewById(R.id.pie_chart);
        ArrayList<PieEntry> pieChartEntries = new ArrayList<>();
        pieChartEntries.add(new PieEntry(290000,"Recovered"));
        pieChartEntries.add(new PieEntry(67000,"Deaths"));
        pieChartEntries.add(new PieEntry(80000,"Active Cases"));


        final int[] MY_COLORS = {Color.rgb(0,200,0), Color.rgb(255,0,0), Color.rgb(0,0,180)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);



        PieDataSet pieDataSet= new PieDataSet(pieChartEntries,"Details");
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLineColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("India");
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setCenterTextSize(20);
        pieChart.setHoleColor(R.color.grey);
        pieChart.getLegend().setTextColor(Color.WHITE);
        pieChart.getLegend().setEnabled(false);
        pieChart.animate();


    }
}
