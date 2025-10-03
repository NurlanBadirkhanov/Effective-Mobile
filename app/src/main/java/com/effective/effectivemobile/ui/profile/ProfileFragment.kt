package com.effective.effectivemobile.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.effective.effectivemobile.R
import com.effective.effectivemobile.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentProfileBinding.bind(view)

        b.rowSupport.setOnClickListener {
            Toast.makeText(requireContext(), "Заглушка: открыть поддержку", Toast.LENGTH_SHORT).show()
        }
        b.rowSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Заглушка: открыть настройки", Toast.LENGTH_SHORT).show()
        }
        b.rowLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Заглушка: выйти из аккаунта", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
