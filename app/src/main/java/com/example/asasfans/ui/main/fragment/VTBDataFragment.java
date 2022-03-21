package com.example.asasfans.ui.main.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.asasfans.R;
import com.example.asasfans.data.VTBData;
import com.example.asasfans.ui.main.adapter.MembersCardStackAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zyxdb
 * @description VTB数据页Fragment，嵌套在LiveDataActivity中
 */

public class VTBDataFragment extends Fragment implements CardStackView.ItemExpendListener{
    private MembersCardStackAdapter membersCardStackAdapter;
    private CardStackView mStackView;
    private LinearLayout mActionButtonContainer;

    //颜色数组
    Integer[] color = {
            R.color.cardBlue,
            R.color.cardSubjectBlue,
            R.color.cardButtonDeepBlue,
            R.color.cardGray
    };

    String[] name = {"这里应该是个折线图，还没做好"};
    String[] fansnum = {"1"};

    public static VTBDataFragment newInstance() {
        VTBDataFragment fragment = new VTBDataFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vtb_data,container,false);
        initWight(view);
        return view;
    }

    private void initWight(View view) {
        /* 创建Adapter，并将它和Adapter绑定 */
        CardStackView mCardStack = (CardStackView) view.findViewById(R.id.cardStackView);
        MembersCardStackAdapter adapter = new MembersCardStackAdapter(getContext());
        mCardStack.setAdapter(adapter);
        mCardStack.setItemExpendListener(this);

        List<VTBData> list = new LinkedList<>();
        for(int i = 0;i<5;i++)
        {
            list.add(new VTBData(true));
        }

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(Arrays.asList(color),list);
                    }
                }
                , 200
        );
//        adapter.updateData(Arrays.asList(color),lists);

//        mCardStack.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(mCardStack));
//        mCardStack.setAnimatorAdapter(new UpDownAnimatorAdapter(mCardStack));
        mCardStack.setAnimatorAdapter(new UpDownStackAnimatorAdapter(mCardStack));
    }

    @Override
    public void onItemExpend(boolean expend) {

    }
}