package com.comincini_micheli.quest4run.objects;

import static com.comincini_micheli.quest4run.other.Constants.EXP_FOR_NEXT_LEVEL;

/**
 * Created by Daniele on 30/05/2017.
 */

public class Character {
    private int id;
    private String name;
    private int gender;
    private int exp;
    private int attack, defence, magic;

    public Character() {
        this.name = "Ciao";
        this.gender = 1;
        this.exp = 75;
        this.attack = 5;
        this.defence = 3;
        this.magic = 1;
    }

    public Character(String name, int gender, int exp, int attack, int defence, int magic) {
        this.name = name;
        this.gender = gender;
        this.exp = exp;
        this.attack = attack;
        this.defence = defence;
        this.magic = magic;
    }

    public Character(int id, String name, int gender, int exp, int attack, int defence, int magic) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.exp = exp;
        this.attack = attack;
        this.defence = defence;
        this.magic = magic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getLevel(){ return (int)Math.round(exp/EXP_FOR_NEXT_LEVEL);}

}
