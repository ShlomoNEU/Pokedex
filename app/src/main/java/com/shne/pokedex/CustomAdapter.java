package com.shne.pokedex;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shlomo on 26/10/2016.
 */

public class CustomAdapter extends BaseAdapter {
    Bitmap[] bitmaps;
    String[] names;
    Context context;

    public CustomAdapter(Bitmap[] bitmaps, String[] names, Context context) {
        this.bitmaps = bitmaps;
        this.names = names;
        this.context = context;
    }

    public CustomAdapter(ArrayList<Bitmap> bitmaps, ArrayList<String> names, Context context) {
        Bitmap[] bitmaps1 = new Bitmap[bitmaps.size()];
        String[] names1 = new String[names.size()];
        for (int i = 0; i < bitmaps.size(); i++) {
            bitmaps1[i] = bitmaps.get(i);
            names1[i] = names.get(i);
        }
        this.bitmaps = bitmaps1;
        this.names = names1;
        this.context = context;

    }


    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public Object getItem(int position) {
        return bitmaps[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.evolotion_list_item, parent, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.ListNameThumbEvo);
        TextView textView = (TextView) rootView.findViewById(R.id.ListNameEvo);
        imageView.setImageBitmap(bitmaps[position]);
        textView.setText(names[position]);
        return rootView;
    }
}
