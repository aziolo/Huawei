package com.alemz.compare3.similarity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.alemz.compare3.R
import com.alemz.compare3.data.Similarity
import com.alemz.compare3.newCompare.NewCompareActivity
import kotlinx.android.synthetic.main.fragment_similarity.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SimilarityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SimilarityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SimilarityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: SimilarityAdapter

    private val viewModel: SViewModel by lazy {
        ViewModelProviders.of(this).get(
            SViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_similarity, container, false)
        adapter = SimilarityAdapter(this)
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = adapter

        val allList = viewModel.getAllNoLive().size

        viewModel.getAllSimilarity().observe(viewLifecycleOwner, Observer<List<Similarity>> { t ->
            adapter.set(t!!)

        })

        view.btnSimilarity.setOnClickListener {
            if (allList > 0){
                val intent = Intent(context, NewCompareActivity::class.java)
                startActivityForResult(intent, 1)
            }
            else Toast.makeText(context, "You need to create family member first!", Toast.LENGTH_SHORT).show()

        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SimilarityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun onClickButtonR(view: View) {
        val requestCode: Int
        when (view.id) {
            R.id.btnSimilarity -> {
                val intent = Intent(context, NewCompareActivity::class.java)
                startActivityForResult(intent, 1)
            }
            else -> {
            }
        }
    }
}
