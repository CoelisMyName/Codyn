package com.coel.codyn;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.appUtil.JSONUtil;
import com.coel.codyn.appUtil.SystemUtil;
import com.coel.codyn.appUtil.ViewUtil;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.KeyUtil;
import com.coel.codyn.fragment.key.KeyVM;
import com.coel.codyn.room.Key;
import com.coel.codyn.room.User;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMain extends AppCompatActivity {
    public static final int LOGIN_REQUEST = 3;
    public static final int SCAN_QR_REQUEST = 4;

    private MainVM mainVM;
    private KeyVM keyVM;

    private View infoview;
    private TextView key_type;
    private TextView key;
    private TextView key_attr;

    private TextView user_name;

    private AppBarConfiguration NavCfg;//工具栏配置

    private FileCryptoService.CryptoBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (FileCryptoService.CryptoBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    //private Handler handler;//异步消息处理器

    /*public Handler getHandler() {
        return handler;
    }*/


    @Override //创建活动视图
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);//设置布局样式

        Toolbar toolbar = findViewById(R.id.toolbar_main);//获取工具栏
        setSupportActionBar(toolbar);//本活动设置工具栏

        infoview = findViewById(R.id.layout_key_info_main_);
        key_type = findViewById(R.id.key_info_type);
        key = findViewById(R.id.key_info_key);
        key_attr = findViewById(R.id.key_info_attr);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);//获得DrawerLayout实例
        NavigationView sideNavView = findViewById(R.id.side_nav_view_main);//获得侧边栏实例
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view_main);//获得底部导航栏实例
        user_name = sideNavView.getHeaderView(0).findViewById(R.id.textView_user_name);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.open, R.string.close);//侧边栏开关绑定在工具栏上（左侧三道杠）
        actionBarDrawerToggle.syncState();

        NavCfg = new AppBarConfiguration.Builder(R.id.bottom_nav_function, R.id.bottom_nav_key).build();//设定AppBarConfiguration

        NavController NavController = Navigation.findNavController(this, R.id.nav_host_fragment);//获得nav fragment实例

        NavigationUI.setupActionBarWithNavController(this, NavController, NavCfg);//将id对应fragment与工具栏绑定，不绑定顶部会出现返回箭头
        NavigationUI.setupWithNavController(bottomNavView, NavController);//底部导航栏与nav fragment绑定

        mainVM = new ViewModelProvider(this).get(MainVM.class);
        keyVM = new ViewModelProvider(this).get(KeyVM.class);

        //注册观察事件
        mainVM.getInfo().observe(this, new Observer<Info>() {
            @Override
            public void onChanged(Info info) {
                if (info.getTYPE() == Info.DEFAULT) {
                    infoview.setVisibility(View.GONE);
                } else {
                    infoview.setVisibility(View.VISIBLE);
                    key_type.setText(KeyUtil.type2Str(info.getTYPE()));
                    key_attr.setText(KeyUtil.attrStr(info.getATTR()));
                    key.setText(Coder.Base64_encode2text(info.getKey()));
                }
            }
        });

        startActivityForResult(new Intent(this, ActivityLogin.class), LOGIN_REQUEST);
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
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            ActivityScanQR.CAMERA_PERMISSION_REQUEST);
                }
                else {
                    startActivityForResult(new Intent(getApplicationContext(),ActivityScanQR.class),SCAN_QR_REQUEST);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        //登录初始化
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK && data.hasExtra(ActivityLogin.EXTRA_USER_ID)) {
            int uid = data.getIntExtra(ActivityLogin.EXTRA_USER_ID, -1);
            if (uid == -1) {
                finish();
            }
            mainVM.initialUser(uid);
            mainVM.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        keyVM.setUserId(user.getId());
                        user_name.setText(user.getUser_name());
                    }
                }
            });
            return;
        }
        if (requestCode == SCAN_QR_REQUEST && resultCode == RESULT_OK && data.hasExtra(ActivityScanQR.EXTRA_QR_STRING)) {
            String jsonStr = data.getStringExtra(ActivityScanQR.EXTRA_QR_STRING);
            JSONObject result;
            try {
                assert jsonStr != null;
                result = new JSONObject(jsonStr);
                int attr = result.getInt(JSONUtil.KEY_ATTR);
                int type = result.getInt(JSONUtil.KEY_TYPE);
                String ks = result.getString(JSONUtil.KEY_VALUE);
                Key key;
                if(attr == Key.PUBLIC_KEY){
                    key = new Key(keyVM.getUser_id(),type,"new QR scan Key,","",ks);
                }
                else{
                    key = new Key(keyVM.getUser_id(),type,"new QR scan Key,",ks,"");
                }

                keyVM.insertKey(key);
                ViewUtil.showToast(getApplicationContext(),"Scan QR success, Key inserted");
            } catch (JSONException e) {
                e.printStackTrace();
                ViewUtil.showToast(getApplicationContext(),"Invalid QR code");
                return;
            }
            return;
        }

        Log.d("ERR", "requestCode: " + requestCode + " resultCode: " + resultCode);
    }
}
