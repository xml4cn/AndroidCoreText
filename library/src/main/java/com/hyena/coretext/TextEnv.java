/*
 * Copyright (C) 2017 The AndroidCoreText Project
 */

package com.hyena.coretext;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.View;

import com.hyena.coretext.blocks.CYBlock;
import com.hyena.coretext.blocks.CYPageBlock;
import com.hyena.coretext.builder.IBlockMaker;
import com.hyena.coretext.event.CYEventDispatcher;
import com.hyena.coretext.utils.CachedPage;
import com.hyena.coretext.utils.EditableValue;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangzc on 17/1/20.
 */
public class TextEnv {

    public static enum Align {
        TOP, CENTER, BOTTOM
    }

    private Context context;
    private int fontSize = 50;
    private int textColor = Color.BLACK;
    private Typeface typeface;
    private int verticalSpacing = 0;
    private int suggestedPageWidth = 0;
    private int suggestedPageHeight = 0;
    private boolean editable = true;
    private Align textAlign = Align.BOTTOM;
    private float mFontScale = 1.0f;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CYEventDispatcher mEventDispatcher = new CYEventDispatcher();
    private SparseArray<EditableValue> mEditableValues = new SparseArray<EditableValue>();
    private String mTag = "";
    private IBlockMaker mBlockMaker;
    private View mAttachedView;

    public TextEnv(Context context) {
        this.context = context;
    }

    public void set(TextEnv textEnv) {
        this.context = textEnv.getContext();
        this.fontSize = textEnv.getFontSize();
        this.textColor = textEnv.getTextColor();
        this.typeface = textEnv.getTypeface();
        this.verticalSpacing = textEnv.getVerticalSpacing();
        this.suggestedPageWidth = textEnv.getSuggestedPageWidth();
        this.suggestedPageHeight = textEnv.getSuggestedPageHeight();
        this.editable = textEnv.isEditable();
        this.textAlign = textEnv.getTextAlign();
        this.mFontScale = textEnv.getFontScale();
        this.mPaint = textEnv.getPaint();
        this.mEditableValues = textEnv.getEditableValues();
        this.mTag = textEnv.getTag();
        this.mBlockMaker = textEnv.getBlockMaker();
        this.mAttachedView = textEnv.mAttachedView;
        mEventDispatcher.set(textEnv.getEventDispatcher());
    }

    public Context getContext() {
        return context;
    }

    public int getFontSize() {
        return fontSize;
    }

    public TextEnv setFontSize(int fontSize) {
        this.fontSize = fontSize;
        mPaint.setTextSize(fontSize);
        return this;
    }

    public void setCachePage(String content, CYPageBlock pageBlock, List<CYBlock> blocks) {
        if (mAttachedView != null) {
            int id = getId(mAttachedView.getContext(), "id_attached");
            HashMap<String, CachedPage> cached = (HashMap<String, CachedPage>) mAttachedView
                    .getTag(id);
            if (cached == null) {
                cached = new HashMap<>();
                mAttachedView.setTag(id, cached);
            }
            cached.put(content, new CachedPage(pageBlock, blocks));
        }
    }

    public void clearCache(String content) {
        if (mAttachedView != null) {
            int id = getId(mAttachedView.getContext(), "id_attached");
            HashMap<String, CachedPage> cached = (HashMap<String, CachedPage>) mAttachedView
                    .getTag(id);
            if (cached != null) {
                cached.remove(content);
            }
        }
    }

    public CachedPage getCachedPage(String content) {
        if (mAttachedView != null) {
            int id = getId(mAttachedView.getContext(), "id_attached");
            HashMap<String, CachedPage> cached = (HashMap<String, CachedPage>) mAttachedView
                    .getTag(id);
            if (cached != null) {
                return cached.get(content);
            }
        }
        return null;
    }

    public int getId(Context context, String paramString) {
        return context.getResources().getIdentifier(paramString, "id", context.getPackageName());
    }

    public TextEnv setAttachedView(View view) {
        this.mAttachedView = view;
        return this;
    }

    public TextEnv setTag(String tag) {
        this.mTag = tag;
        return this;
    }

    public String getTag() {
        return mTag;
    }

    public TextEnv setBlockMaker(IBlockMaker maker) {
        this.mBlockMaker = maker;
        return this;
    }

    public IBlockMaker getBlockMaker() {
        return mBlockMaker;
    }

    public TextEnv setFontScale(float scale) {
        this.mFontScale = scale;
        return this;
    }

    public float getFontScale() {
        return mFontScale;
    }

    public int getTextColor() {
        return textColor;
    }

    public TextEnv setTextColor(int textColor) {
        this.textColor = textColor;
        mPaint.setColor(textColor);
        return this;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public TextEnv setTypeface(Typeface typeface) {
        this.typeface = typeface;
        if (typeface != null)
            mPaint.setTypeface(typeface);
        return this;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public TextEnv setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
        return this;
    }

    public int getSuggestedPageWidth() {
        return suggestedPageWidth;
    }

    public TextEnv setSuggestedPageWidth(int pageWidth) {
        this.suggestedPageWidth = pageWidth;
        return this;
    }

    public int getSuggestedPageHeight() {
        return suggestedPageHeight;
    }

    public TextEnv setSuggestedPageHeight(int pageHeight) {
        this.suggestedPageHeight = pageHeight;
        return this;
    }

    public boolean isEditable() {
        return editable;
    }

    public TextEnv setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public Align getTextAlign() {
        return textAlign;
    }

    public TextEnv setTextAlign(Align textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public CYEventDispatcher getEventDispatcher() {
        return mEventDispatcher;
    }

    public void setEditableValue(int tabId, String value) {
        EditableValue editableValue = getEditableValue(tabId);
        if (editableValue == null) {
            editableValue = new EditableValue();
            setEditableValue(tabId, editableValue);
        }
        editableValue.setValue(value);
    }

    public void setEditableValue(int tabId, EditableValue value) {
        if (mEditableValues == null)
            return;
        mEditableValues.put(tabId, value);
    }

    public EditableValue getEditableValue(int tabId) {
        if (mEditableValues != null) {
            return mEditableValues.get(tabId);
        }
        return null;
    }

    public SparseArray<EditableValue> getEditableValues() {
        return mEditableValues;
    }

    public void clearEditableValues() {
        if (mEditableValues != null) {
            mEditableValues.clear();
        }
    }

    private boolean mDebug = false;
    public TextEnv setDebug(boolean debug) {
        this.mDebug = debug;
        return this;
    }

    public boolean isDebug() {
        return mDebug;
    }
}
