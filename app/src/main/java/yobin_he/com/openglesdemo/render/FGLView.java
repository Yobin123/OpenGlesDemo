package yobin_he.com.openglesdemo.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: FGLView
 * @Date : 2018/12/26  17:56
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class FGLView extends GLSurfaceView{
    public FGLRender render;

    public FGLView(Context context) {
        this(context,null);

    }

    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2); //设置版本。
        setRenderer(render = new FGLRender(this));//设置render
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//设置render模式。
    }

    public void setShape(Class<? extends Shape> clazz){
        try {
            render.setShape(clazz);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
