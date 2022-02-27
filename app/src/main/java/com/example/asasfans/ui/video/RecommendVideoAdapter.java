package com.example.asasfans.ui.video;

import android.content.Context;

import java.util.List;

/**
 * @author akarinini
 * @description 本想直接用继承改个名字方便区分，但是没有使用
 */

public class RecommendVideoAdapter extends  PubdateVideoAdapter {
    public RecommendVideoAdapter(Context context, int pageSize, List<List<String>> videosBvid) {
        super(context, pageSize, videosBvid);
    }
}
