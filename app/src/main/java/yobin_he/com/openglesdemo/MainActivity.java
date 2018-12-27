package yobin_he.com.openglesdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import yobin_he.com.openglesdemo.render.FGLViewActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView mList;
    private ArrayList<MenuBean> data;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mList = findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        if(data == null){
            data = new ArrayList<>();
        }

        add("绘制形体", FGLViewActivity.class);
        mList.setAdapter(new MenuAdapter());

    }



    private class MenuBean{
        String name;
        Class<?> clazz;
    }

    private void  add(String name,Class<?> clazz){
        MenuBean bean = new MenuBean();
        bean.name = name;
        bean.clazz = clazz;
        data.add(bean);
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        MenuBean bean = data.get(position);
        startActivity(new Intent(this,bean.clazz));
    }


}
