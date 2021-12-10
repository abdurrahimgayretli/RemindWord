package com.negotium.remindword.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.negotium.remindword.R
import com.negotium.remindword.model.Word
import com.negotium.remindword.viewmodel.WordViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class AddFragment : Fragment() {
    private lateinit var mWordViewModel: WordViewModel
    var trWordString = ""
    var enWordString = ""
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.TURKISH)
        .build()
    val englishTurkishTranslator = Translation.getClient(options)
    var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        view.translateWordButton.setOnClickListener({ translateWord()})
        view.addWordButton.setOnClickListener({ addWord()})
        englishTurkishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }
        return view
    }
    fun addWord(){

        trWordString = addWordText.text.toString()
        englishTurkishTranslator.translate(trWordString)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                enWordString = translatedText
                if(inputCheck(trWordString,enWordString)){
                    val Word = Word(0,trWordString,enWordString)
                    mWordViewModel.addWord(Word)
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
                else{
                    Toast.makeText(requireContext(), "Please fill out field.", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                infoText.text ="error"
            }
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        addWordText.text.clear()
    }


    fun translateWord(){
        trWordString = addWordText.text.toString()
        englishTurkishTranslator.translate(trWordString)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                infoText.text ="${translatedText}"
                enWordString = translatedText
            }
            .addOnFailureListener { exception ->
                infoText.text ="error"
            }
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

private fun inputCheck(turkishWord: String,englishWord: String): Boolean{
    return !(TextUtils.isEmpty(englishWord)&& TextUtils.isEmpty(turkishWord))
}
