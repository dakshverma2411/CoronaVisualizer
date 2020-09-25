package daksh.coronavisualizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapter.RecyclerViewAdapter;
import Model.ListItems;
import Utils.NetworkUtils;
import Utils.sortByActiveCases;
import Utils.sortByCountryName;
import Utils.sortByRecoveredCases;
import Utils.sortByTotalCases;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // TODO(2) add a activity for news api and implement
    // TODO(3) add detailed activity
    // TODO(4) implement the search bar
    // TODO(5) add the notification feature using services -- last priority
    private final int LOADER_ID = 11;
    private RecyclerView recyclerView;
    private ArrayList<ListItems> entries;
    public String BUNDLE_ID="Query";
    private final static String PREFS_STRING="PREFS";
    private SharedPreferences myPrefs;
    private ProgressBar progressBar;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        entries=new ArrayList<>();
        Log.i("debuggin","oncreate called");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        SharedPreferences prefs=getSharedPreferences(PREFS_STRING,0);
        if(prefs.contains("result"))
        {
            String result = prefs.getString("result","");
            updateRecyclerView(result);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i("debuggin","onResume called");
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void getData()
    {
        Log.i("speed","in getData");
        Bundle queryBundle=new Bundle();
        queryBundle.putString(BUNDLE_ID,"https://api.covid19api.com/summary");
        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<String> searchLoader=loaderManager.getLoader(LOADER_ID);
        if(searchLoader==null)
        {
            loaderManager.initLoader(LOADER_ID,queryBundle,this);
        }
        else
        {
            loaderManager.restartLoader(LOADER_ID,queryBundle, this);
        }

    }

    public void updateRecyclerView(String result) {
        if (result != null && !result.equals("")) {
            entries.clear();
            SharedPreferences prefs=getSharedPreferences(PREFS_STRING,0);
            int sortID;
            if(!prefs.contains("sort order"))
            {
                sortID=R.id.radioButtonAlpha;
            }
            else
            {
                sortID=prefs.getInt("sort order",R.id.radioButtonAlpha);
            }
            Log.i("query", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Countries");
                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    String CountryName = jsonPart.getString("Country");
                    String TotalCases = jsonPart.getString("TotalConfirmed");
                    String TotalRecovereds = jsonPart.getString("TotalRecovered");
                    int TotalCase = Integer.parseInt(TotalCases);
                    int TotalRecovered = Integer.parseInt(TotalRecovereds);
                    int TotalDeath = Integer.parseInt(jsonPart.getString("TotalDeaths"));
                    int ActiveCase = TotalCase - TotalRecovered - TotalDeath;
                    entries.add(new ListItems(TotalCase, ActiveCase, TotalRecovered, CountryName));
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }
            entries = sortEntries(entries,sortID);
            setDataFromEntries(entries);
        }
    }

    public void setDataFromEntries(ArrayList<ListItems> entries)
    {
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,entries);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<ListItems> sortEntries(ArrayList<ListItems> entries,int sortID)
    {
        switch (sortID)
        {
            case R.id.radioButtonAlpha:
                Collections.sort(entries,new sortByCountryName());
                break;

            case R.id.radioButtonTotal:
                Collections.sort(entries,new sortByTotalCases());
                break;
            case R.id.radioButtonActive:
                Collections.sort(entries,new sortByActiveCases());
                break;

            case R.id.radioButtonRecovered:
                Collections.sort(entries,new sortByRecoveredCases());
                break;
        }
        return entries;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.sorting_button:
                popupDialog();
                break;
            case R.id.settings_button:
                Toast.makeText(this,"move to settings screen",Toast.LENGTH_SHORT).show();
                break;
            case R.id.refresh:
                entries.clear();
                getData();
                break;


        }
        return true;
    }

    public void popupDialog()
    {
        alertDialogBuilder=new AlertDialog.Builder(this);
        final View view=getLayoutInflater().inflate(R.layout.sort_by_popup,null);
        radioGroup=(RadioGroup) view.findViewById(R.id.sortRadioGroup);
        SharedPreferences prefs=getSharedPreferences(PREFS_STRING,0);
        if(prefs.contains("sort order"))
        {
            radioGroup.check(prefs.getInt("sort order",R.id.radioButtonAlpha));
        }
        String result="";
        if(prefs.contains("result"))
        {
            result=prefs.getString("result","");

        }
        alertDialogBuilder.setView(view);
        alertDialog=alertDialogBuilder.create();
        alertDialog.show();
        final String finalResult = result;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                myPrefs=getSharedPreferences(PREFS_STRING,0);
                final SharedPreferences.Editor editor=myPrefs.edit();
                switch(i)
                {
                    case R.id.radioButtonAlpha:

                        editor.putInt("sort order",i);
                        alertDialog.dismiss();
                        break;

                    case R.id.radioButtonTotal:
                        editor.putInt("sort order",i);
                        alertDialog.dismiss();
                        break;

                    case R.id.radioButtonActive:

                        editor.putInt("sort order",i);
                        alertDialog.dismiss();

                        break;

                    case R.id.radioButtonRecovered:
                        editor.putInt("sort order",i);
                        alertDialog.dismiss();
                        break;
                }

                editor.commit();

                updateRecyclerView(finalResult);

            }
        });


    }

    @Override
    public Loader<String> onCreateLoader(int id,final  Bundle args) {
        return new AsyncTaskLoader<String>(this){
            @Override
            protected void onStartLoading() {
                Log.i("speed","on start loading"+String.valueOf(entries.size()));
                if(args==null)
                {
                    return;
                }
                if(entries.size()==0) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    forceLoad();
                }
                super.onStartLoading();

            }

            @Override
            public String loadInBackground() {

                String queryURL=args.getString(BUNDLE_ID);
                String result;
                if(queryURL==null || TextUtils.isEmpty(queryURL))
                {
                    return null;
                }
                else
                {
                    try {
                        URL url=new URL(queryURL);
                        result=NetworkUtils.getResponseFromHttpUrl(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }
                Log.i("speed Checking","getting string data complete");
                return result;
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {

        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        myPrefs=getSharedPreferences(PREFS_STRING,0);
        SharedPreferences.Editor editor=myPrefs.edit();
        editor.putString("result",result);
        editor.commit();
        updateRecyclerView(result);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}