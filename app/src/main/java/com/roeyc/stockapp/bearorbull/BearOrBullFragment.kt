package com.roeyc.stockapp.bearorbull

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.roeyc.stockapp.R
import kotlinx.android.synthetic.main.fragment_bear_or_bull.*


/**
 * A simple [Fragment] subclass.
 * Use the [BearOrBullFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BearOrBullFragment : Fragment() {

    lateinit var barChart: BarChart
    lateinit var bardataset: BarDataSet


    // With ViewModelFactory
  //  val viewModel = ViewModelProvider(this, YourViewModelFactory).get(YourViewModel::class.java)

    //Without ViewModelFactory
    lateinit var viewModel: BearOrBullViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BearOrBullViewModel::class.java)
        viewModel.getVotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_bear_or_bull, container, false)
        barChart = view?.findViewById(R.id.barChart) as BarChart
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservables()
        initListeners()
    }

    private fun initObservables() {
        viewModel.bullButtonPressed.observe(viewLifecycleOwner, Observer {
            viewModel.setUserVote(it)
        })

        viewModel.bearButtonPressed.observe(viewLifecycleOwner, Observer {
            viewModel.setUserVote(false)
        })

        viewModel.totalUsersVotes.observe(viewLifecycleOwner, Observer {
            initBarChart(it)
        })
    }

    private fun initListeners() {
        green_button?.setOnClickListener {
            viewModel.buttonPressed(true)
        }

        red_button?.setOnClickListener {
            viewModel.buttonPressed(false)
        }
    }

    private fun initBarChart(data: HashMap<String, String>?) {

        val labels = arrayListOf<String>()
        val barEntries: ArrayList<BarEntry> = ArrayList()

        if (!data.isNullOrEmpty()) {
            val keysItr = data.keys.iterator()
            val valuesItr = data.values.iterator()

            labels.add(keysItr.next())
            barEntries.add(BarEntry(valuesItr.next().toFloat(), 0))

            labels.add(keysItr.next())
            barEntries.add(BarEntry(valuesItr.next().toFloat(), 1))
        }

         //    barEntries.add(BarEntry(0f, 0))
         //    barEntries.add(BarEntry(1f, 1))

        bardataset = BarDataSet(barEntries, "Cells")

        val colors = intArrayOf(Color.GREEN, Color.RED)
        bardataset.setColors(colors)
        bardataset.setValueTextColor(Color.BLACK)

        val data = BarData(labels, bardataset)
        barChart.data = data // set the data and list of labels into chart
        barChart.setDescription("Nasdaq") // set the description
        barChart.animateY(2000)


        barChart.setDrawBarShadow(true)
        barChart.setDrawValueAboveBar(false)

        //check maximum
        //  val max = maxOf(barEntries.get(0), barEntries.get(1))
        //  val max = maxOf(barEntries.get(0).`val`, barEntries.get(1).`val`)


        //  barChart.axisLeft.setAxisMinValue(0f)
      //  barChart.axisLeft.setAxisMaxValue(20f)
      //  leftAxis.setLabelCount(5, true)

      //  barChart.axisRight.setAxisMinValue(0f)
      //  barChart.axisRight.setAxisMaxValue(20f)
      //  leftAxis.setLabelCount(5, true)

      //  barChart.invalidate()
    }

    private fun updateBarChart(data: HashMap<String, String>?) {
    //    barEntries.add(BarEntry(1f, 0))
    //    barEntries.add(BarEntry(1f, 1))
    //  bardataset = BarDataSet(barEntries, "Cells")
   //     barChart.data.dataSets.get(0).addEntry(BarEntry(5f, 0))
    }


}