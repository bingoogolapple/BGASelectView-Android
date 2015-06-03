package cn.bingoogolapple.selectview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/17 10:39
 * 描述:
 */
public class BGASelectView extends TextView implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = BGASelectView.class.getSimpleName();
    private SelectViewDelegate mDelegate;
    private Activity mActivity;
    private Dialog mDialog;

    private PopupWindow mValuePw;
    private ListView mListView;

    public BGASelectView(Context context) {
        super(context);
    }

    public BGASelectView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textStyle);
    }

    public BGASelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);
        setOnClickListener(this);

        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGASelectView);
        mListView = (ListView) View.inflate(getContext(), typedArray.getResourceId(R.styleable.BGASelectView_sv_listViewLayoutId, R.layout.selectview_popwindow) , null);
        mListView.setOnItemClickListener(this);
        typedArray.recycle();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    public void reset() {
        setText("");
    }

    private void closeValuePw() {
        if (mValuePw != null) {
            mValuePw.dismiss();
        }
    }

    public ListView getListView() {
        return mListView;
    }

    public void setAdapter(BaseAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setDelegate(SelectViewDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        closeValuePw();
        if (mDelegate != null) {
            mDelegate.onSelectViewValueChanged(this, position);
        }
    }

    @Override
    public void onClick(View v) {
        BGASelectView.closeKeyboard(mActivity);
        BGASelectView.closeKeyboard(mDialog);
        postDelayed(mShowPwTask, 200);
    }

    private Runnable mShowPwTask = new Runnable() {
        @Override
        public void run() {
            if (mValuePw == null) {
                mValuePw = new PopupWindow(mListView, getWidth(), getHeight() * 4);
                mValuePw.setFocusable(true);
                mValuePw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            if (mListView.getAdapter().getCount() > 0) {
                mValuePw.showAsDropDown(BGASelectView.this, 0, 0);
            }
        }
    };

    public interface SelectViewDelegate {
        void onSelectViewValueChanged(BGASelectView selectView, int position);
    }

    /**
     * 关闭activity中打开的键盘
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 关闭dialog中打开的键盘
     * @param dialog
     */
    public static void closeKeyboard(Dialog dialog) {
        if (dialog ==  null) {
            return;
        }
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}