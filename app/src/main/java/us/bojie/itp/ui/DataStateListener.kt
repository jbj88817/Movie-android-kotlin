package us.bojie.itp.ui

import us.bojie.itp.util.DataState


interface DataStateListener {

    fun onDataStateChange(dataState: DataState<*>?)
}