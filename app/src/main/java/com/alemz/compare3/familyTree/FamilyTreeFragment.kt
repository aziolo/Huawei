package com.alemz.compare3.familyTree

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alemz.compare3.R
import com.alemz.compare3.createMember.CreateMemberActivity
import com.alemz.compare3.data.FamilyMember
import de.blox.graphview.*
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration
import kotlinx.android.synthetic.main.fragment_family_tree.view.*

class FamilyTreeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: GraphAdapter
    private val graph = Graph()

    private val viewModel: FTViewModel by lazy {
        ViewModelProviders.of(this).get(
            FTViewModel::class.java
        )
    }
    private var nodeCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_family_tree, container, false)
        val graphView = view.findViewById<GraphView>(R.id.graph)
        adapter = GraphAdapter(this, graph)
        drawFamilyTree(view)


        val node1 = Node(getNodeText())
        val node2 = Node(getNodeText())
        val node3 = Node(getNodeText())
        val node4 = Node(getNodeText())
        val node5 = Node(getNodeText())
        val node6 = Node(getNodeText())

        graph.addEdge(node1, node2)
        graph.addEdge(node1, node6)
        graph.addEdge(node1, node3)
        graph.addEdge(node2, node4)
        graph.addEdge(node2, node5)




        graphView.adapter = adapter
        // set the algorithm here
        // set the algorithm here
        val configuration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(300)
            .setSubtreeSeparation(300)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()

        adapter.algorithm = BuchheimWalkerAlgorithm(configuration)

        view.button_add_new_member.setOnClickListener {
            val intent = Intent(context, CreateMemberActivity::class.java)
            startActivityForResult(intent, 1)
        }


        return view
    }
    private fun getNodeText(): String? {
        return "Node " + nodeCount++
    }
    private fun drawFamilyTree(view: View) {
        viewModel.getAll().observe(viewLifecycleOwner, Observer<List<FamilyMember>> { t ->
            if(!t.isNullOrEmpty()) adapter.setMember(t)
        })
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
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
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
    }
}
