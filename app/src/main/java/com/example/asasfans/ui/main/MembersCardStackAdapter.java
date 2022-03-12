package com.example.asasfans.ui.main;

import android.content.Context;
import android.graphics.PorterDuff;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asasfans.R;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.List;

/**
 * @author zyxdb
 * @description 2022/3/10 vtb.moe 成员卡片适配器
 */

public class MembersCardStackAdapter extends StackAdapter<Integer> {
    //Context mContext;
    List<List<VTBData>> vtbdataList;
    public MembersCardStackAdapter(Context context)
    {
        super(context);
        //mContext = context;
    }


    public void updateData(List data,List<List<VTBData>> vtbDataList) {
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
        return super.getItemViewType(position);
//        return R.layout.item_memberscard;
    }

    /**
     * @description 创建每一个卡片的view，返回一个ViewHolder，ViewHolder中保存着这个布局中的View
     * @author zyxdb
     * @time 2022/3/10 19:55
     */
    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memberscard,parent,false);
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
        View root;
        FrameLayout cardTitle;          //布局头部
        RecyclerView members_list;      //布局头部下方的RecyclerView
        TextView titleText;             //布局头部
        public CardViewHolder(View view)
        {
            /*
                在创建ViewHolder是 对控件进行绑定
            */
            super(view);
            root = view;
            cardTitle = (FrameLayout)view.findViewById(R.id.card_title);
            titleText = (TextView)view.findViewById(R.id.card_title_text);
            members_list = (RecyclerView)view.findViewById(R.id.members_list);
            System.out.println("CardViewHolder constructor");
        }

        public void onBind(Integer backgroundColorId,int position,List<List<VTBData>> dataList)
        {
            /*
                该方法是在bindView调用时被调用的，因为可能有不同的布局，因而有不同的ViewHolder，将bindView实现的操作放在了ViewHolder中的onBind方法中，会使代码看来起更简洁，易懂。
            */
            cardTitle.getBackground().setColorFilter(ContextCompat.getColor(getContext(),backgroundColorId), PorterDuff.Mode.SRC_IN);
            MembersListAdapter adapter = new MembersListAdapter(dataList.get(position));
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
