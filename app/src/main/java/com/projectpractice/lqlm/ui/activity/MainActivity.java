package com.projectpractice.lqlm.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.base.BaseActivity;
import com.projectpractice.lqlm.base.BaseFragment;
import com.projectpractice.lqlm.databinding.ActivityMainBinding;
import com.projectpractice.lqlm.ui.fragment.HomeFragment;
import com.projectpractice.lqlm.ui.fragment.RedPacketFragment;
import com.projectpractice.lqlm.ui.fragment.SearchFragment;
import com.projectpractice.lqlm.ui.fragment.SelectedFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private BottomNavigationView mainNavigationBar;
    private HomeFragment homeFragment;
    private SelectedFragment selectedFragment;
    private RedPacketFragment redPacketFragment;
    private SearchFragment searchFragment;
    private FragmentManager fm;
    private BaseFragment lastFragment;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        mainNavigationBar = binding.mainNavigationBar;
//        initFragment();
//        initListener();

    //    }
    @Override
    public View getLayoutResource() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    protected void initView() {
        mainNavigationBar = binding.mainNavigationBar;
        homeFragment = new HomeFragment();
        selectedFragment = new SelectedFragment();
        redPacketFragment = new RedPacketFragment();
        searchFragment = new SearchFragment();
        fm = getSupportFragmentManager();
        switchFragment(homeFragment);

    }

    protected void initListener() {
        mainNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();
                Log.d(TAG, "onNavigationItemSelected ===> " + title);
                if (title.equals("首页")) {
                    switchFragment(homeFragment);
                } else if (title.equals("精选")) {
                    switchFragment(selectedFragment);
                } else if (title.equals("特惠")) {
                    switchFragment(redPacketFragment);
                } else if (title.equals("搜索")) {
                    switchFragment(searchFragment);
                }
                return true;
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        if (lastFragment == targetFragment) {
            return;
        }
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        transaction.show(targetFragment);
        lastFragment = targetFragment;
        transaction.commit();
    }


}