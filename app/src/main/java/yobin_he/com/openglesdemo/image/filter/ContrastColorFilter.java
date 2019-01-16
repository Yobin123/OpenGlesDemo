package yobin_he.com.openglesdemo.image.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.image.filter
 * @fileName: ContrastColorFilter
 * @Date : 2019/1/16  13:12
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ContrastColorFilter extends AFilter {
    private ColorFilter.Filter filter;
    private int hChangeType;
    private int hChangeColor;

    public ContrastColorFilter(Context context, ColorFilter.Filter filter) {
        super(context,"filter/half_color_vertex.sh", "filter/half_color_fragment.sh");
        this.filter = filter;
    }

    @Override
    public void onDrawSet() {
        GLES20.glUniform1i(hChangeType,filter.getType());
        GLES20.glUniform3fv(hChangeColor,1,filter.data(),0);
    }

    @Override
    public void onDrawCreatedSet(int mProgram) {
        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType");
        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor");
    }
}
