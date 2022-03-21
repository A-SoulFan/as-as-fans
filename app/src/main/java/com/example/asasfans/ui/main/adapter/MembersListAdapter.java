package com.example.asasfans.ui.main.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asasfans.R;
import com.example.asasfans.data.VTBData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author akarinini
 * @description 2022/3/10 vtb.moe 成员详细信息适配器
 */

public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.VTBViewHolder> {
    List<VTBData> VTBData_list;

    public MembersListAdapter(List<VTBData> list)
    {
        VTBData_list = list;
    }

    public static class VTBViewHolder extends RecyclerView.ViewHolder
    {
        TextView member_name;      //成员名字
        TextView member_slogan;    //个性签名
        TextView member_followed;  //关注数
        TextView dayIncrease;      //日增
        TextView CaptainNum;       //舰长数
        public VTBViewHolder(View view)
        {
            super(view);
            member_name = (TextView)view.findViewById(R.id.item_member_name);
            member_slogan = (TextView)view.findViewById(R.id.item_member_slogan);
            member_followed = (TextView)view.findViewById(R.id.item_member_followed);
            dayIncrease = (TextView)view.findViewById(R.id.item_member_dayIncrease);
            CaptainNum = (TextView)view.findViewById(R.id.MemberCaptainNum);
        }
    }

    @Override
    public void onBindViewHolder(VTBViewHolder holder, int position) {
        holder.member_name.setText(VTBData_list.get(position).getMemberName());
        holder.member_slogan.setText(VTBData_list.get(position).getMembersign());
        holder.member_followed.setText(VTBData_list.get(position).getMemberFansNum());
        holder.dayIncrease.setText(VTBData_list.get(position).getMemberFansNumRise());
        holder.CaptainNum.setText(VTBData_list.get(position).getMemberGuardNum());
    }

    @Override
    @NotNull
    public VTBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_members_recyclerview,parent,false);
        return new VTBViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return VTBData_list.size();
    }
}
