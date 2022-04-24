package com.example.asasfans.ui.main.fragment;

import static com.example.asasfans.util.ViewUtilsKt.setMargin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.asasfans.AsApplication;
import com.example.asasfans.R;
import com.example.asasfans.util.ACache;
import com.google.android.material.appbar.AppBarLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author akarinini
 * @description:
 * @date :2022/4/13 15:46
 */
public class NewToolsFragment extends Fragment {
    private ImageView drop;
    private ImageView icon;
    private TextView title;
    private ImageView refresh;
    private ImageView browser;
    private RecyclerView recyclerView;
    private DialogPlus dialog;
    private View dialogView;
    private TextView tips;
    private List<WebInfo> webInfos = new ArrayList<>();
    private int currentIndex;

    public FrameLayout frameLayout;

    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    private List<String> tipList = Arrays.asList(
            "ASDB:能在线查看ASOUL所有录播的时间轴，五人的服饰，出场的场景；有字幕库，能在线搜索AS成员在直播中说过的每一句话，以及出自于哪场录播的几分几秒；内置切片工具，能精准的只下载一整场录播需要的那一小部分",
            "录音棚:收录A-SOUL几乎全部Solo以及合唱歌曲，并可在线播放并缓存，有搜索及标签筛选功能，手机端电脑端皆可使用",
            "AU都在溜:A-SOUL相关投稿播放排行，展示了B站ASOUL相关视频在线观看人数前百的稿件",
//            "二创搜图:A-SOUL二创图片原动态出处搜索",
            "tips:性能问题，有些web第一次加载会很慢，出现ERR_TIMED_OUT请尝试刷新或右上角浏览器打开哦",
            "直播搜图:能根据一张图片，在线搜索到这张图片是出自于哪场录播的几分几秒",
            "枝网查重:提供ASoul评论区小作文查重、检索功能。还有其他小工具：粉丝编号释义(对应的小行星编号)，枝网年度报告，阿草的太极教室(模拟和羊驼私信)等。枝网，用了都说好！另外还为开发者提供查重和OAuth接口。",
            "魂维基:这里记录了最详细的关于虚拟偶像团体A-SOUL及其相关的内容！",
            "小作文:收集AU小作文的网站，有直接搜索和标签筛选功能",
            "方言词典:收录A-SOUL直播及二创产生的衍生梗或偷来的梗，可以进行方便的检索，方便新来的AU快速了解AU话术！",
            "聊天公示:随时能实时查看各大论坛的管理群的聊天记录, 还有能查看五人粉丝量变化",
            "AS抖音:A-SOUL 抖音视频 All in one ！展示了ASOUL所有抖音视频投稿",
            "嘉然音声:收录了嘉然的各种各样的萌萌的声音的按钮站，点击按钮就能发出声音",
            "向晚音声:收录了向晚的各种各样的萌萌的声音的按钮站，点击按钮就能发出声音",
            "作文生成:使用GPT-2模型训练的小作文生成器，可以根据你的输入自动补(feng)写(he)出生草的小作文。（提示：输入越长越有意义补全效果越好）",
            "录播站:在OneDrive，GoogleDrive，百度云盘，阿里云盘，夸克云盘内存放了：ASOUL出道到现在所有的原画录播和配套弹幕及字幕库；录播的音频流文件；4K超分辨率的直播切片；出道到现在唱过的所有歌的MP3歌曲切片；能带弹幕在线观看高码率ASOUL录播"
    );

