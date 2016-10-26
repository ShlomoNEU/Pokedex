package com.shne.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shlomo on 26/10/2016.
 */

public class CustumFlavorAdpter extends BaseAdapter {
    String[] GameVersion, Text;
    Context context;

    public CustumFlavorAdpter(JSONObject flavors, Context context) {
        GameVersion = new String[flavors.length() + 1];
        Text = new String[flavors.length() + 1];
        GameVersion[0] = "Game";
        Text[0] = "Pokedex Enetry";
        for (int i = 0; i < flavors.length(); i++) {
            try {
                Text[i + 1] = flavors.getJSONObject(String.valueOf(i)).getString("Text");
                GameVersion[i + 1] = flavors.getJSONObject(String.valueOf(i)).getString("game version");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return Text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.twoline_listitem, parent, false);
        TextView Title = (TextView) rootView.findViewById(R.id.GameVersion);
        TextView SubTitle = (TextView) rootView.findViewById(R.id.FlavorText);
        Title.setText(GameVersion[position]);
        SubTitle.setText(Text[position]);
        return rootView;

    }
}
