package com.cilledyr.myfriendsapp

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.Address
import android.location.LocationManager
import android.location.LocationProvider
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.security.Permission
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_FRIEND_ID = "friend_id"
private const val TAG = "FriendFragment"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_PHOTO = 1

class FriendFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var friend : BEFriend
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var phoneField: EditText
    private lateinit var emailField: EditText
    private lateinit var dateButton: Button
    private lateinit var favoriteChkbx: CheckBox
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var emailBtn: Button
    private lateinit var phoneBtn: Button
    private lateinit var smsBtn: Button
    private lateinit var webField: EditText
    private lateinit var webSeeBtn: Button
    private lateinit var photoBtn: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri


    private val friendDetailViewModel: FriendDetailViewModel by lazy {
        ViewModelProviders.of(this).get(FriendDetailViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_friend, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.deleteFriend -> {
                friendDetailViewModel.deleteFriend(friend)//The detailViewMOdel handles hte delete action.
                activity?.onBackPressed();
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        friend = BEFriend()
        val friendId: UUID = arguments?.getSerializable(ARG_FRIEND_ID) as UUID
        friendDetailViewModel.loadFriend(friendId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        firstNameField = view.findViewById<EditText>(R.id.etFirstName) as EditText
        lastNameField = view.findViewById<EditText>(R.id.etLastName) as EditText
        phoneField = view.findViewById<EditText>(R.id.etPhone) as EditText
        emailField = view.findViewById<EditText>(R.id.etEmail) as EditText
        dateButton = view.findViewById(R.id.btnBirthday) as Button
        favoriteChkbx = view.findViewById(R.id.cbFavorite) as CheckBox
        phoneBtn = view.findViewById(R.id.btnCall) as Button
        emailBtn = view.findViewById(R.id.btnMail) as Button
        smsBtn = view.findViewById(R.id.btnSMS) as Button
        webField = view.findViewById(R.id.etWebsite) as EditText
        webSeeBtn = view.findViewById(R.id.btnWeb) as Button
        photoBtn = view.findViewById(R.id.btnPhoto) as ImageButton
        photoView = view.findViewById(R.id.imgFriendPhoto) as ImageView



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendDetailViewModel.friendLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { friend ->
                friend?.let {
                    this.friend = friend
                    photoFile = friendDetailViewModel.getPhotoFile(friend)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.cilledyr.myfriendsapp.fileprovider",
                    photoFile)
                    UpdateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val FirstNameWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                friend.firstName = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        val LastNameWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                friend.lastName = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        val PhoneWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                friend.phoneNr = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        val EmailWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                friend.email = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        val WebWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                friend.website = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        firstNameField.addTextChangedListener(FirstNameWatcher)
        lastNameField.addTextChangedListener(LastNameWatcher)
        phoneField.addTextChangedListener(PhoneWatcher)
        emailField.addTextChangedListener(EmailWatcher)
        webField.addTextChangedListener(WebWatcher)

        favoriteChkbx.apply {
            setOnCheckedChangeListener{_, isChecked ->
            friend.isFavorite = isChecked
            }
        }

        dateButton.setOnClickListener{
            DatePickerFragment.Companion.newInstance(friend.birthday).apply {
                setTargetFragment(this@FriendFragment, REQUEST_DATE)
                show(this@FriendFragment.parentFragmentManager, DIALOG_DATE)
            }
        }

        phoneBtn.setOnClickListener {
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${friend.phoneNr}")
            }.also { intent -> startActivity(intent) }
        }

        smsBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:${friend.phoneNr}")
            }.also { intent -> startActivity(intent) }
        }

        emailBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, friend.email)
            }.also { intent -> startActivity(intent) }
        }
        webSeeBtn.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(friend.website)
            }.also { intent -> startActivity(intent) }
        }

        photoBtn.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                    packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener{
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                        packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for(cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        /*if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if(location != null) {
                    friend.address = location as Address
                }
            }*/
        friendDetailViewModel.saveFriend(friend)
    }

    override fun onDateSelected(date: Date) {
        friend.birthday = date
        UpdateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun updatePhotoView() {
        if(photoFile.exists()) {
            val bitmap = PictureUtils().getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        }
        else {
            photoView.setImageDrawable(null)
        }
    }
    private fun UpdateUI() {
        firstNameField.setText(friend.firstName)
        lastNameField.setText(friend.lastName)
        phoneField.setText(friend.phoneNr)
        emailField.setText(friend.email)
        dateButton.setText("Birthday is: " + getTheDateString(friend.birthday))
        webField.setText(friend.website)
        favoriteChkbx.apply {
            isChecked = friend.isFavorite
            jumpDrawablesToCurrentState()
        }
        updatePhotoView()
    }

    private fun getTheDateString(date: Date) : String {
        val sdf = SimpleDateFormat("dd - MM - yyyy")
        return sdf.format(date)
    }


    companion object {

        fun newInstance(friendId: UUID): FriendFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FRIEND_ID, friendId)
            }
            return FriendFragment().apply {
                arguments = args
            }
        }
    }
}