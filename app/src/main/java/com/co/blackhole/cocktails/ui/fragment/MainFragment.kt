package com.co.blackhole.cocktails.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.co.blackhole.cocktails.R
import com.co.blackhole.cocktails.adapter.CocktailAdapter
import com.co.blackhole.cocktails.data.model.CocktailResponse
import com.co.blackhole.cocktails.data.model.Cocktail
import com.co.blackhole.cocktails.databinding.FragmentMainBinding
import com.co.blackhole.cocktails.data.network.RetrofitInstance
import com.co.blackhole.cocktails.ui.viewmodel.CocktailViewModel
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Response

class MainFragment : Fragment(), CocktailAdapter.OnItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var adapter: CocktailAdapter
    private var listCocktail: MutableList<Cocktail> = mutableListOf()
    private lateinit var cocktailViewModel: CocktailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setTitle("Cocteles")
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        cocktailViewModel = ViewModelProvider(this).get(CocktailViewModel::class.java)
        cocktailViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        getCocktails()
        setUpRecyclerView()
    }

    private fun getCocktails() {
        val call = RetrofitInstance.getRetrofit.getCocktails()
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<CocktailResponse>,
                response: Response<CocktailResponse>
            ) {
                listCocktail.clear()
                val list = response.body()?.cocktails ?: mutableListOf()
                listCocktail.addAll(list)

                var name: String
                var image: String
                var price: Int

                if (listCocktail[0].price == 0) {
                    for (i in 0..99) {
                        price = (10..100).random() * 1000
                        listCocktail[i].price = price
                    }
                }

                for (i in 0 until listCocktail.size-1) {
                    name = listCocktail[i].name
                    image = listCocktail[i].image
                    price = listCocktail[i].price

                    if (inputCheck(name, image, price)) {
                        val cocktail = Cocktail(0, name, image, price)
                        if (cocktailViewModel.readAllData.value!!.isEmpty()) {
                            cocktailViewModel.addCocktail(cocktail)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please fill out all fields.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "An error ocurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = CocktailAdapter(this)
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onItemClick(position: Int) {

        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
            cocktailViewModel.readAllData.value!![position].name,
            cocktailViewModel.readAllData.value!![position].price.toString(),
            cocktailViewModel.readAllData.value!![position].image
        )
        navController.navigate(action)
    }

    private fun inputCheck(name: String, image: String, price: Int): Boolean {
        return !(name.isEmpty() && image.isEmpty() && price.toString().isEmpty())
    }
}