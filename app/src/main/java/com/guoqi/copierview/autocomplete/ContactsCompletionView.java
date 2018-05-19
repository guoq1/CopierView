package com.guoqi.copierview.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.guoqi.copierview.R;
import com.guoqi.copierview.UserBean;

/**
 * Sample token completion view for basic contact info
 * <p>
 * Created on 9/12/13.
 *
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView<UserBean> {

    InputConnection testAccessibleInputConnection;

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(UserBean bean) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.view_contact_token, (ViewGroup) getParent(), false);
        token.setText(bean.name + ";");
        return token;
    }

    @Override
    protected UserBean defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
//            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
            UserBean userBean = new UserBean();
            userBean.name = completionText;
            return userBean;
        } else {
//            return new Person(completionText.substring(0, index), completionText);
            UserBean userBean = new UserBean();
            userBean.name = completionText;
            return userBean;
        }
    }

    @Override
    public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
        testAccessibleInputConnection = super.onCreateInputConnection(outAttrs);
        return testAccessibleInputConnection;
    }
}
