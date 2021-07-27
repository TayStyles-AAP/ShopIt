package com.example.shopit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopit.R

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Search"
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}