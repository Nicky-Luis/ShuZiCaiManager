package com.jiangtao.shuzicaimanager.model.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseFragment;
import com.jiangtao.shuzicaimanager.model.setting.SettingFragment;
import com.jiangtao.shuzicaimanager.model.statistical.StatisticalFragment;

import java.lang.reflect.Method;


/**
 * Created by Nicky on 2016/11/12.
 * 主页的fragment manager
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    //main tab
    private String[] titles;
    //上下文
    private Context context;
    //fragmentManager
    private FragmentManager fragmentManager;
    //各个fragment 类
    private Class[] clas = {StatisticalFragment.class, SettingFragment.class};
    //工厂类
    private FragmentFactory fragmentFactory;
    //各个fragment
    private BaseFragment[] mContentFragments;

    /***
     * 构造
     *
     * @param fm
     * @param context
     */
    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.fragmentFactory = new FragmentFactory();
        //获取title资源
        this.titles = context.getResources().getStringArray(R.array.main_tab_titles);
        if (titles.length > 0) {
            mContentFragments = new BaseFragment[titles.length];
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (null == mContentFragments[position]) {
            mContentFragments[position] = fragmentFactory.createProduct(clas[position], position + 1);
        }
        return mContentFragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    ///////////////////////////////////////////////


    /**
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_main_tabs, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_main_txt);
        tv.setText(titles[position]);
        return v;
    }

    /////////////////////////////////////////////////////////////////////
    // 抽象的工厂类，定义了其子类必须实现的createProduct()方法
    private abstract class Factory {
        //运用了Java 中的泛型和反射技术
        public abstract <T extends BaseFragment> T createProduct(Class<T> c, int args);
    }

    /**
     * fragment 工厂类
     */
    private class FragmentFactory extends Factory {
        public <T extends BaseFragment> T createProduct(Class<T> c, int args) {
            T product = null;
            try {
                //product = (T) Class.forName(c.getName()).newInstance();
                //反射获取
                Class<?> threadClazz = Class.forName(c.getName());
                Method method = threadClazz.getMethod("newInstance", int.class);
                product = (T) method.invoke(null, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return product;
        }
    }
}
