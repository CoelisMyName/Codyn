package com.coel.codyn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.coel.codyn.collector.ActivityCollector;
import com.coel.codyn.cypherUtil.Function;
import com.coel.codyn.fragment.key.KeyVM;
import com.coel.codyn.room.Key;
import com.coel.codyn.room.User;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ActivityMain extends AppCompatActivity {
    public static final int ADD_KEY_REQUEST = 1;
    public static final int EDIT_KEY_REQUEST = 2;
    public static final int LOGIN_REQUEST = 3;
    public static final String USER_NAME = "com.coel.codyn.EXTRA_USER_NAME";
    private MainVM mainVM;
    private KeyVM keyVM;

    private TextView key_type;
    private TextView key;
    private TextView key_attr;
    private TextView user_name;

    private AppBarConfiguration NavCfg;//工具栏配置

    //private Handler handler;//异步消息处理器

    /*public Handler getHandler() {
        return handler;
    }*/

    @Override //创建活动视图
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);//向活动收集器添加活动
        setContentView(R.layout.activity_main);//设置布局样式

        Toolbar toolbar = findViewById(R.id.toolbar_main);//获取工具栏
        setSupportActionBar(toolbar);//本活动设置工具栏

        key_type = findViewById(R.id.key_info_type);
        key = findViewById(R.id.key_info_key);
        key_attr = findViewById(R.id.key_info_attr);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);//获得DrawerLayout实例
        NavigationView sideNavView = findViewById(R.id.side_nav_view_main);//获得侧边栏实例
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view_main);//获得底部导航栏实例
        user_name = sideNavView.findViewById(R.id.textView_user_name);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.open, R.string.close);//侧边栏开关绑定在工具栏上（左侧三道杠）
        actionBarDrawerToggle.syncState();

        NavCfg = new AppBarConfiguration.Builder(R.id.bottom_nav_function, R.id.bottom_nav_key).build();//设定AppBarConfiguration

        NavController NavController = Navigation.findNavController(this, R.id.nav_host_fragment);//获得nav fragment实例

        NavigationUI.setupActionBarWithNavController(this, NavController, NavCfg);//将id对应fragment与工具栏绑定，不绑定顶部会出现返回箭头
        NavigationUI.setupWithNavController(bottomNavView, NavController);//底部导航栏与nav fragment绑定

        mainVM = new ViewModelProvider(this).get(MainVM.class);
        Log.d(this.getClass().toString(), "Create Main Activity View Success");
        keyVM = new ViewModelProvider(this).get(KeyVM.class);

        mainVM.getKey_type().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                key_type.setText(Function.key_type2Str(integer));
            }
        });

        mainVM.getKey_attr().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                key_attr.setText(Function.key_attr2String(integer));
            }
        });

        mainVM.getKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                key.setText(s);
            }
        });

        startActivityForResult(new Intent(this, ActivityLogin.class), LOGIN_REQUEST);
        Log.d(this.getClass().toString(), "Start LoginActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//顶部工具栏填充菜单
        getMenuInflater().inflate(R.menu.bar_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_scan:
                Toast.makeText(this, "扫描二维码", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        //登录初始化
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            //TODO 初始化
            Log.d(this.getClass().toString(), "enter login");
            mainVM.initialUser(data.getStringExtra(USER_NAME));
            mainVM.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        keyVM.setUser_id(user.getId());
                        //                    user_name.setText(user.getUser_name());
                    }
                }
            });
            Log.d(this.getClass().toString(), "Main Activity Data Initial Success");
        } else if (requestCode == LOGIN_REQUEST) {
            Log.d(this.getClass().toString(), "enter cancel login");
            //登录错误整个退出
            finish();
        } else if (requestCode == ADD_KEY_REQUEST && resultCode == RESULT_OK) {
            Log.d(this.getClass().toString(), "enter add key");
            if (data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1) == -1) {
                Toast.makeText(this, "Could not create new key! Error!", Toast.LENGTH_SHORT).show();
                return;
            }

            //从data获得值
            Key key = new Key(mainVM.getUser().getValue().getId(),
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY));

            //插入新钥匙
            keyVM.insertKey(key);

            Toast.makeText(this, "Key saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_KEY_REQUEST && resultCode == RESULT_OK) {
            Log.d(this.getClass().toString(), "enter edit key");
            if (data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1) == -1 ||
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_ID, -1) == -1) {
                Toast.makeText(this, "Could not update! Error!", Toast.LENGTH_SHORT).show();
                return;
            }

            Key key = new Key(mainVM.getUser().getValue().getId(),
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY));
            key.setId(data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_ID, -1));

            //更新钥匙
            keyVM.updateKey(key);

            Toast.makeText(this, "Key saved!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Key not saved!", Toast.LENGTH_SHORT).show();
        }
        Log.d(this.getClass().toString(), "onActivityResult Success");
    }

    @Override
    protected void onDestroy() {//摧毁活动
        super.onDestroy();
        ActivityCollector.removeActivity(this);//从活动收集器去除活动
    }
}
