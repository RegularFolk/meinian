package com.atguigu.pojo.interim;

public class SetmealAndTravelGroup {
    Integer setMealId;
    Integer travelGroupId;

    public SetmealAndTravelGroup(Integer setMealId, Integer travelGroupId) {
        this.setMealId = setMealId;
        this.travelGroupId = travelGroupId;
    }

    public Integer getSetMealId() {
        return setMealId;
    }

    public void setSetMealId(Integer setMealId) {
        this.setMealId = setMealId;
    }

    public Integer getTravelGroupId() {
        return travelGroupId;
    }

    public void setTravelGroupId(Integer travelGroupId) {
        this.travelGroupId = travelGroupId;
    }
}
