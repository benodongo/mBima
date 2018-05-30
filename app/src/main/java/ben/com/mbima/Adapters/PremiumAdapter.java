package ben.com.mbima.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ben.com.mbima.R;
import ben.com.mbima.models.Clients;

public class PremiumAdapter extends RecyclerView.Adapter <PremiumAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Clients> premiums;

    public PremiumAdapter(Context context, ArrayList<Clients> premiums) {
        this.context = context;
        this.premiums = premiums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.premium_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tv_policy_no.setText(premiums.get(i).getDate());
        holder.tv_premium.setText(premiums.get(i).getPremium());
        holder.tv_policy.setText(premiums.get(i).getPolicy_type());

    }

    @Override
    public int getItemCount() {
        return premiums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_policy_no, tv_premium,tv_policy;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_policy_no = itemView.findViewById(R.id.tv_policy_no);
            tv_premium = itemView.findViewById(R.id.tv_premium);
            tv_policy = itemView.findViewById(R.id.tv_policy);
        }
    }
}
