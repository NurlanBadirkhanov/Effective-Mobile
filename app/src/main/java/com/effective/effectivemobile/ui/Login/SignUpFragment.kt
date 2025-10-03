package com.effective.effectivemobile.ui.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.effective.effectivemobile.R
import com.effective.effectivemobile.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        llHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }
        btnOK.setOnClickListener { openUrl(OK_URL) }
        btnVK.setOnClickListener { openUrl(VK_URL) }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Snackbar.make(requireView(),"Ошибка", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val OK_URL = "https://ok.ru"
        private const val VK_URL = "https://vk.com"
    }
}
