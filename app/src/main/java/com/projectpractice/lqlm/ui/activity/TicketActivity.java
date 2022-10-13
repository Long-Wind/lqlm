package com.projectpractice.lqlm.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projectpractice.lqlm.base.BaseActivity;
import com.projectpractice.lqlm.databinding.ActivityTicketBinding;
import com.projectpractice.lqlm.model.entity.TicketResult;
import com.projectpractice.lqlm.presenter.ITicketPresenter;
import com.projectpractice.lqlm.ui.custom.LoadingView;
import com.projectpractice.lqlm.utils.PresenterManager;
import com.projectpractice.lqlm.view.ITicketCallback;

public class TicketActivity extends BaseActivity implements ITicketCallback {

    private static final String TAG = "TicketActivity";
    private ActivityTicketBinding binding;
    private ITicketPresenter ticketPresenter;
    private ImageView ticketCover;
    private ImageView ticketBack;
    private LoadingView coverLoading;
    private LinearLayout ticketOpen;
    private boolean hasTbApp = false;
    private String ticketCode;

    @Override
    public View getLayoutResource() {
        binding = ActivityTicketBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initPresenter() {
        ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.registerCallback(this);
        //检查是否安装有淘宝
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            if (packageInfo != null) {
                hasTbApp = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            hasTbApp = false;
        }
        Log.d(TAG, "hasTbApp ==> " + hasTbApp);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        long volume = intent.getLongExtra("volume", 0);
        long couponAmount = intent.getLongExtra("coupon_amount", 0);
        String originalPrice = intent.getStringExtra("original_price");
        String title = intent.getStringExtra("title");
        ticketCover = binding.ticketCover;
        TextView ticketVolume = binding.ticketVolume;
        TextView ticketOriginalPrice = binding.ticketOriginalPrice;
        TextView ticketAfterPrice = binding.ticketAfterPrice;
        TextView ticketTitle = binding.ticketTitle;
        TextView ticketCouponAmount = binding.ticketCouponAmount;

        ticketBack = binding.ticketBack;
        coverLoading = binding.coverLoading;
        ticketOpen = binding.ticketOpenBtn;

        ticketTitle.setText(title);
        ticketVolume.setText("已售" + volume);
        ticketOriginalPrice.setText("￥" + originalPrice);
        ticketOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        float finalPrice = Float.parseFloat(originalPrice) - couponAmount;
        ticketAfterPrice.setText(String.format("%.2f", finalPrice));
        ticketCouponAmount.setText(couponAmount + "元优惠券");
    }

    @Override
    protected void initListener() {
        ticketBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ticketOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令到粘贴板
                String code = ticketCode.toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("taobao_ticket_code", code);
                cm.setPrimaryClip(clipData);
                if (hasTbApp) {
                    Intent tbIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
                    tbIntent.setComponent(componentName);
                    startActivity(tbIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"已复制淘口令",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onTicketLoad(String cover, TicketResult ticket) {
        Log.d(TAG, "ticket ==> " + ticket.getData().getTbk_tpwd_create_response().getData().getModel());
        ticketCode = ticket.getData().getTbk_tpwd_create_response().getData().getModel();
        if (!TextUtils.isEmpty(cover)) {
            String coverPath = "https:" + cover + "_400x400.jpg";
            Glide.with(this).load(coverPath).into(ticketCover);
            coverLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoading() {
        coverLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ticketPresenter != null) {
            ticketPresenter.unregisterCallback(this);
        }
    }

}
