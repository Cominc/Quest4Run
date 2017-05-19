package com.comincini_micheli.quest4run.objects;

/**
 * Created by Gianmaria on 19/05/2017.
 */

public class Task
{
    private int id;
    private String name;
    private int reward;
    private int idTaskType;
    private String objective;
    private boolean completed;
    private boolean active;

    public Task(int id, String name, int reward, int idTaskType, String objective, boolean completed, boolean active)
    {
        this.id = id;
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.objective = objective;
        this.completed = completed;
        this.active = active;
    }

    public Task(String name, int reward, int idTaskType, String objective, boolean completed, boolean active)
    {
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.objective = objective;
        this.completed = completed;
        this.active = active;
    }

    public Task(int id, String name, int reward, int idTaskType, String objective)
    {
        this.id = id;
        this.name = name;
        this.reward = reward;
        this.idTaskType = idTaskType;
        this.objective = objective;
        this.completed = false;
        this.active = true;
    }

    public Task()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getObjective()
    {
        return objective;
    }

    public void setObjective(String objective)
    {
        this.objective = objective;
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
