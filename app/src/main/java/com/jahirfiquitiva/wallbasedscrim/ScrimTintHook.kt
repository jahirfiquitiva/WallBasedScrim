package com.jahirfiquitiva.wallbasedscrim

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ScrimTintHook : IXposedHookLoadPackage {
    
    private const val METHOD_NAME = "getDarkGradientColor"
    private const val GRADIENT_COLORS_CLASS =
        "com.android.internal.colorextraction.ColorExtractor\$GradientColors"
    
    private val replacement: XC_MethodReplacement by lazy {
        object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                return try {
                    param?.let { it.args?.first() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        lpparam?.let {
            if (it.packageName != "com.android.systemui")
                return
            
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.systemui.globalactions.GlobalActionsDialog\$ActionsDialog",
                    it.classLoader, METHOD_NAME, GRADIENT_COLORS_CLASS, replacement)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            try {
                XposedHelpers.findAndHookMethod(
                    "com.android.systemui.statusbar.phone.ScrimController", it.classLoader,
                    METHOD_NAME, GRADIENT_COLORS_CLASS, replacement)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}