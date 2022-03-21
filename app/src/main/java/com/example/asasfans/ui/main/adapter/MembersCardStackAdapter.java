package com.example.asasfans.ui.main.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asasfans.R;
import com.example.asasfans.data.VTBDataBean;
import com.example.asasfans.data.VTBData;
import com.google.gson.Gson;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author zyxdb
 * @description 2022/3/10 vtb.moe 成员卡片适配器
 */

public class MembersCardStackAdapter extends StackAdapter<Integer> {
    //Context mContext;
    List<VTBData> vtbdataList;
    public MembersCardStackAdapter(Context context)
    {
        super(context);
        //mContext = context;
    }


    public void updateData(List data,List<VTBData> vtbDataList) {
        this.vtbdataList = vtbDataList;
        updateData(data);
        System.out.println("Updata!");
    }

    /**
     * @description 获得viewType这个参数，供onCreateView函数调用
     * 传入的参数position代表是第几项，重写该方法，根据position判断类型。
     * @author zyxdb
     * @time 2022/3/10 19:55
     */
    @Override
    public int getItemViewType(int position) {
        System.out.println("getItemViewType");
//        return super.getItemViewType(position);
        return R.layout.item_memberscard;
    }

    /**
     * @description 创建每一个卡片的view，返回一个ViewHolder，ViewHolder中保存着这个布局中的View
     * @author zyxdb
     * @time 2022/3/10 19:55
     */
    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memberscard,parent,false);
        View view = getLayoutInflater().inflate(R.layout.item_memberscard, parent, false);
        CardViewHolder holder = new CardViewHolder(view);
        System.out.println("onCreateView");
        return holder;
    }

    /**
     * @description 需要重写的第二个方法，在创建完VIew之后调用，得到View后进行一些操作，比如显示数据等操作。
     * @author zyxdb
     * @time 2022/3/10 21:53
     */
    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if(holder instanceof CardViewHolder)
        {
            CardViewHolder cardHolder = (CardViewHolder)holder;
            cardHolder.onBind(data,position,vtbdataList);
        }
        System.out.println("bindView");
    }

    /**
     * @description 该方法决定列表可以显示多少项卡片，需要根据传入的数据去判断返回多少项。
     * @author zyxdb
     * @time 2022/3/10 22:06
     */
    @Override
    public int getItemCount() {
        System.out.println("getItemCount");
        return super.getItemCount();
    }



    @Override
    public Integer getItem(int position) {
        System.out.println("getItem");
        return super.getItem(position);
    }

    /**
     * @description 创建CardViewHolder类，将每一项的布局缓存起来，以供后面反复使用。
     * @author zyxdb
     * @time 2022/3/10 21:56
     */
    public static class CardViewHolder extends CardStackView.ViewHolder
    {
        private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        private String UID_SEARCH_URL = "https://api.tokyo.vtbs.moe/v1/detail/";
        private String membername = "";
        public static final int GET_DATA_SUCCESS = 1;
        public static final int NETWORK_ERROR = 2;

        View root;
        FrameLayout cardTitle;          //布局头部
        RecyclerView members_list;      //布局头部下方的RecyclerView
        TextView member_name;      //成员名字
        TextView member_slogan;    //个性签名
        TextView member_followed;  //关注数
        TextView dayIncrease;      //日增
        TextView CaptainNum;       //舰长数

        /**
         * @description 在创建ViewHolder时对控件进行绑定
         * @author zyxdb
         * @time 2022/3/13 11:29
         */
        public CardViewHolder(View view)
        {
            super(view);
            root = view;
            cardTitle = (FrameLayout)view.findViewById(R.id.card_title);
            member_name = (TextView)view.findViewById(R.id.card_title_text);
            member_slogan = (TextView)view.findViewById(R.id.item_member_slogan);
            member_followed = (TextView)view.findViewById(R.id.item_member_followed);
            dayIncrease = (TextView)view.findViewById(R.id.item_member_dayIncrease);
            CaptainNum = (TextView)view.findViewById(R.id.MemberCaptainNum);
            members_list = (RecyclerView)view.findViewById(R.id.members_list);
            System.out.println("CardViewHolder constructor");
        }

        public void onBind(Integer backgroundColorId,int position,List<VTBData> dataList)
        {
            /*
                该方法是在bindView调用时被调用的，因为可能有不同的布局，因而有不同的ViewHolder，将bindView实现的操作放在了ViewHolder中的onBind方法中，会使代码看来起更简洁，易懂。
            */
            cardTitle.getBackground().setColorFilter(ContextCompat.getColor(getContext(),backgroundColorId), PorterDuff.Mode.SRC_IN);

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bundle data = msg.getData();
                    String val = data.getString("VTBData");
//                Log.i("singleVideoData", "请求结果为-->" + val);
                    Gson gson =new Gson();
                    VTBDataBean VTBDataBean = gson.fromJson(val, VTBDataBean.class);
//                if (singleVideoBean.getCode() == 62002)
                    switch (msg.what){
                        case GET_DATA_SUCCESS:
                            if (VTBDataBean.getUuid() != null) {
                                member_name.setText(VTBDataBean.getUname());
                                dataList.get(position).setMemberName(VTBDataBean.getUname());
                                member_slogan.setText(VTBDataBean.getSign());
                                dataList.get(position).setMembersign(VTBDataBean.getSign());
                                member_followed.setText(Integer.toString((VTBDataBean.getFollower())));
                                dataList.get(position).setMemberFansNum(VTBDataBean.getFollower());
                                dayIncrease.setText(Integer.toString((VTBDataBean.getRise())));
                                dataList.get(position).setMemberFansNumRise(VTBDataBean.getRise());
                                CaptainNum.setText(Integer.toString((VTBDataBean.getGuardNum())));
                                dataList.get(position).setMemberGuardNum(VTBDataBean.getGuardNum());
                                dataList.get(position).setFirstLoad(false);
                            }else {
                                member_name.setText(val);
                                Log.i("GET_DATA_SUCCESS:null", val);
                            }
                            break;
                        case NETWORK_ERROR:
                            member_name.setText("NETWORK_ERROR");
                    }
                    // TODO
                    // UI界面的更新等相关操作
                }
            };

            switch (position) {
                case 0:
                    membername = "672346917";//向晚uid
                    break;
                case 1:
                    membername = "672353429";//贝拉uid
                    break;
                case 2:
                    membername = "351609538";//珈乐uid
                    break;
                case 3:
                    membername = "672328094";//嘉然uid
                    break;
                case 4:
                    membername = "672342685";//乃琳uid
                    break;
            }

            Runnable networkTask = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    // TODO
                    // 在这里进行 http request.网络请求相关操作
                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
                    Request request = new Request.Builder().url(UID_SEARCH_URL + membername)
                            .get().build();
                    Call call = client.newCall(request);
                    Response response = null;
                    String tmp;
                    try {
                        response = call.execute();
                        tmp = response.body().string();
                        msg.what = GET_DATA_SUCCESS;
                        data.putString("VTBData", tmp);

                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(NETWORK_ERROR);
                    }
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            };

            if (dataList.get(position).getFirstLoad()) {
                cachedThreadPool.execute(networkTask);
            }else {
                member_name.setText(dataList.get(position).getMemberName());
                member_slogan.setText(dataList.get(position).getMembersign());
                member_followed.setText(dataList.get(position).getMemberFansNum());
                dayIncrease.setText(dataList.get(position).getMemberFansNumRise());
                CaptainNum.setText(dataList.get(position).getMemberGuardNum());
                Log.i("getFirstLoad:false", String.valueOf(position));
            }

            MembersListAdapter adapter = new MembersListAdapter(dataList);
            members_list.setLayoutManager(new LinearLayoutManager(getContext()));
            members_list.setAdapter(adapter);
            System.out.println("holder onBind");
        }

        /**
         * @description 判断卡片是否被点击，true就将卡片展开.
         * @author zyxdb
         * @time 2022/3/11 23:09
         */
        @Override
        public void onItemExpand(boolean b) {
            members_list.setVisibility(b ? View.VISIBLE : View.GONE);
            System.out.println("holder onItemExpand");
        }
    }
}
