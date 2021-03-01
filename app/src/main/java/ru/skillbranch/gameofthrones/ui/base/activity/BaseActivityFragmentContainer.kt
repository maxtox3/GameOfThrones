package ru.skillbranch.gameofthrones.ui.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.skillbranch.gameofthrones.debugLog

abstract class BaseActivityFragmentContainer : AppCompatActivity(),
  FragmentContainer {

  private var currentTag: String? = null
  private var containerId: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    debugLog("ON CREATE")
    super.onCreate(savedInstanceState)
    setContentView(getContentViewResourceId())
    setContainerId()
    if (savedInstanceState != null) {
      currentTag = savedInstanceState.getString(SAVED_FRAGMENT_TAG)
      if (currentTag != null) {
        restoreFragment(currentTag!!)
      }
    }
    setupWidgets()
  }

  abstract fun setupWidgets()

  abstract fun getContentViewResourceId(): Int

  override fun onDestroy() {
    debugLog("ON DESTROY")
    super.onDestroy()
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStackImmediate()
    } else {
      super.onBackPressed()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    if (currentTag != null) {
      outState.putString(SAVED_FRAGMENT_TAG, currentTag)
    }
    super.onSaveInstanceState(outState)
  }

//    protected fun navigateToFragmentWithRemoveExisting(
//        tag: String,
//        args: Bundle,
//        addToBackStack: Boolean
//    ) {
//        val transaction = supportFragmentManager.beginTransaction()
//
//        if (addToBackStack) {
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            transaction.addToBackStack(null)
//        }
//
//        if (supportFragmentManager.findFragmentByTag(tag) != null) {
//            remove(transaction, supportFragmentManager.findFragmentByTag(tag))
//        } else {
//            val fragment = createFragment(tag, args)
//            add(transaction, fragment, tag)
//        }
//    }
//
//    protected fun removeFragment(tag: String) {
//        val transaction = supportFragmentManager.beginTransaction()
//
//        if (supportFragmentManager.findFragmentByTag(tag) != null) {
//            remove(transaction, supportFragmentManager.findFragmentByTag(tag))
//        }
//    }

  protected fun navigateToFragment(tag: String, args: Bundle?, addToBackStack: Boolean) {

    val transaction = supportFragmentManager.beginTransaction()

    if (addToBackStack) {
      transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      transaction.addToBackStack(null)
    }

    when (val fragmentByTag = supportFragmentManager.findFragmentByTag(tag)) {
      null -> {
        val fragment = createFragment(tag, args)
        add(transaction, fragment, tag)
      }
      else -> replace(transaction, fragmentByTag, tag)
    }
  }

  protected fun setContainerId(containerId: Int) {
    this.containerId = containerId
  }

  protected abstract fun createFragment(tag: String, args: Bundle?): Fragment

  private fun restoreFragment(tag: String) {
    val transaction = supportFragmentManager.beginTransaction()
    supportFragmentManager.findFragmentByTag(tag)?.let {
      replace(transaction, it, tag)
    }
  }

  private fun replace(transaction: FragmentTransaction, fragment: Fragment, tag: String) {
    transaction.replace(containerId, fragment, tag).commit()
    setCurrentTag(tag)
  }

  private fun remove(transaction: FragmentTransaction, fragment: Fragment) {
    transaction.remove(fragment).commit()
  }

  protected fun add(transaction: FragmentTransaction, fragment: Fragment, tag: String) {
    transaction.add(containerId, fragment, tag).commit()
  }

  protected fun setCurrentTag(tag: String) {
    currentTag = tag
  }

  companion object {
    const val SAVED_FRAGMENT_TAG = "SAVED_FRAGMENT_TAG"
  }
}