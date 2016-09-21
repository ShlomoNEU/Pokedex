package com.shne.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shlomo on 23-Aug-16.
 */
public class Pokemon extends Object {

    private String name = null;
    private int ID;
    private String [] pokemon_type = new String[2];
    private Bitmap thumb = null,sprite = null;
    private String spriteLink,thumbLink;
    private Pokemon pokemon =this;
    private String Height;
    private String weight;
    private String  evolution_chain_URL;
    private String EVONAME,EVOCouse,EVOID;
    private ArrayList<String> EVO2Name,EVO2Couse,EVO2ID;
    private ArrayList<String> EVO3Name,EVO3Couse,EVO3ID;
    private ArrayList<HashMap<String,Bitmap>> Varieties;



    //Cunstractors
    public Pokemon(String name, int ID, Bitmap thumb) {
        EVO2Name = new ArrayList<String>();
        EVO2Couse = new ArrayList<String>();
        EVO2ID  = new ArrayList<String>();
        EVO3Name = new ArrayList<String>();
        EVO3Couse = new ArrayList<String>();
        EVO3ID  = new ArrayList<String>();
        Varieties = new ArrayList<HashMap<String, Bitmap>>();
        this.name = name;
        this.ID = ID;
        this.thumb = thumb;
    }
    public Pokemon(String name,int ID) {
        EVO2Name = new ArrayList<String>();
        EVO2Couse = new ArrayList<String>();
        EVO2ID  = new ArrayList<String>();
        EVO3Name = new ArrayList<String>();
        EVO3Couse = new ArrayList<String>();
        EVO3ID  = new ArrayList<String>();
        Varieties = new ArrayList<HashMap<String, Bitmap>>();
        this.name = name;
        this.ID = ID;
    }

    //setters And getters

