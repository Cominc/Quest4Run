package com.comincini_micheli.quest4run.objects;

import java.util.concurrent.TimeUnit;

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
    private long duration;
    private long dateStart;
    private long dateFinish;

    public Quest(int id, String title, String description, boolean completed, boolean active, int minAttack, int minDefense, int minMagic, int expReward, long duration, long dateStart, long dateFinish)
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

    public Quest(String title, String description, int minAttack, int minDefense, int minMagic, int expReward, long duration)
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
        this.completed = false;
        this.active = false;
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

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public long getDateStart()
    {
        return dateStart;
    }

    public void setDateStart(long dateStart)
    {
        this.dateStart = dateStart;
    }

    public long getDateFinish()
    {
        return dateFinish;
    }

    public void setDateFinish(long dateFinish)
    {
        this.dateFinish = dateFinish;
    }

    public String getDurationString(){
        String durationString = "";
        long durationMS = duration;
        long temp;
        temp = TimeUnit.MILLISECONDS.toHours(durationMS);
        durationString += temp+":";
        durationMS -= TimeUnit.HOURS.toMillis(temp);
        temp = TimeUnit.MILLISECONDS.toMinutes(durationMS);
        durationString += temp+":";
        durationMS -= TimeUnit.MINUTES.toMillis(temp);
        durationString += TimeUnit.MILLISECONDS.toSeconds(durationMS);
        return durationString;
    }

    public boolean checkCompleted()
    {
        if(this.completed)
            return true;
        else
        {
            if(this.dateStart + this.duration <= System.currentTimeMillis())
            {
                return true;
            }
            else
                return false;
        }
    }
}
