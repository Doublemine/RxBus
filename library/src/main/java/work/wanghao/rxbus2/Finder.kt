@file:JvmName("Finder")

package work.wanghao.rxbus2

import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.Modifier

/**
 * @author doublemine
 *         Created  on 2017/02/07 23:33.
 *         Summary:
 */

fun findAnnotatedSubscribeFunctions(any: Any, compositeDisposable: CompositeDisposable) {
  val methods = any.javaClass.declaredMethods
  for (method in methods) {
    if (method.isBridge) continue
    if (!method.isAnnotationPresent(Subscribe::class.java)) continue
    val subscribeMethod = method
    val parametersTypes = subscribeMethod.parameterTypes
    if (parametersTypes.size != 1) {
      throw IllegalArgumentException(
          "Method $subscribeMethod  has @Subscribe annotation but requires ${parametersTypes.size}  arguments.  Methods must require a single argument.")
    }
    val parametersClazz = parametersTypes[0]
    if ((parametersClazz.modifiers.and(Modifier.PUBLIC)) == 0) {
      throw IllegalArgumentException(
          "Method $subscribeMethod  has @Subscribe on $parametersClazz but  is NOT 'public'. ")
    }


  }


}
