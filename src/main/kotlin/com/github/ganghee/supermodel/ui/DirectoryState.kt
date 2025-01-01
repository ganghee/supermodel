package com.github.ganghee.supermodel.ui

interface DirectoryState {
    val className: String
    val isCheck: Boolean
    val filePath: String

    fun check(isCheck: Boolean)
    fun changeFilePath(path: String)
}

object ResponseDirectory : DirectoryState {
    private var _isCheck = false
    override val className: String
        get() = "Response"
    override val isCheck: Boolean
        get() = this._isCheck

    private var _filePath = ""
    override val filePath: String
        get() = this._filePath

    override fun check(isCheck: Boolean) {
        _isCheck = isCheck
    }

    override fun changeFilePath(path: String) {
        _filePath = path
    }
}

object DtoDirectory : DirectoryState {
    private var _isCheck = false
    override val className: String
        get() = "DTO"
    override val isCheck: Boolean
        get() = this._isCheck

    private var _filePath = ""
    override val filePath: String
        get() = this._filePath

    override fun check(isCheck: Boolean) {
        _isCheck = isCheck
    }

    override fun changeFilePath(path: String) {
        _filePath = path
    }
}

object VoDirectory : DirectoryState {
    private var _isCheck = false
    override val className: String
        get() = "VO"
    override val isCheck: Boolean
        get() = this._isCheck

    private var _filePath = ""
    override val filePath: String
        get() = this._filePath

    override fun check(isCheck: Boolean) {
        _isCheck = isCheck
    }

    override fun changeFilePath(path: String) {
        _filePath = path
    }
}
