package com.roeyc.stockapp.dialogs

import android.os.Parcel
import android.os.Parcelable
import android.text.SpannableString
import com.roeyc.stockapp.R
import com.roeyc.stockapp.utils.ResourceID
import com.roeyc.stockapp.utils.StyleID
import com.roeyc.stockapp.utils.fromHtml
import com.roeyc.stockapp.utils.toHtml

data class AppDialogItem(val titleText: String? = null,
                         private val messageText: String? = null,
                         val positiveText: String,
                         val negativeText: String? = null,
                         val isCancelable: Boolean = true,
                         val requestCode: Int? = null,
                         val imageUrl: String? = null,
                         val imageResId: ResourceID? = null,
                         val duration: Int? = null,
                         val isShowTimer: Boolean = true,
                         val isCloseOnCountdownFinish: Boolean = true,
                         val isEnableOnCountdown: Boolean = true,
                         val dialogStyle: DialogStyle = DialogStyle.InfoDialog,
                         val tag: String = APPDialog::class.java.simpleName) : Parcelable {

    var message: CharSequence? = null
        get() = messageText?.fromHtml()

    enum class DialogStyle(val styleResID: StyleID) {
        InfoDialog(R.style.InfoDialog)
     //   InfoDialogRegularButtons(R.style.InfoDialog_RegularButtons),
     //   AlertDialog(R.style.InfoDialog_Alert),
    }

    /**
     * DBXDialogItem builder
     * Once all calls will be from Kotlin, this class can be deleted
     */
    class Builder {
        private var title: String? = null
        private var message: String? = null
        private var positiveBtn: String = ""
        private var negativeBtn: String? = null
        private var cancelable: Boolean = true
        private var isShowTimer: Boolean = true
        private var reqCode: Int? = null
        private var urlImage: String? = null
        private var imageId: ResourceID? = null
        private var durationSec: Int? = null
        private var closeOnCountdownFinish: Boolean = true
        private var enableOnCountdown: Boolean = true
        private var dialogsStyle: DialogStyle = DialogStyle.InfoDialog
        private var tag: String = APPDialog::class.java.simpleName

        fun setTitleText(titleString: String?): Builder {
            title = if (titleString.isNullOrBlank())
                null
            else
                titleString
            return this
        }

        fun setMessageText(messageText: String?): Builder {
            this.message = messageText
            return this
        }

        fun setSpannableMsg(spannableMsg: SpannableString?): Builder {
            if (!spannableMsg.isNullOrBlank()) {
                this.message = spannableMsg?.toHtml()
            }
            return this
        }

        fun setPositiveButtonText(positiveText: String): Builder {
            this.positiveBtn = positiveText
            return this
        }

        fun setImageUrl(imageUrl: String?): Builder {
            this.urlImage = imageUrl
            return this
        }

        fun setNegativeButtonText(negativeText: String?): Builder {
            this.negativeBtn = negativeText
            return this
        }

        fun setImageResId(imageResId: ResourceID?): Builder {
            this.imageId = imageResId
            return this
        }

        fun setDuration(duration: Int?): Builder {
            this.durationSec = duration
            return this
        }

        fun setStyle(style: DialogStyle): Builder {
            this.dialogsStyle = style
            return this
        }

        fun setRequestCode(requestCode: Int?): Builder {
            this.reqCode = requestCode
            return this
        }

        fun setIsCancelable(isCancelable: Boolean): Builder {
            this.cancelable = isCancelable
            return this
        }

        fun setIsCloseOnCountdownFinish(isCloseOnCountdownFinish: Boolean): Builder {
            this.closeOnCountdownFinish = isCloseOnCountdownFinish
            return this
        }

        fun setIsEnableOnCountdown(isEnableOnCountdown: Boolean): Builder {
            this.enableOnCountdown = isEnableOnCountdown
            return this
        }

        fun setShouldShowTimer(showTimer: Boolean): Builder {
            this.isShowTimer = showTimer
            return this
        }

        fun setTag(tag: String): Builder {
            this.tag = tag
            return this;
        }

        fun build(): AppDialogItem = AppDialogItem(
            titleText = this.title,
            dialogStyle = this.dialogsStyle,
            messageText = this.message,
            positiveText = this.positiveBtn,
            negativeText = this.negativeBtn,
            isCancelable = this.cancelable,
            requestCode = this.reqCode,
            duration = this.durationSec,
            isShowTimer = this.isShowTimer,
            imageUrl = this.urlImage,
            imageResId = this.imageId,
            isCloseOnCountdownFinish = this.closeOnCountdownFinish,
            isEnableOnCountdown = this.enableOnCountdown,
            tag = this.tag)
    }

    constructor(parcel: Parcel) : this(
        titleText = parcel.readString(),
        messageText = parcel.readString(),
        positiveText = parcel.readString()!!,
        negativeText = parcel.readString(),
        isCancelable = parcel.readByte() != 0.toByte(),
        isCloseOnCountdownFinish = parcel.readByte() != 0.toByte(),
        isEnableOnCountdown = parcel.readByte() != 0.toByte(),
        requestCode = parcel.readValue(Int::class.java.classLoader) as? Int,
        imageUrl = parcel.readString(),
        imageResId = parcel.readValue(Int::class.java.classLoader) as? Int,
        duration = parcel.readValue(Int::class.java.classLoader) as? Int,
        isShowTimer = parcel.readByte() != 0.toByte(),
        dialogStyle = DialogStyle.values()[parcel.readInt()],
        tag = parcel.readString()!!)


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titleText)
        parcel.writeString(messageText)
        parcel.writeString(positiveText)
        parcel.writeString(negativeText)
        parcel.writeByte(if (isCancelable) 1 else 0)
        parcel.writeByte(if (isCloseOnCountdownFinish) 1 else 0)
        parcel.writeByte(if (isEnableOnCountdown) 1 else 0)
        parcel.writeValue(requestCode)
        parcel.writeString(imageUrl)
        parcel.writeValue(imageResId)
        parcel.writeValue(duration)
        parcel.writeByte(if (isShowTimer) 1 else 0)
        parcel.writeInt(this.dialogStyle.ordinal)
        parcel.writeString(this.tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppDialogItem> {
        override fun createFromParcel(parcel: Parcel): AppDialogItem {
            return AppDialogItem(parcel)
        }

        override fun newArray(size: Int): Array<AppDialogItem?> {
            return arrayOfNulls(size)
        }
    }
}