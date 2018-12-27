package yobin_he.com.openglesdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo
 * @fileName: BaseActivity
 * @Date : 2018/12/26  17:31
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class BaseActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("OpenGl Es Demo");
    }

}
