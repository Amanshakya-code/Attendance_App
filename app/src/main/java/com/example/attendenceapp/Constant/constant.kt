package com.example.attendenceapp.Constant

import android.icu.lang.UCharacter.GraphemeClusterBreak.T

class constant {
    companion object{
        const val SUBJECT_NAME = "subject"
        const val CLASS_NAME = "class"
        const val POSITION = "position"
        const val CID = "classId"
        const val SIDARRAY = "idarray"
        const val NAMEARRAY = "namearray"
        const val ROLLARRAY = "rollarray"
        const val MONTH = "month"
        const val PREFKEY = "onboarding"
        var sidArray = arrayOf(T)
        var nameArray = arrayOf<String>()
        var rollnumArray = arrayOf<String>()
    }
}