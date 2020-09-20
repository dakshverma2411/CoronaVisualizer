package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.ListItems;
import daksh.coronavisualizer.MainActivity;
import daksh.coronavisualizer.R;
import daksh.coronavisualizer.VisualizerActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private int numberOfItems;
    final Context context;
    private List<ListItems> entries;

    public RecyclerViewAdapter(Context context, List<ListItems> entries) {
        this.context = context;
        this.entries = entries;
        numberOfItems = entries.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListItems item = entries.get(position);
        holder.countryTextView.setText(item.getCountryName());
        holder.casesTextView.setText(String.valueOf(item.getCases()));
        holder.activeCasesTextView.setText(String.valueOf(item.getActiveCases()));
        holder.recoveredCasesTextView.setText(String.valueOf(item.getRecoveredCases()));
        String gap="";
        if(position<10)
        {
            gap=" ";
        }
        holder.listNumber.setText(String.valueOf(position+1)+"."+gap);
    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView casesTextView;
        public TextView countryTextView;
        public TextView activeCasesTextView;
        public TextView recoveredCasesTextView;
        public CardView cardView;
        public TextView listNumber;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card);
            countryTextView =(TextView) itemView.findViewById(R.id.country_name);
            countryTextView.setOnClickListener(this);
            casesTextView = (TextView) itemView.findViewById(R.id.numbers);
            activeCasesTextView = (TextView) itemView.findViewById(R.id.active_numbers);
            listNumber=(TextView) itemView.findViewById(R.id.listNumber);
            recoveredCasesTextView = (TextView) itemView.findViewById(R.id.recovered_numbers);
        }

        @Override
        public void onClick(View view) {

            int pos=getAdapterPosition();
            ListItems ITEM=entries.get(pos);
            String country=ITEM.getCountryName();
            Intent intent=new Intent(view.getContext(), VisualizerActivity.class);
            intent.putExtra("Country",country);
            view.getContext().startActivity(intent);

        }
    }
}
