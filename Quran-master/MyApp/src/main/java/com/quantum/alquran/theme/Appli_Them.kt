package com.quantum.alquran.theme

import android.app.Activity
import com.quantum.alquran.R
import com.quantum.alquran.database.Application_D


class Appli_Them(context: Activity) {

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