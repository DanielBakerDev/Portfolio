package com.example.cpawellnessapp.Diary

import org.jetbrains.annotations.NotNull
import java.util.*
import kotlin.properties.Delegates

data class Diary(var Id: Int = -1) {

    lateinit var dateRecorded: String;
    lateinit var fileURI: String;
    var smileScore by Delegates.notNull<Float>();
    var entryNumber: Int = 0;
}