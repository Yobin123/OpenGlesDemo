package yobin_he.com.openglesdemo.render;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yobin_he.com.openglesdemo.ChooseActivity;
import yobin_he.com.openglesdemo.R;

public class FGLViewActivity extends AppCompatActivity {
    private static  final  int REQ_CHOOSE = 0x001;
    private Button mChange;
    private FGLView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
        init();
    }

    private void init() {
        mChange = findViewById(R.id.mChange);
        mGLView = findViewById(R.id.mGLView);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mChange:
                // TODO: 2018/12/27 进行跳转
                Intent intent=new Intent(this,ChooseActivity.class);
                startActivityForResult(intent,REQ_CHOOSE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            mGLView.setShape((Class<? extends Shape>)data.getSerializableExtra("name"));
        }
    }
}
