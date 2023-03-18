package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardSliceProvider$$ExternalSyntheticLambda0;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.util.Assert;
import com.google.protobuf.nano.MessageNano;
import dagger.internal.SetBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class SmartSpaceController implements Dumpable {
    public static final boolean DEBUG = Log.isLoggable("SmartSpaceController", 3);
    public final AlarmManager mAlarmManager;
    public boolean mAlarmRegistered;
    public final Handler mBackgroundHandler;
    public final BroadcastSender mBroadcastSender;
    public final Context mContext;
    public int mCurrentUserId;
    public final SmartSpaceData mData;
    public boolean mHidePrivateData;
    public boolean mHideWorkData;
    public final AnonymousClass1 mKeyguardMonitorCallback;
    public final SetBuilder mStore;
    public final Handler mUiHandler;
    public final ArrayList<SmartSpaceUpdateListener> mListeners = new ArrayList<>();
    public final KeyguardSliceProvider$$ExternalSyntheticLambda0 mExpireAlarmAction = new KeyguardSliceProvider$$ExternalSyntheticLambda0(1, this);

    /* loaded from: classes2.dex */
    public class UserSwitchReceiver extends BroadcastReceiver {
        public UserSwitchReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (SmartSpaceController.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("Switching user: ");
                m.append(intent.getAction());
                m.append(" uid: ");
                m.append(UserHandle.myUserId());
                Log.d("SmartSpaceController", m.toString());
            }
            if (intent.getAction().equals("android.intent.action.USER_SWITCHED")) {
                SmartSpaceController.this.mCurrentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                SmartSpaceController smartSpaceController = SmartSpaceController.this;
                SmartSpaceData smartSpaceData = smartSpaceController.mData;
                smartSpaceData.mWeatherCard = null;
                smartSpaceData.mCurrentCard = null;
                smartSpaceController.onExpire(true);
            }
            SmartSpaceController.this.onExpire(true);
        }
    }

    public final boolean isSmartSpaceDisabledByExperiments() {
        boolean z;
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "always_on_display_constants");
        KeyValueListParser keyValueListParser = new KeyValueListParser(',');
        try {
            keyValueListParser.setString(string);
            z = keyValueListParser.getBoolean("smart_space_enabled", true);
        } catch (IllegalArgumentException unused) {
            Log.e("SmartSpaceController", "Bad AOD constants");
            z = true;
        }
        return !z;
    }

    public final SmartSpaceCard loadSmartSpaceData(boolean z) {
        SmartspaceProto$CardWrapper smartspaceProto$CardWrapper = new SmartspaceProto$CardWrapper();
        SetBuilder setBuilder = this.mStore;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("smartspace_");
        m.append(this.mCurrentUserId);
        m.append("_");
        m.append(z);
        File fileStreamPath = ((Context) setBuilder.contributions).getFileStreamPath(m.toString());
        boolean z2 = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
            int length = (int) fileStreamPath.length();
            byte[] bArr = new byte[length];
            fileInputStream.read(bArr, 0, length);
            MessageNano.mergeFrom(smartspaceProto$CardWrapper, bArr);
            fileInputStream.close();
            z2 = true;
        } catch (FileNotFoundException unused) {
            Log.d("ProtoStore", "no cached data");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to load data", e);
        }
        if (z2) {
            return SmartSpaceCard.fromWrapper(this.mContext, smartspaceProto$CardWrapper, !z);
        }
        return null;
    }

    public final void onGsaChanged() {
        if (DEBUG) {
            Log.d("SmartSpaceController", "onGsaChanged");
        }
        ArrayList arrayList = new ArrayList(this.mListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((SmartSpaceUpdateListener) arrayList.get(i)).onGsaChanged();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a9 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void update() {
        /*
            r18 = this;
            r0 = r18
            com.android.systemui.util.Assert.isMainThread()
            boolean r1 = com.google.android.systemui.smartspace.SmartSpaceController.DEBUG
            java.lang.String r2 = "SmartSpaceController"
            if (r1 == 0) goto L10
            java.lang.String r3 = "update"
            android.util.Log.d(r2, r3)
        L10:
            boolean r3 = r0.mAlarmRegistered
            r4 = 0
            if (r3 == 0) goto L1e
            android.app.AlarmManager r3 = r0.mAlarmManager
            com.android.systemui.keyguard.KeyguardSliceProvider$$ExternalSyntheticLambda0 r5 = r0.mExpireAlarmAction
            r3.cancel(r5)
            r0.mAlarmRegistered = r4
        L1e:
            com.google.android.systemui.smartspace.SmartSpaceData r3 = r0.mData
            boolean r5 = r3.hasCurrent()
            r6 = 0
            r8 = 1
            if (r5 == 0) goto L43
            com.google.android.systemui.smartspace.SmartSpaceCard r5 = r3.mWeatherCard
            if (r5 == 0) goto L2f
            r5 = r8
            goto L30
        L2f:
            r5 = r4
        L30:
            if (r5 == 0) goto L43
            com.google.android.systemui.smartspace.SmartSpaceCard r5 = r3.mCurrentCard
            long r9 = r5.getExpiration()
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mWeatherCard
            long r11 = r3.getExpiration()
            long r9 = java.lang.Math.min(r9, r11)
            goto L5d
        L43:
            boolean r5 = r3.hasCurrent()
            if (r5 == 0) goto L50
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mCurrentCard
            long r9 = r3.getExpiration()
            goto L5d
        L50:
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mWeatherCard
            if (r3 == 0) goto L56
            r5 = r8
            goto L57
        L56:
            r5 = r4
        L57:
            if (r5 == 0) goto L5f
            long r9 = r3.getExpiration()
        L5d:
            r13 = r9
            goto L60
        L5f:
            r13 = r6
        L60:
            int r3 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r3 <= 0) goto L76
            android.app.AlarmManager r11 = r0.mAlarmManager
            r12 = 0
            com.android.systemui.keyguard.KeyguardSliceProvider$$ExternalSyntheticLambda0 r3 = r0.mExpireAlarmAction
            android.os.Handler r5 = r0.mUiHandler
            java.lang.String r15 = "SmartSpace"
            r16 = r3
            r17 = r5
            r11.set(r12, r13, r15, r16, r17)
            r0.mAlarmRegistered = r8
        L76:
            java.util.ArrayList<com.google.android.systemui.smartspace.SmartSpaceUpdateListener> r3 = r0.mListeners
            if (r3 == 0) goto La9
            if (r1 == 0) goto L8e
            java.lang.String r1 = "notifying listeners data="
            java.lang.StringBuilder r1 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r1)
            com.google.android.systemui.smartspace.SmartSpaceData r3 = r0.mData
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r2, r1)
        L8e:
            java.util.ArrayList r1 = new java.util.ArrayList
            java.util.ArrayList<com.google.android.systemui.smartspace.SmartSpaceUpdateListener> r2 = r0.mListeners
            r1.<init>(r2)
            int r2 = r1.size()
        L99:
            if (r4 >= r2) goto La9
            java.lang.Object r3 = r1.get(r4)
            com.google.android.systemui.smartspace.SmartSpaceUpdateListener r3 = (com.google.android.systemui.smartspace.SmartSpaceUpdateListener) r3
            com.google.android.systemui.smartspace.SmartSpaceData r5 = r0.mData
            r3.onSmartSpaceUpdated(r5)
            int r4 = r4 + 1
            goto L99
        La9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.SmartSpaceController.update():void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.google.android.systemui.smartspace.SmartSpaceController$1] */
    public SmartSpaceController(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler, AlarmManager alarmManager, BroadcastSender broadcastSender, DumpManager dumpManager) {
        ?? r0 = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.smartspace.SmartSpaceController.1
            /* JADX WARN: Removed duplicated region for block: B:27:0x005b  */
            /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onTimeChanged() {
                /*
                    r10 = this;
                    com.google.android.systemui.smartspace.SmartSpaceController r0 = com.google.android.systemui.smartspace.SmartSpaceController.this
                    com.google.android.systemui.smartspace.SmartSpaceData r0 = r0.mData
                    if (r0 == 0) goto L60
                    boolean r0 = r0.hasCurrent()
                    if (r0 == 0) goto L60
                    com.google.android.systemui.smartspace.SmartSpaceController r0 = com.google.android.systemui.smartspace.SmartSpaceController.this
                    com.google.android.systemui.smartspace.SmartSpaceData r0 = r0.mData
                    r0.getClass()
                    long r1 = java.lang.System.currentTimeMillis()
                    boolean r3 = r0.hasCurrent()
                    r4 = 0
                    r6 = 1
                    r7 = 0
                    if (r3 == 0) goto L3b
                    com.google.android.systemui.smartspace.SmartSpaceCard r3 = r0.mWeatherCard
                    if (r3 == 0) goto L27
                    r3 = r6
                    goto L28
                L27:
                    r3 = r7
                L28:
                    if (r3 == 0) goto L3b
                    com.google.android.systemui.smartspace.SmartSpaceCard r3 = r0.mCurrentCard
                    long r6 = r3.getExpiration()
                    com.google.android.systemui.smartspace.SmartSpaceCard r0 = r0.mWeatherCard
                    long r8 = r0.getExpiration()
                    long r6 = java.lang.Math.min(r6, r8)
                    goto L54
                L3b:
                    boolean r3 = r0.hasCurrent()
                    if (r3 == 0) goto L48
                    com.google.android.systemui.smartspace.SmartSpaceCard r0 = r0.mCurrentCard
                    long r6 = r0.getExpiration()
                    goto L54
                L48:
                    com.google.android.systemui.smartspace.SmartSpaceCard r0 = r0.mWeatherCard
                    if (r0 == 0) goto L4d
                    goto L4e
                L4d:
                    r6 = r7
                L4e:
                    if (r6 == 0) goto L56
                    long r6 = r0.getExpiration()
                L54:
                    long r6 = r6 - r1
                    goto L57
                L56:
                    r6 = r4
                L57:
                    int r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
                    if (r0 <= 0) goto L60
                    com.google.android.systemui.smartspace.SmartSpaceController r10 = com.google.android.systemui.smartspace.SmartSpaceController.this
                    r10.update()
                L60:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.SmartSpaceController.AnonymousClass1.onTimeChanged():void");
            }
        };
        this.mKeyguardMonitorCallback = r0;
        this.mContext = context;
        this.mUiHandler = new Handler(Looper.getMainLooper());
        this.mStore = new SetBuilder(context);
        new HandlerThread("smartspace-background").start();
        this.mBackgroundHandler = handler;
        this.mCurrentUserId = UserHandle.myUserId();
        this.mAlarmManager = alarmManager;
        this.mBroadcastSender = broadcastSender;
        SmartSpaceData smartSpaceData = new SmartSpaceData();
        this.mData = smartSpaceData;
        if (isSmartSpaceDisabledByExperiments()) {
            return;
        }
        keyguardUpdateMonitor.registerCallback(r0);
        smartSpaceData.mCurrentCard = loadSmartSpaceData(true);
        smartSpaceData.mWeatherCard = loadSmartSpaceData(false);
        update();
        onGsaChanged();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.smartspace.SmartSpaceController.2
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                SmartSpaceController.this.onGsaChanged();
            }
        };
        String[] strArr = {"android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_DATA_CLEARED"};
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < 4; i++) {
            intentFilter.addAction(strArr[i]);
        }
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart("com.google.android.googlequicksearchbox", 0);
        context.registerReceiver(broadcastReceiver, intentFilter, 2);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_SWITCHED");
        intentFilter2.addAction("android.intent.action.USER_UNLOCKED");
        context.registerReceiver(new UserSwitchReceiver(), intentFilter2);
        context.registerReceiver(new SmartSpaceBroadcastReceiver(this, this.mBroadcastSender), new IntentFilter("com.google.android.apps.nexuslauncher.UPDATE_SMARTSPACE"), "android.permission.CAPTURE_AUDIO_HOTWORD", this.mUiHandler, 2);
        dumpManager.registerDumpable(SmartSpaceController.class.getName(), this);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println("SmartspaceController");
        printWriter.println("  weather " + this.mData.mWeatherCard);
        printWriter.println("  current " + this.mData.mCurrentCard);
        printWriter.println("serialized:");
        printWriter.println("  weather " + loadSmartSpaceData(false));
        printWriter.println("  current " + loadSmartSpaceData(true));
        printWriter.println("disabled by experiment: " + isSmartSpaceDisabledByExperiments());
    }

    public final void onExpire(boolean z) {
        Assert.isMainThread();
        this.mAlarmRegistered = false;
        if (!this.mData.handleExpire() && !z) {
            if (DEBUG) {
                Log.d("SmartSpaceController", "onExpire - cancelled");
                return;
            }
            return;
        }
        update();
    }
}
