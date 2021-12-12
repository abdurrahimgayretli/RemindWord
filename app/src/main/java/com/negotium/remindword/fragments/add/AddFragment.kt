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
import android.widget.ArrayAdapter
import com.google.mlkit.nl.translate.Translator
import com.jwang123.flagkit.FlagKit


class AddFragment : Fragment() {

    private lateinit var conditions: DownloadConditions
    private lateinit var translator: Translator
    private lateinit var options: TranslatorOptions
    private lateinit var mWordViewModel: WordViewModel
    var fromWordString = ""
    var toWordString = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        var languages1 = resources.getStringArray(R.array.languages)
        var languages2 = resources.getStringArray(R.array.languages)

        var arrayAdapter1 = ArrayAdapter(requireContext(),R.layout.dropdown_item,languages1)
        var arrayAdapter2 = ArrayAdapter(requireContext(),R.layout.dropdown_item,languages2)
        var fromFlag = FlagKit.drawableWithFlag(requireContext(),"tr")
        view.fromImage.setImageDrawable(fromFlag)
        var toFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
        view.toImage.setImageDrawable(toFlag)
        view.fromInputText.setOnItemClickListener { adapterView, view1, i, l ->
            if(view.fromInputText.text.toString() == "en"){
                var fromFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
                fromImage.setImageDrawable(fromFlag)
            }else{
                var fromFlag = FlagKit.drawableWithFlag(requireContext(),view.fromInputText.text.toString())
                fromImage.setImageDrawable(fromFlag)
            }

        }
        view.toInputText.setOnItemClickListener() { adapterView, view1, i, l ->
            if(view.toInputText.text.toString() == "en"){
                var toFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
                toImage.setImageDrawable(toFlag)
            }
            else{
                var toFlag = FlagKit.drawableWithFlag(requireContext(),view.toInputText.text.toString())
                toImage.setImageDrawable(toFlag)
            }
        }

        view.fromInputText.setAdapter(arrayAdapter1)
        view.toInputText.setAdapter(arrayAdapter2)
        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        view.translateWordButton.setOnClickListener({ translateWord()})
        view.addWordButton.setOnClickListener({ addWord()})

        return view
    }
    fun addWord(){

        options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(fromInputText.text.toString()))
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(toInputText.text.toString()))
            .build()
        translator = Translation.getClient(options)
        conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }
        fromWordString = addWordText.text.toString()
        translator.translate(fromWordString)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                toWordString = translatedText
                if(inputCheck(fromWordString,toWordString)){
                    val Word = Word(0,fromWordString,toWordString,fromInputText.text.toString(),toInputText.text.toString())
                    mWordViewModel.addWord(Word)
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
                else{
                    Toast.makeText(requireContext(), "Please fill out field.", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                infoText.text ="Downloading please waiting"
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
        options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(fromInputText.text.toString()))
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(toInputText.text.toString()))
            .build()
        translator = Translation.getClient(options)
        conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }
        fromWordString = addWordText.text.toString()
        translator.translate(fromWordString)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                infoText.text ="${translatedText}"
                toWordString = translatedText
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
