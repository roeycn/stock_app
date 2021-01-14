package com.roeyc.stockapp.dialogs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.roeyc.stockapp.R
import com.roeyc.stockapp.common.FragmentUtils
import kotlinx.android.synthetic.main.app_dialog.*
import kotlinx.android.synthetic.main.app_dialog_buttons.*


class APPDialog : BaseDialogFragment() {

    private val VALUE_KEY = "VALUE_KEY"
    private val DELAY_MS: Long = 600
    private val SECOND_TO_MILLI: Long = 1000

    private var trackerAnimation: ValueAnimator? = null
    private var trackerValue: Float = 0f
    private var trackerParams: ConstraintLayout.LayoutParams? = null

    private var listener: APPDialogListener? = null
    lateinit var dialogItem: AppDialogItem

    companion object {
        @JvmField
        val TAG: String = APPDialog::class.java.name
        private val DIALOG_ITEM = "DIALOG_ITEM"

        private fun newInstance(item: AppDialogItem): APPDialog {
            val args = Bundle()
            args.putParcelable(DIALOG_ITEM, item)
            val fragment = APPDialog()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        @JvmOverloads
        fun show(fm: FragmentManager, dbxDialogItem: AppDialogItem, forceShow: Boolean = false) {
            val currentDialog = fm.findFragmentByTag(APPDialog.TAG) as? DialogFragment
            if (currentDialog != null) {
                if (forceShow) {
                    currentDialog.dismissAllowingStateLoss()
                } else {
                    if (currentDialog.tag != dbxDialogItem.tag) {
                        currentDialog.dismissAllowingStateLoss()
                    } else {
                        return
                    }
                }
            }
            val dbxDialog = APPDialog.newInstance(dbxDialogItem)
            dbxDialog.show(fm, APPDialog.TAG)
            fm.executePendingTransactions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogItem = arguments!![DIALOG_ITEM] as AppDialogItem
        setStyle(DialogFragment.STYLE_NO_TITLE, dialogItem.dialogStyle.styleResID)
        savedInstanceState?.let { trackerValue = savedInstanceState[VALUE_KEY] as Float }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.app_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackerParams = ddGuidelineTracker.layoutParams as ConstraintLayout.LayoutParams?

        dialogItem.titleText?.let {
            if(it.isNotBlank()) {
                ddTitleTv.visibility = View.VISIBLE
                ddTitleTv.text = it
            }
        }

        dialogItem.message?.let {
            if(it.isNotBlank()) {
                ddMessageTv.visibility = View.VISIBLE
                ddMessageTv.text = it
            }
        }

        dialogItem.imageUrl?.let {
            ddImageIv.visibility = View.VISIBLE
         //   GetTaxiDriverBoxApp.getImageLoader()
         //       .loadCircularImage(ddImageIv, it, R.drawable.ic_noimage_passenger_panel, 0,
         //           0)
        }

        dialogItem.imageResId?.let {
            ddImageIv.visibility = View.VISIBLE
            ddImageIv.setImageResource(it)
        }

        ddPositiveBtn.text = dialogItem.positiveText
        ddPositiveBtn.isEnabled = dialogItem.isEnableOnCountdown

        dialogItem.negativeText?.let {
            if(it.isNotBlank()) {
                ddNegativeBtn.visibility = View.VISIBLE
                buttonsSep.visibility = View.VISIBLE
                ddNegativeBtn.text = dialogItem.negativeText
            }
        }

        ddPositiveBtn.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.onPositiveClicked(dialogItem.requestCode)
        }

        ddNegativeBtn.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.onNegativeClicked(dialogItem.requestCode)
        }

        dialog?.setCancelable(dialogItem.isCancelable)
        isCancelable = dialogItem.isCancelable
    }

    /**
     * This method will be invoked when the dialog is canceled.
     * for example when the user clicks outside the dialog
     * and the dialog gets canceled
     */
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener?.onDialogCanceled(dialogItem.requestCode)
    }

    override fun onStart() {
        super.onStart()
       // LOGGER.info("DBXDialog shown, requestCode = {}", dialogItem.requestCode)
        dialogItem.duration?.let {
            if (it > 0) {
                if (trackerValue == 1f) {
                    ddPositiveBtn.isEnabled = true
                    updateGuidePercent()
                } else {
                    animateProgress(it)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopAnimation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = FragmentUtils.getParent<APPDialogListener>(this,
                APPDialogListener::class.java)
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() + " must implement DBXDialogListener")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(VALUE_KEY, trackerValue)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        stopAnimation()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAnimation()
        listener = null
    }

    private fun stopAnimation() {
        trackerAnimation?.removeAllListeners()
        trackerAnimation?.removeAllUpdateListeners()
    }

    fun stopCountdown() {
        trackerValue = 1f
        updateGuidePercent()
    }

    //Animate the progress bar
    private fun animateProgress(duration: Int) {
        trackerAnimation = ValueAnimator.ofFloat(trackerValue, 1.0f)
        trackerAnimation?.let {
            updateGuidePercent()

            if (dialogItem.isShowTimer) {
                ddProgressBackground.visibility = View.VISIBLE
                ddProgressTrack.visibility = View.VISIBLE
            }

            it.addUpdateListener { valueAnimator ->
                trackerValue = valueAnimator.animatedValue as Float
                updateGuidePercent()
            }

            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (dialogItem.isCloseOnCountdownFinish) {
                        listener?.onTimerReached(dialogItem.requestCode)
                        dismissAllowingStateLoss()
                    } else {
                        ddPositiveBtn.isEnabled = true
                    }
                }
            })

            it.duration = duration * SECOND_TO_MILLI
            it.startDelay = DELAY_MS
            it.start()
        }
    }

    private fun updateGuidePercent() {
        trackerParams?.let {
            it.guidePercent = trackerValue
            ddGuidelineTracker?.layoutParams = it
        }
    }

    interface APPDialogListener {
        fun onPositiveClicked(requestCode: Int?) {}
        fun onNegativeClicked(requestCode: Int?) {}
        fun onDialogCanceled(requestCode: Int?) {}
        fun onTimerReached(requestCode: Int?) {}
    }

}