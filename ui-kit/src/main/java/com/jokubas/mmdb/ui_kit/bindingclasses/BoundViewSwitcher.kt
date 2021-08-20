package com.jokubas.mmdb.ui_kit.bindingclasses

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewSwitcher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import me.tatarka.bindingcollectionadapter2.ItemBinding

class BoundViewSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewSwitcher(context, attrs) {

    var itemBinding: ItemBinding<Any>? = null
        set(value) {
            if (field == null) {
                field = value
                value?.let { itemBinding ->
                    adapter = SwitcherAdapter(itemBinding)
                }
            }
        }

    var item: Any? = null
        set(value) {
            field = value
            adapter?.item = item
        }

    private var adapter: SwitcherAdapter<Any>? = null
        set(value) {
            field = value
            value?.let { adapter ->
                adapter.attach(
                    onSwitchView = { view ->
                        switchView(view = view)
                    },
                    context = context,
                    lifecycleOwner = findLifecycleOwner()
                )
                adapter.item = item
            }
        }

    private fun switchView(view: View?) {
        if (view != null && childCount >= MAX_CHILD_COUNT) {
            currentView?.let { removeView(it) } ?: removeViewAt(0)
        }

        currentView?.let { currentView ->
            view?.let {
                addView(view)
                showNext()
            }

            post { removeView(currentView) }
        } ?: view?.let {
            addView(view)
        }
    }

    private fun findLifecycleOwner(): LifecycleOwner? =
        (DataBindingUtil.findBinding(this) as? ViewDataBinding)?.lifecycleOwner
            ?: context as? LifecycleOwner

    companion object {
        private const val MAX_CHILD_COUNT = 2
    }

    inner class SwitcherAdapter<T>(private val itemBinding: ItemBinding<T>) {

        private var currentBinding: ViewDataBinding? = null

        fun attach(
            context: Context,
            lifecycleOwner: LifecycleOwner?,
            onSwitchView: (view: View?) -> (Unit)
        ) {
            this.context = context
            this.onSwitchView = onSwitchView
            this.lifecycleOwner = lifecycleOwner

            trySwitchView()
        }

        var item: T? = null
            set(value) {
                synchronized(this) {
                    field = value
                    trySwitchView()
                }
            }

        private var context: Context? = null

        private var lifecycleOwner: LifecycleOwner? = null

        private var onSwitchView: ((view: View?) -> (Unit))? = null

        private fun trySwitchView() {
            context?.let { context ->
                onSwitchView?.let { onSwitchView ->
                    item?.let { item ->
                        switchView(
                            itemBinding = itemBinding,
                            item = item,
                            context = context,
                            onSwitchView = onSwitchView
                        )
                    } ?: clearView(onSwitchView)
                }
            }
        }

        private fun clearView(onSwitchView: (view: View?) -> Unit) {
            itemBinding.set(0, 0)
            currentBinding = null
            onSwitchView.invoke(null)
        }

        private fun switchView(
            itemBinding: ItemBinding<T>,
            item: T,
            context: Context,
            onSwitchView: (view: View?) -> Unit
        ) {
            val currentLayoutRes = itemBinding.layoutRes()
            itemBinding.onItemBind(0, item)

            val shouldInflate = currentLayoutRes != itemBinding.layoutRes()

            if (shouldInflate) {
                currentBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    itemBinding.layoutRes(),
                    null,
                    false
                )
            }

            currentBinding?.let { binding ->
                binding.setVariable(itemBinding.variableId(), item)
                binding.lifecycleOwner = lifecycleOwner
                binding.executePendingBindings()

                if (shouldInflate) {
                    onSwitchView.invoke(binding.root)
                }
            }
        }
    }
}

