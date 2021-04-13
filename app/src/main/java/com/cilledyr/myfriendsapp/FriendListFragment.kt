package com.cilledyr.myfriendsapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*

private const val TAG = "FriendListFragment"

class FriendListFragment : Fragment() {

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    interface Callbacks {
        fun onFriendSelected(friendID: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var friendRecyclerView: RecyclerView
    private var adapter: FriendAdapter? = FriendAdapter(emptyList())

    private val friendListViewModel: FriendListViewModel by lazy {
        ViewModelProviders.of(this).get(FriendListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)
        friendRecyclerView = view.findViewById(R.id.rvFriend) as RecyclerView

        friendRecyclerView.layoutManager = LinearLayoutManager(context)
        friendRecyclerView.adapter = adapter
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendListViewModel.friendListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { friends ->
                friends?.let {
                    Log.i(TAG, "Got friends ${friends.size}")
                    updateUI(friends)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_friend_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_friend -> {
                val friend = BEFriend()
                friendListViewModel.addFriend(friend)
                callbacks?.onFriendSelected(friend.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(friends: List<BEFriend>) {
        adapter = FriendAdapter(friends)
        friendRecyclerView.adapter = adapter
    }

    private inner class FriendHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var friend: BEFriend

        val nameView: TextView = itemView.findViewById(R.id.tvFriendName)
        val phoneView: TextView = itemView.findViewById(R.id.tvFriendPhone)
        val UserView: ImageView = itemView.findViewById(R.id.imgUser)
        val favoriteView: ImageView = itemView.findViewById(R.id.imgFavoriteInList)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(friend: BEFriend) {
            this.friend = friend
            nameView.text = friend.firstName + " " + friend.lastName
            phoneView.text = friend.phoneNr

            favoriteView.setImageResource(R.drawable.notok)
            if(friend.isFavorite)
            {
                favoriteView.setImageResource(R.drawable.ok)
            }
            updatePhotoView()

        }

        private fun updatePhotoView() {
            if (photoFile.exists()) {
                val bitmap = PictureUtils().getScaledBitmap(photoFile.path, requireActivity())
                UserView.setImageBitmap(bitmap)
            } else {
                UserView.setImageDrawable(null)
            }
        }

        override fun onClick(v: View) {
            callbacks?.onFriendSelected(friend.id)
        }
    }

    private inner class FriendAdapter(var friends: List<BEFriend>): RecyclerView.Adapter<FriendHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
            val view = layoutInflater.inflate(R.layout.list_item_friend, parent, false)
            return FriendHolder(view)
        }

        override fun getItemCount(): Int {
            return friends.size
        }

        override fun onBindViewHolder(holder: FriendHolder, position: Int) {
            val friend = friends[position]
            photoFile = friendListViewModel.getPhotoFile(friend)
            photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.cilledyr.myfriendsapp.fileprovider",
                    photoFile)
            holder.bind(friend)
        }
    }

    companion object {
        fun newInstance():FriendListFragment {
            return FriendListFragment()
        }
    }
}