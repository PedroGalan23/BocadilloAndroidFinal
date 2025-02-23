package com.example.bocadilloandroidfinal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.viewmodels.ResetPasswordViewModel

class ResetPasswordFragment : Fragment() {

    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)

        val emailEditText: EditText = view.findViewById(R.id.emailEditText)
        val resetPasswordButton: Button = view.findViewById(R.id.resetPasswordButton)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
        val volver:Button=view.findViewById(R.id.backButton)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            viewModel.resetPassword(email)
        }

        viewModel.resetStatus.observe(viewLifecycleOwner) { status ->
            statusTextView.text = status
            statusTextView.visibility = View.VISIBLE
        }

        volver.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

    }
}
