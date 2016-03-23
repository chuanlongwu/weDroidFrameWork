package com.wedroid.framework.v2.activity;

public interface IActivityLifeCycle {
	void afterOnStart();
	void afterOnResume();
	void afterOnPause();
	void afterOnStop();
	void afterOnReStart();
	void afterOnDestory();
}
