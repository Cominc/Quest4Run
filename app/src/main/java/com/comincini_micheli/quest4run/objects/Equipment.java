package com.comincini_micheli.quest4run.objects;

/**
 * Created by Daniele on 17/05/2017.
 */
public class Equipment {
    int id;
    String name;
    int atk, def, mgc;
    int price;
    String icon;

    public Equipment() {
        this.name = "Test";
        this.atk = 5;
        this.def = 4;
        this.mgc = 3;
        this.price = 2;
        this.icon = "boh";
    }

    public Equipment(int id, String name, int atk, int def, int mgc, int price, String icon) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.mgc = mgc;
        this.price = price;
        this.icon = icon;
    }

    public Equipment(String name, int atk, int def, int mgc, int price, String icon) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.mgc = mgc;
        this.price = price;
        this.icon = icon;
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

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMgc() {
        return mgc;
    }

    public void setMgc(int mgc) {
        this.mgc = mgc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

