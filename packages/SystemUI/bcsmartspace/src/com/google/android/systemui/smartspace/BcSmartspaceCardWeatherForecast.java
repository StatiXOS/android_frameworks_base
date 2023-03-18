package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda6;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda7;
import com.google.android.systemui.lowlightclock.LowLightClockDreamService$$ExternalSyntheticLambda0;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import java.util.Locale;
/* loaded from: classes2.dex */
public class BcSmartspaceCardWeatherForecast extends BcSmartspaceCardSecondary {
    public static final /* synthetic */ int $r8$clinit = 0;

    /* loaded from: classes2.dex */
    public interface ItemUpdateFunction {
        void update(View view, int i);
    }

    public BcSmartspaceCardWeatherForecast(Context context) {
        super(context);
    }

    public BcSmartspaceCardWeatherForecast(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void setTextColor(final int i) {
        updateFields(new ItemUpdateFunction() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardWeatherForecast$$ExternalSyntheticLambda0
            @Override // com.google.android.systemui.smartspace.BcSmartspaceCardWeatherForecast.ItemUpdateFunction
            public final void update(View view, int i2) {
                int i3 = i;
                int i4 = BcSmartspaceCardWeatherForecast.$r8$clinit;
                ((TextView) view).setTextColor(i3);
            }
        }, 4, R.id.temperature_value, "temperature value");
        updateFields(new ItemUpdateFunction() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardWeatherForecast$$ExternalSyntheticLambda1
            @Override // com.google.android.systemui.smartspace.BcSmartspaceCardWeatherForecast.ItemUpdateFunction
            public final void update(View view, int i2) {
                int i3 = i;
                int i4 = BcSmartspaceCardWeatherForecast.$r8$clinit;
                ((TextView) view).setTextColor(i3);
            }
        }, 4, R.id.timestamp, "timestamp");
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        ConstraintLayout constraintLayout;
        ConstraintLayout constraintLayout2;
        super.onFinishInflate();
        View[] viewArr = new ConstraintLayout[4];
        for (int i = 0; i < 4; i++) {
            ConstraintLayout constraintLayout3 = (ConstraintLayout) ViewGroup.inflate(getContext(), R.layout.smartspace_card_weather_forecast_column, null);
            constraintLayout3.setId(View.generateViewId());
            viewArr[i] = constraintLayout3;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, 0);
            View view = viewArr[i2];
            if (i2 > 0) {
                constraintLayout = viewArr[i2 - 1];
            } else {
                constraintLayout = null;
            }
            if (i2 < 3) {
                constraintLayout2 = viewArr[i2 + 1];
            } else {
                constraintLayout2 = null;
            }
            if (i2 == 0) {
                layoutParams.startToStart = 0;
                layoutParams.horizontalChainStyle = 1;
            } else {
                layoutParams.startToEnd = constraintLayout.getId();
            }
            if (i2 == 3) {
                layoutParams.endToEnd = 0;
            } else {
                layoutParams.endToStart = constraintLayout2.getId();
            }
            layoutParams.topToTop = 0;
            layoutParams.bottomToBottom = 0;
            addView(view, layoutParams);
        }
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle extras;
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            extras = null;
        } else {
            extras = baseAction.getExtras();
        }
        boolean z = false;
        if (extras == null) {
            return false;
        }
        if (extras.containsKey("temperatureValues")) {
            String[] stringArray = extras.getStringArray("temperatureValues");
            if (stringArray == null) {
                Log.w("BcSmartspaceCardWeatherForecast", "Temperature values array is null.");
            } else {
                updateFields(new LowLightClockDreamService$$ExternalSyntheticLambda0(stringArray), stringArray.length, R.id.temperature_value, "temperature value");
            }
            z = true;
        }
        if (extras.containsKey("weatherIcons")) {
            Bitmap[] bitmapArr = (Bitmap[]) extras.get("weatherIcons");
            if (bitmapArr == null) {
                Log.w("BcSmartspaceCardWeatherForecast", "Weather icons array is null.");
            } else {
                updateFields(new BubbleController$$ExternalSyntheticLambda6(bitmapArr), bitmapArr.length, R.id.weather_icon, "weather icon");
            }
            z = true;
        }
        if (extras.containsKey("timestamps")) {
            String[] stringArray2 = extras.getStringArray("timestamps");
            if (stringArray2 == null) {
                Log.w("BcSmartspaceCardWeatherForecast", "Timestamps array is null.");
            } else {
                updateFields(new BubbleController$$ExternalSyntheticLambda7(stringArray2), stringArray2.length, R.id.timestamp, "timestamp");
            }
            return true;
        }
        return z;
    }

    public final void updateFields(ItemUpdateFunction itemUpdateFunction, int i, int i2, String str) {
        int i3;
        int i4;
        if (getChildCount() < 4) {
            Log.w("BcSmartspaceCardWeatherForecast", String.format(Locale.US, AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Missing %d ", str, " view(s) to update."), Integer.valueOf(4 - getChildCount())));
            return;
        }
        if (i < 4) {
            Locale locale = Locale.US;
            int i5 = 4 - i;
            Log.w("BcSmartspaceCardWeatherForecast", String.format(locale, AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Missing %d ", str, "(s). Hiding incomplete columns."), Integer.valueOf(i5)));
            if (getChildCount() < 4) {
                Log.w("BcSmartspaceCardWeatherForecast", String.format(locale, "Missing %d columns to update.", Integer.valueOf(4 - getChildCount())));
            } else {
                int i6 = 3 - i5;
                for (int i7 = 0; i7 < 4; i7++) {
                    View childAt = getChildAt(i7);
                    if (i7 <= i6) {
                        i4 = 0;
                    } else {
                        i4 = 8;
                    }
                    BcSmartspaceTemplateDataUtils.updateVisibility(childAt, i4);
                }
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ((ConstraintLayout) getChildAt(0)).getLayoutParams();
                if (i5 == 0) {
                    i3 = 1;
                } else {
                    i3 = 0;
                }
                layoutParams.horizontalChainStyle = i3;
            }
        }
        int min = Math.min(4, i);
        for (int i8 = 0; i8 < min; i8++) {
            View findViewById = getChildAt(i8).findViewById(i2);
            if (findViewById == null) {
                Log.w("BcSmartspaceCardWeatherForecast", String.format(Locale.US, AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Missing ", str, " view to update at column: %d."), Integer.valueOf(i8 + 1)));
                return;
            }
            itemUpdateFunction.update(findViewById, i8);
        }
    }
}
