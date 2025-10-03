package com.effective.effectivemobile.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.effective.effectivemobile.Extensions.openUrl
import com.effective.effectivemobile.R

import com.effective.effectivemobile.databinding.FragmentSignInBinding
import com.effective.effectivemobile.helpers.AuthPrefs

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPrefs: AuthPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        authPrefs = AuthPrefs(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    private fun setupUI() = with(binding) {
        btnSignIn.isEnabled = false
    }

    private fun setupListeners() = with(binding) {
        tvGoSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        tvForgot.setOnClickListener {
            Toast.makeText(requireContext(), "Экран восстановления позже", Toast.LENGTH_SHORT).show()
        }

        btnVK.setOnClickListener { openUrl(VK_URL) }
        btnOK.setOnClickListener { openUrl(OK_URL) }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignInEnabled()
                clearErrorsIfValid()
            }
        }
        etEmail.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trySignIn()
                true
            } else false
        }

        btnSignIn.setOnClickListener { trySignIn() }
    }

    private fun trySignIn() {
        if (validate()) {
            authPrefs.setSignedIn(true)
            findNavController().navigate(
                R.id.main_graph,
                null,
                navOptions {
                    popUpTo(R.id.auth_graph) { inclusive = true }
                    launchSingleTop = true
                }
            )
        }
    }

    private fun updateSignInEnabled() = with(binding) {
        val emailOk = android.util.Patterns.EMAIL_ADDRESS
            .matcher(etEmail.text?.toString()?.trim().orEmpty())
            .matches()
        val passOk = (etPassword.text?.length ?: 0) >= 6
        btnSignIn.isEnabled = emailOk && passOk
    }

    private fun clearErrorsIfValid() = with(binding) {
        val email = etEmail.text?.toString()?.trim().orEmpty()
        tilEmail.error = if (email.isNotEmpty() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        ) null else tilEmail.error

        val pass = etPassword.text?.toString().orEmpty()
        tilPassword.error = if (pass.length >= 6) null else tilPassword.error
    }

    private fun validate(): Boolean = with(binding) {
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val pass = etPassword.text?.toString().orEmpty()
        var ok = true

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Неверный email"
            ok = false
        } else tilEmail.error = null

        if (pass.length < 6) {
            tilPassword.error = "Мин. 6 символов"
            ok = false
        } else tilPassword.error = null

        ok
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VK_URL = "https://vk.com"
        private const val OK_URL = "https://ok.ru"
    }
}
