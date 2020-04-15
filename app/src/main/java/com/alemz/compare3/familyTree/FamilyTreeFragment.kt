package com.alemz.compare3.familyTree

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FamilyTreeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: GraphAdapter
    private val graph = Graph()
    private var couples = arrayListOf<List<Long>>()
    private var singles = arrayListOf<Long>()
    private var nodes = arrayListOf<Node>()
    private var graphElements = arrayListOf<FamilyMember>()

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
        draw()

        val job = GlobalScope.launch { // launch a new coroutine and keep a reference to its Job
            readDB()
        }
        drawFamilyTree()

//        val node1 = Node(getNodeText())
//        val node2 = Node(getNodeText())
//        val node3 = Node(getNodeText())
//        val node4 = Node(getNodeText())
//        val node5 = Node(getNodeText())
//
//
//        val edge1 = Edge(node1,node2)
//        val edge2 = Edge(node1,node3)
//        val edge3 = Edge(node1,node4)
//        val edge4 = Edge(node2,node3)
//        val edge5 = Edge(node3,node5)
//
//        graph.addEdges(edge1,edge2,edge3,edge4,edge5)

//        graph.addEdge(node1,node2)
//        graph.addEdge(node1,node3)
//        graph.addEdge(node2,node4)
//        graph.addEdge(node2,node5)
//        graph.addEdge(node3,node6)
//        graph.addEdge(node3,node7)
//        graph.addEdge(7,8)
//        Log.i("node1",node1.toString())
//        Log.i("node2",node2.toString())
//        Log.i("node3",node3.toString())

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

    private fun drawFamilyTree() {
        for (i in couples.indices){
            val name1 = viewModel.getBeloved(couples[i][0]).firstName + "" + viewModel.getBeloved(couples[i][0]).lastName
            val name2 = viewModel.getBeloved(couples[i][1]).firstName + "" + viewModel.getBeloved(couples[i][1]).lastName
            graphElements.add(viewModel.getBeloved(couples[i][0]))
            val node = Node(name1 + "\n" + name2)
            nodes.add(node)
        }
        for (i in singles.indices){
            val name = viewModel.getBeloved(singles[i]).firstName + "" + viewModel.getBeloved(singles[i]).lastName
            graphElements.add(viewModel.getBeloved(singles[i]))
            val node = Node(name)
            nodes.add(node)
        }
        adapter.setMember(graphElements)
        for (i in graphElements.indices){
            if (graphElements[i].mother !== null){
                val idMother = graphElements[i].mother
                val mom = viewModel.getBeloved(idMother!!)
                val nr = graphElements.indexOf(mom)
                graph.addEdge(nodes[nr], nodes[i])
            }
        }
        for (i in graphElements.indices){
            if (graphElements[i].father !== null){
                val idDad = graphElements[i].father
                val dad = viewModel.getBeloved(idDad!!)
                val nr = graphElements.indexOf(dad)
                graph.addEdge(nodes[nr], nodes[i])
            }
        }
        Log.e("graph elements ",graphElements.size.toString())
    }


    @SuppressLint("FragmentLiveDataObserve")
    private fun readDB() {
        val d = arrayListOf<FamilyMember>()
        viewModel.getSex("F").observe(this, Observer<List<FamilyMember>> { t ->
            for (i in t.indices){
                d.add(t[i])
            }
            Log.e("d = ",d[0].toString())
            Log.e("t = ",t[0].toString())
            if(d.isNotEmpty()) {
                Log.e("d if = ",d[0].toString())
                for (i in d.indices){
                    if (d[i].marriedTo !== null) {
                        couples.add(arrayListOf(
                            d[i].uid,
                            d[i].marriedTo!!
                        ))
                    }
                    else{
                        singles.add(d[i].uid)
                    }
                }
            }
            Log.e("singles = ",singles.size.toString())
            Log.e("couples = ",couples.size.toString())
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

    inner class GetListAsyncTask() :
        AsyncTask<Unit, Unit, Unit>(){
        override fun doInBackground(vararg params: Unit?) {
            readDB()
        }
        private fun onPostExecute(result: Long) {
            drawFamilyTree()
        }
    }
    fun draw() {
        GetListAsyncTask().execute()
    }
}
