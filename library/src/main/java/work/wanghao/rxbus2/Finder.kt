@file:JvmName("Finder")

package work.wanghao.rxbus2

import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.Modifier
import java.util.*

/**
 * @author doublemine
 *         Created  on 2017/02/07 23:33.
 *         Summary:
 */

fun findAnnotatedMethods(target: Any,
    compositeDisposable: CompositeDisposable): SubscribeContainer {
  val methodsContainer = HashSet<HandleEvent>()
  return findAnnotatedSubscribeFunctions(target = target, compositeDisposable = compositeDisposable,
      methodContainer = methodsContainer)
}

private fun findAnnotatedSubscribeFunctions(target: Any,
    compositeDisposable: CompositeDisposable,
    methodContainer: HashSet<HandleEvent>): SubscribeContainer {
  for (method in target.javaClass.declaredMethods) {
    if (method.isBridge) continue
    if (!method.isAnnotationPresent(Subscribe::class.java)) continue
    val subscribeMethod = method
    val parametersTypes = subscribeMethod.parameterTypes
    if (parametersTypes.size != 1) {
      throw RxBusException(
          "Method ${subscribeMethod.declaringClass.name}.${subscribeMethod.name} must have exactly 1 parameter but has ${parametersTypes.size}")
    }
    if ((method.modifiers.and(
        Modifier.PUBLIC)) == 0 || method.modifiers and (Modifier.ABSTRACT or Modifier.STATIC or 0x40 or 0x1000) != 0) {
      throw RxBusException(
          "Method $subscribeMethod is a illegal @Subscribe method: must be public, non-static, and non-abstract")
    }


    val subscribeAnnotation = method.getAnnotation(Subscribe::class.java)
    val threadMode = subscribeAnnotation.threadMode
    val handleEvent = HandleEvent(target, method, threadMode)

    if (!methodContainer.contains(handleEvent)) {
      methodContainer.add(handleEvent)
      compositeDisposable.add(handleEvent.mDisposable)
    }

  }

  if (methodContainer.isEmpty()) {
    throw RxBusException(
        "Subscriber ${target.javaClass.canonicalName} and its super classes have no public methods with the @Subscribe annotation")
  }

  return SubscribeContainer(target, compositeDisposable, methodContainer)

}
