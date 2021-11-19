package com.example.android.navigation

import android.app.AlertDialog
import android.app.Dialog
import android.app.usage.UsageEvents
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

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



        //Botón de Rules que lleva al fragmento RulesFragment.
        binding.rulesButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_rulesFragment)
        }

        //Botón de About que lleva al fragmento AboutFragment.
        binding.aboutButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_aboutFragment)
        }

        //Botón de Level que abre el diálogo de dificultad.
        binding.levelButton.setOnClickListener {
            onCreateDialog(savedInstanceState).show()

        }


            binding.playButton.setOnClickListener { view: View ->
                if (selectedMode == -1) {
                    Snackbar.make(view, resources.getString(R.string.aviso_seleccion), Snackbar.LENGTH_SHORT)
                        .show();
                } else {
                    view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment(selectedMode))
                }
            }



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

    private var selectedMode: Int = -1
    private val singleItems by lazy { arrayOf(resources.getString(R.string.facil), resources.getString(R.string.medio), resources.getString(R.string.dificil)) }
    private val checkedItem = 4

    private fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)
            builder.setTitle(resources.getString(R.string.title_dialog))
                .setSingleChoiceItems(singleItems,checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        setSelectedMode(which)
                    })
                .setPositiveButton(resources.getString(R.string.aceptar), DialogInterface.OnClickListener { dialog, id ->

                })
                .setNegativeButton(resources.getString(R.string.cancelar), DialogInterface.OnClickListener { dialog, id ->
                    setSelectedMode(-1)
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setSelectedMode(selectedMode: Int) {
        this.selectedMode = selectedMode
    }



}