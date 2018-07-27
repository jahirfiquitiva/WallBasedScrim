/*
 * Copyright (c) 2018. Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jahirfiquitiva.wallbasedscrim

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ScrimTintHook : IXposedHookLoadPackage {
    
    private const val METHOD_NAME = "getDarkGradientColor"
    private const val GRADIENT_COLORS_CLASS =
        "com.android.internal.colorextraction.ColorExtractor\$GradientColors"
    
    private val classesToModify: Array<String> = arrayOf(
        "com.android.systemui.globalactions.GlobalActionsDialog\$ActionsDialog",
        "com.android.systemui.statusbar.phone.ScrimController")
    
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
        lpparam?.let { param ->
            if (param.packageName != "com.android.systemui")
                return
            
            classesToModify.forEach {
                try {
                    XposedHelpers.findAndHookMethod(
                        it, param.classLoader, METHOD_NAME, GRADIENT_COLORS_CLASS, replacement)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}