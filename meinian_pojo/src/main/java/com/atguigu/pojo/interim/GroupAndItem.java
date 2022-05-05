package com.atguigu.pojo.interim;

public class GroupAndItem {
    private Integer travelGroupId;
    private Integer travelItemId;

    public GroupAndItem(Integer travelGroupId, Integer travelItemId) {
        this.travelGroupId = travelGroupId;
        this.travelItemId = travelItemId;
    }

    public Integer getTravelGroupId() {
        return travelGroupId;
    }

    public void setTravelGroupId(Integer travelGroupId) {
        this.travelGroupId = travelGroupId;
    }

    public Integer getTravelItemId() {
        return travelItemId;
    }

    public void setTravelItemId(Integer travelItemId) {
        this.travelItemId = travelItemId;
    }
}
