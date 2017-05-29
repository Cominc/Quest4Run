package com.comincini_micheli.quest4run.objects;

import java.io.Serializable;

/**
 * Created by Gianmaria on 19/05/2017.
 */

public class Task implements Serializable
{
    private int id;
    private String name;
    private int reward;
    private int idTaskType;
    private String goal;
    private boolean completed;
    private boolean active;

    public Task(int id, String name, int reward, int idTaskType, String goal, boolean completed, boolean active)
    {
        this.id = id;
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.goal = goal;
        this.completed = completed;
        this.active = active;
    }

    public Task(String name, int reward, int idTaskType, String goal, boolean completed, boolean active)
    {
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.goal = goal;
        this.completed = completed;
        this.active = active;
    }

    public String getGoal()
    {
        return goal;
    }

    public void setGoal(String goal)
    {
        this.goal = goal;
    }

    public Task(int id, String name, int reward, int idTaskType, String goal)
    {
        this.id = id;
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.goal = goal;

        this.completed = false;
        this.active = true;
    }

    public Task()
    {
        this.completed = false;
        this.active = true;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getReward()
    {
        return reward;
    }

    public void setReward(int reward)
    {
        this.reward = reward;
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getIdTaskType()
    {
        return idTaskType;
    }

    public void setIdTaskType(int idTaskType)
    {
        this.idTaskType = idTaskType;
    }


}
