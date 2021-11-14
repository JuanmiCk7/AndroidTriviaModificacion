package com.example.android.navigation

import android.app.AlertDialog
import android.app.Dialog
import android.app.usage.UsageEvents
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

var selectedMode : Int = 0

/**
 * A simple [Fragment] subclass.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TitleFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)





        //Botón de Play que lleva al fragmento GameFragment.
        binding.rulesButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_rulesFragment)
        }

        //Botón de About que lleva al fragmento AboutFragment.
        binding.aboutButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_aboutFragment)
        }

        //Botón de About que lleva al fragmento AboutFragment.
        binding.levelButton.setOnClickListener {
            onCreateDialog(savedInstanceState).show()
        }

        if (selectedMode != -1) {
            //Botón de Play que lleva al fragmento GameFragment.
            Log.d("1","El item seleccionado es: $selectedMode")
            binding.playButton.setOnClickListener { view : View ->
                view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
            }
        } else {
            Log.d("1","El item seleccionado es: $selectedMode")
            showSnackbar()
        }

        Log.d("1","El item seleccionado es: $selectedMode")



        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_opciones, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TitleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TitleFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    val singleItems = arrayOf("Easy", "Medium", "Hard")
    val checkedItem = 0

    private fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Select a level:")
                .setSingleChoiceItems(singleItems,checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        selectedMode = which

                        Log.d("Aquiiiiii", "The selected option is $selectedMode")
                    })
                .setPositiveButton("Accept", DialogInterface.OnClickListener { dialog, id ->
                    Log.d("Aquiiiiii", "The selected option is $selectedMode")
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    selectedMode = -1
                    Log.d("Aquiiiiii", "The selected option is $selectedMode")
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showSnackbar() {


    }



}