package com.shne.pokedex;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shlomo on 19/09/2016.
 */
public class VarietiesDialog  extends DialogFragment{

    public VarietiesDialog() {
        super();
    }
    ArrayList<HashMap<String,Bitmap>> hashMap;
    @SuppressLint("ValidFragment")
    public VarietiesDialog(ArrayList<HashMap<String,Bitmap>> hashMap) {
        this.hashMap = hashMap;
    }
    VarietiesDialog dialog = this;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.varieties_dialog, null);
        ListView listView = (ListView) rootView.findViewById(R.id.DialogListView);
        String [] names = new String[hashMap.size()];
        Bitmap [] bitmaps = new Bitmap[hashMap.size()];
        for(int i = 0;i < hashMap.size();i++){
           String itratorName = hashMap.get(i).keySet().iterator().next();
            names[i] = itratorName ;
            bitmaps [i] = hashMap.get(i).get(itratorName);
        }
        CustomAdapter customAdapter = new CustomAdapter(bitmaps,names);
        listView.setAdapter(customAdapter);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView);
        return builder.create();
    }

    private class CustomAdapter extends BaseAdapter {
        Bitmap[] bitmaps;
        String [] names;
        public CustomAdapter(Bitmap[] bitmaps,String[] names){
            this.bitmaps = bitmaps;
            this.names = names;
        }
        public CustomAdapter(ArrayList<Bitmap> bitmaps, ArrayList<String> names){
            Bitmap [] bitmaps1 = new Bitmap[bitmaps.size()];
            String [] names1 = new String[names.size()];
            for(int i = 0;i<bitmaps.size();i++){
                bitmaps1[i] = bitmaps.get(i);
                names1[i] = names.get(i);
            }
            this.bitmaps = bitmaps1;
            this.names = names1;
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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rootView = inflater.inflate(R.layout.evolotion_list_item,parent,false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ListNameThumbEvo);
            TextView textView = (TextView) rootView.findViewById(R.id.ListNameEvo);
            imageView.setImageBitmap(bitmaps[position]);
            textView.setText(names[position]);

            return rootView;
        }
    }

}

