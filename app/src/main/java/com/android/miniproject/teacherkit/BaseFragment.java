package com.android.miniproject.teacherkit;

import com.wangjie.androidinject.annotation.present.AISupportFragment;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

/**
 * Created by user on 12/9/2016.
 */

public abstract class BaseFragment extends AISupportFragment {

    public abstract String getRfabIdentificationCode();

    public abstract String getTitle();

    public void onInitialRFAB(RapidFloatingActionButton rfab) {

    }
}
