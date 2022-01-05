package com.co.blackhole.cocktails.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.co.blackhole.cocktails.CocktailApplication.Companion.prefs
import com.co.blackhole.cocktails.R
import com.co.blackhole.cocktails.data.DataSource
import com.co.blackhole.cocktails.data.Prefs
import com.co.blackhole.cocktails.databinding.FragmentDetailBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
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
            val numRandom = (DataSource().clients.indices).random()
            val client = DataSource().clients[numRandom]
            val price = args.price.toInt()
            val cocktail: String = binding.tvDetailTitle.text.toString()

            insertShopFirebase(client, cocktail, quantity, price)
        }
    }

    private fun insertShopFirebase(client: String, cocktail: String, quantity: Int, price: Int) {
        val shop = hashMapOf(
            "usuario" to client,
            "nombre" to cocktail,
            "cantidad" to quantity,
            "precio" to price
        )

        val rootRef = FirebaseFirestore.getInstance()
        val shopsRef = rootRef.collection("compras")
        shopsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.apply {
                    print("size: ${size()}")
                    prefs.shopID = size()
                }
            } else {
                task.exception?.message?.let {
                    print(it)
                }
            }
        }

        db.collection("compras").document(prefs.shopID.toString())
            .set(shop)
            .addOnSuccessListener {
                MaterialAlertDialogBuilder(requireContext(),
                    R.style.MaterialAlertDialog_MaterialComponents_App)
                    .setTitle("Registro Pedido")
                    .setBackground(resources.getDrawable(R.drawable.alert_background))
                    .setMessage("Hola $client, has pedido $quantity cocteles $cocktail, en breve recibiras tu orden, gracias por tu compra.")
                    .setPositiveButton("Continuar") { dialog, which ->
                        navController.navigate(R.id.action_detailFragment_to_mainFragment)
                    }
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Lamentamos los inconvenientes, intentalo de nuevamente.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}