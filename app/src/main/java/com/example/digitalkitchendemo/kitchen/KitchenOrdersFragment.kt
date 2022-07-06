package com.example.digitalkitchendemo.kitchen


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
import com.example.digitalkitchendemo.databinding.FragmentKitchenOrdersBinding
import com.google.firebase.firestore.FirebaseFirestore

class KitchenOrdersFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var adapter: KitchenOrderAdapter
    private lateinit var holdOrderAdapter: KitchenOrderAdapter
    private var dataSource: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentKitchenOrdersBinding
    private lateinit var kitchenOrdersViewModel: KitchenOrdersViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_kitchen_orders, container, false
        )
        val application = this.requireActivity().application

        setUpLayoutManagers()

        setUpViewModel(application)

        setUpQueries()

        setUpButtons()

        setUpSpinner()

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
        holdOrderAdapter.stopListening()
    }

    private fun setUpSpinner() {
        val spinner = binding.siteIdSpinner

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.siteIds,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner?.adapter = adapter
            }
        }

        //var sharedPref = getSharedPreferences("SiteId", 0)
        spinner?.onItemSelectedListener = this

    }

    private fun setUpQueries() {
        adapter = KitchenOrderAdapter(kitchenOrdersViewModel.queuedOrderOptions)
        holdOrderAdapter = KitchenOrderAdapter(kitchenOrdersViewModel.heldOrderOptions)

        binding.queuedOrderList.adapter = adapter
        //binding.holdOrderList.adapter = holdOrderAdapter
    }

    private fun setUpLayoutManagers() {
        val queuedLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        val holdLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        binding.queuedOrderList.layoutManager = queuedLayoutManager
        //binding.holdOrderList.layoutManager = holdLayoutManager
    }

    private fun setUpViewModel(application: Application) {
        val viewModelFactory = KitchenOrdersViewModelFactory(dataSource, application)

        kitchenOrdersViewModel =
            ViewModelProvider(this, viewModelFactory).get(KitchenOrdersViewModel::class.java)

        binding.kitchenOrdersViewModel = kitchenOrdersViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setUpButtons() {
        binding.kitchenOrderFinishedButton.setOnClickListener {
            val snapshots = kitchenOrdersViewModel.queuedOrderOptions.snapshots
            val snapshotSize = snapshots.size
            var currentOrderNumber = -1
            if (snapshotSize != 0) {
                currentOrderNumber = snapshots[0].orderNum
            }
            if (snapshotSize == 0) {
                Toast.makeText(context, "There are no orders to serve!", Toast.LENGTH_SHORT).show()
            } else if (snapshotSize == 1) {
                kitchenOrdersViewModel.updateOrderStatus(snapshots[0].orderNum, "Packaging"
                )
            } else if (snapshotSize > 1) {
                if (snapshots[0].orderNum == currentOrderNumber) {
                    kitchenOrdersViewModel.updateOrderStatus(snapshots[1].orderNum, "Prepping Your Order")
                    kitchenOrdersViewModel.updateOrderStatus(currentOrderNumber, "Packaging")
                }
            }
        }

        binding.kitchenOrderHoldBt.setOnClickListener {

            kitchenOrdersViewModel
            val queuedSnapshots = kitchenOrdersViewModel.queuedOrderOptions.snapshots
            val snapshotSize = queuedSnapshots.size
            var currentOrderNumber = 0
            if (snapshotSize != 0) {
                currentOrderNumber = queuedSnapshots[0].orderNum
            }
            if (snapshotSize == 0) {
                Toast.makeText(context, "There are no orders to hold!", Toast.LENGTH_SHORT).show()
            } else if (snapshotSize == 1) {
                kitchenOrdersViewModel.updateOrderStatus(currentOrderNumber, "Cooking")
            } else if (snapshotSize > 1) {
                if (queuedSnapshots[0].orderNum == currentOrderNumber) {
                    kitchenOrdersViewModel.updateOrderStatus(queuedSnapshots[1].orderNum, "Prepping Your Order")
                    kitchenOrdersViewModel.updateOrderStatus(currentOrderNumber, "Cooking")
                }
            }
        }

        binding.kitchenOrderFinishHoldBt.setOnClickListener {
            val snapshots = kitchenOrdersViewModel.heldOrderOptions.snapshots
            val snapshotSize = snapshots.size
            var currentOrderNumber = 0
            if (snapshotSize != 0) {
                currentOrderNumber = snapshots[0].orderNum
            }

            if (snapshotSize == 0) {
                Toast.makeText(context, "There are no held orders!", Toast.LENGTH_SHORT).show()
            } else if (snapshotSize == 1) {
                kitchenOrdersViewModel.updateOrderStatus(
                    currentOrderNumber, "Packaging"
                )
            } else if (snapshotSize > 1) {
                if (snapshots[0].orderNum == currentOrderNumber) {
                    kitchenOrdersViewModel.updateOrderStatus(currentOrderNumber, "Packaging")
                }
            }

        }

        binding.moveToPackagingAreaBt.setOnClickListener {
            this.findNavController().navigate(R.id.action_kitchenOrderFragment_to_packagingAreaFragment)
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //kitchenOrdersViewModel.currentSite = parent?.selectedItem.toString().toInt()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        /*adapter.stopListening()
        holdOrderAdapter.stopListening()
        //kitchenOrdersViewModel.updateCurrentSite(parent?.getItemAtPosition(position).toString().toInt())
        //setUpQueries()
        adapter.updateOptions(kitchenOrdersViewModel.queuedOrderOptions)
        holdOrderAdapter.updateOptions(kitchenOrdersViewModel.holderOptions)
        adapter.notifyDataSetChanged()
        holdOrderAdapter.notifyDataSetChanged()
        adapter.startListening()
        holdOrderAdapter.startListening()*/
    }

}