    public Bitmap getThumb() {
        return thumb;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public String[] getPokemon_type() {
        return pokemon_type;
    }

    public String getNumberToString(){
        String tmp = String.valueOf(this.getID());
        if(tmp.length() == 1){
            tmp = "00"+tmp;
        }else if(tmp.length() == 2){
            tmp = "0"+tmp;
        }
        return tmp;
    }

    public String getHeight() {
        return Height;
    }

    public String getWeight() {
        return weight;
    }

    public String getEvolution_chain_URL() {
        return evolution_chain_URL;
    }

    public String getSpriteLink() {
        return spriteLink;
    }

    public Bitmap getSprite() {
        return sprite;
    }


    public void setEvolution_chain_URL(String evolution_chain_URL) {
        this.evolution_chain_URL = evolution_chain_URL;
    }

    public void setSpriteLink(String spriteLink) {
        try {
            this.sprite = BitmapFactory.decodeStream(new URL(spriteLink).openStream());
        } catch (MalformedURLException e) {
            Log.d("PokemonError",e.getMessage());
        } catch (IOException e) {
            Log.d("PokemonError",e.getMessage());
        }
        this.spriteLink = spriteLink;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public void setPokemon_type(String[] pokemon_type) {
        this.pokemon_type = pokemon_type;
    }

    public void setPokemon_type(String pokemon_type) {
        this.pokemon_type[0] = pokemon_type;
        this.pokemon_type[1] = null;
    }

    public String getEVONAME() {
        return EVONAME;
    }

    public void setEVONAME(String EVONAME) {
        this.EVONAME = EVONAME;
    }

    public String getEVOCouse() {
        return EVOCouse;
    }

    public void setEVOCouse(String EVOCouse) {
        this.EVOCouse = EVOCouse;
    }

    public String getEVOID() {
        return EVOID;
    }

    public void setEVOID(String EVOID) {
        this.EVOID = EVOID;
    }

    public ArrayList<String> getEVO2Name() {
        return EVO2Name;
    }

    public void setEVO2Name(ArrayList<String> EVO2Name) {
        this.EVO2Name = EVO2Name;
    }

    public ArrayList<String> getEVO2Couse() {
        return EVO2Couse;
    }

    public void setEVO2Couse(ArrayList<String> EVO2Couse) {
        this.EVO2Couse = EVO2Couse;
    }

    public ArrayList<String> getEVO2ID() {
        return EVO2ID;
    }

    public void setEVO2ID(ArrayList<String> EVO2ID) {
        this.EVO2ID = EVO2ID;
    }

    public ArrayList<String> getEVO3ID() {
        return EVO3ID;
    }

    public void setEVO3ID(ArrayList<String> EVO3ID) {
        this.EVO3ID = EVO3ID;
    }

    public ArrayList<String> getEVO3Couse() {
        return EVO3Couse;
    }

    public void setEVO3Couse(ArrayList<String> EVO3Couse) {
        this.EVO3Couse = EVO3Couse;
    }

    public ArrayList<String> getEVO3Name() {
        return EVO3Name;
    }

    public void setEVO3Name(ArrayList<String> EVO3Name) {
        this.EVO3Name = EVO3Name;
    }

    public ArrayList<HashMap<String, Bitmap>> getVarieties() {
        return Varieties;
    }

    public void setVarieties(ArrayList<HashMap<String, Bitmap>> varieties) {
        Varieties = varieties;
    }


    public class Pokemon_type{
        public static final int BUG = 0;
        public static final int DARK = 1;
        public static final int DRAGON = 2;
        public static final int ELECTRIC = 3;
        public static final int FAIRY = 4;
        public static final int FIGHTING = 5;
        public static final int FIRE = 6;
        public static final int FLYING = 7;
        public static final int GHOST = 8;
        public static final int GRASS = 9;
        public static final int GROUND = 10;
        public static final int ICE = 11;
        public static final int NORMAL = 12;
        public static final int POISON = 13;
        public static final int PSYCHIC = 14;
        public static final int ROCK  = 15;
        public static final int STEEL = 16;
        public static final int WATER  = 17;



        public int returnTypeINT(String str){
            str = str.toUpperCase();
            switch (str){
                case "BUG":
                    return BUG;
                case "DARK":
                    return DARK;

                case "DRAGON":
                    return DRAGON;

                case "ELECTRIC":
                    return ELECTRIC;

                case "FAIRY":
                    return FAIRY;

                case "FIGHTING":
                    return FIGHTING;

                case "FIRE":
                    return FIRE;

                case "FLYING":
                    return FLYING;

                case "GHOST":
                    return GHOST;

                case "GRASS":
                    return GRASS;

                case "GROUND":
                    return GROUND;

                case "ICE":
                    return ICE;

                case "NORMAL":
                    return NORMAL;

                case "POISON":
                    return POISON;

                case "PSYCHIC":
                    return PSYCHIC;

                case "ROCK":
                    return ROCK;

                case "STEEL":
                    return STEEL;

                case "WATER":
                    return WATER;

                default:
                    return 100;

            }
        }
        public String returnTypeString(int str){

            switch (str){
                case BUG:
                    return "BUG";
                case DARK:
                    return "DARK";

                case DRAGON:
                    return "DRAGON";

                case ELECTRIC:
                    return "ELECTRIC";

                case FAIRY:
                    return "FAIRY";

                case FIGHTING:
                    return "FIGHTING";

                case FIRE:
                    return "FIRE";

                case FLYING:
                    return "FLYING";

                case GHOST:
                    return "GHOST";

                case GRASS:
                    return "GRASS";

                case GROUND:
                    return "GROUND";

                case ICE:
                    return "ICE";

                case NORMAL:
                    return "NORMAL";

                case POISON:
                    return "POISON";

                case PSYCHIC:
                    return "PSYCHIC";

                case ROCK:
                    return "ROCK";

                case STEEL:
                    return "STEEL";

                case WATER:
                    return "WATER";

                default:
                    return "undifned";

            }
        }

    }

    interface PokemonDataListener{
        Pokemon DataArrived();
        void pokemonSetariived(Pokemon pokemon);
        void DataArrived(Pokemon pokemon);
        void NotifyAllDataArrived();
    }

    private List<PokemonDataListener> listeners = new ArrayList<PokemonDataListener>();

    public void addListener(PokemonDataListener toAdd){
        listeners.add(toAdd);
    }
    public void NotifyAllListeners(){
        for (PokemonDataListener hl : listeners)
            hl.NotifyAllDataArrived();
    }
    public void NotifyPokeSet(){
        for (PokemonDataListener hl : listeners)
            hl.pokemonSetariived(this);
    }
}

