package daksh.coronavisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import Adapter.VisualizerRecyclerViewAdapter;
import Model.VisualizerItems;
import Utils.NetworkUtils;

public class VisualizerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    // TODO(1) change link to specific country
    RecyclerView recyclerView;
    private String RECOVERED_URL="https://api.covid19api.com/country/india/status/recovered/live?from=2020-08-10T00:00:00Z&to=2020-09-21T00:00:00Z";
    private String DEATHS_URL="https://api.covid19api.com/country/india/status/deaths/live?from=2020-08-10T00:00:00Z&to=2020-09-21T00:00:00Z";
    private String TOTAL_URL="https://api.covid19api.com/country/india/status/confirmed/live?from=2020-08-10T00:00:00Z&to=2020-09-21T00:00:00Z";
    ArrayList<VisualizerItems> entries;
    private String VISUALIZER_BUNDLE_ID="VIS_BUNDLE_ID";
    private int VISUALIZER_LOADER_ID=111;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizer_window);
        Intent intent=getIntent();
        String country=intent.getStringExtra("Country");
        Log.i("visualizerrrr",country);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(country);
        actionBar.setSubtitle("Data Analysis");
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView) findViewById(R.id.visualizer_recycler_view);
        entries=new ArrayList<>();
        getData();
        progressBar = (ProgressBar) findViewById(R.id.visualizer_progress_bar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public ArrayList<VisualizerItems> getEntries(ArrayList<String> result)
    {

        try {
            VisualizerItems itemTotal=getVisualizerItemFromResult(result.get(0),"Total Cases");
            VisualizerItems itemRecovered=getVisualizerItemFromResult(result.get(1),"Recovered Cases");
            VisualizerItems itemDeaths=getVisualizerItemFromResult(result.get(2),"Deaths");
            VisualizerItems itemActiveCases=getActiveCases(itemTotal,itemRecovered,itemDeaths);
            VisualizerItems itemDailyCases=getDailyCases(itemTotal);
            ArrayList<VisualizerItems> entri=new ArrayList<>();
            entri.add(itemTotal);
            entri.add(itemRecovered);
            entri.add(itemDeaths);
            entri.add(itemActiveCases);
            entri.add(itemDailyCases);;
            return entri;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public VisualizerItems getDailyCases(VisualizerItems totalCases){

        ArrayList<Integer> total=totalCases.getPastData();
        ArrayList<Integer> daily=new ArrayList<>();
        daily.add(total.get(1)-total.get(0));
        for(int i=0;i<total.size()-1;i++)
        {
            daily.add(total.get(i + 1) - total.get(i));
        }

        return new VisualizerItems("Daily Cases",daily);
    }



    public VisualizerItems getActiveCases(VisualizerItems totalCases,VisualizerItems recoveredCases,VisualizerItems deathCases)
    {
        ArrayList<Integer> activeCases=new ArrayList<>();
        ArrayList<Integer> total=totalCases.getPastData();
        ArrayList<Integer> recovered=recoveredCases.getPastData();
        ArrayList<Integer> deaths=deathCases.getPastData();

        for(int i=0;i<total.size();i++)
        {
            Log.i("activeee",String.valueOf(total.get(i)));
            activeCases.add(total.get(i)-recovered.get(i)-deaths.get(i));
        }
        return new VisualizerItems("Active Cases",activeCases);
    }

    public VisualizerItems getVisualizerItemFromResult(String result,String ItemName) throws JSONException {
        if(!result.equals(""))
        {
            Log.i("visualizerrrr","i am here");
            JSONArray jsonArray=new JSONArray(result);
            ArrayList<Integer> arr=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonPart=jsonArray.getJSONObject(i);
                String Cases=jsonPart.getString("Cases");
                String Date=jsonPart.getString("Date");
                arr.add(Integer.parseInt(Cases));
            }
            return new VisualizerItems(ItemName,arr);

        }
        return null;
    }

    public void updateVisualizerRecyclerView(ArrayList<VisualizerItems> entries) throws JSONException {

        VisualizerRecyclerViewAdapter adapter=new VisualizerRecyclerViewAdapter(this,entries);
        recyclerView.setAdapter(adapter);

    }

    public void getData()
    {
        Bundle queryBundle=new Bundle();
        queryBundle.putString(VISUALIZER_BUNDLE_ID,RECOVERED_URL);
        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<ArrayList<String>> searchLoader=loaderManager.getLoader(VISUALIZER_LOADER_ID);
        if(searchLoader==null)
        {
            loaderManager.initLoader(VISUALIZER_LOADER_ID,queryBundle,this);
        }
        else
        {
            loaderManager.restartLoader(VISUALIZER_LOADER_ID,queryBundle, this);
        }

    }

    @NonNull
    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<ArrayList<String>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
                Log.i("visualizerrrr","started Loading");
                if(args==null)
                {
                    return;
                }
                forceLoad();
            }

            @Override
            public ArrayList<String> loadInBackground() {
                String queryURL=args.getString(VISUALIZER_BUNDLE_ID);
                String result_total="";
                String result_recovered="";
                String result_deaths="";
                if(queryURL==null || TextUtils.isEmpty(queryURL))
                {
                    return null;
                }
                else
                {
                    try {
                        URL url_total=new URL(TOTAL_URL);
                        URL url_recovered=new URL(RECOVERED_URL);
                        URL url_deaths=new URL(DEATHS_URL);
                        result_total= NetworkUtils.getResponseFromHttpUrl(url_total);
                        result_recovered= NetworkUtils.getResponseFromHttpUrl(url_recovered);
                        result_deaths=NetworkUtils.getResponseFromHttpUrl(url_deaths);
                        ArrayList<String> result=new ArrayList<>();
                        result.add(result_total);
                        result.add(result_recovered);
                        result.add(result_deaths);
                        return result;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> result) {

        entries.clear();
        progressBar.setVisibility(View.INVISIBLE);
        entries=getEntries(result);
        try {
            updateVisualizerRecyclerView(entries);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {

    }
}
