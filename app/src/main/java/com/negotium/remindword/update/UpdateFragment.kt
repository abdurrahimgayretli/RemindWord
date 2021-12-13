package com.negotium.remindword.update

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.negotium.remindword.R
import com.negotium.remindword.model.Word
import com.negotium.remindword.viewmodel.WordViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.jwang123.flagkit.FlagKit
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    var fromWord = ""
    var toWord = ""
    private lateinit var conditions: DownloadConditions
    private lateinit var translator: Translator
    private lateinit var options: TranslatorOptions
    private lateinit var mWordViewModel: WordViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)
        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        view.updateAddWordText.setText(args.currentWord.fromWord)
        var languages1 = resources.getStringArray(R.array.languages)
        var languages2 = resources.getStringArray(R.array.languages)

        var arrayAdapter1 = ArrayAdapter(requireContext(),R.layout.dropdown_item,languages1)
        var arrayAdapter2 = ArrayAdapter(requireContext(),R.layout.dropdown_item,languages2)

        if(args.currentWord.fromFlag == "en"){
            var fromFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
            view.updateFromImage.setImageDrawable(fromFlag)
        }else{
            var fromFlag = FlagKit.drawableWithFlag(requireContext(),args.currentWord.fromFlag)
            view.updateFromImage.setImageDrawable(fromFlag)
        }
        if(args.currentWord.toFlag == "en"){
            var toFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
            view.updateToImage.setImageDrawable(toFlag)
        }else{
            var toFlag = FlagKit.drawableWithFlag(requireContext(),args.currentWord.toFlag)
            view.updateToImage.setImageDrawable(toFlag)
        }


        view.updateFromInputText.setOnItemClickListener { adapterView, view1, i, l ->
            if(view.updateFromInputText.text.toString() == "en"){
                var fromFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
                updateFromImage.setImageDrawable(fromFlag)
            }else{
                var fromFlag = FlagKit.drawableWithFlag(requireContext(),view.updateFromInputText.text.toString())
                updateFromImage.setImageDrawable(fromFlag)
            }

        }
        view.updateToInputText.setOnItemClickListener() { adapterView, view1, i, l ->
            if(view.updateToInputText.text.toString() == "en"){
                var toFlag = FlagKit.drawableWithFlag(requireContext(),"gb")
                updateToImage.setImageDrawable(toFlag)
            }
            else{
                var toFlag = FlagKit.drawableWithFlag(requireContext(),view.updateToInputText.text.toString())
                updateToImage.setImageDrawable(toFlag)
            }
        }
        view.updateFromInputText.setText(args.currentWord.fromFlag)
        view.updateToInputText.setText(args.currentWord.toFlag)
        view.updateFromInputText.setAdapter(arrayAdapter1)
        view.updateToInputText.setAdapter(arrayAdapter2)






        view.updateTranslateWordButton.setOnClickListener{ translateWord() }
        view.updateWordButton.setOnClickListener{ updateItem() }

        setHasOptionsMenu(true)

        return view
    }
    fun translateWord(){
        options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(updateFromInputText.text.toString()))
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(updateToInputText.text.toString()))
            .build()
        translator = Translation.getClient(options)
        conditions = DownloadConditions.Builder()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Please open  your wifi", Toast.LENGTH_SHORT).show()
            }
        toWord = updateAddWordText.text.toString()
        translator.translate(toWord)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                updateInfoText.text ="${translatedText}"
                fromWord = translatedText
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Downloading please waiting", Toast.LENGTH_SHORT).show()
            }
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun updateItem(){
        options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(updateFromInputText.text.toString()))
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(updateToInputText.text.toString()))
            .build()
        translator = Translation.getClient(options)
        conditions = DownloadConditions.Builder()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Downloading please waiting", Toast.LENGTH_SHORT).show()
            }
        fromWord = updateAddWordText.text.toString()
        translator.translate(fromWord)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                toWord = translatedText
                if(inputCheck(toWord,fromWord)){
                    val updateWord = Word(args.currentWord.id,fromWord,toWord,updateFromInputText.text.toString(),updateToInputText.text.toString())
                    mWordViewModel.updateWord(updateWord)
                    Toast.makeText(requireContext(), "Updated Succesfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                }
                else{
                    Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Downloading please waiting", Toast.LENGTH_SHORT).show()
            }
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun inputCheck(turkishWord: String,englishWord: String): Boolean{
        return !(TextUtils.isEmpty(englishWord)&& TextUtils.isEmpty(turkishWord))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteWord()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteWord(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mWordViewModel.deleteWord(args.currentWord)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentWord.toWord}", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete ${args.currentWord.toWord}?")
        builder.setMessage("Are you sure you want to delete ${args.currentWord.toWord}?")
        builder.create().show()
    }
}