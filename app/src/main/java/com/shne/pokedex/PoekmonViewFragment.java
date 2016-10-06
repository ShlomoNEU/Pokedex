package com.shne.pokedex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Shlomo on 24-Aug-16.
 */
public class PoekmonViewFragment extends AppCompatActivity {

    public static final String ARG_NAME = "ARG_NAME";
    public static final String ARG_NUM = "ARG_Num";
    private static final String HASHBitMap_Key_Type1_Pic = "Type1_Pic";
    private static final String HASHBitMap_Key_Type2_Pic = "Type2_Pic";
    private static final String HASHBitMap_Key_Title_Pic = "Title_Pic";
    // Handler
    private static final String Handler_ProgressDialog_Title = "ProgressDialog_Title";
    private ImageView TitleIV,Type1,Type2;
    private TextView TitleTX,HeightTX,WeightTX;
    private View rootView;
    private FloatingActionButton fab;
    private boolean Evo2Flag=false,Evo3Flag=false;
    private ProgressDialog progressDialog;
    private Context context;
    private HashMap<String,Bitmap> bitmapHashMap;
    private int background;
    private ListView EVO1,EVO2,EVO3;
    private CustomAdapter EVO1A,EVO2A,EVO3A;
    private Pokemon pokemon;
    private int mProgressStatus = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            data.getString(Handler_ProgressDialog_Title);
            switch (msg.what) {
                case 1:
                    //UpdateView
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    parms.weight = 1;
                    if (bitmapHashMap.get(HASHBitMap_Key_Type1_Pic) != null) {
                        Type1.setImageBitmap(bitmapHashMap.get(HASHBitMap_Key_Type1_Pic));
                    } else {
                        Type1.setVisibility(View.INVISIBLE);
                        Type1.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
                    }
                    if (bitmapHashMap.get(HASHBitMap_Key_Type2_Pic) != null) {
                        Type2.setImageBitmap(bitmapHashMap.get(HASHBitMap_Key_Type2_Pic));
                    } else {
                        Type2.setVisibility(View.INVISIBLE);
                        Type2.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
                    }
                    TitleIV.setImageBitmap(bitmapHashMap.get(HASHBitMap_Key_Title_Pic));
                    HeightTX.setText(pokemon.getHeight());
                    WeightTX.setText(pokemon.getWeight());

                    EVO1.setAdapter(EVO1A);
                    EVO1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Snackbar snackbar;
                            snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_INDEFINITE);
                            final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                            TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                            snackbartext.setVisibility(View.INVISIBLE);
                            LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                            Button Snackbar_Yes, Snackbar_No;
                            TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                            Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                            Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                            snackbarLayout.addView(snackView, 0);
                            textView.setText("Open Pokemon Data");
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
                                    intent.putExtra("PokeID", pokemon.getEVOID());
                                    intent.putExtra("PokeIString", pokemon.getEVONAME());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    });
                    parms.weight = 1;
                    int i = 1;
                    if (Evo2Flag) {
                        EVO2.setAdapter(EVO2A);
                        EVO2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final Snackbar snackbar;
                                snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_INDEFINITE);
                                final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                                TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                                snackbartext.setVisibility(View.INVISIBLE);
                                LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                                Button Snackbar_Yes, Snackbar_No;
                                TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                                Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                                Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                                snackbarLayout.addView(snackView, 0);
                                textView.setText("Open Pokemon Data");
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
                                        intent.putExtra("PokeID", pokemon.getEVO2ID().get(position));
                                        intent.putExtra("PokeIString", pokemon.getEVO2Name().get(position));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                            }
                        });
                        EVO2.deferNotifyDataSetChanged();
                    } else {
                        EVO2.setVisibility(View.INVISIBLE);
                        EVO2.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
                        i++;
                    }
                    if (Evo3Flag) {
                        EVO3.setAdapter(EVO3A);

                        EVO3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final Snackbar snackbar;
                                snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_INDEFINITE);
                                final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                                TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                                snackbartext.setVisibility(View.INVISIBLE);
                                LinearLayout snackView = (LinearLayout) getLayoutInflater().inflate(R.layout.customsnackbar, null);
                                Button Snackbar_Yes, Snackbar_No;
                                TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
                                Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
                                Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
                                snackbarLayout.addView(snackView, 0);
                                textView.setText("Open Pokemon Data");
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
                                        intent.putExtra("PokeID", pokemon.getEVO3ID().get(position));
                                        intent.putExtra("PokeIString", pokemon.getEVO3Name().get(position));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                            }
                        });
                        EVO3.deferNotifyDataSetChanged();
                    } else {
                        EVO3.setVisibility(View.INVISIBLE);
                        EVO3.setLayoutParams(new AppBarLayout.LayoutParams(0, 0));
                        i++;
                    }
                    if (pokemon.getVarieties().size() > 0) {
                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VarietiesDialog varietiesDialog = new VarietiesDialog(pokemon.getVarieties());
                                varietiesDialog.show(getFragmentManager(), "Tag");
                            }
                        });
                    }


                    Evo2Flag = Evo3Flag = false;
                    progressDialog.dismiss();
                    rootView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    if (mProgressStatus >= 85 && mProgressStatus <= 100) {
                        mProgressStatus -= 85;
                    } else {
                        mProgressStatus = mProgressStatus;
                    }
                    progressDialog.setProgress(mProgressStatus);
                    break;
                default:
                    break;
            }
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        bitmapHashMap = new HashMap<String, Bitmap>();
        pokemon = new Pokemon(getIntent().getExtras().getString(ARG_NAME),getIntent().getExtras().getInt(ARG_NUM));
        LayoutInflater inflater = getLayoutInflater();

        //View and View Elements Initializer
        rootView = inflater.inflate(R.layout.pokemon_activity,null);
        TitleIV = (ImageView) rootView.findViewById(R.id.TitleImage);
        Type1 = (ImageView) rootView.findViewById(R.id.Type1);
        Type2 = (ImageView) rootView.findViewById(R.id.Type2);
        TitleTX  = (TextView) rootView.findViewById(R.id.Pokemon_name);
        HeightTX  = (TextView) rootView.findViewById(R.id.Pokemon_Height);
        WeightTX = (TextView) rootView.findViewById(R.id.Pokemon_Weight);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_back);
        fab.setVisibility(View.INVISIBLE);
        Type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Go to Power Table for info PowerInfo",Toast.LENGTH_LONG).show();
            }
        });
        Type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Go to Power Table for info PowerInfo",Toast.LENGTH_LONG).show();
            }
        });
        //Setting Some Elements
        TitleTX.setText(pokemon.getName());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                GetOnlineData();
            }
        })).start();

        EVO1 = (ListView) rootView.findViewById(R.id.Evo1ListView);
        EVO2 = (ListView) rootView.findViewById(R.id.Evo2ListView);
        EVO3 = (ListView) rootView.findViewById(R.id.Evo3ListView);

        setContentView(rootView);
        rootView.setVisibility(View.INVISIBLE);
    }

    void GetOnlineData(){
        try {
            bitmapHashMap.put(HASHBitMap_Key_Title_Pic,BitmapFactory.decodeStream((new URL("http://www.serebii.net/art/th/"+pokemon.getID()+".png")).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mHandler.sendEmptyMessage(2);
            URL url = new URL("https://hotimg-f397a.firebaseapp.com/ID"+pokemon.getID());
            //URL url = new URL("https://hotimg-f397a.firebaseapp.com/New/ID341.txt");
            InputStream is  = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject MainJsonObject = new JSONObject(jsonText);

            updateProgress(5);

            pokemon.setHeight((float)(MainJsonObject.getInt("height"))/10+"m");
            pokemon.setWeight((float)(MainJsonObject.getInt("weight"))/10+"kg");
            pokemon.setPokemon_type(new String[]{MainJsonObject.getJSONObject("type").getString("1"),MainJsonObject.getJSONObject("type").getString("2")});

            updateProgress(10);

            pokemon.setThumb(BitmapFactory.decodeStream((new URL(("http://www.serebii.net/art/th/"+pokemon.getID()+".png").replace(" ","")).openStream())));
            if(MainJsonObject.getJSONObject("varieties").length()>1){
                ArrayList<HashMap<String,Bitmap>> hashMaps = pokemon.getVarieties();
                JSONObject object = MainJsonObject.getJSONObject("varieties");
                for (int i = 0; i < MainJsonObject.getJSONObject("varieties").length(); i++, updateProgress(mProgressStatus++)) {
                    HashMap<String,Bitmap> hashMap = new HashMap<>();
                    String name = object.getJSONObject(String.valueOf(i)).getString("name");
                    try {
                        String Url = ("https://img.pokemondb.net/artwork/" + name + ".jpg").replace(" ", "");
                        Bitmap bitmap = BitmapFactory.decodeStream(new URL(Url).openStream());
                        hashMap.put(name, bitmap);
                        hashMaps.add(hashMap);
                    }catch (Exception e){
                        hashMaps = new ArrayList<HashMap<String, Bitmap>>();
                    }
                }
                pokemon.setVarieties(hashMaps);
            }

            String [] ss = pokemon.getPokemon_type();
            if(ss[1].isEmpty()){
                bitmapHashMap.put(HASHBitMap_Key_Type1_Pic, BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/" + ss[0] + ".gif").openStream())));
            }else {
                bitmapHashMap.put(HASHBitMap_Key_Type1_Pic,BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/"+ss[0]+".gif").openStream())));
                bitmapHashMap.put(HASHBitMap_Key_Type2_Pic,BitmapFactory.decodeStream((new URL("http://www.serebii.net/pokedex-bw/type/"+ss[1]+".gif").openStream())));
            }

            updateProgress(mProgressStatus += 3);
            for (int i = 0; i < MainJsonObject.getJSONObject("Evoltion").length(); i++, updateProgress(mProgressStatus++)) {
                try {
                    pokemon.setEVOID(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getString("name").split("@URL")[1].split("/")[6]);
                    pokemon.setEVONAME(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getString("name").split("@URL")[0].replace("name:", ""));
                    pokemon.setEVOCouse("Base Evolotion");
                    String tempoo = ("http://www.serebii.net/art/th/" + pokemon.getEVOID() + ".png").replace(" ", "");
                    EVO1A = new CustomAdapter(new Bitmap[]{BitmapFactory.decodeStream(new URL(tempoo).openStream())}, new String[]{pokemon.getEVONAME()});
                }catch (JSONException e){
                    try {
                        ArrayList<String> tmpID = new ArrayList<String>();
                        ArrayList<String> tmpName = new ArrayList<String>();
                        ArrayList<String> tmpEvo = new ArrayList<String>();
                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        for (int x = 0; x < MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).length(); x++) {
                            tmpID.add(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getString("name").split("@URL")[1].split("/")[6]);
                            tmpName.add(MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getString("name").split("@URL")[0].replace("name:", "").replace("name", ""));
                            mProgressStatus += 1;
                            mHandler.sendEmptyMessage(2);
                            String tmpo = ("http://www.serebii.net/art/th/"+tmpID.get(x)+".png").replace(" ","");
                            bitmaps.add(BitmapFactory.decodeStream(new URL(tmpo).openStream()));
                            mProgressStatus += 1;
                            mHandler.sendEmptyMessage(2);
                            JSONObject jsonObject = (JSONObject) MainJsonObject.getJSONObject("Evoltion").getJSONObject(String.valueOf(i)).getJSONObject(String.valueOf(x)).getJSONArray("evolution_details").get(0);
                            Iterator<String> iterator =  jsonObject.keys();
                            mProgressStatus += 1;
                            mHandler.sendEmptyMessage(2);
                            while (iterator.hasNext()){
                                mProgressStatus += 1;
                                mHandler.sendEmptyMessage(2);
                                String tmp = iterator.next();
                                tmpEvo.add( tmp + " "+jsonObject.getString(tmp));
                            }

                        }
                        if (i == 1) {
                            pokemon.setEVO2ID(tmpID);
                            pokemon.setEVO2Name(tmpName);
                            pokemon.setEVO2Couse(tmpEvo);
                            EVO2A = new CustomAdapter(bitmaps,tmpName);
                            Evo2Flag = true;

                        } else if(i==2){
                            pokemon.setEVO3ID(tmpID);
                            pokemon.setEVO3Name(tmpName);
                            pokemon.setEVO3Couse(tmpEvo);
                            EVO3A = new CustomAdapter(bitmaps,tmpName);
                            Evo3Flag = true;
                        }
                    }catch (Exception e1){
                    }
                }
            }
            updateProgress(100);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(1);
    }

    void updateProgress(int prog) {
        mProgressStatus = prog;
        mHandler.sendEmptyMessage(2);
    }

    private class CustomAdapter extends BaseAdapter{
        Bitmap [] bitmaps;
        String [] names;
        public CustomAdapter(Bitmap[] bitmaps,String[] names){
            this.bitmaps = bitmaps;
            this.names = names;
        }
        public CustomAdapter(ArrayList<Bitmap> bitmaps,ArrayList<String> names){
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
            LayoutInflater inflater = getLayoutInflater();
            View rootView = inflater.inflate(R.layout.evolotion_list_item,parent,false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ListNameThumbEvo);
            TextView textView = (TextView) rootView.findViewById(R.id.ListNameEvo);
            imageView.setImageBitmap(bitmaps[position]);
            textView.setText(names[position]);

            return rootView;
        }
    }
}

