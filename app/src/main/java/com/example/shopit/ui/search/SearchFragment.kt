package com.example.shopit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.R

class SearchFragment : Fragment() {

    lateinit var scanButton: Button
    lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Search"
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanButton = view.findViewById(R.id.search_fragment_scan_button)
        searchView = view.findViewById(R.id.search_fragment_search_bar)

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        scanButton.setOnClickListener {
            searchView.isIconified = true
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_barcodeScanner)
        }
    }
}