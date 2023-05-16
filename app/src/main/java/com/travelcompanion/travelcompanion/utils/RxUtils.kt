package com.travelcompanion.travelcompanion.utils

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

fun subscribeOnBackground(function: () -> Unit) {
    Single.fromCallable {
        function()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe()
}