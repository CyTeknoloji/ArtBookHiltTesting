package com.atilsamancioglu.artbookhilttesting.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.databinding.FragmentArtDetailsBinding
import com.atilsamancioglu.artbookhilttesting.util.Status
import com.atilsamancioglu.artbookhilttesting.viewmodel.ArtViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtDetailsFragment @Inject constructor(
        val glide : RequestManager
) : Fragment() {
    private var _binding : FragmentArtDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArtDetailsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        binding.artImageView.setOnClickListener {
            findNavController().navigate(
                ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
            )
        }


        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.setSelectedImage("")
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        binding.saveButton.setOnClickListener {
            viewModel.makeArt(binding.nameText.text.toString(),
                    binding.artistText.text.toString(),
                    binding.yearText.text.toString())

        }

    }

    private fun observeLiveData() {
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            glide.load(url).into(binding.artImageView)
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireActivity(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                    viewModel.resetInsertArtMsg()
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}