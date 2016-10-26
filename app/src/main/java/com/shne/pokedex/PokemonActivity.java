package com.shne.pokedex;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PokemonActivity extends AppCompatActivity {
    public static final String ARG_NAME = "ARG_NAME";
    public static final String ARG_NUM = "ARG_Num";

    //For Floating Action Circle
    private boolean isOpen = false;


    private Pokemon MainPokemon;
    private Dialog progressDialog;
    private TextView HightTX, WieghtTX, TitleTX;
    private ImageView Type1, Type2, MainPic;
    private ListView Evo1ListView, Evo2ListView, Evo3ListView, DexEntriesListView;
    private boolean Evo2Flag = false, Evo3Flag = false;
    private CustomAdapter EVO1A, EVO2A, EVO3A;
    private CustumFlavorAdpter Adpter;
    private Context context;
    private View RootView;
    private FloatingActionButton main_fab, browser_fab, varieties_fab;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            HandlerControler(msg.what);
            super.handleMessage(msg);
        }
    };

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MainPokemon = new Pokemon(getIntent().getExtras().getString(ARG_NAME), getIntent().getExtras().getInt(ARG_NUM));
        RootView = getLayoutInflater().inflate(R.layout.activity_pokemon, null);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.customprogressdialoglayout);
        progressDialog.show();

        ActionBar mActionBar = this.getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle("#" + MainPokemon.getID() + " " + MainPokemon.getName());

        setView();
        setOnClickListeners();
        setOnClickListeners();

        main_fab.setVisibility(View.VISIBLE);
        setContentView(RootView);
        RootView.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();

    }

    void setView() {
        //TextViews
        HightTX = (TextView) RootView.findViewById(R.id.HeightDataN);
        WieghtTX = (TextView) RootView.findViewById(R.id.WeightDataN);
        TitleTX = (TextView) RootView.findViewById(R.id.NewMainTitle);

        //ImageView
        Type1 = (ImageView) RootView.findViewById(R.id.Type1N);
        Type2 = (ImageView) RootView.findViewById(R.id.Type2N);
        MainPic = (ImageView) RootView.findViewById(R.id.NewMainPic);

        //ListViews
        DexEntriesListView = (ListView) RootView.findViewById(R.id.PokdexEntryListN);
        Evo1ListView = (ListView) RootView.findViewById(R.id.Evo1LN);
        Evo2ListView = (ListView) RootView.findViewById(R.id.Evo2LN);
        Evo3ListView = (ListView) RootView.findViewById(R.id.Evo3LN);
        Evo3ListView.setVisibility(View.VISIBLE);
        Evo2ListView.setVisibility(View.VISIBLE);
        Evo1ListView.setVisibility(View.VISIBLE);

        //Floating Action Circle
        browser_fab = (FloatingActionButton) RootView.findViewById(R.id.open_BrowserN);
        main_fab = (FloatingActionButton) RootView.findViewById(R.id.main_fabN);
        varieties_fab = (FloatingActionButton) RootView.findViewById(R.id.var_fabN);


    }

    void setOnClickListeners() {

        varieties_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Varieties_dialog();
            }
        });
        browser_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://www.serebii.net/pokedex-xy/" + MainPokemon.getNumberToString() + ".shtml";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_fab.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_360));
                if (isOpen) {
                    browser_fab.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_fab));
                    if (MainPokemon.getVarieties().size() > 0) {
                        varieties_fab.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_fab));
                        varieties_fab.setClickable(false);
                    }
                    browser_fab.setClickable(false);
                    browser_fab.setClickable(false);
                    isOpen = false;
                } else {
                    browser_fab.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_fab));
                    if (MainPokemon.getVarieties().size() > 0) {
                        varieties_fab.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_fab));
                        varieties_fab.setClickable(true);
                    }
                    browser_fab.setClickable(true);
                    isOpen = true;
                }
            }
        });


        Evo1ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Snackbar snackbar;
                snackbar = Snackbar.make(RootView, "", Snackbar.LENGTH_INDEFINITE);
                final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                snackbartext.setVisibility(View.INVISIBLE);
                LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                Button Snackbar_Yes, Snackbar_No;
                TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                snackbarLayout.addView(snackView, 0);
                textView.setText(R.string.Open_Pokemon_Data);
                textView.setTextColor(Color.WHITE);
                Snackbar_Yes.setTextColor(Color.WHITE);
                Snackbar_No.setTextColor(Color.WHITE);
                Snackbar_No.setText(R.string.No);
                Snackbar_Yes.setText(R.string.Yes);
                Snackbar_No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                Snackbar_Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("PokeID", MainPokemon.getEVOID());
                        intent.putExtra("PokeIString", MainPokemon.getEVONAME());
                        setResult(RESULT_OK, intent);
                        finish();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });


        Evo2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Snackbar snackbar;
                snackbar = Snackbar.make(RootView, "", Snackbar.LENGTH_INDEFINITE);
                final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                snackbartext.setVisibility(View.INVISIBLE);
                LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                Button Snackbar_Yes, Snackbar_No;
                TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                snackbarLayout.addView(snackView, 0);
                textView.setText(R.string.open_Pokemon_Data);
                textView.setTextColor(Color.WHITE);
                Snackbar_Yes.setTextColor(Color.WHITE);
                Snackbar_No.setTextColor(Color.WHITE);
                Snackbar_No.setText(R.string.No);
                Snackbar_Yes.setText(R.string.Yes);
                Snackbar_No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                Snackbar_Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("PokeID", MainPokemon.getEVO2ID().get(position));
                        intent.putExtra("PokeIString", MainPokemon.getEVO2Name().get(position));
                        setResult(RESULT_OK, intent);
                        finish();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        Evo3ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Snackbar snackbar;
                snackbar = Snackbar.make(RootView, "", Snackbar.LENGTH_INDEFINITE);
                final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                snackbartext.setVisibility(View.INVISIBLE);
                LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                Button Snackbar_Yes, Snackbar_No;
                TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                snackbarLayout.addView(snackView, 0);
                textView.setText(R.string.open_Pokemon_Data);
                textView.setTextColor(Color.WHITE);
                Snackbar_Yes.setTextColor(Color.WHITE);
                Snackbar_No.setTextColor(Color.WHITE);
                Snackbar_No.setText(R.string.No);
                Snackbar_Yes.setText(R.string.Yes);
                Snackbar_No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                Snackbar_Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("PokeID", MainPokemon.getEVO3ID().get(position));
                        intent.putExtra("PokeIString", MainPokemon.getEVO3Name().get(position));
                        setResult(RESULT_OK, intent);
                        finish();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }

    void open_Varieties_dialog() {
        VarietiesDialog varietiesDialog = new VarietiesDialog(MainPokemon.getVarieties());
        varietiesDialog.show(getFragmentManager(), "Tag");
    }

    void getData() {
        try {
            MainPokemon.setThumb(BitmapFactory.decodeStream((new URL("http://www.serebii.net/art/th/" + MainPokemon.getID() + ".png")).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL("https://hotimg-f397a.firebaseapp.com/ID" + MainPokemon.getID() + "");
            InputStream is = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject MainJsonObject = new JSONObject(jsonText);

            URL url2 = new URL("https://hotimg-f397a.firebaseapp.com/New/ID" + MainPokemon.getID() + ".txt");
            InputStream is2 = url2.openStream();
            BufferedReader rd2 = new BufferedReader(new InputStreamReader(is2, Charset.forName("UTF-8")));
            String jsonText2 = readAll(rd2);
            JSONObject MainJsonObject2 = new JSONObject(jsonText2);

            //Now AllData Been Downloaded

            //Filling General Data From Json
            MainPokemon.setHeight((float) (MainJsonObject.getInt("height")) / 10 + " m");
            MainPokemon.setWeight((float) (MainJsonObject.getInt("weight")) / 10 + " kg");
            MainPokemon.setPokemon_type(new String[]{MainJsonObject.getJSONObject("type").getString("1"), MainJsonObject.getJSONObject("type").getString("2")});

            //Getting varieties
            if (MainJsonObject.getJSONObject("varieties").length() > 1) {
                ArrayList<HashMap<String, Bitmap>> hashMaps = MainPokemon.getVarieties();
                JSONObject object = MainJsonObject.getJSONObject("varieties");
                for (int i = 0; i < MainJsonObject.getJSONObject("varieties").length(); i++) {
                    HashMap<String, Bitmap> hashMap = new HashMap<>();
                    String name = object.getJSONObject(String.valueOf(i)).getString("name");
                    try {
                        String Url = ("https://img.pokemondb.net/artwork/" + name + ".jpg").replace(" ", "");
                        Bitmap bitmap = BitmapFactory.decodeStream(new URL(Url).openStream());
                        hashMap.put(name, bitmap);
                        hashMaps.add(hashMap);
                    } catch (Exception e) {
                        hashMaps = new ArrayList<HashMap<String, Bitmap>>();
                    }
                }
                MainPokemon.setVarieties(hashMaps);
            }
            //Getting Pokemon Type
            String[] ss = MainPokemon.getPokemon_type();
            if (ss[1].isEmpty()) {
                MainPokemon.setType1(BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/" + ss[0] + ".gif").openStream())));
            } else {
                MainPokemon.setType1(BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/" + ss[0] + ".gif").openStream())));
                MainPokemon.setType2(BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/" + ss[1] + ".gif").openStream())));
            }

            //Getting Evolotions
            for (int i = 0; i < MainJsonObject.getJSONObject("Evoltion").length(); i++) {
                try {
                    MainPokemon.setEVOID(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getString("name").split("@URL")[1].split("/")[6]);
                    MainPokemon.setEVONAME(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getString("name").split("@URL")[0].replace("name:", ""));
                    MainPokemon.setEVOCouse("Base Evolotion");
                    String tempoo = ("http://www.serebii.net/art/th/" + MainPokemon.getEVOID() + ".png").replace(" ", "");
                    EVO1A = new CustomAdapter(new Bitmap[]{BitmapFactory.decodeStream(new URL(tempoo).openStream())}, new String[]{MainPokemon.getEVONAME()}, context);
                } catch (JSONException e) {
                    try {
                        ArrayList<String> tmpID = new ArrayList<String>();
                        ArrayList<String> tmpName = new ArrayList<String>();
                        ArrayList<String> tmpEvo = new ArrayList<String>();
                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        for (int x = 0; x < MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).length(); x++) {
                            tmpID.add(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getString("name").split("@URL")[1].split("/")[6]);
                            tmpName.add(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getString("name").split("@URL")[0].replace("name:", "").replace("name", ""));
                            String tmpo = ("http://www.serebii.net/art/th/" + tmpID.get(x) + ".png").replace(" ", "");
                            bitmaps.add(BitmapFactory.decodeStream(new URL(tmpo).openStream()));
                            JSONObject jsonObject = (JSONObject) MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getJSONArray("evolution_details").get(0);
                            Iterator<String> iterator = jsonObject.keys();

                            while (iterator.hasNext()) {
                                String tmp = iterator.next();
                                tmpEvo.add(tmp + " " + jsonObject.getString(tmp));
                            }
                        }
                        if (i == 1) {
                            MainPokemon.setEVO2ID(tmpID);
                            MainPokemon.setEVO2Name(tmpName);
                            MainPokemon.setEVO2Couse(tmpEvo);
                            EVO2A = new CustomAdapter(bitmaps, tmpName, context);
                            Evo2Flag = true;

                        } else if (i == 2) {
                            MainPokemon.setEVO3ID(tmpID);
                            MainPokemon.setEVO3Name(tmpName);
                            MainPokemon.setEVO3Couse(tmpEvo);
                            EVO3A = new CustomAdapter(bitmaps, tmpName, context);
                            Evo3Flag = true;
                        }
                    } catch (Exception e1) {
                    }

                    Adpter = new CustumFlavorAdpter(MainJsonObject2.getJSONObject("Flavor Text"), context);
                    mHandler.sendEmptyMessage(0);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    void HandlerControler(int what) {

        switch (what) {
            case 0:
                SetListViewAdapters();
                setGenarlData();
                SetPics();
                SetListViewAdapters();
                setOnClickListeners();
                progressDialog.dismiss();
                RootView.setVisibility(View.VISIBLE);
                SetListViewAdapters();
                break;
        }

    }

    void setGenarlData() {
        HightTX.setText(MainPokemon.getHeight());
        WieghtTX.setText(MainPokemon.getWeight());
        TitleTX.setText(MainPokemon.getName());
    }

    void SetPics() {
        if (MainPokemon.getType1() != null) {
            Type1.setImageBitmap(MainPokemon.getType1());
        } else {
            Type1.setVisibility(View.INVISIBLE);
            Type1.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
        }
        if (MainPokemon.getType2() != null) {
            Type2.setImageBitmap(MainPokemon.getType2());
        } else {
            Type2.setVisibility(View.INVISIBLE);
            Type2.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
        }

        MainPic.setImageBitmap(MainPokemon.getThumb());
    }

    void SetListViewAdapters() {
        Evo1ListView.setAdapter(EVO1A);
        Evo1ListView.deferNotifyDataSetChanged();
        if (Evo2Flag) {
            Evo2ListView.setAdapter(EVO2A);
            Evo2ListView.deferNotifyDataSetChanged();
        } else {
        }
        if (Evo3Flag) {
            Evo3ListView.setAdapter(EVO3A);
            Evo3ListView.deferNotifyDataSetChanged();
        } else {

        }
        DexEntriesListView.setAdapter(Adpter);
        Evo3ListView.deferNotifyDataSetChanged();
        Evo2ListView.deferNotifyDataSetChanged();
        Evo1ListView.deferNotifyDataSetChanged();
        DexEntriesListView.deferNotifyDataSetChanged();
    }
}
