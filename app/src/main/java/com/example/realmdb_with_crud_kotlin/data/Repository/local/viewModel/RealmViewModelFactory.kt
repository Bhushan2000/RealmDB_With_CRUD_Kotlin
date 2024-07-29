package com.example.realmdb_with_crud_kotlin.data.Repository.local.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.RealmViewModel
import com.example.realmdb_with_crud_kotlin.data.Repository.local.repo.RealmRepo

class RealmViewModelFactory(private val repo: RealmRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RealmViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}