package com.dm.auth.domain

interface PatternValidator {
    fun matches(value: String) : Boolean
}