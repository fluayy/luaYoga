package com.flua.luayoga.yoganode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.yoga.YogaNode;
import com.flua.luayoga.constant.PropertyType;
import com.flua.luayoga.utils.LogUtil;

/**
 * Created by hjx on 2018/11/19
 */
public class YogaListView extends RecyclerView implements IYoga {

    private static final String TAG = "YogaListView";

    /**
     * The native pointer address returned from Jni calling.
     */
    private long self, parent, root;

    private Context context;

    private YogaNode yogaNode;

    private YogaLayoutHelper yogaLayoutHelper;

    private YogaListViewAdapter adapter;

    public YogaListView(@NonNull Context context) {
        super(context);
        this.context = context;
        yogaNode = new YogaNode();
        setLayoutManager(new LinearLayoutManager(context));
        yogaLayoutHelper = YogaLayoutHelper.getInstance();
    }

    @Override
    public boolean setYogaProperty(int type, String propertyName, float value) {
        LogUtil.i(TAG, "setYogaProperty -> propertyName: " + propertyName + ", value: " + value);
        if (PropertyType.YOGA_IS_ENABLE.equals(propertyName)) {
            boolean enabled = value == 1.0f;
            setEnabled(enabled);
            setClickable(enabled);
            return true;
        } else {
            return yogaLayoutHelper.setYogaProperty(yogaNode, propertyName, value);
        }
    }

    @Override
    public float getYogaProperty(int type, String propertyName) {
        return yogaLayoutHelper.getYogaProperty(yogaNode, propertyName);
    }

    @Override
    public YogaNode getYogaNode() {
        return yogaNode;
    }

    @Override
    public void setNativePointer(long self, long parent, long root) {
        this.self = self;
        this.parent = parent;
        this.root = root;
        LogUtil.i(TAG, "The self = " + self + ", parent = " + parent + ", root = " + root);
    }

    @Override
    public long getSelfPointer() {
        return self;
    }

    @Override
    public long getParentPointer() {
        return parent;
    }

    @Override
    public long getRootPointer() {
        return root;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public void inflate() {
        setX(yogaNode.getLayoutX());
        setY(yogaNode.getLayoutY());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.width = (int) yogaNode.getWidth().value;
        params.height = (int) yogaNode.getHeight().value;
        setLayoutParams(params);
        // Bind the adapter.
        adapter = new YogaListViewAdapter(context, self, root);
        setAdapter(adapter);
    }

    @Override
    public void nativeSetBackgroundColor(float r, float g, float b, float a) {

    }

    @Override
    public void nativeAddTapGesture() {

    }

    @Override
    public void nativeAddLongPressGesture() {

    }

    @Override
    public boolean removeFromParent() {
        return false;
    }

    @Override
    public void reloadYoga() {

    }

    /**
     * Jni calling method
     */
    public void nativeListReload() {
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        LogUtil.i(TAG, "android listReload");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
