package com.example.shopit.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.ui.business.BusinessListAdapter
import com.example.shopit.ui.profile.ProfileFragment.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment() {

    lateinit var scanButton: Button
    lateinit var searchView: SearchView
    var searchListRecyclerView : RecyclerView? = null
    var searchListAdapter : SearchListAdapter = SearchListAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search,container,false)
        val manager = LinearLayoutManager(requireContext())

        searchListRecyclerView = rootView?.findViewById(R.id.business_recycler_view)
        searchListRecyclerView!!.setHasFixedSize(true)
        searchListAdapter.data = mutableListOf()
        searchListRecyclerView!!.layoutManager = manager
        searchListRecyclerView!!.adapter = searchListAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListRecyclerView!!.scheduleLayoutAnimation()

        scanButton = view.findViewById(R.id.search_fragment_scan_button)
        searchView = view.findViewById(R.id.search_fragment_search_bar)
        getShopData {

        }

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        scanButton.setOnClickListener {
            searchView.isIconified = true
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_barcodeScanner)
        }
    }

    private fun getShopData(completion : (isSuccess : ShopDataClass?) -> Unit) {
        FirebaseFirestore.getInstance().collection("Store")
            .get()
            .addOnCompleteListener { documents ->
              for (item in documents.result!!) {
                  Log.d(TAG, "${documents.result}")
              }
            }
    }
}