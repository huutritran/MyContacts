package com.example.mycontacts.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import com.example.mycontacts.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignIn.setOnClickListener {
            viewModel.login(
                username = etUsername.text.toString(),
                password = etPassword.text.toString()
            )
        }

        viewModel.user.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_loginFragment_to_contactListFragment)
        })

        viewModel.error.observe(viewLifecycleOwner, { msg ->
            msg?.let { toast(it) }
        })
    }

}