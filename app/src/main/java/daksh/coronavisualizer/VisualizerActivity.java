package daksh.coronavisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import Adapter.VisualizerRecyclerViewAdapter;
import Model.VisualizerItems;

public class VisualizerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<VisualizerItems> entries;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizer_window);
        Intent intent=getIntent();
        String country=intent.getStringExtra("Country");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(country);
        actionBar.setSubtitle("Data Analysis");
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView) findViewById(R.id.visualizer_recycler_view);
        entries=new ArrayList<>();

        ArrayList<Integer> arr=new ArrayList<>();
        arr.add(1000);
        arr.add(1290);
        arr.add(2000);
        arr.add(3000);
        arr.add(4000);
        arr.add(3500);
        arr.add(4400);
        arr.add(3000);
        arr.add(1200);

        VisualizerItems item=new VisualizerItems("Total Cases",arr);
        entries.add(item);

        arr=new ArrayList<>();
        arr.add(10000);
        arr.add(12090);
        arr.add(12000);
        arr.add(23000);
        arr.add(40300);
        arr.add(23500);
        arr.add(14400);
        arr.add(13000);
        arr.add(52200);
        item=new VisualizerItems("Deaths",arr);
        entries.add(item);

        arr=new ArrayList<>();
        arr.add(12000);
        arr.add(12290);
        arr.add(32000);
        arr.add(32000);
        arr.add(45000);
        arr.add(53500);
        arr.add(64400);
        arr.add(32000);
        arr.add(51200);
        item=new VisualizerItems("Recovered Cases",arr);
        entries.add(item);

        arr=new ArrayList<>();
        arr.add(11000);
        arr.add(41290);
        arr.add(32000);
        arr.add(23000);
        arr.add(34000);
        arr.add(13500);
        arr.add(14400);
        arr.add(10000);
        arr.add(5200);
        item=new VisualizerItems("Daily Cases",arr);
        entries.add(item);

        arr=new ArrayList<>();
        arr.add(51000);
        arr.add(51290);
        arr.add(42000);
        arr.add(33000);
        arr.add(34000);
        arr.add(23500);
        arr.add(24400);
        arr.add(13000);
        arr.add(5200);
        item=new VisualizerItems("Active Cases",arr);
        entries.add(item);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VisualizerRecyclerViewAdapter adapter=new VisualizerRecyclerViewAdapter(this,entries);
        recyclerView.setAdapter(adapter);



    }
}
