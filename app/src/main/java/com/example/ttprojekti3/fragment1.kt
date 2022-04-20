package com.example.ttprojekti3

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.Navigation
//import kotlinx.android.synthetic.main.fragment_fragment1.*

class fragment1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragment1, container, false)

        val button = view.findViewById<Button>(R.id.frag12)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_to_fragment2)


            // PasswordText.doAfterTextChanged { frag12.setVisibility(View.VISIBLE)}


        }

        return view


    }
}