    public static NewToolsFragment newInstance() {

        Bundle args = new Bundle();

        NewToolsFragment fragment = new NewToolsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void randomText(){
        int min = 0;
        int max = tipList.size();
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        tips.setText(tipList.get(num));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.dialog_web_list))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setPadding(0,AsApplication.Companion.getStatusBarHeight(),0,0)
                .setCancelable(true)
                .setGravity(Gravity.TOP)
                .create();
        dialogView = dialog.getHolderView();
    }

    public WebFragment current(){
        return (WebFragment) getChildFragmentManager().findFragmentById(R.id.frameLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", String.valueOf(currentIndex));
        ACache aCache = ACache.get(getActivity());
        aCache.put("toolsFragmentWebUrl", String.valueOf(currentIndex));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_tools, container, false);

        drop = view.findViewById(R.id.drop);
        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);
        refresh = view.findViewById(R.id.refresh);
        browser = view.findViewById(R.id.browser);
        frameLayout = view.findViewById(R.id.frameLayout);
        tips = view.findViewById(R.id.tips);

        frameLayout.setFocusableInTouchMode(true);
        frameLayout.requestFocus();

        initWebInfo();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRotateAnimation == null) {
                    mRotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnimation.setDuration(800);
                    mRotateAnimation.setRepeatCount(-1);
                }
                refresh.setAnimation(mRotateAnimation);
                refresh.startAnimation(mRotateAnimation);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.clearAnimation();
                    }
                },800);
                updateFragment(currentIndex);
            }
        });

        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(webInfos.get(currentIndex).getWebUrl());
                intent.setData(content_url);
                getActivity().startActivity(intent);
            }
        });

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropDown();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropDown();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropDown();
            }
        });

        ACache aCache = ACache.get(getActivity());
        String tmpACache =  aCache.getAsString("toolsFragmentWebUrl");
        if (tmpACache == null){
            currentIndex = 0;
        }else {
            currentIndex = Integer.valueOf(tmpACache);
        }
        updateFragment(currentIndex);

        View emptyView = view.findViewById(R.id.emptyViewTools);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AsApplication.Companion.getStatusBarHeight());
        emptyView.setLayoutParams(layoutParams);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);
        appBarLayout.setPadding(0, AsApplication.Companion.getStatusBarHeight(),0,0);
        return view;
    }

    private void dropDown(){
        recyclerView = dialogView.findViewById(R.id.tools_recyclerview);
        tips = dialogView.findViewById(R.id.tips);
        NewToolsAdapter toolsAdapter = new NewToolsAdapter(getActivity(), webInfos);
        toolsAdapter.setHasStableIds(true);
        setMargin(recyclerView, 0, 0, 0, AsApplication.Companion.getStatusBarHeight());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(toolsAdapter);
        randomText();
        dialog.show();
    }

    private void initWebInfo(){
        webInfos.clear();
        webInfos.add(new WebInfo("https://asoulcnki.asia", "icon_zwcc", "枝网查重", "枝网查重"));
        webInfos.add(new WebInfo("https://tools.asoulfan.com/zhijiangDict", "icon_zhijiang_book", "方言词典", "方言词典"));
        webInfos.add(new WebInfo("https://asoul.icu/", "icon_long_comment", "小作文", "小作文"));
        webInfos.add(new WebInfo("https://asoul.asia/", "icon_asoul", "管理群聊天记录公示", "聊天公示"));
        webInfos.add(new WebInfo("https://asoulwiki.com/", "icon_asoul", "一个魂维基", "魂维基"));
        webInfos.add(new WebInfo("http://asoul.infedg.xyz/", "icon_asoul", "小作文生成器", "作文生成"));
        webInfos.add(new WebInfo("https://nf.asoul-rec.com", "icon_asoul", "A-SOUL原画录播站", "录播站"));
        webInfos.add(new WebInfo("https://tools.asoulfan.com/ingredientChecking", "icon_asf_bak", "成分姬", "成分姬"));
        webInfos.add(new WebInfo("http://asoulfan.xyz/", "icon_asf_bak", "羊驼打过的太极/QA查询", "查QA"));
        webInfos.add(new WebInfo("https://jiaranjintianchishen.me/", "icon_eat_what", "今天吃什么捏", "吃什么"));
        webInfos.add(new WebInfo("https://bili-url-1251468786.cos-website.ap-shanghai.myqcloud.com/", "icon_url_cos", "黄嘉琪诈骗链接制作工具", "诈骗链接"));
//        webInfos.add(new WebInfo("https://pic.asoulfan.com/", "icon_asf_bak", "搜图姬-二创图片搜索", "二创搜图"));
        webInfos.add(new WebInfo("https://vtbkeyboard.moe/672328094", "icon_diana_keyboard", "Keyboard 嘉然", "嘉然音声"));
        webInfos.add(new WebInfo("https://vtbkeyboard.moe/672346917", "icon_ava_keyboard", "Keyboard 向晚", "向晚音声"));
        webInfos.add(new WebInfo("https://asoul.video/#/", "icon_asoul", "A-SOUL 抖音视频 All in one!", "AS抖音"));
        webInfos.add(new WebInfo("https://livedb.asoulfan.com/PhotoSearch/index.html", "icon_asoul", "A-Soul 直播图片搜索", "直播搜图"));
        webInfos.add(new WebInfo("https://online.asoulfan.com/", "icon_asoul", "AU都在溜什么？", "AU都在溜"));
        webInfos.add(new WebInfo("http://asdb.live", "icon_asoul", "Asoul二创切片的有力助手", "ASDB"));
    }

    public void updateFragment(int index){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();;
        transaction.replace(R.id.frameLayout, WebFragment.newInstance(webInfos.get(index).getWebUrl(), false));
        currentIndex = index;
        title.setText(webInfos.get(index).getWebDesc());
        icon.setImageResource(getResource(webInfos.get(index).getWebIconFileName(), getActivity()));
        transaction.commit();
    }

    public void dialog(){
        dialog.show();
    }

    public class NewToolsAdapter extends RecyclerView.Adapter<NewToolsAdapter.NewToolsViewHolder>{
        private Context context;
        private List<WebInfo> webInfos;

        public NewToolsAdapter(Context context, List<WebInfo> webInfos) {
            this.context = context;
            this.webInfos = webInfos;
        }

        @NonNull
        @Override
        public NewToolsAdapter.NewToolsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.tools_recyclerview, parent,false);
            NewToolsAdapter.NewToolsViewHolder newToolsViewHolder = new NewToolsViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFragment(newToolsViewHolder.getBindingAdapterPosition());
                    dialog.dismiss();
                }
            });
            return newToolsViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewToolsViewHolder holder, int position) {
            holder.imageView.setImageResource(getResource(webInfos.get(position).getWebIconFileName(), context));
            holder.textView.setText(webInfos.get(position).getWebName());
        }



        @Override
        public int getItemCount() {
            return webInfos.size();
        }

        public class NewToolsViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private ImageView imageView;

            public NewToolsViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.web_desc);
                imageView = itemView.findViewById(R.id.web_icon);
            }
        }
    }
    public class WebInfo{
        private String webUrl;
        private String WebIconFileName;
        private String webDesc;
        private String webName;

        public WebInfo(String webUrl, String webIconFileName, String webDesc, String webName) {
            this.webUrl = webUrl;
            WebIconFileName = webIconFileName;
            this.webDesc = webDesc;
            this.webName = webName;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getWebIconFileName() {
            return WebIconFileName;
        }

        public void setWebIconFileName(String webIconFileName) {
            WebIconFileName = webIconFileName;
        }

        public String getWebDesc() {
            return webDesc;
        }

        public void setWebDesc(String webDesc) {
            this.webDesc = webDesc;
        }

        public String getWebName() {
            return webName;
        }

        public void setWebName(String webName) {
            this.webName = webName;
        }
    }
    public static int getResource(String imageName, Context c) {
        int resId = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());
        return resId;
    }

}
