package com.tawk.to.mars.git.view.fragment.viewpager.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.tawk.to.mars.git.databinding.FragmentSettingBinding
import com.tawk.to.mars.git.databinding.FragmentViewpagerBinding
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.PreferenceViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

class SettingsFragment:Fragment() {
    lateinit var binding: FragmentSettingBinding
    @Inject
    lateinit var pvm:PreferenceViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as TawkTo).appComponent.inject(this)
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view =binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPageSizeValue.text =pvm.getPageSize().toString()
        binding.seekbarPageSize.progress = pvm.getPageSize()-30
        binding.seekbarPageSize.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.tvPageSizeValue!!.text = (p1+30).toString()
                pvm.setPageSize(p1+30)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }


}