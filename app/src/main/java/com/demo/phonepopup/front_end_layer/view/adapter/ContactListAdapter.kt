package com.demo.phonepopup.front_end_layer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.demo.phonepopup.front_end_layer.controller.MainActivity
import com.demo.phonepopup.integration_layer.model.PhoneCallModel
import com.demo.phonepopup.integration_layer.sharedprefernce.SharedPreferenceManager
import com.demo.phonepopup.R
import java.util.*
import kotlin.collections.ArrayList









class ContactListAdapter(var context: MainActivity, var myContactList: ArrayList<PhoneCallModel>) : RecyclerView.Adapter<ContactListViewHolder>(),Filterable {
    var itemsModelListFiltered: List<PhoneCallModel>? = myContactList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.list_items_contact,parent,false)

        return ContactListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {




        var mynumber=""+SharedPreferenceManager.KEY_MY_CONTACT_NUMBER?.let { it1 ->
            SharedPreferenceManager!!.instance!!.readString(
                it1,"")
        }
        holder.txtLetter.setText(""+myContactList[position].name)
        holder.txtName.setText(""+myContactList[position].name)
        holder.txtNumber.setText(""+myContactList[position].number)


        if(mynumber.equals(myContactList[position].number))
            holder.checkBox.isChecked=true
        else
            holder.checkBox.isChecked=false

        /// Change background Randomly

        val images = intArrayOf(R.drawable.item_red, R.drawable.item_green, R.drawable.item_yellow, R.drawable.item_purple)
        val rand = Random()
        holder.txtLetter.setBackgroundResource(images[rand.nextInt(images.size)])

        // Check box Listenr
        var myNumberListSave:Set<String> = setOf()
        holder.checkBox.setOnClickListener {


            myNumberListSave.plus(myContactList.get(position).number)

            SharedPreferenceManager.KEY_MY_CONTACT_NUMBER?.let { it1 ->
                SharedPreferenceManager.instance!!.writeString(
                    it1,myContactList.get(position).number!!)
            }

            SharedPreferenceManager.KEY_MY_CONTACT_NAME?.let { it1 ->
                SharedPreferenceManager.instance!!.writeString(
                    it1,myContactList.get(position).name!!)
            }

            Log.e(":::MY SAVED DATA:::",""+ SharedPreferenceManager.KEY_MY_CONTACT_NUMBER?.let { it1 ->
                SharedPreferenceManager!!.instance!!.readString(
                    it1,"")
            })

            notifyDataSetChanged()
        }



    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return myContactList!!.size
    }



    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()


                if (constraint == null || constraint.length == 0) {
                    filterResults.count = itemsModelListFiltered!!.size
                    filterResults.values = itemsModelListFiltered
                } else {
                    val resultsModel: MutableList<PhoneCallModel> = ArrayList()
                    val searchStr = constraint.toString().toLowerCase()
                    for (itemsModel in itemsModelListFiltered!!) {
                        if (itemsModel.name!!.contains(searchStr) || itemsModel.number!!
                                .contains(searchStr)
                        ) {
                            resultsModel.add(itemsModel)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                try{
                    myContactList = results.values as ArrayList<PhoneCallModel>
                    notifyDataSetChanged()
                }
                catch (e:Exception){
                    e.printStackTrace()



                }


            }
        }
        return filter;
    }
}

class ContactListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    var txtLetter = itemView.findViewById<TextView>(R.id.txtLetter)
    var txtName = itemView.findViewById<TextView>(R.id.txtName)
    var txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)
    var checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
}
