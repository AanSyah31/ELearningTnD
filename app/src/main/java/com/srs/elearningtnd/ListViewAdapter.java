package com.srs.elearningtnd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@SuppressWarnings({"WeakerAccess", "deprecation"})
public class ListViewAdapter extends BaseAdapter{

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<ModelList> modellist;
    ArrayList<ModelList> arrayList;

    public ViewHolder holder;
    TextView judul, tag;

    //constructor
    public ListViewAdapter(Context context, List<ModelList> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView tvJudul, tvTag;
        LinearLayout cardHolder;

    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);

            holder.tvJudul = view.findViewById(R.id.tv_judulYoutube);
            holder.tvTag = view.findViewById(R.id.tv_tagYoutube);
            holder.cardHolder = view.findViewById(R.id.cardHolder);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.tvJudul.setText(modellist.get(position).getJudul());
        holder.tvTag.setText(modellist.get(position).getTag());

        judul = view.findViewById(R.id.tv_judulYoutube);
        tag = view.findViewById(R.id.tv_tagYoutube);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        view.startAnimation(animation);

        return view;

    }

    //filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            for (ModelList model : arrayList) {
                String[] search = {
                        model.getJudul(), model.getTag()
                };
                if ((search[0].toLowerCase(Locale.getDefault()).contains(charText))|(search[1].toLowerCase(Locale.getDefault()).contains(charText))

                        |(((search[0])+" "+(search[1])).toLowerCase().contains(charText))

                        |(((search[1])+" "+(search[0])).toLowerCase().contains(charText))) {
                    modellist.add(model);
                }
                notifyDataSetChanged();
            }
        }
    }


}