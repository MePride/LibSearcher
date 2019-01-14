package libsearcher.mepride.android.librarysearcher.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.model.Situation;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{

    private Context mContext;
    private List<Situation> mSituationList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View BookView;
        @BindView(R.id.tv_barcode)
        TextView barcode;
        @BindView(R.id.tv_state)
        TextView state;
        @BindView(R.id.tv_bookNumber)
        TextView bookNumber;

        public ViewHolder(View view){
            super(view);
            BookView = view;
            ButterKnife.bind(this,view);
        }
    }
    public StateAdapter(List<Situation> situationList){
        mSituationList = situationList;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_state,parent,false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Situation situation = mSituationList.get(holder.getAdapterPosition());
//
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Situation situation = mSituationList.get(position);

        holder.bookNumber.setText(situation.getBookNumber());
        holder.barcode.setText(situation.getBarcode());
        if (situation.getState().length()!=2){
            holder.bookNumber.setTextColor(Color.GRAY);
            holder.bookNumber.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

            holder.barcode.setTextColor(Color.GRAY);
            holder.barcode.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

            holder.state.setTextColor(Color.GRAY);
            holder.state.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        }
        holder.state.setText(situation.getState());
    }
    @Override
    public int getItemCount(){
        return mSituationList.size();
    }
}

