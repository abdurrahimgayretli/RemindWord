package com.example.notificationdictionaryapp.update

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notificationdictionaryapp.R
import com.example.notificationdictionaryapp.model.Word
import com.example.notificationdictionaryapp.viewmodel.WordViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    var englishWord = ""
    var turkishWord = ""
    private lateinit var mWordViewModel: WordViewModel

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
        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        englishTurkishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }
        view.updateAddWordText.setText(args.currentWord.englishWord)

        view.updateTranslateWordButton.setOnClickListener{ translateWord() }
        view.updateAddWordButton.setOnClickListener{ updateItem() }

        setHasOptionsMenu(true)

        return view
    }
    fun translateWord(){
        turkishWord = updateAddWordText.text.toString()
        englishTurkishTranslator.translate(turkishWord)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                updateInfoText.text ="${translatedText}"
                englishWord = translatedText
            }
            .addOnFailureListener { exception ->
                infoText.text ="error"
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

        turkishWord = updateAddWordText.text.toString()
        englishTurkishTranslator.translate(turkishWord)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                englishWord = translatedText
                if(inputCheck(turkishWord,englishWord)){
                    val updateWord = Word(args.currentWord.id,turkishWord,englishWord)
                    mWordViewModel.updateWord(updateWord)
                    Toast.makeText(requireContext(), "Updated Succesfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                }
                else{
                    Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                infoText.text ="error"
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
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentWord.turkishWord}", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete ${args.currentWord.turkishWord}?")
        builder.setMessage("Are you sure you want to delete ${args.currentWord.turkishWord}?")
        builder.create().show()
    }
}