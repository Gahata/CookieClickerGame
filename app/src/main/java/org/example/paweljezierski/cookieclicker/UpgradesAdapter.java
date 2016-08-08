package org.example.paweljezierski.cookieclicker;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UpgradesAdapter extends RecyclerView.Adapter<UpgradesAdapter.MyViewHolder> {

    private List<Upgrade> upgradesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView upgradeTitle;
        public TextView upgradePrice;
        public TextView upgradeDescription;
        public TextView upgradePurchase;

        public MyViewHolder(View view) {
            super(view);
            upgradeTitle = (TextView) view.findViewById(R.id.upgrade_title);
            upgradePrice = (TextView) view.findViewById(R.id.upgrade_cost);
            upgradeDescription = (TextView) view.findViewById(R.id.upgrade_description);
            upgradePurchase = (TextView) view.findViewById(R.id.upgrade_purchase);
        }
    }

    public UpgradesAdapter(List<Upgrade> upgradesList) {
        this.upgradesList = upgradesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upgrade_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Upgrade upgrade = upgradesList.get(position);
        holder.upgradeTitle.setText(upgrade.getTitle());
        holder.upgradePrice.setText(String.valueOf(upgrade.getPrice()));
        holder.upgradeDescription.setText(upgrade.getDescription());
    }

    @Override
    public int getItemCount() {
        return upgradesList.size();
    }
}
