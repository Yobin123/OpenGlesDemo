package yobin_he.com.openglesdemo.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.io.IOException;

import yobin_he.com.openglesdemo.image.filter.AFilter;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.image
 * @fileName: SGLView
 * @Date : 2019/1/16  10:05
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class SGLView extends GLSurfaceView{

    private SGLRender render;

    public SGLView(Context context) {
        this(context,null);
    }

    public SGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        render = new SGLRender(this);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        try {
            render.setImage(BitmapFactory.decodeStream(getResources().getAssets().open("texture/icon.jpg")));
            requestRender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SGLRender getRender(){
        return render;
    }
    public void setFilter(AFilter filter){
        render.setFilter(filter);
    }
}
