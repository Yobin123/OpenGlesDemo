package yobin_he.com.openglesdemo.render;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import yobin_he.com.openglesdemo.R;

public class FGLViewActivity extends AppCompatActivity {
    private static  final  int REQ_CHOOSE = 0x001;
    private Button mChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
    }
}
