package com.quantum.quran.theme

import android.app.Activity
import com.quantum.quran.R
import com.quantum.quran.database.Application_D


class ApplicationTheme(context: Activity) {

    init {
        if (Application_D(context).darkTheme) {
            when(Application_D(context).primaryColor) {
                Application_D.PURPLE -> context.setTheme(R.style.ThemeDarkPurple)
                Application_D.BLUE -> context.setTheme(R.style.ThemeDarkBlue)
                Application_D.ORANGE -> context.setTheme(R.style.ThemeDarkOrange)
            }
        } else {
            when(Application_D(context).primaryColor) {
                Application_D.PURPLE -> context.setTheme(R.style.ThemeLightPurple)
                Application_D.BLUE -> context.setTheme(R.style.ThemeLightBlue)
                Application_D.ORANGE -> context.setTheme(R.style.ThemeLightOrange)
            }
        }
    }
}