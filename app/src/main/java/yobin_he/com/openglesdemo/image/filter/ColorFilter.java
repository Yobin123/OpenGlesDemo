package yobin_he.com.openglesdemo.image.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.image.filter
 * @fileName: ColorFilter
 * @Date : 2019/1/16  13:14
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ColorFilter extends AFilter {

    private Filter filter;
    private int hChangeType;
    private int hChangeColor;


    public ColorFilter(Context context, Filter filter) {
        super(context, "filter/default_vertex.sh", "filter/color_fragment.sh");
        this.filter = filter;
    }

    @Override
    public void onDrawSet() {
        GLES20.glUniform1i(hChangeType, filter.getType());
        GLES20.glUniform3fv(hChangeColor, 1, filter.data(), 0);
    }

    @Override
    public void onDrawCreatedSet(int mProgram) {
        hChangeType = GLES20.glGetUniformLocation(mProgram, "vChangeType");
        hChangeColor = GLES20.glGetUniformLocation(mProgram, "vChangeColor");
    }

    public enum Filter {
        NONE(0, new float[]{0.0f, 0.0f, 0.0f}),
        GRAY(1, new float[]{0.299f, 0.587f, 0.114f}),
        COOL(2, new float[]{0.0f, 0.0f, 0.1f}),
        WARM(2, new float[]{0.1f, 0.1f, 0.1f}),
        BLUR(3, new float[]{0.006f, 0.004f, 0.002f}),
        MAGN(4, new float[]{0.0f, 0.0f, 0.4f});

        private int vChangeType;
        private float[] data;

        Filter(int vChangeType, float[] data) {
            this.vChangeType = vChangeType;
            this.data = data;
        }

        public int getType() {
            return vChangeType;
        }

        public float[] data() {
            return data;
        }
    }
}
