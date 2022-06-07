package com.example.digitalkitchendemo.packagingArea

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.digitalkitchendemo.R
import com.example.digitalkitchendemo.databinding.FragmentPackagingAreaBinding
import com.example.digitalkitchendemo.kitchen.KitchenOrdersViewModel
import com.example.digitalkitchendemo.kitchen.KitchenOrdersViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore


class PackagingAreaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var adapter: PackagingOrderAdapter
    private lateinit var holdOrderAdapter: PackagingOrderAdapter
    private lateinit var dataSource: FirebaseFirestore
    private lateinit var binding: FragmentPackagingAreaBinding
    private lateinit var packagingAreaViewModel: PackagingAreaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_packaging_area, container, false
        )
        val application = this.requireActivity().application
        dataSource = FirebaseFirestore.getInstance()

        setUpLayoutManagers()
        setUpViewModel(application)
        setUpQueries()
        setUpSpinner()
        setUpButtons()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        holdOrderAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
        holdOrderAdapter.startListening()
    }

    private fun setUpLayoutManagers() {
        val queuedLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        val holdLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        binding.packagingQueuedOrderList.layoutManager = queuedLayoutManager
        binding.packagingHoldOrderList.layoutManager = holdLayoutManager
    }

    private fun setUpViewModel(application: Application) {
        val kitchenVMF = KitchenOrdersViewModelFactory(dataSource, application)
        val kitchenVM = ViewModelProvider(this, kitchenVMF).get(KitchenOrdersViewModel::class.java)
        Toast.makeText(context, "currentSite: ${kitchenVM.currentSite}", Toast.LENGTH_SHORT).show()

        val viewModelFactory = PackagingAreaViewModelFactory(dataSource, application)

        packagingAreaViewModel = ViewModelProvider(this, viewModelFactory).get(PackagingAreaViewModel::class.java)

        packagingAreaViewModel.updateCurrentSite(kitchenVM.currentSite)

        binding.packagingAreaViewModel = packagingAreaViewModel
        binding.lifecycleOwner = this
    }

    private fun setUpQueries() {
        adapter = PackagingOrderAdapter(packagingAreaViewModel.packagingQueuedOrderOptions)
        holdOrderAdapter = PackagingOrderAdapter(packagingAreaViewModel.packagingHoldOrderOptions)

        binding.packagingQueuedOrderList.adapter = adapter
        binding.packagingHoldOrderList.adapter = holdOrderAdapter
    }


    private fun setUpSpinner() {
        val spinner = binding.packagingAreaLocationSpinner

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.siteIds,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }

        //var sharedPref = getSharedPreferences("SiteId", 0)
        spinner.onItemSelectedListener = this

    }


    private fun setUpButtons() {
        binding.packagingOrderFinishedButton.setOnClickListener {
            val snapshots = packagingAreaViewModel.packagingQueuedOrderOptions.snapshots
            val snapshotSize = snapshots.size
            var currentOrderNumber = ""
            if (snapshotSize != 0) {
                //currentOrderNumber = binding.packagingQueuedOrderList[0].order_number_tv.text.toString()
            }


            if (snapshotSize == 0) {
                Toast.makeText(context, "There are no orders to serve!", Toast.LENGTH_SHORT).show()
            } else {
                packagingAreaViewModel.updateOrderStatus(currentOrderNumber, "Your Food is Ready!")
            }
        }

        binding.packagingOrderHoldBt.setOnClickListener {
            val snapshots = packagingAreaViewModel.packagingQueuedOrderOptions.snapshots
            val snapshotSize = snapshots.size
            var currentOrderNumber = ""
            if (snapshotSize != 0) {
                //currentOrderNumber = binding.packagingQueuedOrderList[0].order_number_tv.text.toString()
            }

            if (snapshotSize == 0) {
                Toast.makeText(context, "There are no orders to hold!", Toast.LENGTH_SHORT).show()
            } else {
                packagingAreaViewModel.updateOrderStatus(
                    currentOrderNumber, "Almost Ready"
                )
            }
        }

        binding.packagingOrderFinishHoldBt.setOnClickListener {
            val snapshots = packagingAreaViewModel.packagingHoldOrderOptions.snapshots
            val snapshotSize = snapshots.size
            var currentOrderNumber = ""
            if(snapshotSize != 0) {
                //currentOrderNumber = binding.packagingHoldOrderList[0].order_number_tv.text.toString()
            }


            if(snapshotSize == 0) {
                Toast.makeText(context, "There are no held orders!", Toast.LENGTH_SHORT).show()
            } else {
                packagingAreaViewModel.updateOrderStatus(
                    currentOrderNumber, "Your Food is Ready!"
                )
            }

        }

        binding.moveToKitchenBt.setOnClickListener {
            findNavController().navigate(R.id.action_packagingAreaFragment_to_kitchenOrderFragment)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        adapter.stopListening()
        holdOrderAdapter.stopListening()
        packagingAreaViewModel.updateCurrentSite(parent?.getItemAtPosition(position).toString().toInt())
        //setUpQueries()
        adapter.updateOptions(packagingAreaViewModel.packagingQueuedOrderOptions)
        holdOrderAdapter.updateOptions(packagingAreaViewModel.packagingHoldOrderOptions)
        adapter.notifyDataSetChanged()
        holdOrderAdapter.notifyDataSetChanged()
        adapter.startListening()
        holdOrderAdapter.startListening()
    }
}