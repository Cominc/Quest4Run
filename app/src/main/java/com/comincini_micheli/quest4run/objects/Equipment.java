package com.comincini_micheli.quest4run.objects;

/**
 * Created by Daniele on 17/05/2017.
 */
public class Equipment {
    private int id;
    private String name;
    private int idType;
    private int atk, def, mgc;
    private int price;
    private String icon;
    private boolean bought, equipped;
    private int minLevel;


    public Equipment() {
        this.bought = false;
        this.equipped = false;
    }

    public Equipment(String name, int idType, int atk, int def, int mgc, int price, String icon, int minLevel) {
        this.name = name;
        this.idType = idType;
        this.atk = atk;
        this.def = def;
        this.mgc = mgc;
        this.price = price;
        this.icon = icon;
        this.bought = false;
        this.equipped = false;
        this.minLevel = minLevel;
    }

    public Equipment(int id, String name, int idType, int atk, int def, int mgc, int price, String icon, boolean bought, boolean equipped, int minLevel) {
        this.id = id;
        this.name = name;
        this.idType = idType;
        this.atk = atk;
        this.def = def;
        this.mgc = mgc;
        this.price = price;
        this.icon = icon;
        this.bought = bought;
        this.equipped = equipped;
        this.minLevel = minLevel;
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

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
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

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public int getMinLevel()
    {
        return minLevel;
    }

    public void setMinLevel(int minLevel)
    {
        this.minLevel = minLevel;
    }
}

