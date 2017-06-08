package com.comincini_micheli.quest4run.objects;

/**
 * Created by Gianmaria on 08/06/2017.
 */

public class Quest
{
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private boolean active;
    private int minAttack;
    private int minDefense;
    private int minMagic;
    private int expReward;
    private int duration;
    private int dateStart;
    private int dateFinish;

    public Quest(int id, String title, String description, boolean completed, boolean active, int minAttack, int minDefense, int minMagic, int expReward, int duration, int dateStart, int dateFinish)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.active = active;
        this.minAttack = minAttack;
        this.minDefense = minDefense;
        this.minMagic = minMagic;
        this.expReward = expReward;
        this.duration = duration;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    public Quest(String title, String description, int minAttack, int minDefense, int minMagic, int expReward, int duration)
    {
        this.title = title;
        this.description = description;
        this.minAttack = minAttack;
        this.minDefense = minDefense;
        this.minMagic = minMagic;
        this.expReward = expReward;
        this.duration = duration;
        this.completed = false;
        this.active = false;
    }

    public Quest()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public int getMinAttack()
    {
        return minAttack;
    }

    public void setMinAttack(int minAttack)
    {
        this.minAttack = minAttack;
    }

    public int getMinDefense()
    {
        return minDefense;
    }

    public void setMinDefense(int minDefense)
    {
        this.minDefense = minDefense;
    }

    public int getMinMagic()
    {
        return minMagic;
    }

    public void setMinMagic(int minMagic)
    {
        this.minMagic = minMagic;
    }

    public int getExpReward()
    {
        return expReward;
    }

    public void setExpReward(int expReward)
    {
        this.expReward = expReward;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public int getDateStart()
    {
        return dateStart;
    }

    public void setDateStart(int dateStart)
    {
        this.dateStart = dateStart;
    }

    public int getDateFinish()
    {
        return dateFinish;
    }

    public void setDateFinish(int dateFinish)
    {
        this.dateFinish = dateFinish;
    }
}
