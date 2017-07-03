package com.comincini_micheli.quest4run.objects;

import static com.comincini_micheli.quest4run.other.Constants.EXP_FOR_NEXT_LEVEL;

/**
 * Created by Daniele on 30/05/2017.
 */

public class Character {
    private int id;
    private String name;
    private int gender;
    private int avatar;
    private int exp;
    private int attack, defense, magic;
    private int wallet;

    public Character(String name, int gender, int avatar) {
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
        this.exp = 0;
        this.attack = 3;
        this.defense = 3;
        this.magic = 3;
        this.wallet = 100;
    }

    public Character(int id, String name, int gender, int avatar, int exp, int attack, int defense, int magic, int wallet) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
        this.exp = exp;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.wallet = wallet;
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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
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

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getWallet()
    {
        return wallet;
    }

    public void setWallet(int wallet)
    {
        this.wallet = wallet;
    }

    public int getLevel(){ return (int)Math.round(exp/EXP_FOR_NEXT_LEVEL) + 1;}

}
