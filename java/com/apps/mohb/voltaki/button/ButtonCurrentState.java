/*
 *  Copyright (c) 2016 mohb apps - All Rights Reserved
 *
 *  Project       : Voltaki
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : ButtonCurrentState.java
 *  Last modified : 7/17/16 12:19 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.voltaki.button;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Button;

import com.apps.mohb.voltaki.Constants;
import com.apps.mohb.voltaki.R;


// This class manages the button current states

public class ButtonCurrentState {

    private static Button button;
    private static ButtonStatus buttonStatus;


    public static void setButton(Button b) {
        button = b;
    }

    public static Button getButton() {
        return button;
    }

    public static ButtonStatus getButtonStatus() {
        return buttonStatus;
    }

    public static void setButtonStatus(ButtonStatus status) {
        buttonStatus = status;
    }

    @TargetApi(23)
    public static void setButtonProperties(Context context, int color, int textColor, int text, float textSize, boolean enabled) {
        if (button != null) {
            // check sdk version to apply correct methods
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundColor(context.getResources().getColor(color));
                button.setTextColor(context.getResources().getColor(textColor));
            }
            else {
                button.setBackgroundColor(context.getResources().getColor(color, context.getTheme()));
                button.setTextColor(context.getResources().getColor(textColor, context.getTheme()));
            }
            button.setTextSize(textSize);
            button.setText(text);
            button.setEnabled(enabled);
        }
    }

    public static void setButtonOffline(Context context) {
        setButtonProperties(context, R.color.colorOfflineButton,
                R.color.colorWhiteTextButton, R.string.button_offline,
                Constants.TEXT_LARGE, false);
    }

    public static void setButtonGetLocation(Context context) {
        setButtonProperties(context, R.color.colorGetLocationButton,
                R.color.colorBlackTextButton, R.string.button_get_location, Constants.TEXT_LARGE, false);
    }

    public static void setButtonComeBack(Context context) {
        setButtonProperties(context, R.color.colorComeBackHereButton,
                R.color.colorBlackTextButton, R.string.button_come_back_here, Constants.TEXT_LARGE, true);
    }

    public static void setButtonGoBack(Context context) {
        setButtonProperties(context, R.color.colorGoBackButton,
                R.color.colorBlackTextButton, R.string.button_go_back, Constants.TEXT_LARGE, true);
    }

    public static void setButtonGoBackClicked(Context context) {
        setButtonProperties(context, R.color.colorGoBackButton,
                R.color.colorYellowTextButton, R.string.button_go_back, Constants.TEXT_LARGE, true);
    }

}
