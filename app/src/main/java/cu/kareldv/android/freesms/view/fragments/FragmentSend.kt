/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cu.kareldv.android.freesms.R
import cu.kareldv.android.freesms.databinding.FragmentSendFragmentBinding
import cu.kareldv.android.freesms.model.FragmentSendViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.parseInt
import java.util.*

/**
 * Fragment of the viewPager which show the form to send a mesage
 * @see cu.kareldv.android.freesms.view.activities.ActivityMain
 */
@AndroidEntryPoint
class FragmentSend : Fragment() {
    companion object {
        fun newInstance() = FragmentSend()
    }

    /**
     * The viewModel
     */
    private val viewModel by viewModels<FragmentSendViewModel>()

    /**
     * The View
     */
    private lateinit var bind: FragmentSendFragmentBinding

    /**
     * Field containing the activityResult handler for pick contacts
     */
    private val pickCtcs = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->

        if (uri == null) return@registerForActivityResult

        val list = MutableList<String>(0) { "" }

        var phonesCursor: Cursor?

        context?.contentResolver?.query(uri, null, null, null, null)?.let { cursor ->
            //Verificamos que no esté vacío
            if (cursor.moveToFirst()) {

                if (parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                    phonesCursor =
                        context?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null,
                            null)

                    phonesCursor?.let { it ->
                        while (it.moveToNext()) {
                            //Obtenemos el numero de telefono
                            list.add(
                                it.getString(
                                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                ).replace("-", "").replace(" ", "")
                            )
                        }
                        it.close()
                    }

                }

            }

            cursor.close()
        }

        if (list.size != 0) {
            if (list.size > 1) {
                pickANumber(list)
            } else {
                bind.phoneInput.setText(list[0])
            }
        }
    }

    /**
     * Field containing the activityResult handler for requests permissions
     */
    private val reqPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickCtcs.launch(null)
            }
        }

    /**
     * Picks a phone number from the given list
     */
    private fun pickANumber(list: MutableList<String>) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.dialog_reqcontact)
            val itms = Array<CharSequence>(list.size) { list[it] }
            setItems(itms) { dlgIntf, pos ->
                bind.phoneInput.setText(list[pos])
            }
            create().show()
        }
    }

    /**
     * Creates the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        //return inflater.inflate(R.layout.fragment_send_fragment, container, false)
        bind = FragmentSendFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    /**
     * Called after "onCreateView" and setups the view events etc
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        bind.sendFab.setOnClickListener(this::onSendClick)
        bind.phoneInputLayout.setEndIconOnClickListener {
            val perm = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS)

            if (perm == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle(R.string.dialog_contactspermission_title)
                        setMessage(R.string.dialog_contactspermission_msg)
                        setPositiveButton(R.string.dialog_contactspermission_btnaccept) { dialogInterface, _ ->
                            dialogInterface.cancel()
                            requestPermissionOfContacts()
                        }
                        setNegativeButton(R.string.dialog_contactspermission_btncancel) { dlgIntf, pos -> dlgIntf.cancel() }
                        create().show()
                    }
                else {
                    requestPermissionOfContacts()
                }
            } else {
                pickCtcs.launch(null)
            }
        }
    }

    /**
     * Setups the viewmodel, adds the observers
     */
    private fun setupViewModel() {
        viewModel.messageModel.observe(viewLifecycleOwner) {
            if (it.success) {
                viewModel.postHistoryItem(
                    it,
                    bind.phoneInput.text.toString().trim(),
                    bind.messageInput.text.toString().trim()
                )
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle(R.string.dialog_sendsuccess_title)
                    setMessage(String.format(getString(R.string.dialog_sendsuccess_msgformat),
                        it.quotaRemaining))
                    create().show()
                }
            } else {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle(R.string.dialog_servererror_title)
                    setMessage(String.format(getString(R.string.dialog_servererror_msgformat),
                        it.error))
                    create().show()
                }
            }
            bind.phoneInput.isEnabled = true
            bind.messageInput.isEnabled = true
            bind.sendFab.isEnabled = true
            requireContext().sendBroadcast(Intent("action_update_items"))
        }
        viewModel.neterrorModel.observe(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_neterror_title)
                setMessage(String.format(getString(R.string.dialog_neterror_msgformat),
                    it.toString()))
                create().show()
            }
        }
    }

    /**
     * Handles when the "send" button was clicked, so it gets the key, phone and message strings,
     * checks for a correct value and asks the viewmodel to send the message
     */
    private fun onSendClick(vw: View) {
        val key = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(getString(R.string.pref_textbeltkey_key), "textbelt")?.trim()
        if (key != null && key.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_badconfigurated_title)
                setMessage(R.string.dialog_badconfigurated_msg_nokey)
                create().show()
            }
            return
        }
        val phone = bind.phoneInput.text.toString().trim()
        if (phone.isEmpty() || !phone.startsWith("+")) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_baddata_title)
                setMessage(R.string.dialog_baddata_nophonespecified)
                create().show()
            }
            return
        }

        val msg = bind.messageInput.text.toString().trim()
        if (msg.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_baddata_title)
                setMessage(R.string.dialog_baddata_nomessage)
                create().show()
            }
            return
        }
        viewModel.sendMessage(key!!, msg, phone)
        bind.phoneInput.isEnabled = false
        bind.messageInput.isEnabled = false
        bind.sendFab.isEnabled = false
    }

    /**
     * Requests the contacts permission
     */
    private fun requestPermissionOfContacts() {
        reqPermission.launch(Manifest.permission.READ_CONTACTS)
    }

}