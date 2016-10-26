package com.shne.pokedex;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shlomo on 24-Aug-16.
 *
 */
public class Pokedex_List extends Fragment {
    View rootView; // Globlizing the MainView
    int  clickcounter =0;
    int cnt = 0,num=0,num1=0;
    int [] location;
    ArrayList<NativeExpressAdView> ads;
    Context context;
    HashMap<Integer,String> integerStringHashMap;
    HashMap<Integer,Integer>ReversLocation;
    HashMap<String,Integer> stringIntegerHashMap;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem = 0;
    private ArrayList<Pokemon> Main_Pokemons_Object, PokemonListObject;
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private Snackbar snackbar;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (cnt >= 720) {
                        Toast.makeText(getActivity(), "Done Refrash", Toast.LENGTH_SHORT).show();
                        floatingActionButton.hide();
                        progressDialog.dismiss();
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Save To File");
                        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save for one time Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog1 = new ProgressDialog(context);
                                progressDialog1.setMax(721);
                                progressDialog1.setTitle("Saving Files");
                                progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog1.show();
                                new Thread(SaveToFileBitmaps).start();
                            }
                        });
                        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Don't Want to Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        progressDialog.show();
                    }

                    break;
                case 2:
                    cnt++;
                    progressDialog.setProgress(cnt);
                    listView.setAdapter(new CustomList(Main_Pokemons_Object));
                    listView.deferNotifyDataSetChanged();

                    break;
                case 3:
                    listView.setAdapter(new CustomList(Main_Pokemons_Object));
                    listView.deferNotifyDataSetChanged();
                    break;
                case 4:
                    num1++;
                    progressDialog1.setProgress(num1);
                    if (num1 >= 720 && progressDialog1.isShowing()) {
                        progressDialog1.dismiss();
                    }
                    break;
                default:
                    num++;
                    if (num <= 4) {
                        floatingActionButton.setImageResource(R.drawable.ic_sync_black);
                    } else {
                        floatingActionButton.hide();
                    }

            }

        }
    };
    Runnable runnable300 = new Runnable() {
        @Override
        public void run() {
            CustomDownload(150, 300);
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(1);


        }
    };
    Runnable runnable150 = new Runnable() {
        @Override
        public void run() {
            new Thread(runnable300).start();
            CustomDownload(0, 150);
            handler.sendEmptyMessage(1);

        }
    };
    Runnable runnableEND = new Runnable() {
        @Override
        public void run() {
            CustomDownload(600, Main_Pokemons_Object.size());
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(1);
        }
    };
    Runnable runnable600 = new Runnable() {
        @Override
        public void run() {
            new Thread(runnableEND).start();
            CustomDownload(450, 600);
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(1);

        }
    };
    Runnable runnable450 = new Runnable() {
        @Override
        public void run() {
            new Thread(runnable600).start();
            CustomDownload(300, 450);
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(1);

        }
    };
    Runnable SaveToFileBitmaps = new Runnable() {
        @Override
        public void run() {

            for (int i = 0; i < Main_Pokemons_Object.size(); i++) {
                String FILENAME = Main_Pokemons_Object.get(i).getNumberToString() + ".png";
                File file = new File(getActivity().getFilesDir(), FILENAME);
                try {
                    FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    if (Main_Pokemons_Object.get(i).getThumb() != null && fos != null) {
                        Main_Pokemons_Object.get(i).getThumb().compress(Bitmap.CompressFormat.PNG, 100, fos);
                        handler.sendEmptyMessage(4);
                    }
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    //    public static final String ARG_position = "ARG_postion";
    private EditText SearchET;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-4305314132412937~7895429800");

        rootView = inflater.inflate(R.layout.pokedex_list, null);
        context = getActivity();
        //Configure Base Of view//
        integerStringHashMap = new HashMap<>();
        stringIntegerHashMap = new HashMap<>();
        ReversLocation = new HashMap<>();
        location = new int[73];
        location[location.length - 1] = 720;

        AdView adView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        for (int i = 1; i < location.length; i++) {
            int x = getRandomInt(10 * (i - 1), i * 10);
            if (x % 2 != 0) {
                x--;
            }
            location[i - 1] = x;
            ReversLocation.put(x, i - 1);
        }
        adCretor();

        Main_Pokemons_Object = ArrayOfPokemon();
        PokemonListObject = Main_Pokemons_Object;
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        android.support.v7.app.ActionBar actionBar = ((MainActivity)this.getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setShowHideAnimationEnabled(true);
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(OpenFromFile(Main_Pokemons_Object.get(0).getNumberToString()+".png"),0,OpenFromFile(Main_Pokemons_Object.get(0).getNumberToString()+".png").length);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(OpenFromFile(Main_Pokemons_Object.get(Main_Pokemons_Object.size()-1).getNumberToString()+".png"),0,OpenFromFile(Main_Pokemons_Object.get(Main_Pokemons_Object.size()-1).getNumberToString()+".png").length);
            if ((bitmap != null) && (bitmap1 != null))
                for(int i= 0;i<Main_Pokemons_Object.size();i++){

                    Main_Pokemons_Object.get(i).setThumb(BitmapFactory.decodeByteArray(OpenFromFile(Main_Pokemons_Object.get(i).getNumberToString()+".png"),0,OpenFromFile(Main_Pokemons_Object.get(i).getNumberToString()+".png").length));
                    floatingActionButton.hide();
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        listView = (ListView) rootView.findViewById(R.id.pokemonList);
        listView.setAdapter(new CustomList(PokemonListObject));


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(721);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        /////////////////////////

        //SnackBar Confutation//

        snackbar = Snackbar.make(container,"",Snackbar.LENGTH_INDEFINITE);
        final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView snackbartext = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        snackbartext.setVisibility(View.INVISIBLE);

        LinearLayout snackView = (LinearLayout) inflater.inflate(R.layout.customsnackbar, null);

        Button Snackbar_Yes,Snackbar_No;
        TextView textView = (TextView) snackView.findViewById(R.id.textViewSnackBar);
        Snackbar_Yes = (Button) snackView.findViewById(R.id.snackbar_action_left);
        Snackbar_No = (Button) snackView.findViewById(R.id.snackbar_action_right);
        snackbarLayout.addView(snackView,0);
        textView.setText(R.string.Download_Sprites);
        textView.setTextColor(Color.WHITE);
        Snackbar_Yes.setTextColor(Color.WHITE);
        Snackbar_No.setTextColor(Color.WHITE);
        Snackbar_No.setText(R.string.No);
        Snackbar_Yes.setText(R.string.Yes);
        Snackbar_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButton.show();
                snackbar.dismiss();
            }
        });
        Snackbar_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                if(Main_Pokemons_Object.get(0).getThumb() != null && Main_Pokemons_Object.get(Main_Pokemons_Object.size()-1).getThumb() != null  ) {
                    Toast.makeText(getActivity(),"Already Downloaded",Toast.LENGTH_SHORT).show();

                }else if(connectivityManager.getActiveNetworkInfo().isConnected() && connectivityManager.getActiveNetworkInfo() != null) {

                    new Thread(runnable150).start();
                    new Thread(runnable450).start();
                    progressDialog.setTitle("Getting Data...");
                    progressDialog.show();
                    clickcounter =0;
                }else{
                    Toast.makeText(getActivity(),"Unable to get internet Connection",Toast.LENGTH_SHORT).show();
                }
                floatingActionButton.show();
                snackbar.dismiss();
            }
        });
        ///////////////////////
        //Fab Confutation//

        floatingActionButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.show();
                floatingActionButton.hide();
            }
        });


        ///////////////////////

        SearchET = (EditText) rootView.findViewById(R.id.SearchET);
        SearchET.clearFocus();
        final LinearLayout SearchLayout = (LinearLayout) rootView.findViewById(R.id.SearchLayout);
        ImageView searchIV = (ImageView) rootView.findViewById(R.id.SearchIV);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchET.clearFocus();
                ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
                if (SearchET.getText().toString().isEmpty()) {
                    pokemons = Main_Pokemons_Object;
                } else {
                    try {
                        int id = Integer.parseInt(SearchET.getText().toString());
                        for (Pokemon p : Main_Pokemons_Object) {
                            if (String.valueOf(id).equals(String.valueOf(p.getID())) && p.getName() != "Ad") {
                                pokemons.add(p);
                            }
                        }
                    } catch (Exception e) {
                        for (Pokemon p : Main_Pokemons_Object) {
                            if (String.valueOf(p.getName().toLowerCase()).contains(SearchET.getText().toString().toLowerCase()) && p.getName() != "Ad") {
                                pokemons.add(p);
                            }
                        }
                    }
                }
                pokemons.add(new Pokemon("Ad",0));
                PokemonListObject = pokemons;
                listView.setAdapter(new CustomList(PokemonListObject));
                listView.deferNotifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new ListItemListner());
        final Animation close_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.close_view);
        final Animation open_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.open_view);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final ListView lw = listView;
                if (scrollState == 0)
                    Log.i("a", "scrolling stopped...");
                if (view.getId() == lw.getId()) {
                    final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
                    if (currentFirstVisibleItem > mLastFirstVisibleItem && scrollState == SCROLL_STATE_FLING) {
                        mIsScrollingUp = false;
                        SearchLayout.setAnimation(close_animation);
                        SearchLayout.setVisibility(View.GONE);
                        Log.i("a", "scrolling down...");
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;
                        SearchLayout.setAnimation(open_animation);
                        SearchLayout.setVisibility(View.VISIBLE);
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return rootView;
    }

    void CustomDownload(int start,int end){
            for(int i = start; i<end;i++){
                if(!Main_Pokemons_Object.get(i).getName().equals("Ad")){
                    Main_Pokemons_Object.get(i).setThumb(LoadImageFromWebOperations("http://www.pokestadium.com/assets/img/sprites/" + Main_Pokemons_Object.get(i).getID() + ".png"));
                    handler.sendEmptyMessage(2);
                }
            }
    }

    Bitmap LoadImageFromWebOperations(String url) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(url.replace(" ","")).getContent());
        } catch (Exception e) {
            return null;
        }
    }

    ArrayList<Pokemon> ArrayOfPokemon(){

        InputStream inputStream = getResources().openRawResource(R.raw.pokedexlist);

        byte [] bytes = new byte[0];
        String string;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        try {
            bytes = new byte[inputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(bytes);
            string = new String(bytes);
            string = string.replace("\r","");
            string = string.replace("\n","");
            String[] arr = string.split(" END");
            ArrayList<Pokemon> pokemons = new ArrayList<>();
            int x = 0;
            for(int i = 0; i<arr.length;i= i+2){
                int i1 = Integer.parseInt(arr[i].replace("number:",""));
                String i2 = (arr[i+1]).replace("name:","");
                Pokemon pokemon  = new Pokemon(i2,i1);
                pokemons.add(pokemon);
                for (int aLocation : location) {
                    if (aLocation == i) {
                        pokemon = new Pokemon("Ad",0);
                        pokemon.setName("Ad");
                        pokemons.add(pokemon);
                        break;
                    }
                }
                stringIntegerHashMap.put(i2, i1);
                integerStringHashMap.put(i1, i2);
            }
            return pokemons;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  byte[] OpenFromFile(String filename) throws FileNotFoundException {

        File file =  new File(getActivity().getFilesDir(),filename);
        FileInputStream fileInputStream;
        String data ="";
        try{
            fileInputStream = getActivity().openFileInput(filename);
            byte[] bytes =new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (FileNotFoundException e) {
            Log.d("File was not",e.getMessage());
            throw new FileNotFoundException();
        } catch (IOException e) {
            Log.d("File IOException",e.getMessage());
            return null;
        }

    }

    Integer getRandomInt(int Low, int High) {
        Random r = new Random();
        if (High == Low)
            High = Low + 1;
        return r.nextInt(High - Low) + Low;
    }

    public int getRandomWithExclusion(int start, int end, int... exclude) {
        Random rnd = new Random();
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    void adCretor() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float deviceWidthInDp = displayMetrics.widthPixels / displayMetrics.density;
        int adWidth = (int) (deviceWidthInDp);
        if (adWidth < 280) {
            adWidth = 280;
        }
        ads = new ArrayList<NativeExpressAdView>();
        for (int i = 0; i < location.length; i++) {
            ads.add(new NativeExpressAdView(context));
            ads.get(i).setAdSize(new AdSize(adWidth, 80));
            ads.get(i).setAdUnitId(getResources().getString(R.string.ListView_Ad));
            // Create an ad request.
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            // Optionally populate the ad request builder.
            ads.get(i).loadAd(adRequestBuilder.build());
        }
    }

    NativeExpressAdView addAd() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float deviceWidthInDp = displayMetrics.widthPixels / displayMetrics.density;
        int adWidth = (int) (deviceWidthInDp);
        if (adWidth < 280) {
            adWidth = 280;
        }
        ads.add(new NativeExpressAdView(context));
        ads.get(ads.size() - 1).setAdSize(new AdSize(adWidth, 80));
        ads.get(ads.size() - 1).setAdUnitId(getResources().getString(R.string.ListView_Ad));
        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        // Optionally populate the ad request builder.
        ads.get(ads.size() - 1).loadAd(adRequestBuilder.build());
        return ads.get(ads.size() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("PokeID");
                String name = data.getStringExtra("PokeIString");
                if (Integer.valueOf(id) != 0) {
                    Intent intent = new Intent(getActivity().getBaseContext(), PokemonActivity.class);
                    intent.putExtra(PoekmonViewFragment.ARG_NAME, name);
                    intent.putExtra(PoekmonViewFragment.ARG_NUM, Integer.valueOf(id));
                    startActivityForResult(intent, 1);
                }
            }
        }
    }

    private class CustomList extends BaseAdapter {
        ArrayList<Pokemon> pokemons;


        public CustomList(ArrayList<Pokemon> pokemons){
            this.pokemons = pokemons;
        }
        @Override
        public int getCount() {
            return pokemons.size();
        }

        @Override
        public Object getItem(int i) {
            return pokemons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return pokemons.get(i).getID();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-4305314132412937~7895429800");



            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("ViewHolder") View view2 = inflater.inflate(R.layout.pokedex_list_item,viewGroup,false);
            RelativeLayout view1 =(RelativeLayout) view2.findViewById(R.id.parentListRL);
            TextView name =(TextView) view1.findViewById(R.id.list_item_name);
            TextView number = (TextView) view1.findViewById(R.id.list_item_number);
            ImageView thumble = (ImageView) view1.findViewById(R.id.list_item_pokemon_thumb);
            if(!pokemons.get(i).getName().equals("Ad")) {
                String tmp = String.valueOf(pokemons.get(i).getID());
                if (tmp.length() == 1) {
                    tmp = "00" + tmp;
                } else if (tmp.length() == 2) {
                    tmp = "0" + tmp;
                }
                number.setText(tmp);
                name.setText(pokemons.get(i).getName());
                if (pokemons.get(i).getThumb() != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    thumble.setImageBitmap(Bitmap.createScaledBitmap(pokemons.get(i).getThumb(), imageWidth, imageHeight, false));
                } else {
                    thumble.setImageBitmap(null);
                }
                return view1;
            }else {
                View ad = null;
                try {
                    return ads.get(ReversLocation.get(i));
                } catch (Exception e) {
                    ReversLocation.put(i, ads.size());
                    return addAd();
                }
            }
        }
    }

    private class ListItemListner implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int position = i;
            if (!PokemonListObject.get(i).getName().equals("Ad")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getActiveNetworkInfo().isConnected() && connectivityManager.getActiveNetworkInfo() != null) {
                    Intent intent = new Intent(getActivity().getBaseContext(), PokemonActivity.class);
                    intent.putExtra(PoekmonViewFragment.ARG_NAME, PokemonListObject.get(i).getName());
                    intent.putExtra(PoekmonViewFragment.ARG_NUM, PokemonListObject.get(i).getID());
                    startActivityForResult(intent, 1);
                } else {
                    Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

        }
    }
}
