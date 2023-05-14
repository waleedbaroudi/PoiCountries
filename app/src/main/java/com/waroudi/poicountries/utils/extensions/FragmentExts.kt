package com.waroudi.poicountries.utils.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Navigates between destinations
 * @param actionId the ID of the navigation actions
 * @param args optional arguments bundle
 */
fun Fragment.navigate(@IdRes actionId:  Int, args: Bundle? = null) {
    findNavController().navigate(actionId, args)
}

/**
 * Navigates up in the navigation stack
 */
fun Fragment.navigateBack() {
    findNavController().navigateUp()
}

/**
 * Generically creates a [ViewBinding] instance of type [VB]
 * @param inflater the inflater to inflate the ViewBinding
 * @param container a [ViewGroup] for layout inflation of the view binding
 * @return a [ViewBinding] object of type [VB]
 */
fun <VB : ViewBinding> Fragment.createBindingInstance(
    inflater: LayoutInflater,
    container: ViewGroup? = null
): VB {
    val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    val vbClass = vbType as Class<VB>
    val method = vbClass.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )

    // Call VB.inflate(inflater, container, false) Java static method
    return method.invoke(null, inflater, container, false) as VB
}