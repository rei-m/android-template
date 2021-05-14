package me.reim.androidtemplate.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import me.reim.androidtemplate.databinding.MainFragmentBinding
import me.reim.androidtemplate.domain.QiitaArticle

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val adapter = ArticlesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openQiitaArticlesButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainToQiitaArticles()
            findNavController().navigate(action)
        }

//        val unko = view.findViewById<RecyclerView>(R.id.list)
//        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
//        unko.addItemDecoration(decoration)
//        unko.adapter = adapter
//
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    suspend fun submitData(data: PagingData<QiitaArticle>) {
        adapter.submitData(data)
    }
}
