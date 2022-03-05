package com.example.asasfans.data;

/**
 * @author: akari
 * @date: 2022/3/4
 * @description $
 */
public class ToolsData {
    private String desc;
    private String iconUrl;

    public ToolsData(String desc, String iconUrl) {
        this.desc = desc;
        this.iconUrl = iconUrl;
    }

    public String getDesc() {
        return desc;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
