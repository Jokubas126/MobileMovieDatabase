package com.jokubas.mmdb.moviedetails.credits

import com.jokubas.mmdb.model.data.entities.Person
import com.jokubas.mmdb.moviedetails.BR
import com.jokubas.mmdb.moviedetails.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class CreditsContentViewModel(
    val castList: List<Person?>,
    val crewList: List<Person?>
) {

    val creditsItemBinding: ItemBinding<Person> = ItemBinding.of(BR.person, R.layout.item_person)
}