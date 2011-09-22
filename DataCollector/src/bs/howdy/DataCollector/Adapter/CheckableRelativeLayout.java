package bs.howdy.DataCollector.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	private boolean isChecked;
	private List<Checkable> checkables;
	
	public CheckableRelativeLayout(Context context) {
		super(context);
		initialize();
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	
	private void initialize() {
		isChecked = false;
		checkables = new ArrayList<Checkable>();
	}
	
	@Override
	public boolean isChecked() {
		return isChecked;
	}
	@Override
	public void setChecked(boolean checked) {
		isChecked = checked;
		alertCheckables(isChecked);
	}
	@Override
	public void toggle() {
		isChecked = !isChecked;
		alertCheckables(isChecked);
	}
	
	private void alertCheckables(boolean value) {
		for(Checkable c : checkables) {
			c.setChecked(value);
		}
	}
	
	@Override
	protected void onFinishInflate() {
		checkables.clear();
		for(int i = 0; i < getChildCount(); i++) {
			findCheckableChildren(getChildAt(i));
		}
	}
	
	private void findCheckableChildren(View v) {
		if(v instanceof Checkable) {
			checkables.add((Checkable)v);
		}
		if(v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)v;
			for(int i = 0; i < vg.getChildCount(); i++) {
				findCheckableChildren(vg.getChildAt(i));
			}
		}
	}
}