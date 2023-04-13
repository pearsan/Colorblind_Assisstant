package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.graphics.ColorUtils;
import android.support.p000v4.graphics.drawable.DrawableCompat;
import android.support.p000v4.util.ArrayMap;
import android.support.p000v4.util.LongSparseArray;
import android.support.p000v4.util.LruCache;
import android.support.p003v7.appcompat.C0191R;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v7.widget.AppCompatDrawableManager */
public final class AppCompatDrawableManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {C0191R.C0192drawable.abc_popup_background_mtrl_mult, C0191R.C0192drawable.abc_cab_background_internal_bg, C0191R.C0192drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {C0191R.C0192drawable.abc_textfield_activated_mtrl_alpha, C0191R.C0192drawable.abc_textfield_search_activated_mtrl_alpha, C0191R.C0192drawable.abc_cab_background_top_mtrl_alpha, C0191R.C0192drawable.abc_text_cursor_material};
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {C0191R.C0192drawable.abc_textfield_search_default_mtrl_alpha, C0191R.C0192drawable.abc_textfield_default_mtrl_alpha, C0191R.C0192drawable.abc_ab_share_pack_mtrl_alpha};
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static AppCompatDrawableManager INSTANCE = null;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = {C0191R.C0192drawable.abc_btn_check_material, C0191R.C0192drawable.abc_btn_radio_material};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = {C0191R.C0192drawable.abc_ic_ab_back_mtrl_am_alpha, C0191R.C0192drawable.abc_ic_go_search_api_mtrl_alpha, C0191R.C0192drawable.abc_ic_search_api_mtrl_alpha, C0191R.C0192drawable.abc_ic_commit_search_api_mtrl_alpha, C0191R.C0192drawable.abc_ic_clear_mtrl_alpha, C0191R.C0192drawable.abc_ic_menu_share_mtrl_alpha, C0191R.C0192drawable.abc_ic_menu_copy_mtrl_am_alpha, C0191R.C0192drawable.abc_ic_menu_cut_mtrl_alpha, C0191R.C0192drawable.abc_ic_menu_selectall_mtrl_alpha, C0191R.C0192drawable.abc_ic_menu_paste_mtrl_am_alpha, C0191R.C0192drawable.abc_ic_menu_moreoverflow_mtrl_alpha, C0191R.C0192drawable.abc_ic_voice_search_api_mtrl_alpha};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = {C0191R.C0192drawable.abc_edit_text_material, C0191R.C0192drawable.abc_tab_indicator_material, C0191R.C0192drawable.abc_textfield_search_material, C0191R.C0192drawable.abc_spinner_mtrl_am_alpha, C0191R.C0192drawable.abc_spinner_textfield_background_material, C0191R.C0192drawable.abc_ratingbar_full_material, C0191R.C0192drawable.abc_switch_track_mtrl_alpha, C0191R.C0192drawable.abc_switch_thumb_material, C0191R.C0192drawable.abc_btn_default_mtrl_shape, C0191R.C0192drawable.abc_btn_borderless_material};
    private final Object mDelegateDrawableCacheLock = new Object();
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDelegateDrawableCaches = new WeakHashMap<>(0);
    private ArrayMap<String, InflateDelegate> mDelegates;
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArray<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArray<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$InflateDelegate */
    private interface InflateDelegate {
        Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme);
    }

    public static AppCompatDrawableManager get() {
        if (INSTANCE == null) {
            INSTANCE = new AppCompatDrawableManager();
            installDefaultInflateDelegates(INSTANCE);
        }
        return INSTANCE;
    }

    private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager manager) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < 21) {
            manager.addDelegate("vector", new VdcInflateDelegate());
            if (sdk >= 11) {
                manager.addDelegate("animated-vector", new AvdcInflateDelegate());
            }
        }
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return getDrawable(context, resId, false);
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown) {
        Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, resId);
        }
        if (drawable != null) {
            drawable = tintDrawable(context, resId, failIfNotKnown, drawable);
        }
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        return drawable;
    }

    private Drawable tintDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown, @NonNull Drawable drawable) {
        ColorStateList tintList = getTintList(context, resId);
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable, tintList);
            PorterDuff.Mode tintMode = getTintMode(resId);
            if (tintMode != null) {
                DrawableCompat.setTintMode(drawable, tintMode);
            }
        } else if (resId == C0191R.C0192drawable.abc_cab_background_top_material) {
            return new LayerDrawable(new Drawable[]{getDrawable(context, C0191R.C0192drawable.abc_cab_background_internal_bg), getDrawable(context, C0191R.C0192drawable.abc_cab_background_top_mtrl_alpha)});
        } else if (resId == C0191R.C0192drawable.abc_seekbar_track_material) {
            LayerDrawable ld = (LayerDrawable) drawable;
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated), DEFAULT_MODE);
        } else if (resId == C0191R.C0192drawable.abc_ratingbar_indicator_material || resId == C0191R.C0192drawable.abc_ratingbar_small_material) {
            LayerDrawable ld2 = (LayerDrawable) drawable;
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated), DEFAULT_MODE);
        } else if (!tintDrawableUsingColorFilter(context, resId, drawable) && failIfNotKnown) {
            drawable = null;
        }
        return drawable;
    }

    private Drawable loadDrawableFromDelegates(@NonNull Context context, @DrawableRes int resId) {
        int type;
        if (this.mDelegates == null || this.mDelegates.isEmpty()) {
            return null;
        }
        if (this.mKnownDrawableIdTags != null) {
            String cachedTagName = this.mKnownDrawableIdTags.get(resId);
            if (SKIP_DRAWABLE_TAG.equals(cachedTagName) || (cachedTagName != null && this.mDelegates.get(cachedTagName) == null)) {
                return null;
            }
        } else {
            this.mKnownDrawableIdTags = new SparseArray<>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue tv = this.mTypedValue;
        Resources res = context.getResources();
        res.getValue(resId, tv, true);
        long key = (((long) tv.assetCookie) << 32) | ((long) tv.data);
        Drawable dr = getCachedDelegateDrawable(context, key);
        if (dr != null) {
            return dr;
        }
        if (tv.string != null && tv.string.toString().endsWith(".xml")) {
            try {
                XmlPullParser parser = res.getXml(resId);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                do {
                    type = parser.next();
                    if (type == 2 || type == 1) {
                    }
                    type = parser.next();
                    break;
                } while (type == 1);
                if (type != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String tagName = parser.getName();
                this.mKnownDrawableIdTags.append(resId, tagName);
                InflateDelegate delegate = this.mDelegates.get(tagName);
                if (delegate != null) {
                    dr = delegate.createFromXmlInner(context, parser, attrs, context.getTheme());
                }
                if (dr != null) {
                    dr.setChangingConfigurations(tv.changingConfigurations);
                    if (addCachedDelegateDrawable(context, key, dr)) {
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while inflating drawable", e);
            }
        }
        if (dr != null) {
            return dr;
        }
        this.mKnownDrawableIdTags.append(resId, SKIP_DRAWABLE_TAG);
        return dr;
    }

    private Drawable getCachedDelegateDrawable(@NonNull Context context, long key) {
        Drawable drawable = null;
        synchronized (this.mDelegateDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDelegateDrawableCaches.get(context);
            if (cache != null) {
                WeakReference<Drawable.ConstantState> wr = cache.get(key);
                if (wr != null) {
                    Drawable.ConstantState entry = (Drawable.ConstantState) wr.get();
                    if (entry != null) {
                        drawable = entry.newDrawable(context.getResources());
                    } else {
                        cache.delete(key);
                    }
                }
            }
        }
        return drawable;
    }

    private boolean addCachedDelegateDrawable(@NonNull Context context, long key, @NonNull Drawable drawable) {
        Drawable.ConstantState cs = drawable.getConstantState();
        if (cs == null) {
            return false;
        }
        synchronized (this.mDelegateDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDelegateDrawableCaches.get(context);
            if (cache == null) {
                cache = new LongSparseArray<>();
                this.mDelegateDrawableCaches.put(context, cache);
            }
            cache.put(key, new WeakReference(cs));
        }
        return true;
    }

    public final Drawable onDrawableLoadedFromResources(@NonNull Context context, @NonNull TintResources resources, @DrawableRes int resId) {
        Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = resources.superGetDrawable(resId);
        }
        if (drawable != null) {
            return tintDrawable(context, resId, false, drawable);
        }
        return null;
    }

    private static boolean tintDrawableUsingColorFilter(@NonNull Context context, @DrawableRes int resId, @NonNull Drawable drawable) {
        PorterDuff.Mode tintMode = DEFAULT_MODE;
        boolean colorAttrSet = false;
        int colorAttr = 0;
        int alpha = -1;
        if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, resId)) {
            colorAttr = C0191R.attr.colorControlNormal;
            colorAttrSet = true;
        } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, resId)) {
            colorAttr = C0191R.attr.colorControlActivated;
            colorAttrSet = true;
        } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, resId)) {
            colorAttr = 16842801;
            colorAttrSet = true;
            tintMode = PorterDuff.Mode.MULTIPLY;
        } else if (resId == C0191R.C0192drawable.abc_list_divider_mtrl_alpha) {
            colorAttr = 16842800;
            colorAttrSet = true;
            alpha = Math.round(40.8f);
        }
        if (!colorAttrSet) {
            return false;
        }
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawable = drawable.mutate();
        }
        drawable.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, colorAttr), tintMode));
        if (alpha != -1) {
            drawable.setAlpha(alpha);
        }
        return true;
    }

    private void addDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<>();
        }
        this.mDelegates.put(tagName, delegate);
    }

    private void removeDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        if (this.mDelegates != null && this.mDelegates.get(tagName) == delegate) {
            this.mDelegates.remove(tagName);
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int id : array) {
            if (id == value) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public final PorterDuff.Mode getTintMode(int resId) {
        if (resId == C0191R.C0192drawable.abc_switch_thumb_material) {
            return PorterDuff.Mode.MULTIPLY;
        }
        return null;
    }

    public final ColorStateList getTintList(@NonNull Context context, @DrawableRes int resId) {
        ColorStateList tint = getTintListFromCache(context, resId);
        if (tint == null) {
            if (resId == C0191R.C0192drawable.abc_edit_text_material) {
                tint = createEditTextColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_switch_track_mtrl_alpha) {
                tint = createSwitchTrackColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_switch_thumb_material) {
                tint = createSwitchThumbColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_btn_default_mtrl_shape || resId == C0191R.C0192drawable.abc_btn_borderless_material) {
                tint = createDefaultButtonColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_btn_colored_material) {
                tint = createColoredButtonColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_spinner_mtrl_am_alpha || resId == C0191R.C0192drawable.abc_spinner_textfield_background_material) {
                tint = createSpinnerColorStateList(context);
            } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, resId)) {
                tint = ThemeUtils.getThemeAttrColorStateList(context, C0191R.attr.colorControlNormal);
            } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, resId)) {
                tint = createDefaultColorStateList(context);
            } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, resId)) {
                tint = createCheckableButtonColorStateList(context);
            } else if (resId == C0191R.C0192drawable.abc_seekbar_thumb_material) {
                tint = createSeekbarThumbColorStateList(context);
            }
            if (tint != null) {
                addTintListToCache(context, resId, tint);
            }
        }
        return tint;
    }

    private ColorStateList getTintListFromCache(@NonNull Context context, @DrawableRes int resId) {
        SparseArray<ColorStateList> tints;
        if (this.mTintLists == null || (tints = this.mTintLists.get(context)) == null) {
            return null;
        }
        return tints.get(resId);
    }

    private void addTintListToCache(@NonNull Context context, @DrawableRes int resId, @NonNull ColorStateList tintList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<>();
        }
        SparseArray<ColorStateList> themeTints = this.mTintLists.get(context);
        if (themeTints == null) {
            themeTints = new SparseArray<>();
            this.mTintLists.put(context, themeTints);
        }
        themeTints.append(resId, tintList);
    }

    private ColorStateList createDefaultColorStateList(Context context) {
        int colorControlNormal = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int colorControlActivated = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int[][] states = new int[7][];
        int[] colors = new int[7];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i = 0 + 1;
        states[i] = ThemeUtils.FOCUSED_STATE_SET;
        colors[i] = colorControlActivated;
        int i2 = i + 1;
        states[i2] = ThemeUtils.ACTIVATED_STATE_SET;
        colors[i2] = colorControlActivated;
        int i3 = i2 + 1;
        states[i3] = ThemeUtils.PRESSED_STATE_SET;
        colors[i3] = colorControlActivated;
        int i4 = i3 + 1;
        states[i4] = ThemeUtils.CHECKED_STATE_SET;
        colors[i4] = colorControlActivated;
        int i5 = i4 + 1;
        states[i5] = ThemeUtils.SELECTED_STATE_SET;
        colors[i5] = colorControlActivated;
        int i6 = i5 + 1;
        states[i6] = ThemeUtils.EMPTY_STATE_SET;
        colors[i6] = colorControlNormal;
        int i7 = i6 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createCheckableButtonColorStateList(Context context) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i = 0 + 1;
        states[i] = ThemeUtils.CHECKED_STATE_SET;
        colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int i2 = i + 1;
        states[i2] = ThemeUtils.EMPTY_STATE_SET;
        colors[i2] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createSwitchTrackColorStateList(Context context) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.1f);
        int i = 0 + 1;
        states[i] = ThemeUtils.CHECKED_STATE_SET;
        colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated, 0.3f);
        int i2 = i + 1;
        states[i2] = ThemeUtils.EMPTY_STATE_SET;
        colors[i2] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.3f);
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createSwitchThumbColorStateList(Context context) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        ColorStateList thumbColor = ThemeUtils.getThemeAttrColorStateList(context, C0191R.attr.colorSwitchThumbNormal);
        if (thumbColor == null || !thumbColor.isStateful()) {
            states[0] = ThemeUtils.DISABLED_STATE_SET;
            colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorSwitchThumbNormal);
            int i = 0 + 1;
            states[i] = ThemeUtils.CHECKED_STATE_SET;
            colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
            int i2 = i + 1;
            states[i2] = ThemeUtils.EMPTY_STATE_SET;
            colors[i2] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorSwitchThumbNormal);
            int i3 = i2 + 1;
        } else {
            states[0] = ThemeUtils.DISABLED_STATE_SET;
            colors[0] = thumbColor.getColorForState(states[0], 0);
            int i4 = 0 + 1;
            states[i4] = ThemeUtils.CHECKED_STATE_SET;
            colors[i4] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
            int i5 = i4 + 1;
            states[i5] = ThemeUtils.EMPTY_STATE_SET;
            colors[i5] = thumbColor.getDefaultColor();
            int i6 = i5 + 1;
        }
        return new ColorStateList(states, colors);
    }

    private ColorStateList createEditTextColorStateList(Context context) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i = 0 + 1;
        states[i] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i2 = i + 1;
        states[i2] = ThemeUtils.EMPTY_STATE_SET;
        colors[i2] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return createButtonColorStateList(context, C0191R.attr.colorButtonNormal);
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return createButtonColorStateList(context, C0191R.attr.colorAccent);
    }

    private ColorStateList createButtonColorStateList(Context context, int baseColorAttr) {
        int[][] states = new int[4][];
        int[] colors = new int[4];
        int baseColor = ThemeUtils.getThemeAttrColor(context, baseColorAttr);
        int colorControlHighlight = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlHighlight);
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorButtonNormal);
        int i = 0 + 1;
        states[i] = ThemeUtils.PRESSED_STATE_SET;
        colors[i] = ColorUtils.compositeColors(colorControlHighlight, baseColor);
        int i2 = i + 1;
        states[i2] = ThemeUtils.FOCUSED_STATE_SET;
        colors[i2] = ColorUtils.compositeColors(colorControlHighlight, baseColor);
        int i3 = i2 + 1;
        states[i3] = ThemeUtils.EMPTY_STATE_SET;
        colors[i3] = baseColor;
        int i4 = i3 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createSpinnerColorStateList(Context context) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i = 0 + 1;
        states[i] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlNormal);
        int i2 = i + 1;
        states[i2] = ThemeUtils.EMPTY_STATE_SET;
        colors[i2] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }

    private ColorStateList createSeekbarThumbColorStateList(Context context) {
        int[][] states = new int[2][];
        int[] colors = new int[2];
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int i = 0 + 1;
        states[i] = ThemeUtils.EMPTY_STATE_SET;
        colors[i] = ThemeUtils.getThemeAttrColor(context, C0191R.attr.colorControlActivated);
        int i2 = i + 1;
        return new ColorStateList(states, colors);
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$ColorFilterLruCache */
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }

        /* access modifiers changed from: package-private */
        public PorterDuffColorFilter get(int color, PorterDuff.Mode mode) {
            return (PorterDuffColorFilter) get(Integer.valueOf(generateCacheKey(color, mode)));
        }

        /* access modifiers changed from: package-private */
        public PorterDuffColorFilter put(int color, PorterDuff.Mode mode, PorterDuffColorFilter filter) {
            return (PorterDuffColorFilter) put(Integer.valueOf(generateCacheKey(color, mode)), filter);
        }

        private static int generateCacheKey(int color, PorterDuff.Mode mode) {
            return ((color + 31) * 31) + mode.hashCode();
        }
    }

    public static void tintDrawable(Drawable drawable, TintInfo tint, int[] state) {
        if (!DrawableUtils.canSafelyMutateDrawable(drawable) || drawable.mutate() == drawable) {
            if (tint.mHasTintList || tint.mHasTintMode) {
                drawable.setColorFilter(createTintFilter(tint.mHasTintList ? tint.mTintList : null, tint.mHasTintMode ? tint.mTintMode : DEFAULT_MODE, state));
            } else {
                drawable.clearColorFilter();
            }
            if (Build.VERSION.SDK_INT <= 23) {
                drawable.invalidateSelf();
                return;
            }
            return;
        }
        Log.d(TAG, "Mutated drawable is not the same instance as the input.");
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode, int[] state) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return getPorterDuffColorFilter(tint.getColorForState(state, 0), tintMode);
    }

    public static PorterDuffColorFilter getPorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        PorterDuffColorFilter filter = COLOR_FILTER_CACHE.get(color, mode);
        if (filter != null) {
            return filter;
        }
        PorterDuffColorFilter filter2 = new PorterDuffColorFilter(color, mode);
        COLOR_FILTER_CACHE.put(color, mode, filter2);
        return filter2;
    }

    private static void setPorterDuffColorFilter(Drawable d, int color, PorterDuff.Mode mode) {
        if (DrawableUtils.canSafelyMutateDrawable(d)) {
            d = d.mutate();
        }
        if (mode == null) {
            mode = DEFAULT_MODE;
        }
        d.setColorFilter(getPorterDuffColorFilter(color, mode));
    }

    private static boolean isVectorDrawable(@NonNull Drawable d) {
        return (d instanceof VectorDrawableCompat) || PLATFORM_VD_CLAZZ.equals(d.getClass().getName());
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$VdcInflateDelegate */
    private static class VdcInflateDelegate implements InflateDelegate {
        private VdcInflateDelegate() {
        }

        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    /* renamed from: android.support.v7.widget.AppCompatDrawableManager$AvdcInflateDelegate */
    private static class AvdcInflateDelegate implements InflateDelegate {
        private AvdcInflateDelegate() {
        }

        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }
}
