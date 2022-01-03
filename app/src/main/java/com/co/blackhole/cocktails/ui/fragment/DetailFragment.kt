package com.co.blackhole.cocktails.ui.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.co.blackhole.cocktails.R
import com.co.blackhole.cocktails.data.DataSource
import com.co.blackhole.cocktails.databinding.FragmentDetailBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val args: DetailFragmentArgs by navArgs()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setTitle(args.name)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(args.image).into(binding.ivDetailMain)
        val dec = DecimalFormat("#,###.##")
        val price: Int = args.price.toInt()
        val priceText = "$${dec.format(price)}"
        binding.tvDetailPrice.text = priceText
        binding.tvDetailTitle.text = args.name

        navController = Navigation.findNavController(view)

        var quantity = binding.tvQuantity.text.toString().toInt()

        binding.minus.setOnClickListener {
            if (!binding.tvQuantity.text.equals("1")) {
                quantity -= 1
                binding.tvQuantity.text = quantity.toString()
            }
        }
        binding.plus.setOnClickListener {
            quantity += 1
            binding.tvQuantity.text = quantity.toString()
        }
        binding.fabCart.setOnClickListener {
            val size = DataSource().clients.size
            val numRandom = (0..size).random()
            val client = DataSource().clients[numRandom]
            val price = args.price.toInt()
            val cocktail: String = binding.tvDetailTitle.text.toString()

            insertShopFirebase(client, cocktail, quantity, price)
            //Thread.sleep(2000)
            //navController.navigate(R.id.action_detailFragment_to_mainFragment)
        }
    }

    private fun insertShopFirebase(client: String, cocktail: String, quantity: Int, price: Int) {
        val shop = hashMapOf(
            "usuario" to client,
            "nombre" to cocktail,
            "cantidad" to quantity,
            "precio" to price
        )

        db.collection("compras")
            .add(shop)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "Hola $client, en breve recibiras tu pedido, gracias por tu compra.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(requireContext(), "Lamentamos los inconvenientes, intentalo de nuevamente.", Toast.LENGTH_LONG).show()
            }
    }
}