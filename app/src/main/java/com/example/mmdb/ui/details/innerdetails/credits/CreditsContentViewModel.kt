package com.example.mmdb.ui.details.innerdetails.credits

import com.example.mmdb.BR
import com.example.mmdb.R
import com.jokubas.mmdb.model.data.entities.Person
import me.tatarka.bindingcollectionadapter2.ItemBinding

class CreditsContentViewModel(
    val castList: List<Person?>,
    val crewList: List<Person?>
) {

    val creditsItemBinding: ItemBinding<Person> = ItemBinding.of(BR.person, R.layout.item_person)
}