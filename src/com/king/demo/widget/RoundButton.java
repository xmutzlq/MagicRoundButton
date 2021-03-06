package com.king.demo.widget;

import com.king.demo.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author zlq
 */
public class RoundButton extends TextView {
	
	private RoundDrawable rd;
	private float pressedRatio;
	
	private int startColor, endColor; 
	private boolean isNeedDarkPress;
	
    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);

        pressedRatio = a.getFloat(R.styleable.RoundButton_btnPressedRatio, 0.80f);
        int cornerRadius = a.getLayoutDimension(R.styleable.RoundButton_btnCornerRadius, 0);

        ColorStateList solidColor = a.getColorStateList(R.styleable.RoundButton_btnSolidColor);
        int strokeColor = a.getColor(R.styleable.RoundButton_btnStrokeColor, 0x0);
        int strokeWidth = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeWidth, 0);
        int strokeDashWidth = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeDashWidth, 0);
        int strokeDashGap = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeDashGap, 0);

        a.recycle();

        setSingleLine(true);
        setGravity(Gravity.CENTER);

        rd = new RoundDrawable(cornerRadius == -1);
        rd.setCornerRadius(cornerRadius == -1 ? 0 : cornerRadius);
        rd.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
        
        if (solidColor == null) {
            solidColor = ColorStateList.valueOf(0);
        }
        if (solidColor.isStateful()) {
            rd.setSolidColors(solidColor);
        } else if (pressedRatio > 0.0001f) {
            rd.setSolidColors(csl(solidColor.getDefaultColor(), pressedRatio));
        } else {
            rd.setColor(solidColor.getDefaultColor());
        }
        setBackground(rd);
    }

    public void setCornerRadius(int cornerRadius) {
    	if(rd != null) {
    		rd.setCornerRadius(cornerRadius);
    	}
    }
    
    public void reSetBg(int id) {
    	if(rd != null) {
    		ColorStateList csl = ContextCompat.getColorStateList(getContext(), id);
    		rd.setSolidColors(csl);
    	}
    	setBackground(rd);
    }
    
    public void reSetBg(int startColor, int endColor) {
    	reSetBg(startColor, endColor, true);
    }
    
    public void reSetBg(int startColor, int endColor, boolean isNeedDarkPress) {
    	this.startColor = startColor;
    	this.endColor = endColor;
    	this.isNeedDarkPress = isNeedDarkPress;
    	if(rd != null) {
    		ColorStateList csl_start = ContextCompat.getColorStateList(getContext(), startColor);
    		ColorStateList csl_end = ContextCompat.getColorStateList(getContext(), endColor);
    		if(isNeedDarkPress) {
    			rd.setSolidColors(csl(csl_start.getDefaultColor(), pressedRatio), 
    					csl(csl_end.getDefaultColor(), pressedRatio));
    		} else {
    			rd.setSolidColors(csl_start, csl_end);
    		}
    	}
    	setBackground(rd);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	if(!enabled) {
    		reSetBg(R.color.color_c6cbd7, R.color.color_c6cbd7, false);
    	}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(MotionEvent.ACTION_CANCEL == event.getAction()
    			|| MotionEvent.ACTION_UP == event.getAction()) {
    		if(startColor != 0 && endColor != 0) {
    			reSetBg(startColor, endColor, isNeedDarkPress);
    		}
    	}
    	return super.onTouchEvent(event);
    }
    
    // 灰度
    public int greyer(int color) {
        int blue = (color & 0x000000FF) >> 0;
        int green = (color & 0x0000FF00) >> 8;
        int red = (color & 0x00FF0000) >> 16;
        int grey = Math.round(red * 0.299f + green * 0.587f + blue * 0.114f);
        return Color.argb(0xff, grey, grey, grey);
    }

    // 明度
    int darker(int color, float ratio) {
        color = (color >> 24) == 0 ? 0x22808080 : color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= ratio;
        return Color.HSVToColor(color >> 24, hsv);
    }

    ColorStateList csl(int normal, float ratio) {
        //        int disabled = greyer(normal);
        int pressed = darker(normal, ratio);
        int[][] states = new int[][]{{android.R.attr.state_pressed}, {}};
        int[] colors = new int[]{pressed, normal};
        return new ColorStateList(states, colors);
    }

    private static class RoundDrawable extends GradientDrawable {
        private boolean mIsStadium = false;
        private boolean mIsGradientColor = false;
        private ColorStateList mSolidColors;
        private ColorStateList mEndSolidColors;
        private int mFillColor;

        public RoundDrawable(boolean isStadium) {
            mIsStadium = isStadium;
        }

        public void setSolidColors(ColorStateList colors) {
        	mIsGradientColor = false;
            mSolidColors = colors;
            setColor(colors.getDefaultColor());
        }
        
        public void setSolidColors(ColorStateList startColor, ColorStateList endColor) {
        	mIsGradientColor = true;
        	mSolidColors = startColor;
        	mEndSolidColors = endColor;
        	setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            setColors(new int[]{startColor.getDefaultColor(), endColor.getDefaultColor()});
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            if (mIsStadium) {
                RectF rect = new RectF(getBounds());
                setCornerRadius((rect.height() > rect.width() ? rect.width() : rect.height()) / 2);
            }
        }

        @Override
        public void setColor(int argb) {
            mFillColor = argb;
            super.setColor(argb);
        }

        @Override
        protected boolean onStateChange(int[] stateSet) {
            if (mSolidColors != null) {
            	if(mIsGradientColor) {
            		final int newStartColor = mSolidColors != null ? mSolidColors.getColorForState(stateSet, 0) : 0;
            		final int newEndColor = mEndSolidColors != null ? mEndSolidColors.getColorForState(stateSet, 0) : 0;
            		if (mFillColor != newStartColor) {
                        setColors(new int[]{newStartColor, newEndColor});
                        return true;
                    }
            	} else {
            		final int newStartColor = mSolidColors != null ? mSolidColors.getColorForState(stateSet, 0) : 0;
            		if (mFillColor != newStartColor) {
                        setColor(newStartColor);
                        return true;
                    }
            	}
            }
            return false;
        }

        @Override
        public boolean isStateful() {
            return super.isStateful() || (mSolidColors != null && mSolidColors.isStateful());
        }
    }
}
