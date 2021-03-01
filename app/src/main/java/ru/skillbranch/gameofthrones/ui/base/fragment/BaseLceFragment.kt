package ru.skillbranch.gameofthrones.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.presentation.BaseIntent
import ru.skillbranch.gameofthrones.presentation.BaseView
import ru.skillbranch.gameofthrones.presentation.BaseViewModel
import ru.skillbranch.gameofthrones.presentation.BaseViewState
import ru.skillbranch.gameofthrones.ui.base.activity.BaseActivityCallback
import javax.inject.Inject

abstract class BaseLceFragment<
  INTENT : BaseIntent,
  VIEW_STATE : BaseViewState,
  VIEW_MODEL : BaseViewModel<INTENT, VIEW_STATE>,
  ACTIVITY_CALLBACK : BaseActivityCallback> : BaseFragmentWithCallback<ACTIVITY_CALLBACK>(),
  BaseView<INTENT, VIEW_STATE> {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var viewModel: VIEW_MODEL
  protected val compositeDisposable = CompositeDisposable()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel = getViewModel()
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  abstract fun getViewModel(): VIEW_MODEL

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    subscribeForUpdatesFromViewModel()
    processIntents()
  }

  protected open fun processIntents() {
    viewModel.processIntents(intents())
  }

  protected open fun subscribeForUpdatesFromViewModel() {
    compositeDisposable.add(viewModel.states().subscribe { render(it) })
  }

  override fun onDestroy() {
    compositeDisposable.dispose()
    super.onDestroy()
  }

  abstract fun getLoadingView(): View

  protected fun setupScreenForError(error: String) {
    getLoadingView().visibility = View.GONE
    debugLog(error)
    //TODO(add error rendering)
  }

  protected fun setupScreenForLoading() {
    getLoadingView().visibility = View.VISIBLE
  }

}