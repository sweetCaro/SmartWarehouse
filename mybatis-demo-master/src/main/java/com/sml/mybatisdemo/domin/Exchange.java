package com.sml.mybatisdemo.domin;

import lombok.Data;

@Data
public class Exchange {
    public String goodId;
    public String type;
    public String position;
    public int transformation;
    public String dateAndTime;
    public String warehouseId;
    public int volume;
}