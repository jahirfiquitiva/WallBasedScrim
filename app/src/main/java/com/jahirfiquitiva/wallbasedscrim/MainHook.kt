package com.jahirfiquitiva.wallbasedscrim

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MainHook : IXposedHookZygoteInit, IXposedHookLoadPackage {
    private var MODULE_PATH = ""
    
    private val hooks = ArrayList<Any>().apply {
        add(ScrimTintHook)
    }
    
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        MODULE_PATH = startupParam.modulePath
    }
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hooks.forEach { (it as? IXposedHookLoadPackage)?.handleLoadPackage(lpparam) }
    }
}