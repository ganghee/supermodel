package com.github.ganghee.supermodel.model

object Setting {
    private var _rootClassName: String = ""
    val rootClassName: String
        get() = this._rootClassName

    private val _modelItems = mutableListOf<ModelInfo>()
    val modelItems: List<ModelInfo>
        get() = this._modelItems

    private var _isSeparatedFile = false
    val isSeparatedFile: Boolean
        get() = this._isSeparatedFile

    fun setModelItems(modelItems: List<ModelInfo>) {
        _modelItems.clear()
        _modelItems.addAll(modelItems)
    }

    fun setRootClassName(rootClassName :String) {
        _rootClassName = rootClassName
    }

    fun setSeparateFileState(isSeparate: Boolean) {
        _isSeparatedFile = isSeparate
    }
}